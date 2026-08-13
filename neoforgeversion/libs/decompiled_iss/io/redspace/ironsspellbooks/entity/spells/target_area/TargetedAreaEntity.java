/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.fluids.FluidType
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.entity.spells.target_area;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

public class TargetedAreaEntity
extends Entity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(TargetedAreaEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(TargetedAreaEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FADING = SynchedEntityData.defineId(TargetedAreaEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    boolean hasOwner;
    boolean shouldFade;
    private int duration;

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
            this.hasOwner = true;
        }
    }

    @Nullable
    public Entity getOwner() {
        Level level;
        if (this.cachedOwner != null && this.cachedOwner.isAlive()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && (level = this.level) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                this.cachedOwner = livingEntity;
            }
            return this.cachedOwner;
        }
        return null;
    }

    public TargetedAreaEntity(EntityType<TargetedAreaEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius(3.0f);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static TargetedAreaEntity createTargetAreaEntity(Level level, Vec3 center, float radius, int color) {
        TargetedAreaEntity targetedAreaEntity = new TargetedAreaEntity(level, radius, color);
        targetedAreaEntity.setPos(center);
        level.addFreshEntity((Entity)targetedAreaEntity);
        return targetedAreaEntity;
    }

    public static TargetedAreaEntity createTargetAreaEntity(Level level, Vec3 center, float radius, int color, Entity owner) {
        TargetedAreaEntity targetedAreaEntity = new TargetedAreaEntity(level, radius, color);
        targetedAreaEntity.setPos(center);
        targetedAreaEntity.setOwner(owner);
        level.addFreshEntity((Entity)targetedAreaEntity);
        return targetedAreaEntity;
    }

    public void tick() {
        this.firstTick = false;
        Entity owner = this.getOwner();
        if (owner != null) {
            this.setPos(owner.position());
            this.xOld = owner.xOld;
            this.yOld = owner.yOld;
            this.zOld = owner.zOld;
            this.xo = owner.xo;
            this.yo = owner.yo;
            this.zo = owner.zo;
        }
        if (this.shouldFade && this.tickCount >= this.duration - 10) {
            this.entityData.set(DATA_FADING, (Object)true);
        }
        if (!this.level.isClientSide && (this.duration > 0 && this.tickCount > this.duration || this.duration == 0 && this.tickCount > 400 || this.hasOwner && (owner == null || owner.isRemoved()))) {
            this.discard();
        }
    }

    public TargetedAreaEntity(Level level, float radius, int color) {
        this((EntityType<TargetedAreaEntity>)((EntityType)EntityRegistry.TARGET_AREA_ENTITY.get()), level);
        this.setRadius(radius);
        this.setColor(color);
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)0.8f);
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public boolean isOnFire() {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, (Object)Float.valueOf(2.0f));
        pBuilder.define(DATA_COLOR, (Object)0xFFFFFF);
        pBuilder.define(DATA_FADING, (Object)false);
    }

    public boolean isFading() {
        return (Boolean)this.entityData.get(DATA_FADING);
    }

    public void setRadius(float pRadius) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, (Object)Float.valueOf(Mth.clamp((float)pRadius, (float)0.0f, (float)32.0f)));
        }
    }

    public void setShouldFade(boolean shouldFade) {
        this.shouldFade = shouldFade;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getRadius() {
        return ((Float)this.getEntityData().get(DATA_RADIUS)).floatValue();
    }

    public void setColor(int color) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_COLOR, (Object)color);
        }
    }

    public Vector3f getColor() {
        return Utils.deconstructRGB((Integer)this.getEntityData().get(DATA_COLOR));
    }

    public int getColorRaw() {
        return (Integer)this.getEntityData().get(DATA_COLOR);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1f) {
                this.discard();
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Radius", this.getRadius());
        tag.putInt("Color", this.getColorRaw());
        tag.putInt("Age", this.tickCount);
        tag.putBoolean("ShouldFade", this.shouldFade);
        if (this.duration > 0) {
            tag.putInt("Duration", this.duration);
        }
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setRadius(tag.getFloat("Radius"));
        this.setColor(tag.getInt("Color"));
        this.tickCount = tag.getInt("Age");
        this.shouldFade = tag.getBoolean("ShouldFade");
        if (tag.contains("Duration")) {
            this.duration = tag.getInt("Duration");
        }
        if (tag.contains("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.hasOwner = true;
        }
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket((Entity)this, pEntity, entity == null ? 0 : entity.getId());
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        Entity entity = this.level.getEntity(pPacket.getData());
        if (entity != null) {
            this.setOwner(entity);
        }
    }
}

