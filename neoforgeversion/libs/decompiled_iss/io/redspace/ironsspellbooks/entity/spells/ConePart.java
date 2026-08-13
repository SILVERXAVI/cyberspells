/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.Pose
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.entity.PartEntity;

public class ConePart
extends PartEntity<AbstractConeProjectile> {
    public final AbstractConeProjectile parentEntity;
    public final String name;
    private final EntityDimensions size;

    public ConePart(AbstractConeProjectile coneProjectile, String name, float scaleX, float scaleY) {
        super((Entity)coneProjectile);
        this.size = EntityDimensions.scalable((float)scaleX, (float)scaleY);
        this.refreshDimensions();
        this.parentEntity = coneProjectile;
        this.name = name;
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

