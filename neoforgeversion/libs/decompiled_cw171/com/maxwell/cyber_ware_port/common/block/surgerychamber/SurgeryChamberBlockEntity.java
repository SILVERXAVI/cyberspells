/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.surgerychamber;

import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlock;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SurgeryChamberBlockEntity
extends BlockEntity {
    public float animationProgress = 0.0f;
    public float prevAnimationProgress = 0.0f;

    public SurgeryChamberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.SURGERY_CHAMBER.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SurgeryChamberBlockEntity entity) {
        float target;
        entity.prevAnimationProgress = entity.animationProgress;
        boolean isOpen = (Boolean)state.getValue((Property)SurgeryChamberBlock.OPEN);
        float f = target = isOpen ? 1.0f : 0.0f;
        if (entity.animationProgress < target) {
            entity.animationProgress = Math.min(entity.animationProgress + 0.1f, target);
        } else if (entity.animationProgress > target) {
            entity.animationProgress = Math.max(entity.animationProgress - 0.1f, target);
        }
    }

    public void onLoad() {
        super.onLoad();
        if (this.level != null) {
            boolean isOpen = (Boolean)this.getBlockState().getValue((Property)SurgeryChamberBlock.OPEN);
            this.prevAnimationProgress = this.animationProgress = isOpen ? 1.0f : 0.0f;
        }
    }

    public boolean isOpen() {
        return this.level != null && (Boolean)this.getBlockState().getValue((Property)SurgeryChamberBlock.OPEN) != false;
    }

    public void setDoorState(boolean open) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        BlockState currentState = this.getBlockState();
        if ((Boolean)currentState.getValue((Property)SurgeryChamberBlock.OPEN) != open) {
            this.level.setBlock(this.worldPosition, (BlockState)currentState.setValue((Property)SurgeryChamberBlock.OPEN, (Comparable)Boolean.valueOf(open)), 3);
            this.level.playSound(null, this.worldPosition, open ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.2f);
            this.level.playSound(null, this.worldPosition, open ? SoundEvents.PISTON_EXTEND : SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5f, 1.2f);
            BlockPos abovePos = this.worldPosition.above();
            BlockState aboveState = this.level.getBlockState(abovePos);
            if (aboveState.is(currentState.getBlock())) {
                this.level.setBlock(abovePos, (BlockState)aboveState.setValue((Property)SurgeryChamberBlock.OPEN, (Comparable)Boolean.valueOf(open)), 3);
            }
            this.setChanged();
        }
    }

    public void toggleDoor() {
        this.setDoorState(!this.isOpen());
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putFloat("AnimationProgress", this.animationProgress);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("AnimationProgress")) {
            this.prevAnimationProgress = this.animationProgress = pTag.getFloat("AnimationProgress");
        }
    }

    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }
}

