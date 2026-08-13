/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.ItemCombinerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.ItemCombinerMenu
 */
package io.redspace.ironsspellbooks.gui.arcane_anvil;

import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ItemCombinerMenu;

public class ArcaneAnvilScreen
extends ItemCombinerScreen<ArcaneAnvilMenu> {
    private static final ResourceLocation ANVIL_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/arcane_anvil.png");

    public ArcaneAnvilScreen(ArcaneAnvilMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super((ItemCombinerMenu)pMenu, pPlayerInventory, pTitle, ANVIL_LOCATION);
        this.titleLabelX = 48;
        this.titleLabelY = 24;
    }

    protected void renderBg(GuiGraphics guiHelper, float pPartialTick, int pX, int pY) {
        int leftPos = (this.width - this.imageWidth) / 2;
        int topPos = (this.height - this.imageHeight) / 2;
        guiHelper.blit(ANVIL_LOCATION, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (((ArcaneAnvilMenu)this.menu).getSlot(0).hasItem() && ((ArcaneAnvilMenu)this.menu).getSlot(1).hasItem() && !((ArcaneAnvilMenu)this.menu).getSlot(2).hasItem()) {
            guiHelper.blit(ANVIL_LOCATION, leftPos + 99, topPos + 45, this.imageWidth, 0, 28, 21);
        }
    }

    protected void renderErrorIcon(GuiGraphics p_281990_, int p_266822_, int p_267045_) {
    }
}

