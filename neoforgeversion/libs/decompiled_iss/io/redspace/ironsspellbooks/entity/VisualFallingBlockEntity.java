/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.item.FallingBlockEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 */
package io.redspace.ironsspellbooks.entity;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated(forRemoval=true)
public class VisualFallingBlockEntity
extends FallingBlockEntity {
    int maxAge = 200;
    private double originalX;
    private double originalY;
    private double originalZ;
    private double ticks;
    private boolean particlesOnImpact;

    public VisualFallingBlockEntity(EntityType<? extends VisualFallingBlockEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setOnGround(boolean pOnGround) {
    }

    public boolean onGround() {
        return this.tickCount > 1 && this.getDeltaMovement().lengthSqr() < (double)0.001f;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState) {
        this((EntityType<? extends VisualFallingBlockEntity>)((EntityType)EntityRegistry.FALLING_BLOCK.get()), pLevel);
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.ticks = 0.0;
        this.blocksBuilding = false;
        this.blockState = pState;
        this.setPos(pX + 0.5, pY, pZ + 0.5);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
        this.setStartPos(this.blockPosition());
        this.dropItem = false;
        this.cancelDrop = true;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState, int maxAge) {
        this(pLevel, pX, pY, pZ, pState);
        this.maxAge = maxAge;
    }

    public VisualFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState, int maxAge, boolean particlesOnImpact) {
        this(pLevel, pX, pY, pZ, pState, maxAge);
        this.particlesOnImpact = particlesOnImpact;
    }

    public void tick() {
        boolean onGround = this.onGround();
        if (this.blockState.isAir() || onGround || this.tickCount > this.maxAge) {
            if (onGround) {
                this.callOnBrokenAfterFall(this.level.getBlockState(this.blockPosition().below()).getBlock(), this.blockPosition());
            }
            this.discard();
        } else {
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!this.isNoGravity() && !this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.08, 0.0));
            }
        }
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.dropItem = false;
        this.cancelDrop = true;
    }

    public boolean isPickable() {
        return false;
    }

    public void callOnBrokenAfterFall(Block pBlock, BlockPos pPos) {
        if (!this.level.isClientSide && this.particlesOnImpact) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, this.blockState), this.getX(), this.getY(), this.getZ(), 25, 0.25, 0.25, 0.25, 0.04, false);
        }
    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }
}

