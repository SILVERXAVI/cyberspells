/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.screen.custom.ExpandedInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ExpandedInventoryScreen
extends AbstractContainerScreen<ExpandedInventoryMenu> {
    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/inventory_crafting.png");

    public ExpandedInventoryScreen(ExpandedInventoryMenu menu, Inventory inv, Component title) {
        super((AbstractContainerMenu)menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    protected void init() {
        super.init();
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEX, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu != null && ((ExpandedInventoryMenu)this.menu).hasDataShardSlots()) {
            int sx = this.leftPos + 77;
            int sy0 = this.topPos + 8;
            int sy1 = sy0 + 18;
            ExpandedInventoryScreen.drawSlotBackground(graphics, sx, sy0);
            ExpandedInventoryScreen.drawSlotBackground(graphics, sx, sy1);
        }
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        int x1 = this.leftPos + 26;
        int y1 = this.topPos + 8;
        int x2 = this.leftPos + 75;
        int y2 = this.topPos + 78;
        int scale = 30;
        InventoryScreen.renderEntityInInventoryFollowsMouse((GuiGraphics)graphics, (int)x1, (int)y1, (int)x2, (int)y2, (int)scale, (float)0.0f, (float)mouseX, (float)mouseY, (LivingEntity)this.minecraft.player);
    }

    private static void drawSlotBackground(GuiGraphics gg, int x, int y) {
        int left = x - 1;
        int top = y - 1;
        int right = x + 17;
        int bottom = y + 17;
        gg.fill(left, top, right, bottom, -536541430);
        gg.fill(left, top, right, top + 1, -11881473);
        gg.fill(left, bottom - 1, right, bottom, -11881473);
        gg.fill(left, top, left + 1, bottom, -11881473);
        gg.fill(right - 1, top, right, bottom, -11881473);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}

