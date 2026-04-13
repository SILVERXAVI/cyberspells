/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.block.entity;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.block.entity.ModBlockEntities;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class RobosurgeonBlockEntity
extends BlockEntity
implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(65){

        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ItemStack stack = this.getStackInSlot(slot);
            if (stack.isEmpty()) {
                RobosurgeonBlockEntity.this.staged[slot] = false;
                RobosurgeonBlockEntity.this.markedForRemoval[slot] = false;
                RobosurgeonBlockEntity.this.setChanged();
                return;
            }
            if (RobosurgeonBlockEntity.this.surgeryInProgress) {
                RobosurgeonBlockEntity.this.setChanged();
                return;
            }
            if (!RobosurgeonBlockEntity.this.installed[slot]) {
                RobosurgeonBlockEntity.this.staged[slot] = true;
                RobosurgeonBlockEntity.this.markedForRemoval[slot] = false;
            }
            RobosurgeonBlockEntity.this.setChanged();
            if (!((RobosurgeonBlockEntity)RobosurgeonBlockEntity.this).level.isClientSide) {
                RobosurgeonBlockEntity.this.level.sendBlockUpdated(RobosurgeonBlockEntity.this.getBlockPos(), RobosurgeonBlockEntity.this.getBlockState(), RobosurgeonBlockEntity.this.getBlockState(), 3);
            }
        }
    };
    private boolean surgeryInProgress = false;
    public final boolean[] installed = new boolean[65];
    public final boolean[] staged = new boolean[65];
    public final boolean[] markedForRemoval = new boolean[65];

    public RobosurgeonBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ROBOSURGEON_BLOCKENTITY.get(), pos, blockState);
    }

    public boolean isInstalled(int i) {
        return i >= 0 && i < this.installed.length && this.installed[i];
    }

    public boolean isStaged(int i) {
        return i >= 0 && i < this.staged.length && this.staged[i];
    }

    public boolean isMarkedForRemoval(int i) {
        return i >= 0 && i < this.markedForRemoval.length && this.markedForRemoval[i];
    }

    public void setInstalled(int i, boolean value) {
        if (i < 0 || i >= this.installed.length) {
            return;
        }
        this.installed[i] = value;
        if (!value) {
            this.markedForRemoval[i] = false;
        }
        this.setChanged();
    }

    public void setStaged(int i, boolean value) {
        if (i < 0 || i >= this.staged.length) {
            return;
        }
        this.staged[i] = value;
        if (!value) {
            this.markedForRemoval[i] = false;
        }
        this.setChanged();
    }

    public void toggleMarkedForRemoval(int i) {
        ItemStack stack;
        if (i < 0 || i >= this.markedForRemoval.length) {
            return;
        }
        if (!this.installed[i]) {
            return;
        }
        boolean bl = this.markedForRemoval[i] = !this.markedForRemoval[i];
        if (!this.markedForRemoval[i] || !(stack = this.inventory.getStackInSlot(i)).isEmpty()) {
            // empty if block
        }
        this.setChanged();
    }

    public void clearSlotStates() {
        for (int i = 0; i < 65; ++i) {
            this.staged[i] = false;
            this.markedForRemoval[i] = false;
        }
        this.setChanged();
    }

    public void beginSurgery() {
        this.surgeryInProgress = true;
    }

    public void endSurgery() {
        this.surgeryInProgress = false;
    }

    public void clearContents() {
        for (int i = 0; i < this.inventory.getSlots(); ++i) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void drops() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        for (int i = 0; i < this.inventory.getSlots(); ++i) {
            ItemStack stack;
            if (!this.staged[i] || (stack = this.inventory.getStackInSlot(i)).isEmpty()) continue;
            Containers.dropItemStack((Level)this.level, (double)this.worldPosition.getX(), (double)this.worldPosition.getY(), (double)this.worldPosition.getZ(), (ItemStack)stack);
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
            this.staged[i] = false;
        }
        this.setChanged();
    }

    private boolean isDefaultOrgan(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            for (int i = 0; i < slot.size; ++i) {
                ItemStack defaultStack = DefaultOrgans.get(slot, i);
                if (defaultStack.isEmpty() || !stack.is(defaultStack.getItem())) continue;
                return true;
            }
        }
        return false;
    }

    private static byte[] encode(boolean[] data) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = (byte)(data[i] ? 1 : 0);
        }
        return out;
    }

    private static void decode(byte[] src, boolean[] target) {
        int len = Math.min(src.length, target.length);
        for (int i = 0; i < len; ++i) {
            target[i] = src[i] != 0;
        }
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", (Tag)this.inventory.serializeNBT(registries));
        tag.putByteArray("Installed", RobosurgeonBlockEntity.encode(this.installed));
        tag.putByteArray("Staged", RobosurgeonBlockEntity.encode(this.staged));
        tag.putByteArray("Marked", RobosurgeonBlockEntity.encode(this.markedForRemoval));
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("Installed")) {
            RobosurgeonBlockEntity.decode(tag.getByteArray("Installed"), this.installed);
        }
        if (tag.contains("Staged")) {
            RobosurgeonBlockEntity.decode(tag.getByteArray("Staged"), this.staged);
        }
        if (tag.contains("Marked")) {
            RobosurgeonBlockEntity.decode(tag.getByteArray("Marked"), this.markedForRemoval);
        }
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    public Component getDisplayName() {
        return Component.translatable((String)"gui.robosurgeon.title");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RobosurgeonMenu(i, inventory, this);
    }

    public AbstractContainerMenu getMenu(int containerId, Inventory inventory, Player player) {
        return new RobosurgeonMenu(containerId, inventory, this);
    }
}

