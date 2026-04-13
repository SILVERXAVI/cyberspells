/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.effect.ModEffects;
import java.util.UUID;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class NeuropozyneVignetteOverlay {
    private static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"neuropozyne_vignette");
    private static final int VIGNETTE_RGB = 12900645;
    private static final int PULSE_TICKS = 240;
    private static final float MAX_ALPHA = 0.5f;
    private static final float THICKNESS_RATIO = 0.15f;
    private static final int STEPS = 32;
    private static boolean hadNeuropozyne = false;
    private static int pulseStartTick = -1;
    private static UUID lastPlayerUUID = null;

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, NeuropozyneVignetteOverlay::render);
    }

    private static void render(GuiGraphics gui, DeltaTracker delta) {
        int h;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        boolean has = player.hasEffect(ModEffects.NEUROPOZYNE);
        UUID id = player.getUUID();
        if (lastPlayerUUID == null || !lastPlayerUUID.equals(id)) {
            lastPlayerUUID = id;
            hadNeuropozyne = has;
            pulseStartTick = -1;
            return;
        }
        if (has && !hadNeuropozyne) {
            pulseStartTick = player.tickCount;
        }
        hadNeuropozyne = has;
        if (pulseStartTick < 0) {
            return;
        }
        float pt = delta.getGameTimeDeltaPartialTick(false);
        float age = (float)player.tickCount + pt - (float)pulseStartTick;
        if (age >= 240.0f) {
            pulseStartTick = -1;
            return;
        }
        float t = Mth.clamp((float)(age / 240.0f), (float)0.0f, (float)1.0f);
        float eased = 1.0f - NeuropozyneVignetteOverlay.smoothstep(t);
        float alpha = Mth.clamp((float)(eased * 0.5f), (float)0.0f, (float)0.5f);
        if (alpha <= 0.001f) {
            return;
        }
        int w = mc.getWindow().getGuiScaledWidth();
        int thick = (int)((float)Math.min(w, h = mc.getWindow().getGuiScaledHeight()) * 0.15f);
        if (thick <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        NeuropozyneVignetteOverlay.drawVignetteStrips(gui, w, h, thick, 12900645, alpha);
        RenderSystem.disableBlend();
    }

    private static void drawVignetteStrips(GuiGraphics gui, int w, int h, int thick, int rgb, float alpha) {
        int argb;
        float a;
        float edge;
        float u1;
        float u0;
        int i;
        for (i = 0; i < 32; ++i) {
            u0 = (float)i / 32.0f;
            u1 = (float)(i + 1) / 32.0f;
            int x0 = (int)(u0 * (float)thick);
            int x1 = (int)(u1 * (float)thick);
            edge = 1.0f - u0;
            a = alpha * (edge * edge);
            argb = NeuropozyneVignetteOverlay.argb(a, rgb);
            gui.fill(0 + x0, 0, 0 + x1, h, argb);
            gui.fill(w - x1, 0, w - x0, h, argb);
        }
        for (i = 0; i < 32; ++i) {
            u0 = (float)i / 32.0f;
            u1 = (float)(i + 1) / 32.0f;
            int y0 = (int)(u0 * (float)thick);
            int y1 = (int)(u1 * (float)thick);
            edge = 1.0f - u0;
            a = alpha * (edge * edge);
            argb = NeuropozyneVignetteOverlay.argb(a, rgb);
            gui.fill(0, 0 + y0, w, 0 + y1, argb);
            gui.fill(0, h - y1, w, h - y0, argb);
        }
    }

    private static int argb(float alpha01, int rgb) {
        int a = Mth.clamp((int)((int)(alpha01 * 255.0f)), (int)0, (int)255);
        return a << 24 | rgb & 0xFFFFFF;
    }

    private static float smoothstep(float x) {
        x = Mth.clamp((float)x, (float)0.0f, (float)1.0f);
        return x * x * (3.0f - 2.0f * x);
    }

    private NeuropozyneVignetteOverlay() {
    }
}

