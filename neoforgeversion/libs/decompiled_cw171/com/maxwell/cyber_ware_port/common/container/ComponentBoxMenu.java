/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ClickType
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.container;

import com.maxwell.cyber_ware_port.common.block.component_box.ComponentBoxBlockEntity;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ComponentBoxMenu
extends AbstractContainerMenu {
    private final ItemStack lockedStack;
    private final int lockedSlotIndex;

    public ComponentBoxMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
        super((MenuType)ModMenuTypes.COMPONENT_BOX_MENU.get(), id);
        boolean mainHand = extraData.readBoolean();
        this.lockedStack = playerInv.player.getItemInHand(mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        this.lockedSlotIndex = mainHand ? playerInv.selected : 40;
        IItemHandler handler = (IItemHandler)this.lockedStack.getCapability(Capabilities.ItemHandler.ITEM);
        this.addBoxSlots((IItemHandler)(handler != null ? handler : new ItemStackHandler(18)));
        this.addPlayerInventory(playerInv);
    }

    public ComponentBoxMenu(int id, Inventory playerInv, ItemStack boxStack) {
        super((MenuType)ModMenuTypes.COMPONENT_BOX_MENU.get(), id);
        this.lockedStack = boxStack;
        this.lockedSlotIndex = playerInv.player.getMainHandItem() == boxStack ? playerInv.selected : 40;
        IItemHandler handler = (IItemHandler)boxStack.getCapability(Capabilities.ItemHandler.ITEM);
        this.addBoxSlots((IItemHandler)(handler != null ? handler : new ItemStackHandler(18)));
        this.addPlayerInventory(playerInv);
    }

    public ComponentBoxMenu(int id, Inventory playerInv, ComponentBoxBlockEntity blockEntity) {
        super((MenuType)ModMenuTypes.COMPONENT_BOX_MENU.get(), id);
        this.lockedStack = ItemStack.EMPTY;
        this.lockedSlotIndex = -1;
        this.addBoxSlots((IItemHandler)blockEntity.getItemHandler());
        this.addPlayerInventory(playerInv);
    }

    private void addBoxSlots(IItemHandler handler) {
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot((Slot)new SlotItemHandler(handler, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }
    }

    private void addPlayerInventory(Inventory playerInv) {
        int yOffset = 68;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)playerInv, col + row * 9 + 9, 8 + col * 18, yOffset + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot((Container)playerInv, col, 8 + col * 18, yOffset + 58));
        }
    }

    public boolean stillValid(@NotNull Player player) {
        return this.lockedStack.isEmpty() || player.getInventory().contains(this.lockedStack);
    }

    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (this.lockedSlotIndex >= 0) {
            if (slotId >= 0 && slotId < this.slots.size() && ((Slot)this.slots.get(slotId)).getItem() == this.lockedStack) {
                return;
            }
            if (clickType == ClickType.SWAP && button == this.lockedSlotIndex) {
                return;
            }
        }
        super.clicked(slotId, button, clickType, player);
    }

    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 18 ? !this.moveItemStackTo(itemstack1, 18, 54, true) : !this.moveItemStackTo(itemstack1, 0, 18, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}

