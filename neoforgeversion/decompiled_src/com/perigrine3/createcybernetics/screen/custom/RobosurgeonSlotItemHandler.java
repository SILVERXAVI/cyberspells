/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class RobosurgeonSlotItemHandler
extends SlotItemHandler {
    private boolean active = true;
    private final CyberwareSlot slotType;

    public RobosurgeonSlotItemHandler(ItemStackHandler handler, int index, int x, int y, CyberwareSlot slotType) {
        super((IItemHandler)handler, index, x, y);
        this.slotType = slotType;
    }

    public CyberwareSlot getSlotType() {
        return this.slotType;
    }

    public void set(ItemStack stack) {
        Container container;
        super.set(stack);
        if (!stack.isEmpty() && (container = this.container) instanceof ItemStackHandler) {
            ItemStackHandler itemStackHandler = (ItemStackHandler)container;
        }
    }

    public void setActiveFlag(boolean active) {
        this.active = active;
    }

    public boolean mayPlace(ItemStack stack) {
        CyberwareSlot slotType = this.slotType;
        if (slotType == null) {
            return false;
        }
        if (DefaultOrgans.isOrganForSlot(stack, slotType)) {
            return true;
        }
        Item item = stack.getItem();
        if (item instanceof ICyberwareItem) {
            ICyberwareItem cyberware = (ICyberwareItem)item;
            return cyberware.supportsSlot(slotType);
        }
        return false;
    }

    public boolean mayPickup(Player player) {
        return true;
    }

    public boolean isActive() {
        return this.active;
    }

    public int getMaxStackSize() {
        return 1;
    }

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}

