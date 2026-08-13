/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class ScreenEffectsOverlay
implements LayeredDraw.Layer {
    public static final ScreenEffectsOverlay instance = new ScreenEffectsOverlay();
    public static final ResourceLocation MAGIC_AURA_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/overlays/enchanted_ward_vignette.png");
    public static final ResourceLocation HEARTSTOP_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/overlays/heartstop.png");
    public static final ResourceLocation ICE_BLOCK_TEXTURE = ResourceLocation.withDefaultNamespace((String)"textures/block/ice.png");

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.hasEffect(MobEffectRegistry.HEARTSTOP)) {
            ScreenEffectsOverlay.renderOverlayAdditive(guiHelper, HEARTSTOP_TEXTURE, 0.25f, 0.0f, 0.0f, 0.25f, screenWidth, screenHeight);
        }
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.getRootVehicle().getType().equals(EntityRegistry.ICE_TOMB.get())) {
            ScreenEffectsOverlay.renderOverlay(guiHelper, ICE_BLOCK_TEXTURE, 1.0f, 1.0f, 1.0f, 0.5f, screenWidth, screenHeight);
        }
    }

    private static void renderOverlayAdditive(GuiGraphics gui, ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        gui.setColor(r, g, b, a);
        gui.blit(texture, 0, 0, -90, 0.0f, 0.0f, screenWidth, screenHeight, screenWidth, screenHeight);
        gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
    }

    private static void renderOverlay(GuiGraphics gui, ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.enableBlend();
        gui.setColor(r, g, b, a);
        gui.blit(texture, 0, 0, -90, 0.0f, 0.0f, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
        gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

