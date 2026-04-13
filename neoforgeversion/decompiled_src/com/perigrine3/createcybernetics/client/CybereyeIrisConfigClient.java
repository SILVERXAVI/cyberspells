/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.neoforged.fml.loading.FMLPaths
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

public final class CybereyeIrisConfigClient {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<UUID, Layout> CACHE = new ConcurrentHashMap<UUID, Layout>();

    private CybereyeIrisConfigClient() {
    }

    private static Layout defaultLayout() {
        return new Layout(new EyeLayout(IrisSize.ONE_BY_ONE, 3, 5), new EyeLayout(IrisSize.ONE_BY_ONE, 3, 5));
    }

    public static void clamp(EyeLayout eye) {
        int maxX = 8 - eye.size.w;
        int maxY = 8 - eye.size.h;
        if (eye.x < 0) {
            eye.x = 0;
        }
        if (eye.y < 0) {
            eye.y = 0;
        }
        if (eye.x > maxX) {
            eye.x = maxX;
        }
        if (eye.y > maxY) {
            eye.y = maxY;
        }
    }

    private static Path fileFor(UUID playerId) {
        Path dir = FMLPaths.CONFIGDIR.get().resolve("createcybernetics");
        return dir.resolve("cybereye_iris_" + String.valueOf(playerId) + ".json");
    }

    public static Layout get(UUID playerId) {
        return CACHE.computeIfAbsent(playerId, CybereyeIrisConfigClient::loadOrDefault);
    }

    public static void reload(UUID playerId) {
        CACHE.put(playerId, CybereyeIrisConfigClient.loadOrDefault(playerId));
    }

    public static void invalidate(UUID playerId) {
        CACHE.remove(playerId);
    }

    public static void reset(UUID playerId) {
        try {
            Files.deleteIfExists(CybereyeIrisConfigClient.fileFor(playerId));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        CACHE.put(playerId, CybereyeIrisConfigClient.defaultLayout());
    }

    public static void save(UUID playerId, Layout layout) {
        if (layout == null) {
            return;
        }
        CybereyeIrisConfigClient.clamp(layout.left);
        CybereyeIrisConfigClient.clamp(layout.right);
        CACHE.put(playerId, layout.copy());
        Path file = CybereyeIrisConfigClient.fileFor(playerId);
        try {
            Files.createDirectories(file.getParent(), new FileAttribute[0]);
            JsonObject root = new JsonObject();
            JsonObject l = new JsonObject();
            l.addProperty("size", layout.left.size.id);
            l.addProperty("x", (Number)layout.left.x);
            l.addProperty("y", (Number)layout.left.y);
            JsonObject r = new JsonObject();
            r.addProperty("size", layout.right.size.id);
            r.addProperty("x", (Number)layout.right.x);
            r.addProperty("y", (Number)layout.right.y);
            root.add("left", (JsonElement)l);
            root.add("right", (JsonElement)r);
            try (BufferedWriter w = Files.newBufferedWriter(file, new OpenOption[0]);){
                GSON.toJson((JsonElement)root, (Appendable)w);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Layout loadOrDefault(UUID playerId) {
        Path file = CybereyeIrisConfigClient.fileFor(playerId);
        if (!Files.exists(file, new LinkOption[0])) {
            return CybereyeIrisConfigClient.defaultLayout();
        }
        try (BufferedReader r = Files.newBufferedReader(file);){
            JsonObject obj = (JsonObject)GSON.fromJson((Reader)r, JsonObject.class);
            if (obj == null) {
                Layout layout2 = CybereyeIrisConfigClient.defaultLayout();
                return layout2;
            }
            EyeLayout left = new EyeLayout(IrisSize.ONE_BY_ONE, 3, 5);
            EyeLayout right = new EyeLayout(IrisSize.ONE_BY_ONE, 3, 5);
            if (obj.has("left")) {
                JsonObject l = obj.getAsJsonObject("left");
                left.size = IrisSize.fromId(l.has("size") ? l.get("size").getAsString() : "1x1");
                left.x = l.has("x") ? l.get("x").getAsInt() : 3;
                int n = left.y = l.has("y") ? l.get("y").getAsInt() : 5;
            }
            if (obj.has("right")) {
                JsonObject rr = obj.getAsJsonObject("right");
                right.size = IrisSize.fromId(rr.has("size") ? rr.get("size").getAsString() : "1x1");
                right.x = rr.has("x") ? rr.get("x").getAsInt() : 3;
                right.y = rr.has("y") ? rr.get("y").getAsInt() : 5;
            }
            CybereyeIrisConfigClient.clamp(left);
            CybereyeIrisConfigClient.clamp(right);
            Layout layout = new Layout(left, right);
            return layout;
        }
        catch (Throwable ignored) {
            return CybereyeIrisConfigClient.defaultLayout();
        }
    }

    public static final class Layout {
        public final EyeLayout left;
        public final EyeLayout right;

        public Layout(EyeLayout left, EyeLayout right) {
            this.left = left;
            this.right = right;
        }

        public Layout copy() {
            return new Layout(this.left.copy(), this.right.copy());
        }
    }

    public static final class EyeLayout {
        public IrisSize size;
        public int x;
        public int y;

        public EyeLayout(IrisSize size, int x, int y) {
            this.size = size;
            this.x = x;
            this.y = y;
        }

        public EyeLayout copy() {
            return new EyeLayout(this.size, this.x, this.y);
        }
    }

    public static enum IrisSize {
        ONE_BY_ONE(1, 1, "1x1"),
        ONE_BY_TWO(1, 2, "1x2"),
        TWO_BY_TWO(2, 2, "2x2");

        public final int w;
        public final int h;
        public final String id;

        private IrisSize(int w, int h, String id) {
            this.w = w;
            this.h = h;
            this.id = id;
        }

        public static IrisSize fromId(@Nullable String s) {
            if ("1x2".equals(s)) {
                return ONE_BY_TWO;
            }
            if ("2x2".equals(s)) {
                return TWO_BY_TWO;
            }
            return ONE_BY_ONE;
        }
    }

    public static enum Eye {
        LEFT,
        RIGHT;

    }
}

