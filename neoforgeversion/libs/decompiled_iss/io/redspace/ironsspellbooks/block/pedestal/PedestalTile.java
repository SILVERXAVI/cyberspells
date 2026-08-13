/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package io.redspace.ironsspellbooks.block.pedestal;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalTile
extends BlockEntity {
    private static final String NBT_HELD_ITEM = "heldItem";
    private ItemStack heldItem = ItemStack.EMPTY;

    public PedestalTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType)BlockRegistry.PEDESTAL_TILE.get(), pWorldPosition, pBlockState);
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(ItemStack newItem) {
        this.heldItem = newItem;
        this.setChanged();
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(new ItemStack[]{this.heldItem});
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)simpleContainer);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.readNBT(pTag, pRegistries);
    }

    protected void saveAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registryAccess) {
        this.writeNBT(tag, registryAccess);
    }

    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    private CompoundTag writeNBT(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (!this.heldItem.isEmpty()) {
            nbt.put(NBT_HELD_ITEM, this.heldItem.save(pRegistries));
        } else {
            nbt.put(NBT_HELD_ITEM, (Tag)new CompoundTag());
        }
        return nbt;
    }

    private CompoundTag readNBT(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (nbt.contains(NBT_HELD_ITEM)) {
            this.heldItem = ItemStack.parseOptional((HolderLookup.Provider)pRegistries, (CompoundTag)nbt.getCompound(NBT_HELD_ITEM));
        }
        return nbt;
    }
}

