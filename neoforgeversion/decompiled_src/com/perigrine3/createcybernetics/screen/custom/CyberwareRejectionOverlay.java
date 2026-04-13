/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.blaze3d.platform.NativeImage
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.Resource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.effect.ModEffects;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class CyberwareRejectionOverlay {
    private static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"glitch_overlay");
    private static final ResourceLocation GLITCH_TEX = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/glitch_effect.png");
    private static final float MIN_ALPHA = 0.02f;
    private static final float MAX_ALPHA = 0.8f;
    private static final float FADE_SPEED = 0.35f;
    private static final float FLICKER_CHANCE_PER_SEC = 0.2f;
    private static final int FLICKER_HOLD_TICKS_MIN = 2;
    private static final int FLICKER_HOLD_TICKS_MAX = 6;
    private static final int ACTIVATE_FADE_TICKS = 30;
    private static final int RAMP_SECONDS = 45;
    private static final int RAMP_TICKS = 900;
    private static final int BURST_ON_MIN_START = 6;
    private static final int BURST_ON_MAX_START = 12;
    private static final int BURST_ON_MIN_END = 10;
    private static final int BURST_ON_MAX_END = 20;
    private static final int BURST_OFF_MIN_START = 40;
    private static final int BURST_OFF_MAX_START = 90;
    private static final int BURST_OFF_MIN_END = 6;
    private static final int BURST_OFF_MAX_END = 16;
    private static boolean metaLoaded = false;
    private static int frameTime = 2;
    private static List<Integer> frames = null;
    private static int texW = 0;
    private static int texH = 0;
    private static int frameW = 0;
    private static int frameH = 0;
    private static int frameCount = 1;
    private static int flickerHoldTicks = 0;
    private static boolean wasActive = false;
    private static int effectStartTick = 0;
    private static int burstOnTicksLeft = 0;
    private static int burstOffTicksLeft = 0;
    private static int burstStartedAtTick = 0;
    private static int burstLengthLast = 1;

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, CyberwareRejectionOverlay::render);
    }

    private static void render(GuiGraphics gui, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        boolean active = CyberwareRejectionOverlay.hasEffect((Player)player, ModEffects.CYBERWARE_REJECTION);
        if (!active) {
            wasActive = false;
            burstOnTicksLeft = 0;
            burstOffTicksLeft = 0;
            flickerHoldTicks = 0;
            return;
        }
        int nowTick = player.tickCount;
        if (!wasActive) {
            wasActive = true;
            effectStartTick = nowTick;
            burstOnTicksLeft = 0;
            burstOffTicksLeft = 0;
            burstStartedAtTick = nowTick;
            burstLengthLast = 1;
        }
        CyberwareRejectionOverlay.ensureMetaLoaded(mc);
        if (frameCount <= 0 || frameH <= 0 || frameW <= 0) {
            return;
        }
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        float pt = delta.getGameTimeDeltaPartialTick(false);
        float tTicks = (float)player.tickCount + pt;
        int underTicks = nowTick - effectStartTick;
        float ramp = Mth.clamp((float)((float)underTicks / 900.0f), (float)0.0f, (float)1.0f);
        if (burstOnTicksLeft > 0) {
            --burstOnTicksLeft;
        } else if (burstOffTicksLeft > 0) {
            --burstOffTicksLeft;
        } else {
            int onMin = (int)Mth.lerp((float)ramp, (float)6.0f, (float)10.0f);
            int onMax = (int)Mth.lerp((float)ramp, (float)12.0f, (float)20.0f);
            int offMin = (int)Mth.lerp((float)ramp, (float)40.0f, (float)6.0f);
            int offMax = (int)Mth.lerp((float)ramp, (float)90.0f, (float)16.0f);
            int onLen = onMin + player.getRandom().nextInt(Math.max(1, onMax - onMin + 1));
            int offLen = offMin + player.getRandom().nextInt(Math.max(1, offMax - offMin + 1));
            burstOnTicksLeft = onLen;
            burstOffTicksLeft = offLen;
            burstStartedAtTick = nowTick;
            burstLengthLast = onLen;
        }
        if (burstOnTicksLeft <= 0) {
            return;
        }
        int animIndex = (int)(tTicks / (float)Math.max(1, frameTime));
        int frameIndex = frames != null && !frames.isEmpty() ? frames.get(Math.floorMod(animIndex, frames.size())) : Math.floorMod(animIndex, frameCount);
        int u = 0;
        int v = frameIndex * frameH;
        float s = 0.5f + 0.5f * Mth.sin((float)(tTicks * 0.05f * 0.7f));
        float eased = CyberwareRejectionOverlay.smoothstep(s);
        float alpha = Mth.lerp((float)eased, (float)0.02f, (float)0.8f);
        if (flickerHoldTicks > 0) {
            --flickerHoldTicks;
            alpha = 0.8f;
        } else {
            float perTick = 0.01f;
            if (player.getRandom().nextFloat() < perTick) {
                flickerHoldTicks = 2 + player.getRandom().nextInt(5);
            }
        }
        float activateFade = CyberwareRejectionOverlay.smoothstep(Mth.clamp((float)((float)underTicks / 30.0f), (float)0.0f, (float)1.0f));
        int burstAge = nowTick - burstStartedAtTick;
        float burstPhase = burstLengthLast <= 0 ? 1.0f : (float)burstAge / (float)burstLengthLast;
        burstPhase = Mth.clamp((float)burstPhase, (float)0.0f, (float)1.0f);
        float burstEnv = 1.0f - Math.abs(2.0f * burstPhase - 1.0f);
        burstEnv = CyberwareRejectionOverlay.smoothstep(burstEnv);
        alpha = Mth.clamp((float)(alpha * activateFade * burstEnv), (float)0.0f, (float)1.0f);
        float maxAlphaOverTime = Mth.lerp((float)ramp, (float)0.75f, (float)0.8f);
        alpha = Mth.clamp((float)alpha, (float)0.02f, (float)maxAlphaOverTime);
        int jitter = 1 + player.getRandom().nextInt(3);
        int jx = player.getRandom().nextInt(jitter * 2 + 1) - jitter;
        int jy = player.getRandom().nextInt(jitter * 2 + 1) - jitter;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        gui.setColor(1.0f, 1.0f, 1.0f, alpha);
        CyberwareRejectionOverlay.blitScaled(gui, GLITCH_TEX, jx, jy, w, h, u, v, frameW, frameH, texW, texH);
        float chromaA = alpha * 0.35f;
        int split = 1 + player.getRandom().nextInt(3);
        gui.setColor(1.0f, 0.25f, 0.25f, chromaA);
        CyberwareRejectionOverlay.blitScaled(gui, GLITCH_TEX, jx + split, jy, w, h, u, v, frameW, frameH, texW, texH);
        gui.setColor(0.25f, 1.0f, 0.25f, chromaA);
        CyberwareRejectionOverlay.blitScaled(gui, GLITCH_TEX, jx - split, jy, w, h, u, v, frameW, frameH, texW, texH);
        gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    private static void blitScaled(GuiGraphics gui, ResourceLocation tex, int x, int y, int dstW, int dstH, int u, int v, int srcW, int srcH, int texW, int texH) {
        gui.blit(tex, x, y, dstW, dstH, (float)u, (float)v, srcW, srcH, texW, texH);
    }

    private static boolean hasEffect(Player player, Holder<MobEffect> effect) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (inst == null || !inst.is(effect)) continue;
            return true;
        }
        return false;
    }

    private static float smoothstep(float x) {
        x = Mth.clamp((float)x, (float)0.0f, (float)1.0f);
        return x * x * (3.0f - 2.0f * x);
    }

    private static void ensureMetaLoaded(Minecraft mc) {
        if (metaLoaded) {
            return;
        }
        metaLoaded = true;
        TextureManager tm = mc.getTextureManager();
        AbstractTexture tex = tm.getTexture(GLITCH_TEX);
        tm.bindForSetup(GLITCH_TEX);
        try {
            Resource pngRes = mc.getResourceManager().getResource(GLITCH_TEX).orElse(null);
            if (pngRes != null) {
                try (InputStream in = pngRes.open();){
                    NativeImage img = NativeImage.read((InputStream)in);
                    texW = img.getWidth();
                    texH = img.getHeight();
                    img.close();
                }
            }
            frameW = texW;
            frameH = texW;
            ResourceLocation metaLoc = ResourceLocation.fromNamespaceAndPath((String)GLITCH_TEX.getNamespace(), (String)(GLITCH_TEX.getPath() + ".mcmeta"));
            Resource metaRes = mc.getResourceManager().getResource(metaLoc).orElse(null);
            if (metaRes != null) {
                try (InputStreamReader rdr = new InputStreamReader(metaRes.open(), StandardCharsets.UTF_8);){
                    JsonObject anim;
                    JsonObject root = JsonParser.parseReader((Reader)rdr).getAsJsonObject();
                    JsonObject jsonObject = anim = root.has("animation") ? root.getAsJsonObject("animation") : null;
                    if (anim != null) {
                        if (anim.has("frametime")) {
                            frameTime = Math.max(1, anim.get("frametime").getAsInt());
                        }
                        if (anim.has("height")) {
                            frameH = Math.max(1, anim.get("height").getAsInt());
                        }
                        if (anim.has("width")) {
                            frameW = Math.max(1, anim.get("width").getAsInt());
                        }
                        if (anim.has("frames")) {
                            JsonArray arr = anim.getAsJsonArray("frames");
                            ArrayList<Integer> tmp = new ArrayList<Integer>();
                            for (int i = 0; i < arr.size(); ++i) {
                                if (arr.get(i).isJsonPrimitive()) {
                                    tmp.add(arr.get(i).getAsInt());
                                    continue;
                                }
                                if (!arr.get(i).isJsonObject() || !arr.get(i).getAsJsonObject().has("index")) continue;
                                tmp.add(arr.get(i).getAsJsonObject().get("index").getAsInt());
                            }
                            frames = tmp.isEmpty() ? null : tmp;
                        }
                    }
                }
            }
            if (frameH <= 0) {
                frameH = texW;
            }
            frameCount = frameH > 0 ? Math.max(1, texH / frameH) : 1;
        }
        catch (Exception e) {
            frameTime = 2;
            texW = Math.max(1, texW);
            texH = Math.max(1, texH);
            frameW = Math.max(1, frameW);
            frameH = Math.max(1, frameH);
            frameCount = 1;
            frames = null;
        }
    }

    private CyberwareRejectionOverlay() {
    }
}

