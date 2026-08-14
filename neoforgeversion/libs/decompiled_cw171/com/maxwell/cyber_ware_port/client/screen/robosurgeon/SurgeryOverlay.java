/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Post
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 */
package com.maxwell.cyber_ware_port.client.screen.robosurgeon;

import com.maxwell.cyber_ware_port.common.network.ClientPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid="cyber_ware_port", value={Dist.CLIENT})
public class SurgeryOverlay {
    @SubscribeEvent
    public static void onRenderLayer(RenderGuiLayerEvent.Post event) {
        if (!VanillaGuiLayers.CAMERA_OVERLAYS.equals((Object)event.getName())) {
            return;
        }
        int progress = ClientPacketHandler.currentProgress;
        int max = ClientPacketHandler.maxProgress;
        if (progress <= 0) {
            return;
        }
        float alpha = 0.0f;
        if (progress < 30) {
            alpha = (float)progress / 30.0f;
        } else if (progress > max - 20) {
            float remaining = max - progress;
            alpha = remaining / 20.0f;
        } else {
            alpha = 1.0f;
        }
        alpha = Math.max(0.0f, Math.min(1.0f, alpha));
        if (alpha > 0.0f) {
            SurgeryOverlay.renderBlackout(event.getGuiGraphics(), alpha);
        }
    }

    private static void renderBlackout(GuiGraphics g, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        g.pose().pushPose();
        g.pose().translate(0.0f, 0.0f, 2000.0f);
        int color = (int)(alpha * 255.0f) << 24;
        g.fill(0, 0, width, height, color);
        g.pose().popPose();
        RenderSystem.disableBlend();
    }
}

