/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  net.neoforged.neoforge.event.entity.ProjectileImpactEvent
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

public abstract class AbstractMagicProjectile
extends Projectile
implements AntiMagicSusceptible,
IEntityWithComplexSpawn {
    private static final EntityDataAccessor<Boolean> DATA_CURSOR_HOMING = SynchedEntityData.defineId(AbstractMagicProjectile.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_RICOCHET = SynchedEntityData.defineId(AbstractMagicProjectile.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PIERCE_LEVEL = SynchedEntityData.defineId(AbstractMagicProjectile.class, (EntityDataSerializer)EntityDataSerializers.INT);
    protected static final int EXPIRE_TIME = 300;
    protected float damage;
    protected float explosionRadius;
    @Nullable
    protected Entity cachedHomingTarget;
    @Nullable
    protected UUID homingTargetUUID;
    public Vec3 deltaMovementOld = Vec3.ZERO;

    public abstract void trailParticles();

    public abstract void impactParticles(double var1, double var3, double var5);

    public abstract float getSpeed();

    public abstract Optional<Holder<SoundEvent>> getImpactSound();

    public AbstractMagicProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation.scale((double)this.getSpeed()));
    }

    protected boolean canHitEntity(Entity pTarget) {
        Entity owner = this.getOwner();
        return super.canHitEntity(pTarget) && pTarget != owner && (owner == null || !owner.isAlliedTo(pTarget));
    }

    public void checkDespawn() {
        Level level = this.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            if (!serverLevel.getChunkSource().chunkMap.getDistanceManager().inEntityTickingRange(this.chunkPosition().toLong())) {
                this.discard();
            }
        }
    }

    public void tick() {
        super.tick();
        if (this.tickCount == 1) {
            this.deltaMovementOld = this.getDeltaMovement();
        }
        if (this.tickCount > 300) {
            this.discard();
            return;
        }
        if (this.level.isClientSide) {
            this.trailParticles();
        }
        this.handleEntityHoming();
        this.handleCursorHoming();
        this.handleHitDetection();
        this.travel();
        this.deltaMovementOld = this.getDeltaMovement();
        this.rotateWithMotion();
    }

    protected void rotateWithMotion() {
        Vec3 motion = this.getDeltaMovement();
        double speed = motion.horizontalDistance();
        this.setYRot((float)(Mth.atan2((double)motion.x, (double)motion.z) * 57.2957763671875));
        this.setXRot((float)(Mth.atan2((double)motion.y, (double)speed) * 57.2957763671875));
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        } else {
            this.xRotO = AbstractMagicProjectile.enforceRotationContinuity(this.xRotO, this.getXRot());
            this.yRotO = AbstractMagicProjectile.enforceRotationContinuity(this.yRotO, this.getYRot());
        }
    }

    protected static float enforceRotationContinuity(float currentRotation, float targetRotation) {
        while (targetRotation - currentRotation < -180.0f) {
            currentRotation -= 360.0f;
        }
        while (targetRotation - currentRotation >= 180.0f) {
            currentRotation += 360.0f;
        }
        return currentRotation;
    }

    public void handleHitDetection() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector((Entity)this, this::canHitEntity);
        if (hitresult instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult)hitresult;
            hitresult = new EntityHitResult(entityHitResult.getEntity(), entityHitResult.getEntity().getBoundingBox().clip(this.position(), this.position().add(this.getDeltaMovement())).orElse(this.position()));
        }
        if (hitresult.getType() != HitResult.Type.MISS && !((ProjectileImpactEvent)NeoForge.EVENT_BUS.post((Event)new ProjectileImpactEvent((Projectile)this, hitresult))).isCanceled()) {
            this.onHit(hitresult);
        }
    }

    public void travel() {
        this.setPos(this.position().add(this.getDeltaMovement()));
        Vec3 motion = this.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        this.setXRot(Mth.wrapDegrees((float)xRot));
        this.setYRot(Mth.wrapDegrees((float)yRot));
        if (!this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - this.getDefaultGravity(), vec34.z);
        }
    }

    public void stopEntityHoming() {
        this.homingTargetUUID = null;
        this.cachedHomingTarget = null;
    }

    protected void handleEntityHoming() {
        if (this.homingTargetUUID == null) {
            return;
        }
        Entity target = this.getHomingTarget();
        if (target == null) {
            this.homingTargetUUID = null;
            return;
        }
        if (target.isRemoved()) {
            this.stopEntityHoming();
            return;
        }
        Vec3 wantedPos = target.getBoundingBox().getCenter().add(target.getDeltaMovement());
        Vec3 newMotion = this.homeTowards(wantedPos, 0.22f);
        if (newMotion.dot(wantedPos.subtract(this.position())) < -0.25 && this.tickCount > 10) {
            this.stopEntityHoming();
        }
    }

    protected void handleCursorHoming() {
        Vec3 vec3;
        Vec3 end;
        boolean cursorHoming = this.isCursorHoming();
        if (!cursorHoming) {
            return;
        }
        float maxRange = 48.0f;
        Entity owner = this.getOwner();
        if (owner == null || this.position().distanceToSqr(owner.position()) > (double)(maxRange * maxRange)) {
            this.setCursorHoming(false);
            return;
        }
        Vec3 start = owner.getEyePosition();
        HitResult hitresult = Utils.raycastForEntity(this.level, owner, start, end = start.add(owner.getForward().scale((double)maxRange)), true, 0.5f, entity -> Utils.canHitWithRaycast(entity) && !DamageSources.isFriendlyFireBetween(entity, owner));
        if (hitresult instanceof EntityHitResult) {
            EntityHitResult entityHit = (EntityHitResult)hitresult;
            vec3 = entityHit.getEntity().getBoundingBox().getCenter();
        } else {
            vec3 = hitresult.getLocation();
        }
        Vec3 target = vec3;
        this.homeTowards(target, 0.18f);
    }

    protected Vec3 homeTowards(Vec3 target, float strength) {
        double speed = this.getDeltaMovement().length();
        Vec3 currentMotion = this.getDeltaMovement().normalize();
        Vec3 wantedMotion = target.subtract(this.position()).normalize();
        Vec3 newMotion = Utils.slerp(strength, currentMotion, wantedMotion).scale(speed);
        this.setDeltaMovement(newMotion);
        return newMotion;
    }

    protected double getDefaultGravity() {
        return 0.05;
    }

    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        if (this.canRicochet()) {
            this.doRicochet(hitresult);
        }
        if (!this.level.isClientSide) {
            Vec3 vec = hitresult.getLocation();
            this.impactParticles(vec.x, vec.y, vec.z);
            this.getImpactSound().ifPresent(this::doImpactSound);
        }
    }

    public boolean shouldBeSaved() {
        return super.shouldBeSaved() && !Objects.equals(this.getRemovalReason(), Entity.RemovalReason.UNLOADED_TO_CHUNK);
    }

    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 2.0f, 0.9f + Utils.random.nextFloat() * 0.2f);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_CURSOR_HOMING, (Object)false);
        pBuilder.define(DATA_RICOCHET, (Object)false);
        pBuilder.define(DATA_PIERCE_LEVEL, (Object)0);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.impactParticles(this.getX(), this.getY(), this.getZ());
        this.discard();
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", this.getDamage());
        if (this.explosionRadius != 0.0f) {
            tag.putFloat("ExplosionRadius", this.explosionRadius);
        }
        if (this.getPierceLevel() != 0) {
            tag.putInt("PierceLevel", this.getPierceLevel());
        }
        if (this.homingTargetUUID != null) {
            tag.putUUID("homingTarget", this.homingTargetUUID);
        }
        if (this.canRicochet()) {
            tag.putBoolean("ricochet", true);
        }
        tag.putInt("Age", this.tickCount);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.damage = tag.getFloat("Damage");
        if (tag.contains("ExplosionRadius")) {
            this.explosionRadius = tag.getFloat("ExplosionRadius");
        }
        if (tag.contains("PierceLevel")) {
            this.setPierceLevel(tag.getInt("PierceLevel"));
        }
        if (tag.contains("homingTarget", 11)) {
            this.homingTargetUUID = tag.getUUID("homingTarget");
        }
        if (tag.contains("ricochet")) {
            this.setCanRicochet(tag.getBoolean("ricochet"));
        }
        this.tickCount = tag.getInt("Age");
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.shouldPierceShields() && (pResult.getEntity() instanceof ShieldPart || pResult.getEntity() instanceof AbstractShieldEntity)) {
            this.onHitBlock(new BlockHitResult(pResult.getEntity().position(), Direction.fromYRot((double)this.getYRot()), pResult.getEntity().blockPosition(), false));
        }
    }

    public void discardHelper(HitResult hitresult) {
        if (hitresult.getType() == HitResult.Type.ENTITY) {
            this.pierceOrDiscard();
        } else {
            this.discard();
        }
    }

    public void pierceOrDiscard() {
        int p = this.getPierceLevel();
        if (p > 0) {
            this.setPierceLevel(p - 1);
        } else if (p == 0) {
            this.discard();
        }
    }

    public void doRicochet(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            Vec3 deltaMovement = this.getDeltaMovement();
            Vec3 vec = deltaMovement.normalize();
            Entity owner = this.getOwner();
            Entity hit = entityHitResult.getEntity();
            List potentialTargets = this.level.getEntities((Entity)this, this.getBoundingBox().inflate(3.0).expandTowards(deltaMovement.scale(12.0)), entity -> entity != hit && (owner == null || !Utils.shouldHealEntity(owner, entity) || entity.getClass() == hit.getClass()) && entity.getBoundingBox().getCenter().subtract(this.position()).normalize().dot(vec) > 0.6 && Utils.hasLineOfSight(this.level, (Entity)this, entity, false));
            if (potentialTargets.isEmpty()) {
                return;
            }
            Entity target = (Entity)potentialTargets.get(this.getId() % potentialTargets.size());
            this.setDeltaMovement(target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(deltaMovement.length()));
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getExplosionRadius() {
        return this.explosionRadius;
    }

    public void setExplosionRadius(float explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public int getPierceLevel() {
        return (Integer)this.entityData.get(DATA_PIERCE_LEVEL);
    }

    public void setPierceLevel(int pierceLevel) {
        this.entityData.set(DATA_PIERCE_LEVEL, (Object)pierceLevel);
    }

    public void setInfinitePiercing() {
        this.setPierceLevel(-1);
    }

    @Nullable
    public Entity getHomingTarget() {
        if (this.cachedHomingTarget != null && !this.cachedHomingTarget.isRemoved()) {
            return this.cachedHomingTarget;
        }
        if (this.homingTargetUUID != null && this.level instanceof ServerLevel) {
            this.cachedHomingTarget = ((ServerLevel)this.level).getEntity(this.homingTargetUUID);
            return this.cachedHomingTarget;
        }
        return null;
    }

    public void setHomingTarget(LivingEntity entity) {
        this.homingTargetUUID = entity.getUUID();
        this.cachedHomingTarget = entity;
        this.setCursorHoming(false);
    }

    public boolean isCursorHoming() {
        return (Boolean)this.entityData.get(DATA_CURSOR_HOMING);
    }

    public void setCursorHoming(boolean cursorHoming) {
        this.entityData.set(DATA_CURSOR_HOMING, (Object)cursorHoming);
        if (cursorHoming) {
            this.stopEntityHoming();
        }
    }

    public boolean canRicochet() {
        return (Boolean)this.entityData.get(DATA_RICOCHET);
    }

    public void setCanRicochet(boolean ricochet) {
        this.entityData.set(DATA_RICOCHET, (Object)ricochet);
    }

    public boolean isOnFire() {
        return false;
    }

    protected boolean shouldPierceShields() {
        return false;
    }

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        Entity owner = this.getOwner();
        buffer.writeInt(owner == null ? 0 : owner.getId());
        Entity homingTarget = this.getHomingTarget();
        buffer.writeInt(homingTarget == null ? 0 : homingTarget.getId());
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        Entity homingTarget;
        Entity owner = this.level.getEntity(additionalData.readInt());
        if (owner != null) {
            this.setOwner(owner);
        }
        if ((homingTarget = this.level.getEntity(additionalData.readInt())) != null) {
            this.cachedHomingTarget = homingTarget;
            this.homingTargetUUID = homingTarget.getUUID();
        }
    }
}

