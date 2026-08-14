/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.SimpleContainerData
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 */
package com.maxwell.cyber_ware_port.common.container;

import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlockEntity;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ScannerMenu
extends AbstractContainerMenu {
    public final ScannerBlockEntity blockEntity;
    private final ContainerData data;

    public ScannerMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), (ContainerData)new SimpleContainerData(2));
    }

    public ScannerMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super((MenuType)ModMenuTypes.SCANNER_MENU.get(), containerId);
        this.blockEntity = (ScannerBlockEntity)entity;
        this.data = data;
        ItemStackHandler handler = this.blockEntity.getItemHandler();
        this.addSlot((Slot)new SlotItemHandler((IItemHandler)handler, 1, 35, 53));
        this.addSlot((Slot)new SlotItemHandler((IItemHandler)handler, 0, 15, 53));
        this.addSlot((Slot)new SlotItemHandler((IItemHandler)handler, 2, 137, 53));
        ScannerMenu.checkContainerDataCount((ContainerData)data, (int)2);
        this.addDataSlots(data);
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    public int getScaledProgress(int scale) {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        return maxProgress != 0 && progress != 0 ? progress * scale / maxProgress : 0;
    }

    public boolean isCrafting() {
        return this.data.get(0) > 0;
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 3 ? !this.moveItemStackTo(itemstack1, 3, 39, true) : !this.moveItemStackTo(itemstack1, 0, 3, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    public boolean stillValid(Player player) {
        return ScannerMenu.stillValid((ContainerLevelAccess)ContainerLevelAccess.create((Level)this.blockEntity.getLevel(), (BlockPos)this.blockEntity.getBlockPos()), (Player)player, (Block)((Block)ModBlocks.SCANNER.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18, 142));
        }
    }
}

