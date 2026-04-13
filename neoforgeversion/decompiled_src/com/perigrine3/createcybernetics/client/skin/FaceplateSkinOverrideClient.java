/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.blaze3d.platform.NativeImage
 *  net.minecraft.Util
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.client.skin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

public final class FaceplateSkinOverrideClient {
    private static final Map<String, ResolvedSkin> READY = new ConcurrentHashMap<String, ResolvedSkin>();
    private static final Map<String, Long> FAIL_UNTIL = new ConcurrentHashMap<String, Long>();
    private static final Map<String, Boolean> PENDING = new ConcurrentHashMap<String, Boolean>();
    private static final Map<String, UUID> UUID_CACHE = new ConcurrentHashMap<String, UUID>();
    private static final long FAIL_BACKOFF_TICKS = 600L;
    private static final int MAX_NAME_LEN = 16;
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8L)).followRedirects(HttpClient.Redirect.NORMAL).build();

    private FaceplateSkinOverrideClient() {
    }

    public static ResolvedSkin getOrRequest(String rawName) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return null;
        }
        String name = FaceplateSkinOverrideClient.sanitize(rawName);
        if (name == null) {
            return null;
        }
        ResolvedSkin cached = READY.get(name);
        if (cached != null) {
            return cached;
        }
        long now = mc.level.getGameTime();
        Long until = FAIL_UNTIL.get(name);
        if (until != null && now < until) {
            return null;
        }
        if (PENDING.putIfAbsent(name, Boolean.TRUE) != null) {
            return null;
        }
        Util.backgroundExecutor().execute(() -> FaceplateSkinOverrideClient.resolveAsync(name));
        return null;
    }

    public static void clearCache() {
        READY.clear();
        FAIL_UNTIL.clear();
        PENDING.clear();
        UUID_CACHE.clear();
    }

    private static String sanitize(String s) {
        if (s == null) {
            return null;
        }
        if ((s = s.trim()).isEmpty() || s.length() > 16) {
            return null;
        }
        for (int i = 0; i < s.length(); ++i) {
            boolean ok;
            char c = s.charAt(i);
            boolean bl = ok = c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_';
            if (ok) continue;
            return null;
        }
        return s;
    }

    private static void resolveAsync(String name) {
        Minecraft mc = Minecraft.getInstance();
        try {
            SkinInfo info;
            UUID uuid = UUID_CACHE.get(name);
            if (uuid == null) {
                uuid = FaceplateSkinOverrideClient.fetchUuidByName(name);
                if (uuid == null) {
                    FaceplateSkinOverrideClient.fail(mc, name);
                    return;
                }
                UUID_CACHE.put(name, uuid);
            }
            if ((info = FaceplateSkinOverrideClient.fetchSkinInfo(uuid)) == null || info.skinUrl == null || info.skinUrl.isBlank()) {
                FaceplateSkinOverrideClient.fail(mc, name);
                return;
            }
            byte[] png = FaceplateSkinOverrideClient.fetchBytes(info.skinUrl);
            if (png == null || png.length == 0) {
                FaceplateSkinOverrideClient.fail(mc, name);
                return;
            }
            PlayerSkin.Model model = info.slim ? PlayerSkin.Model.SLIM : PlayerSkin.Model.WIDE;
            byte[] finalPng = png;
            mc.execute(() -> {
                try {
                    ResourceLocation rl = FaceplateSkinOverrideClient.registerDynamicSkinTexture(mc, name, finalPng);
                    if (rl != null) {
                        READY.put(name, new ResolvedSkin(rl, model));
                        FAIL_UNTIL.remove(name);
                    } else {
                        FaceplateSkinOverrideClient.backoffFail(mc, name);
                    }
                }
                finally {
                    PENDING.remove(name);
                }
            });
        }
        catch (Throwable t) {
            mc.execute(() -> {
                try {
                    FaceplateSkinOverrideClient.backoffFail(mc, name);
                }
                finally {
                    PENDING.remove(name);
                }
            });
        }
    }

    private static void fail(Minecraft mc, String name) {
        mc.execute(() -> {
            try {
                FaceplateSkinOverrideClient.backoffFail(mc, name);
            }
            finally {
                PENDING.remove(name);
            }
        });
    }

    private static void backoffFail(Minecraft mc, String name) {
        long now = mc.level != null ? mc.level.getGameTime() : 0L;
        FAIL_UNTIL.put(name, now + 600L);
    }

    private static UUID fetchUuidByName(String name) throws Exception {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name)).timeout(Duration.ofSeconds(8L)).header("User-Agent", "CreateCybernetics-FaceplateSkin/1.0").GET().build();
        HttpResponse<String> res = HTTP.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (res.statusCode() == 204 || res.statusCode() == 404) {
            return null;
        }
        if (res.statusCode() != 200) {
            return null;
        }
        JsonObject obj = JsonParser.parseString((String)res.body()).getAsJsonObject();
        if (!obj.has("id")) {
            return null;
        }
        String raw = obj.get("id").getAsString();
        return FaceplateSkinOverrideClient.parseUuidNoDashes(raw);
    }

    private static SkinInfo fetchSkinInfo(UUID uuid) throws Exception {
        String u = uuid.toString().replace("-", "");
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + u + "?unsigned=false")).timeout(Duration.ofSeconds(8L)).header("User-Agent", "CreateCybernetics-FaceplateSkin/1.0").GET().build();
        HttpResponse<String> res = HTTP.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (res.statusCode() != 200) {
            return null;
        }
        JsonObject obj = JsonParser.parseString((String)res.body()).getAsJsonObject();
        if (!obj.has("properties")) {
            return null;
        }
        for (JsonElement el : obj.getAsJsonArray("properties")) {
            JsonObject textures;
            String value;
            JsonObject prop;
            if (!el.isJsonObject() || !"textures".equals(FaceplateSkinOverrideClient.getString(prop = el.getAsJsonObject(), "name")) || (value = FaceplateSkinOverrideClient.getString(prop, "value")) == null || value.isBlank()) continue;
            String decoded = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
            JsonObject texRoot = JsonParser.parseString((String)decoded).getAsJsonObject();
            JsonObject jsonObject = textures = texRoot.has("textures") ? texRoot.getAsJsonObject("textures") : null;
            if (textures == null || !textures.has("SKIN")) {
                return null;
            }
            JsonObject skin = textures.getAsJsonObject("SKIN");
            String url = FaceplateSkinOverrideClient.getString(skin, "url");
            boolean slim = false;
            if (skin.has("metadata") && skin.get("metadata").isJsonObject()) {
                JsonObject meta = skin.getAsJsonObject("metadata");
                String model = FaceplateSkinOverrideClient.getString(meta, "model");
                slim = "slim".equalsIgnoreCase(model);
            }
            return new SkinInfo(url, slim);
        }
        return null;
    }

    private static byte[] fetchBytes(String url) throws Exception {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(10L)).header("User-Agent", "CreateCybernetics-FaceplateSkin/1.0").GET().build();
        HttpResponse<byte[]> res = HTTP.send(req, HttpResponse.BodyHandlers.ofByteArray());
        if (res.statusCode() != 200) {
            return null;
        }
        return res.body();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static ResourceLocation registerDynamicSkinTexture(Minecraft mc, String name, byte[] pngBytes) {
        try (NativeImage img = NativeImage.read((InputStream)new ByteArrayInputStream(pngBytes));){
            if (img == null) {
                ResourceLocation resourceLocation2 = null;
                return resourceLocation2;
            }
            DynamicTexture tex = new DynamicTexture(img);
            String safe = name.toLowerCase(Locale.ROOT);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)("faceplate_skin/" + safe));
            mc.getTextureManager().register(id, (AbstractTexture)tex);
            ResourceLocation resourceLocation = id;
            return resourceLocation;
        }
        catch (Throwable t) {
            return null;
        }
    }

    private static String getString(JsonObject obj, String key) {
        if (obj == null || key == null) {
            return null;
        }
        JsonElement el = obj.get(key);
        if (el == null || el.isJsonNull()) {
            return null;
        }
        return el.getAsString();
    }

    private static UUID parseUuidNoDashes(String s) {
        if (s == null) {
            return null;
        }
        String hex = s.trim();
        if (hex.length() != 32) {
            return null;
        }
        String dashed = hex.substring(0, 8) + "-" + hex.substring(8, 12) + "-" + hex.substring(12, 16) + "-" + hex.substring(16, 20) + "-" + hex.substring(20);
        return UUID.fromString(dashed);
    }

    public record ResolvedSkin(ResourceLocation texture, PlayerSkin.Model model) {
    }

    private static final class SkinInfo {
        final String skinUrl;
        final boolean slim;

        SkinInfo(String skinUrl, boolean slim) {
            this.skinUrl = skinUrl;
            this.slim = slim;
        }
    }
}

