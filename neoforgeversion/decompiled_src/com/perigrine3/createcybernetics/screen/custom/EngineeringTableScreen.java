/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.screen.custom.EngineeringTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EngineeringTableScreen
extends AbstractContainerScreen<EngineeringTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/engineering_gui.png");

    public EngineeringTableScreen(EngineeringTableMenu menu, Inventory playerInv, Component title) {
        super((AbstractContainerMenu)menu, playerInv, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.inventoryLabelY = 9999;
        this.titleLabelY = 9999;
    }

    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        gg.blit(TEXTURE, x, y, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);
        super.render(gg, mouseX, mouseY, partialTick);
        this.renderTooltip(gg, mouseX, mouseY);
    }
}

