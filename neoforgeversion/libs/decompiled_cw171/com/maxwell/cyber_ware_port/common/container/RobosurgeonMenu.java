/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ClickType
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
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.container;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
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
import org.jetbrains.annotations.NotNull;

public class RobosurgeonMenu
extends AbstractContainerMenu {
    public final RobosurgeonBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final int invHeight = 140;

    public RobosurgeonMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), (ContainerData)new SimpleContainerData(2));
    }

    public RobosurgeonMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super((MenuType)ModMenuTypes.ROBO_SURGEON_MENU.get(), pContainerId);
        Player player;
        this.blockEntity = (RobosurgeonBlockEntity)entity;
        this.levelAccess = ContainerLevelAccess.create((Level)entity.getLevel(), (BlockPos)entity.getBlockPos());
        this.addDataSlots(data);
        ItemStackHandler handler = this.blockEntity.getItemHandler();
        for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS; ++i) {
            this.addSlot((Slot)new SlotItemHandler(this, (IItemHandler)handler, i, -10000, -10000, (IItemHandler)handler){
                final /* synthetic */ IItemHandler val$handler;
                {
                    this.val$handler = iItemHandler;
                    super(arg0, arg1, arg2, arg3);
                }

                public boolean mayPlace(@NotNull ItemStack stack) {
                    if (!super.mayPlace(stack)) {
                        return false;
                    }
                    ICyberware myCw = CyberwareAPI.getCyberware(stack);
                    if (myCw == null) {
                        return true;
                    }
                    for (int j = 0; j < RobosurgeonBlockEntity.TOTAL_SLOTS; ++j) {
                        ICyberware otherCw;
                        ItemStack other;
                        if (j == this.getSlotIndex() || (other = this.val$handler.getStackInSlot(j)).isEmpty() || ((Boolean)other.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue() || (otherCw = CyberwareAPI.getCyberware(other)) == null || !myCw.isIncompatible(stack, other) && !otherCw.isIncompatible(other, stack)) continue;
                        return false;
                    }
                    int currentCount = stack.getCount();
                    for (int j = 0; j < RobosurgeonBlockEntity.TOTAL_SLOTS; ++j) {
                        ItemStack other;
                        if (j == this.getSlotIndex() || (other = this.val$handler.getStackInSlot(j)).isEmpty() || !other.is(stack.getItem()) || ((Boolean)other.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) continue;
                        currentCount += other.getCount();
                    }
                    return currentCount <= myCw.getMaxInstallAmount(stack);
                }
            });
        }
        if (!inv.player.level().isClientSide && (player = inv.player) instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            this.blockEntity.populateGhostItems(serverPlayer);
        }
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotId >= 0 && slotId < RobosurgeonBlockEntity.TOTAL_SLOTS) {
            ICyberware newCw;
            ItemStack carried;
            Slot slot = this.getSlot(slotId);
            if (slot.hasItem() && ((Boolean)slot.getItem().getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) {
                slot.set(ItemStack.EMPTY);
            }
            if (!(carried = this.getCarried()).isEmpty() && (newCw = CyberwareAPI.getCyberware(carried)) != null) {
                ItemStackHandler handler = this.blockEntity.getItemHandler();
                for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS; ++i) {
                    ICyberware existingCw;
                    boolean shouldEject;
                    ItemStack existing = handler.getStackInSlot(i);
                    if (existing.isEmpty() || !((Boolean)existing.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) continue;
                    boolean bl = shouldEject = i == slotId;
                    if (!shouldEject && (existingCw = CyberwareAPI.getCyberware(existing)) != null && (newCw.isIncompatible(carried, existing) || existingCw.isIncompatible(existing, carried))) {
                        shouldEject = true;
                    }
                    if (!shouldEject) continue;
                    this.blockEntity.getItemHandler().setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            this.blockEntity.setChanged();
        }
        super.clicked(slotId, button, clickType, player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18, 140 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18, 198));
        }
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return RobosurgeonMenu.stillValid((ContainerLevelAccess)this.levelAccess, (Player)pPlayer, (Block)((Block)ModBlocks.ROBO_SURGEON.get()));
    }

    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < RobosurgeonBlockEntity.TOTAL_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, RobosurgeonBlockEntity.TOTAL_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                ICyberware cw = CyberwareAPI.getCyberware(itemstack1);
                if (cw != null) {
                    int slotType = cw.getSlot(itemstack1);
                    boolean moved = false;
                    for (int pass = 0; pass < 2; ++pass) {
                        for (int i = slotType; i < slotType + 9; ++i) {
                            Slot targetSlot = (Slot)this.slots.get(i);
                            if (targetSlot.hasItem() && ((Boolean)targetSlot.getItem().getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) {
                                targetSlot.set(ItemStack.EMPTY);
                            }
                            if (index < RobosurgeonBlockEntity.TOTAL_SLOTS && ((Boolean)itemstack1.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) {
                                return ItemStack.EMPTY;
                            }
                            if (!targetSlot.mayPlace(itemstack1) || !this.moveItemStackTo(itemstack1, i, i + 1, false)) continue;
                            moved = true;
                            break;
                        }
                        if (moved) break;
                    }
                    if (!moved) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}

