/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 */
package com.maxwell.cyber_ware_port.client.upgrades.handmenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class PortableCraftingButton
extends Button {
    private final ItemStack craftingTableStack = new ItemStack((ItemLike)Items.CRAFTING_TABLE);

    public PortableCraftingButton(int x, int y, Button.OnPress onPress) {
        super(x, y, 20, 20, (Component)Component.empty(), onPress, DEFAULT_NARRATION);
    }

    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(g, mouseX, mouseY, partialTick);
        int itemX = this.getX() + 2;
        int itemY = this.getY() + 2;
        g.renderItem(this.craftingTableStack, itemX, itemY);
    }
}

