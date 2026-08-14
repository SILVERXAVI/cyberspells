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
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.container;

import com.maxwell.cyber_ware_port.common.block.blueprintchest.BlueprintChestBlockEntity;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BlueprintChestMenu
extends AbstractContainerMenu {
    private static final int CONTAINER_SLOTS = 18;
    public final BlueprintChestBlockEntity blockEntity;

    public BlueprintChestMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BlueprintChestMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super((MenuType)ModMenuTypes.BLUEPRINT_CHEST_MENU.get(), pContainerId);
        this.blockEntity = (BlueprintChestBlockEntity)entity;
        ItemStackHandler handler = this.blockEntity.getItemHandler();
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot((Slot)new SlotItemHandler(this, (IItemHandler)handler, col + row * 9, 8 + col * 18, 18 + row * 18){

                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return stack.getItem() instanceof BlueprintItem;
                    }
                });
            }
        }
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = (Slot)this.slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (pIndex < 18) {
            if (!this.moveItemStackTo(sourceStack, 18, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (sourceStack.getItem() instanceof BlueprintItem) {
            if (!this.moveItemStackTo(sourceStack, 0, 18, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return BlueprintChestMenu.stillValid((ContainerLevelAccess)ContainerLevelAccess.create((Level)this.blockEntity.getLevel(), (BlockPos)this.blockEntity.getBlockPos()), (Player)pPlayer, (Block)((Block)ModBlocks.BLUEPRINT_CHEST.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18, 68 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18, 126));
        }
    }
}

