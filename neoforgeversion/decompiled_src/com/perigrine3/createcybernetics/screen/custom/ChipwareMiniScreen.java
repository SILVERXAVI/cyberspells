/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.screen.custom.ChipwareMiniMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ChipwareMiniScreen
extends AbstractContainerScreen<ChipwareMiniMenu> {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/creative_chipware_slots_gui.png");

    public ChipwareMiniScreen(ChipwareMiniMenu menu, Inventory inv, Component title) {
        super((AbstractContainerMenu)menu, inv, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.titleLabelX = 0;
        this.titleLabelY = 0;
        this.inventoryLabelX = 0;
        this.inventoryLabelY = 0;
    }

    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        gg.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            int x1 = this.leftPos + 87;
            int y1 = this.topPos + 44;
            int x2 = x1 + 64;
            int y2 = y1 + 110;
            int scale = 34;
            float yOffset = 0.0f;
            InventoryScreen.renderEntityInInventoryFollowsMouse((GuiGraphics)gg, (int)x1, (int)y1, (int)x2, (int)y2, (int)scale, (float)yOffset, (float)mouseX, (float)mouseY, (LivingEntity)player);
        }
    }

    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);
        super.render(gg, mouseX, mouseY, partialTick);
        this.renderTooltip(gg, mouseX, mouseY);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}

