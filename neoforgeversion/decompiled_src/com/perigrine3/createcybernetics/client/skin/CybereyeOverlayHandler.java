/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.NativeImage
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.Resource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 */
package com.perigrine3.createcybernetics.client.skin;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public final class CybereyeOverlayHandler {
    public static final String NBT_ROOT = "cc_cybereye_cfg";
    public static final String NBT_LEFT = "left";
    public static final String NBT_RIGHT = "right";
    public static final String NBT_X = "x";
    public static final String NBT_Y = "y";
    public static final String NBT_VARIANT = "variant";
    private static final int FACE_U = 8;
    private static final int FACE_V = 8;
    private static final int FACE_W = 8;
    private static final int FACE_H = 8;
    private static final Map<UUID, Entry> CACHE = new ConcurrentHashMap<UUID, Entry>();
    private static final Map<EyeSide, EnumMap<Variant, NativeImage>> TEMPLATES = new EnumMap<EyeSide, EnumMap<Variant, NativeImage>>(EyeSide.class);
    private static boolean templatesLoaded = false;
    private static boolean templatesFailed = false;

    private CybereyeOverlayHandler() {
    }

    public static ResourceLocation getOrBuildOverlay(Player player) {
        NativeImage img;
        if (player == null) {
            return null;
        }
        EyePlacement left = CybereyeOverlayHandler.readPlacement(player, EyeSide.LEFT);
        EyePlacement right = CybereyeOverlayHandler.readPlacement(player, EyeSide.RIGHT);
        if (left == null) {
            left = CybereyeOverlayHandler.defaultPlacement(EyeSide.LEFT);
        }
        if (right == null) {
            right = CybereyeOverlayHandler.defaultPlacement(EyeSide.RIGHT);
        }
        left = CybereyeOverlayHandler.clampToFace(left);
        right = CybereyeOverlayHandler.clampToFace(right);
        int hash = CybereyeOverlayHandler.hash(left, right);
        UUID id = player.getUUID();
        Entry e = CACHE.get(id);
        if (e != null && e.lastHash == hash) {
            return e.textureId;
        }
        CybereyeOverlayHandler.ensureTemplatesLoaded();
        if (templatesFailed) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();
        if (e == null) {
            ResourceLocation texId = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)("dynamic/cybereyes/" + String.valueOf(id)));
            DynamicTexture dyn = new DynamicTexture(64, 64, true);
            mc.getTextureManager().register(texId, (AbstractTexture)dyn);
            e = new Entry(texId, dyn, -1);
            CACHE.put(id, e);
        }
        if ((img = e.dyn.getPixels()) == null) {
            return null;
        }
        CybereyeOverlayHandler.clear(img);
        CybereyeOverlayHandler.stamp(img, EyeSide.LEFT, left);
        CybereyeOverlayHandler.stamp(img, EyeSide.RIGHT, right);
        e.dyn.upload();
        e.lastHash = hash;
        return e.textureId;
    }

    public static void invalidate(Player player) {
        if (player == null) {
            return;
        }
        Entry e = CACHE.get(player.getUUID());
        if (e != null) {
            e.lastHash = -1;
        }
    }

    public static void clearAll() {
        CACHE.clear();
    }

    private static EyePlacement readPlacement(Player player, EyeSide side) {
        CompoundTag root = player.getPersistentData().getCompound(NBT_ROOT);
        if (root == null || root.isEmpty()) {
            return null;
        }
        CompoundTag tag = root.getCompound(side == EyeSide.LEFT ? NBT_LEFT : NBT_RIGHT);
        if (tag == null || tag.isEmpty()) {
            return null;
        }
        int x = tag.getInt(NBT_X);
        int y = tag.getInt(NBT_Y);
        int v = tag.getInt(NBT_VARIANT);
        Variant variant = switch (v) {
            case 1 -> Variant.V1x2;
            case 2 -> Variant.V2x2;
            default -> Variant.V1x1;
        };
        return new EyePlacement(x, y, variant);
    }

    private static EyePlacement defaultPlacement(EyeSide side) {
        int x = side == EyeSide.LEFT ? 10 : 13;
        int y = 10;
        return new EyePlacement(x, y, Variant.V1x1);
    }

    private static EyePlacement clampToFace(EyePlacement p) {
        int w = CybereyeOverlayHandler.variantW(p.variant);
        int h = CybereyeOverlayHandler.variantH(p.variant);
        int minX = 8;
        int minY = 8;
        int maxX = 16 - w;
        int maxY = 16 - h;
        int cx = Mth.clamp((int)p.x, (int)minX, (int)maxX);
        int cy = Mth.clamp((int)p.y, (int)minY, (int)maxY);
        return new EyePlacement(cx, cy, p.variant);
    }

    private static int variantW(Variant v) {
        return v == Variant.V2x2 ? 2 : 1;
    }

    private static int variantH(Variant v) {
        return v == Variant.V1x2 || v == Variant.V2x2 ? 2 : 1;
    }

    private static int hash(EyePlacement l, EyePlacement r) {
        int h = 17;
        h = 31 * h + l.x;
        h = 31 * h + l.y;
        h = 31 * h + l.variant.ordinal();
        h = 31 * h + r.x;
        h = 31 * h + r.y;
        h = 31 * h + r.variant.ordinal();
        return h;
    }

    private static void ensureTemplatesLoaded() {
        if (templatesLoaded || templatesFailed) {
            return;
        }
        try {
            TEMPLATES.put(EyeSide.LEFT, new EnumMap(Variant.class));
            TEMPLATES.put(EyeSide.RIGHT, new EnumMap(Variant.class));
            CybereyeOverlayHandler.loadTemplate(EyeSide.LEFT, Variant.V1x1, "textures/entity/cybereyes/left_1x1.png");
            CybereyeOverlayHandler.loadTemplate(EyeSide.LEFT, Variant.V1x2, "textures/entity/cybereyes/left_1x2.png");
            CybereyeOverlayHandler.loadTemplate(EyeSide.LEFT, Variant.V2x2, "textures/entity/cybereyes/left_2x2.png");
            CybereyeOverlayHandler.loadTemplate(EyeSide.RIGHT, Variant.V1x1, "textures/entity/cybereyes/right_1x1.png");
            CybereyeOverlayHandler.loadTemplate(EyeSide.RIGHT, Variant.V1x2, "textures/entity/cybereyes/right_1x2.png");
            CybereyeOverlayHandler.loadTemplate(EyeSide.RIGHT, Variant.V2x2, "textures/entity/cybereyes/right_2x2.png");
            templatesLoaded = true;
        }
        catch (Throwable t) {
            templatesFailed = true;
        }
    }

    private static void loadTemplate(EyeSide side, Variant variant, String path) throws Exception {
        Minecraft mc = Minecraft.getInstance();
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)path);
        Resource res = mc.getResourceManager().getResourceOrThrow(rl);
        try (InputStream in = res.open();){
            NativeImage img = NativeImage.read((InputStream)in);
            int wantW = CybereyeOverlayHandler.variantW(variant);
            int wantH = CybereyeOverlayHandler.variantH(variant);
            if (img.getWidth() != wantW || img.getHeight() != wantH) {
                // empty if block
            }
            TEMPLATES.get((Object)side).put(variant, img);
        }
    }

    private static void clear(NativeImage img) {
        for (int y = 0; y < 64; ++y) {
            for (int x = 0; x < 64; ++x) {
                img.setPixelRGBA(x, y, 0);
            }
        }
    }

    private static void stamp(NativeImage dst, EyeSide side, EyePlacement p) {
        NativeImage mask = TEMPLATES.get((Object)side).get((Object)p.variant);
        if (mask == null) {
            return;
        }
        int w = mask.getWidth();
        int h = mask.getHeight();
        int maxX = 16 - w;
        int maxY = 16 - h;
        int ox = Mth.clamp((int)p.x, (int)8, (int)maxX);
        int oy = Mth.clamp((int)p.y, (int)8, (int)maxY);
        for (int my = 0; my < h; ++my) {
            for (int mx = 0; mx < w; ++mx) {
                int rgba = mask.getPixelRGBA(mx, my);
                int a = rgba >>> 24 & 0xFF;
                if (a == 0) continue;
                int dx = ox + mx;
                int dy = oy + my;
                int out = a << 24 | 0xFFFFFF;
                dst.setPixelRGBA(dx, dy, out);
            }
        }
    }

    public static enum EyeSide {
        LEFT,
        RIGHT;

    }

    public record EyePlacement(int x, int y, Variant variant) {
    }

    private static final class Entry {
        final ResourceLocation textureId;
        final DynamicTexture dyn;
        int lastHash;

        Entry(ResourceLocation textureId, DynamicTexture dyn, int lastHash) {
            this.textureId = textureId;
            this.dyn = dyn;
            this.lastHash = lastHash;
        }
    }

    public static enum Variant {
        V1x1,
        V1x2,
        V2x2;

    }
}

