/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.screen.custom.HeatEngineMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public final class HeatEngineScreen
extends AbstractContainerScreen<HeatEngineMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/heatengine_gui.png");
    private static final ResourceLocation FLAME_TEX = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/flame_indicator.png");
    private static final int FLAME_TEX_W = 16;
    private static final int FLAME_TEX_H = 16;
    private static final int FLAME_DRAW_W = 12;
    private static final int FLAME_DRAW_H = 12;
    private static final int SLOT_SIZE = 18;
    private static final float FLAME_BASE_SCALE = 2.0f;
    private static final int PROGRESS_W = 24;
    private static final int PROGRESS_H = 2;
    private static final int FLAME_OFFSET_X = -2;
    private static final int FLAME_OFFSET_Y = 0;
    private static final int FLAME_ANCHOR_GAP_X = 3;

    public HeatEngineScreen(HeatEngineMenu menu, Inventory inv, Component title) {
        super((AbstractContainerMenu)menu, inv, title);
        this.imageWidth = 184;
        this.imageHeight = 163;
    }

    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (((HeatEngineMenu)this.menu).getBurnTimeClient() > 0) {
            this.drawFlameBesideFuelSlot(gg, partialTick);
        }
        this.drawProgressBar(gg);
    }

    private void drawProgressBar(GuiGraphics gg) {
        int cook = ((HeatEngineMenu)this.menu).getCookTimeClient();
        int total = ((HeatEngineMenu)this.menu).getCookTimeTotalClient();
        if (cook <= 0 || total <= 0) {
            return;
        }
        int filled = Mth.clamp((int)((int)Math.floor(24.0 * ((double)cook / (double)total))), (int)0, (int)24);
        int[] pos = this.computeProgressBarPos();
        int x0 = pos[0];
        int y0 = pos[1];
        gg.fill(x0, y0, x0 + filled, y0 + 2, -1);
    }

    private void drawFlameBesideFuelSlot(GuiGraphics gg, float partialTick) {
        if (((HeatEngineMenu)this.menu).slots.size() <= 2) {
            return;
        }
        Slot fuel = (Slot)((HeatEngineMenu)this.menu).slots.get(2);
        float baseX = this.leftPos + fuel.x + 18 + 3 + -2;
        float baseY = (float)(this.topPos + fuel.y) + 3.0f + 0.0f;
        Minecraft mc = Minecraft.getInstance();
        float t = mc.level != null ? (float)mc.level.getGameTime() + partialTick : partialTick;
        float pulse = 1.0f + 0.06f * Mth.sin((float)(t * 0.35f));
        float bob = 0.8f * Mth.sin((float)(t * 0.25f));
        float alpha = 0.85f + 0.15f * Mth.sin((float)(t * 0.4f));
        float scale = 2.0f * pulse;
        float cx = baseX + 6.0f;
        float cy = baseY + 6.0f;
        gg.pose().pushPose();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        gg.pose().translate(cx, cy + bob, 0.0f);
        gg.pose().scale(scale, scale, 1.0f);
        gg.pose().translate(-cx, -cy, 0.0f);
        gg.blit(FLAME_TEX, (int)baseX, (int)baseY, 12, 12, 0.0f, 0.0f, 16, 16, 16, 16);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        gg.pose().popPose();
    }

    private int[] computeProgressBarPos() {
        int fallbackX = this.leftPos + (this.imageWidth - 24) / 2;
        int fallbackY = this.topPos + 44;
        if (((HeatEngineMenu)this.menu).slots.size() <= 1) {
            return new int[]{fallbackX, fallbackY};
        }
        Slot input = (Slot)((HeatEngineMenu)this.menu).slots.get(0);
        Slot output = (Slot)((HeatEngineMenu)this.menu).slots.get(1);
        int inputCenterX = input.x + 8;
        int outputCenterX = output.x + 8;
        int centerX = (inputCenterX + outputCenterX) / 2;
        int x0 = this.leftPos + centerX - 12;
        int topSlotY = Math.min(input.y, output.y);
        int y0 = this.topPos + topSlotY - 7;
        y0 = Math.max(this.topPos + 5, y0);
        return new int[]{x0, y0};
    }

    protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
    }
}

