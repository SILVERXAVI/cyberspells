/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package com.perigrine3.createcybernetics.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GuardianBeamEntity
extends Entity {
    private static final EntityDataAccessor<Integer> SHOOTER_ID = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> START_X = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> START_Y = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> START_Z = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> END_X = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> END_Y = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> END_Z = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(GuardianBeamEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);

    public GuardianBeamEntity(EntityType<? extends GuardianBeamEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SHOOTER_ID, (Object)0);
        builder.define(LIFE, (Object)10);
        builder.define(START_X, (Object)Float.valueOf(0.0f));
        builder.define(START_Y, (Object)Float.valueOf(0.0f));
        builder.define(START_Z, (Object)Float.valueOf(0.0f));
        builder.define(END_X, (Object)Float.valueOf(0.0f));
        builder.define(END_Y, (Object)Float.valueOf(0.0f));
        builder.define(END_Z, (Object)Float.valueOf(0.0f));
        builder.define(POWER, (Object)Float.valueOf(1.0f));
        builder.define(CHARGING, (Object)false);
    }

    public void tick() {
        super.tick();
        int life = this.getLife() - 1;
        this.setLife(life);
        if (life <= 0) {
            this.discard();
        }
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public int getShooterId() {
        return (Integer)this.entityData.get(SHOOTER_ID);
    }

    public void setShooterId(int id) {
        this.entityData.set(SHOOTER_ID, (Object)id);
    }

    public int getLife() {
        return (Integer)this.entityData.get(LIFE);
    }

    public void setLife(int ticks) {
        this.entityData.set(LIFE, (Object)ticks);
    }

    public Vec3 getStart() {
        return new Vec3((double)((Float)this.entityData.get(START_X)).floatValue(), (double)((Float)this.entityData.get(START_Y)).floatValue(), (double)((Float)this.entityData.get(START_Z)).floatValue());
    }

    public void setStart(Vec3 v) {
        this.entityData.set(START_X, (Object)Float.valueOf((float)v.x));
        this.entityData.set(START_Y, (Object)Float.valueOf((float)v.y));
        this.entityData.set(START_Z, (Object)Float.valueOf((float)v.z));
    }

    public Vec3 getEnd() {
        return new Vec3((double)((Float)this.entityData.get(END_X)).floatValue(), (double)((Float)this.entityData.get(END_Y)).floatValue(), (double)((Float)this.entityData.get(END_Z)).floatValue());
    }

    public void setEnd(Vec3 v) {
        this.entityData.set(END_X, (Object)Float.valueOf((float)v.x));
        this.entityData.set(END_Y, (Object)Float.valueOf((float)v.y));
        this.entityData.set(END_Z, (Object)Float.valueOf((float)v.z));
    }

    public float getPower() {
        return ((Float)this.entityData.get(POWER)).floatValue();
    }

    public void setPower(float power) {
        this.entityData.set(POWER, (Object)Float.valueOf(power));
    }

    public boolean isCharging() {
        return (Boolean)this.entityData.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(CHARGING, (Object)charging);
    }
}

