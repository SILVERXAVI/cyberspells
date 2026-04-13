/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.screen.custom.SpinalInjectorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SpinalInjectorScreen
extends AbstractContainerScreen<SpinalInjectorMenu> {
    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/spinal_injector_gui.png");
    private static final int TEX_W = 256;
    private static final int TEX_H = 256;
    private static final int GUI_W = 185;
    private static final int GUI_H = 233;
    private static final int GUI_U = 35;
    private static final int GUI_V = 10;

    public SpinalInjectorScreen(SpinalInjectorMenu menu, Inventory inv, Component title) {
        super((AbstractContainerMenu)menu, inv, title);
        this.imageWidth = 185;
        this.imageHeight = 233;
    }

    protected void renderBg(GuiGraphics gfx, float partial, int mouseX, int mouseY) {
        gfx.blit(TEX, this.leftPos, this.topPos, 35.0f, 10.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partial) {
        this.renderBackground(gfx, mouseX, mouseY, partial);
        super.render(gfx, mouseX, mouseY, partial);
        this.renderInjectorCounts(gfx);
        this.renderTooltip(gfx, mouseX, mouseY);
    }

    private void renderInjectorCounts(GuiGraphics gfx) {
        gfx.pose().pushPose();
        gfx.pose().translate(0.0f, 0.0f, 10000.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        for (int i = 0; i < 4; ++i) {
            int count = ((SpinalInjectorMenu)this.menu).getInjectorDisplayCount(i);
            if (count <= 1) continue;
            int x = this.leftPos + ((SpinalInjectorMenu)this.menu).getInjectorSlotX(i);
            int y = this.topPos + ((SpinalInjectorMenu)this.menu).getInjectorSlotY(i);
            String text = Integer.toString(count);
            int w = this.font.width(text);
            int tx = x + 16 - w - 1;
            int ty = y + 16 - 8 - 1;
            gfx.drawString(this.font, text, tx, ty, 0xEDEDED, false);
        }
        RenderSystem.enableDepthTest();
        gfx.pose().popPose();
    }

    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
    }
}

