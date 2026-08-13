/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.Pose
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;

public class ShieldPart
extends PartEntity<AbstractShieldEntity> {
    public final AbstractShieldEntity parentEntity;
    public final String name;
    private final EntityDimensions size;
    private final boolean hasCollision;

    public ShieldPart(AbstractShieldEntity shieldEntity, String name, float scaleX, float scaleY, boolean hasCollision) {
        super((Entity)shieldEntity);
        this.size = EntityDimensions.scalable((float)scaleX, (float)scaleY);
        this.refreshDimensions();
        this.parentEntity = shieldEntity;
        this.name = name;
        this.hasCollision = hasCollision;
    }

    public boolean canBeCollidedWith() {
        return this.hasCollision;
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level.isClientSide && !this.parentEntity.hurtThisTick) {
            this.parentEntity.takeDamage(pSource, pAmount, this.getBoundingBox().getCenter());
            this.parentEntity.hurtThisTick = true;
        }
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public boolean is(Entity entity) {
        return this == entity || this.parentEntity == entity;
    }

    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    public boolean shouldBeSaved() {
        return false;
    }
}

