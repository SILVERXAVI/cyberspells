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
import com.perigrine3.createcybernetics.screen.custom.ArmCannonMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ArmCannonScreen
extends AbstractContainerScreen<ArmCannonMenu> {
    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/arm_cannon_gui.png");

    public ArmCannonScreen(ArmCannonMenu menu, Inventory inv, Component title) {
        super((AbstractContainerMenu)menu, inv, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.titleLabelX = Integer.MAX_VALUE;
        this.inventoryLabelX = Integer.MAX_VALUE;
    }

    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)TEX);
        gfx.blit(TEX, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gfx, mouseX, mouseY, partialTick);
        super.render(gfx, mouseX, mouseY, partialTick);
        this.renderTooltip(gfx, mouseX, mouseY);
    }
}

