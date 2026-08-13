/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.entity.spells.portal;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.damage.PortalDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PortalEntity
extends Entity
implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_PORTAL_CONNECTED = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private Object2ObjectMap<UUID, LoopTrackerData> loopTrackerLookup = new Object2ObjectOpenHashMap();
    private final int loopMax = 5;
    private final int loopTickWindow = 20;
    private long ticksToLive = 0L;
    private boolean isPortalConnected = false;

    public PortalEntity(Level level, PortalData portalData) {
        this((EntityType<? extends PortalEntity>)((EntityType)EntityRegistry.PORTAL.get()), level);
        PortalManager.INSTANCE.addPortalData(this.uuid, portalData);
        this.ticksToLive = portalData.ticksToLive;
    }

    public PortalEntity(EntityType<? extends PortalEntity> portalEntityEntityType, Level level) {
        super(portalEntityEntityType, level);
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    public boolean clearPortalOnDeath() {
        return true;
    }

    public void onRemovedFromLevel() {
        if (!this.level.isClientSide && this.clearPortalOnDeath()) {
            Entity.RemovalReason removalReason = this.getRemovalReason();
            if (removalReason != null && removalReason.shouldDestroy()) {
                PortalManager.INSTANCE.killPortal(this.uuid, this.getOwnerUUID());
            }
            MagicManager.spawnParticles(this.level, new SparkParticleOptions(new Vector3f(0.5f, 0.05f, 0.6f)), this.getX(), this.getY() + 0.5, this.getZ(), 25, 0.2, 0.4, 0.2, 0.3, false);
        }
        super.onRemovedFromLevel();
    }

    private void handleLoopTracking(Entity entity) {
        LoopTrackerData trackerData = (LoopTrackerData)this.loopTrackerLookup.get((Object)entity.getUUID());
        if (trackerData == null) {
            trackerData = new LoopTrackerData(this, this.level.getGameTime(), 1);
            this.loopTrackerLookup.put((Object)entity.getUUID(), (Object)trackerData);
        } else {
            IronsSpellbooks.LOGGER.debug("looping");
            if (++trackerData.loopCount > 5 && this.level.getGameTime() - trackerData.gameTick <= 20L) {
                if (this.getOwnerUUID().equals(entity.getUUID())) {
                    LivingEntity livingEntity;
                    entity.hurt((DamageSource)new PortalDamageSource((Holder<DamageType>)entity.level().damageSources().genericKill().typeHolder(), entity), Float.MAX_VALUE);
                    if (entity instanceof LivingEntity && Float.isNaN((livingEntity = (LivingEntity)entity).getHealth())) {
                        livingEntity.setHealth(0.0f);
                    }
                }
                this.discard();
            } else if (this.level.getGameTime() - trackerData.gameTick > 20L) {
                this.loopTrackerLookup.remove((Object)entity.getUUID());
            }
        }
    }

    public void checkForEntitiesToTeleport() {
        if (this.level.isClientSide) {
            return;
        }
        this.level.getEntities((Entity)null, this.getBoundingBox(), entity -> !entity.getType().is(ModTags.CANT_USE_PORTAL) && (entity.isPickable() || entity instanceof Projectile) && !entity.isVehicle() && !entity.isSpectator()).forEach(entity -> {
            PortalManager.INSTANCE.processDelayCooldown(this.uuid, entity.getUUID(), 1);
            if (PortalManager.INSTANCE.canUsePortal(this, (Entity)entity)) {
                PortalManager.INSTANCE.addPortalCooldown((Entity)entity, this.uuid);
                PortalData portalData = PortalManager.INSTANCE.getPortalData(this);
                portalData.getConnectedPortalPos(this.uuid).ifPresent(portalPos -> {
                    Vec3 destination = portalPos.pos().add(0.0, entity.getY() - this.getY(), 0.0);
                    entity.setYRot(portalPos.rotation());
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    if (this.level.dimension().equals(portalPos.dimension())) {
                        entity.teleportTo(destination.x, destination.y + 0.1, destination.z);
                        Vec3 delta = entity.getDeltaMovement();
                        float hspeed = (float)Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                        float f = portalPos.rotation() * ((float)Math.PI / 180);
                        entity.setDeltaMovement((double)(-Mth.sin((float)f) * hspeed), delta.y, (double)(Mth.cos((float)f) * hspeed));
                        this.handleLoopTracking((Entity)entity);
                    } else {
                        ServerLevel dim;
                        MinecraftServer server = this.level.getServer();
                        if (server != null && (dim = server.getLevel(portalPos.dimension())) != null) {
                            entity.changeDimension(new DimensionTransition(dim, destination, Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
                        }
                    }
                    this.level.playSound(null, destination.x, destination.y, destination.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0f, 1.0f);
                });
            }
        });
    }

    private Vec3 getDestinationPosition(PortalPos globalPos, Entity entity) {
        Vec3 offset = new Vec3(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
        return new Vec3(globalPos.pos().x - offset.x, globalPos.pos().y - offset.y, globalPos.pos().z - offset.z);
    }

    public void setTicksToLive(int ticksToLive) {
        this.ticksToLive = ticksToLive;
    }

    public void tick() {
        if (this.level.isClientSide) {
            Vec3 center = this.getBoundingBox().getCenter();
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleHelper.PORTAL_FRAME, center.x, center.y, center.z, 1.0, (double)2.1f, (double)this.getYRot());
            }
            return;
        }
        PortalManager.INSTANCE.processCooldownTick(this.uuid, -1);
        this.checkForEntitiesToTeleport();
        if (--this.ticksToLive <= 0L) {
            this.discard();
        }
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_ID_OWNER_UUID, Optional.ofNullable(uuid));
    }

    public UUID getOwnerUUID() {
        return ((Optional)this.entityData.get(DATA_ID_OWNER_UUID)).orElseGet(() -> ((Optional)this.entityData.get(DATA_ID_OWNER_UUID)).orElse(null));
    }

    public void setPortalConnected() {
        this.entityData.set(DATA_PORTAL_CONNECTED, (Object)true);
    }

    public boolean getPortalConnected() {
        return (Boolean)this.entityData.get(DATA_PORTAL_CONNECTED);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_ID_OWNER_UUID, Optional.empty());
        pBuilder.define(DATA_PORTAL_CONNECTED, (Object)false);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (!this.level.isClientSide) {
            return;
        }
        if (pKey.id() == DATA_PORTAL_CONNECTED.id()) {
            this.isPortalConnected = this.getPortalConnected();
        }
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        PortalData portalData;
        if (compoundTag.contains("ownerUUID")) {
            this.setOwnerUUID(compoundTag.getUUID("ownerUUID"));
        }
        if (compoundTag.contains("ticksToLive")) {
            this.ticksToLive = compoundTag.getLong("ticksToLive");
        }
        if ((portalData = PortalManager.INSTANCE.getPortalData(this)) == null) {
            this.ticksToLive = 0L;
        } else if (portalData.portalEntityId1 != null && portalData.portalEntityId2 != null) {
            this.setPortalConnected();
        }
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putLong("ticksToLive", this.ticksToLive);
        compoundTag.putUUID("ownerUUID", this.getOwnerUUID());
    }

    public class LoopTrackerData {
        public long gameTick;
        public int loopCount;

        public LoopTrackerData(PortalEntity this$0, long gameTick, int loopCount) {
            this.gameTick = gameTick;
            this.loopCount = loopCount;
        }
    }
}

