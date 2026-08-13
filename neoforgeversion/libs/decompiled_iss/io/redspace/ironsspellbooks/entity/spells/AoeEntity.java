/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Explosion
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.fluids.FluidType
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class AoeEntity
extends Projectile
implements NoKnockbackProjectile {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AoeEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_CIRCULAR = SynchedEntityData.defineId(AoeEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_DELAY = SynchedEntityData.defineId(AoeEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    protected float damage;
    protected int duration = 600;
    protected int reapplicationDelay = 10;
    protected int durationOnUse;
    protected float radiusOnUse;
    protected float radiusPerTick;
    protected int effectDuration;

    public int getReapplicationDelay() {
        return this.reapplicationDelay;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public float getRadiusPerTick() {
        return this.radiusPerTick;
    }

    public void setReapplicationDelay(int reapplicationDelay) {
        this.reapplicationDelay = reapplicationDelay;
    }

    public void setDurationOnUse(int durationOnUse) {
        this.durationOnUse = durationOnUse;
    }

    public void setRadiusOnUse(float radiusOnUse) {
        this.radiusOnUse = radiusOnUse;
    }

    public void setRadiusPerTick(float radiusPerTick) {
        this.radiusPerTick = radiusPerTick;
    }

    public AoeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.blocksBuilding = false;
    }

    protected float particleYOffset() {
        return 0.0f;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    public int getEffectDuration() {
        return this.effectDuration;
    }

    public void tick() {
        super.tick();
        if (this.tickCount > this.getDelay()) {
            if (!this.level.isClientSide) {
                if (this.tickCount > this.duration + this.getDelay()) {
                    this.discard();
                    return;
                }
                if (this.tickCount % this.reapplicationDelay == 0) {
                    this.checkHits();
                }
                if (this.tickCount % 5 == 0) {
                    this.setRadius(this.getRadius() + this.radiusPerTick);
                }
            } else {
                this.ambientParticles();
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
    }

    protected void checkHits() {
        if (this.level.isClientSide) {
            return;
        }
        List targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getInflation().x, this.getInflation().y, this.getInflation().z));
        boolean hit = false;
        float radiusSqr = this.getRadius();
        radiusSqr *= radiusSqr;
        for (LivingEntity target : targets) {
            if (!this.canHitEntity((Entity)target) || this.isCircular().booleanValue() && !(target.distanceToSqr((Entity)this) < (double)radiusSqr) || !this.canHitTargetForGroundContext(target)) continue;
            this.applyEffect(target);
            hit = true;
        }
        if (hit) {
            this.setRadius(this.getRadius() + this.radiusOnUse);
            this.duration += this.durationOnUse;
            this.onPostHit();
        }
    }

    protected Vec3 getInflation() {
        return Vec3.ZERO;
    }

    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return target.onGround() || target.getY() - this.getY() < 0.5;
    }

    protected boolean canHitEntity(Entity pTarget) {
        return (this.getOwner() == null || pTarget != this.getOwner() && !this.getOwner().isAlliedTo(pTarget)) && super.canHitEntity(pTarget);
    }

    public void onPostHit() {
    }

    public abstract void applyEffect(LivingEntity var1);

    public void ambientParticles(ParticleOptions particle) {
        float f = this.getParticleCount();
        f = Mth.clamp((float)(f * this.getRadius()), (float)(f / 4.0f), (float)(f * 10.0f));
        int i = 0;
        while ((float)i < f) {
            Vec3 pos;
            if (f - (float)i < 1.0f && this.random.nextFloat() > f - (float)i) {
                return;
            }
            float r = this.getRadius();
            if (this.isCircular().booleanValue()) {
                float distance = r * (1.0f - this.random.nextFloat() * this.random.nextFloat());
                float theta = this.random.nextFloat() * 6.282f;
                pos = new Vec3((double)(distance * Mth.cos((float)theta)), (double)0.2f, (double)(distance * Mth.sin((float)theta)));
            } else {
                pos = new Vec3(Utils.getRandomScaled(r * 0.85f), (double)0.2f, Utils.getRandomScaled(r * 0.85f));
            }
            Vec3 motion = new Vec3(Utils.getRandomScaled(0.03f), this.random.nextDouble() * (double)0.01f, Utils.getRandomScaled(0.03f)).scale((double)this.getParticleSpeedModifier());
            Vec3 vec3 = new Vec3(this.getX() + pos.x, this.getY() + pos.y + 1.0, this.getZ() + pos.z);
            vec3 = Utils.moveToRelativeGroundLevel(this.level, vec3, 1, 2);
            this.level.addParticle(particle, vec3.x, vec3.y + (double)this.particleYOffset(), vec3.z, motion.x, motion.y, motion.z);
            ++i;
        }
    }

    public void ambientParticles() {
        if (!this.level.isClientSide) {
            return;
        }
        this.getParticle().ifPresent(this::ambientParticles);
    }

    protected float getParticleSpeedModifier() {
        return 1.0f;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public boolean isOnFire() {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, (Object)Float.valueOf(2.0f));
        pBuilder.define(DATA_CIRCULAR, (Object)false);
        pBuilder.define(DATA_DELAY, (Object)0);
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

    public void setRadius(float pRadius) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, (Object)Float.valueOf(Mth.clamp((float)pRadius, (float)0.0f, (float)32.0f)));
        }
    }

    public void setDuration(int duration) {
        if (!this.level.isClientSide) {
            this.duration = duration;
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDelay(int delay) {
        if (!this.level.isClientSide) {
            this.entityData.set(DATA_DELAY, (Object)delay);
        }
    }

    public int getDelay() {
        return (Integer)this.entityData.get(DATA_DELAY);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public float getRadius() {
        return ((Float)this.getEntityData().get(DATA_RADIUS)).floatValue();
    }

    public Boolean isCircular() {
        return (Boolean)this.getEntityData().get(DATA_CIRCULAR);
    }

    public void setCircular() {
        this.getEntityData().set(DATA_CIRCULAR, (Object)true);
    }

    public abstract float getParticleCount();

    public abstract Optional<ParticleOptions> getParticle();

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)1.2f);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Age", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("ReapplicationDelay", this.reapplicationDelay);
        pCompound.putInt("DurationOnUse", this.durationOnUse);
        pCompound.putFloat("RadiusOnUse", this.radiusOnUse);
        pCompound.putFloat("RadiusPerTick", this.radiusPerTick);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putBoolean("Circular", this.isCircular().booleanValue());
        pCompound.putInt("EffectDuration", this.effectDuration);
        pCompound.putInt("Delay", this.getDelay());
        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        if (pCompound.getInt("Duration") > 0) {
            this.duration = pCompound.getInt("Duration");
        }
        if (pCompound.getInt("ReapplicationDelay") > 0) {
            this.reapplicationDelay = pCompound.getInt("ReapplicationDelay");
        }
        if (pCompound.getInt("Radius") > 0) {
            this.setRadius(pCompound.getFloat("Radius"));
        }
        if (pCompound.getInt("DurationOnUse") > 0) {
            this.durationOnUse = pCompound.getInt("DurationOnUse");
        }
        if (pCompound.getInt("RadiusOnUse") > 0) {
            this.radiusOnUse = pCompound.getFloat("RadiusOnUse");
        }
        if (pCompound.getInt("RadiusPerTick") > 0) {
            this.radiusPerTick = pCompound.getFloat("RadiusPerTick");
        }
        if (pCompound.getInt("EffectDuration") > 0) {
            this.effectDuration = pCompound.getInt("EffectDuration");
        }
        if (pCompound.getInt("Delay") > 0) {
            this.setDelay(pCompound.getInt("Delay"));
        }
        this.setDamage(pCompound.getFloat("Damage"));
        if (pCompound.getBoolean("Circular")) {
            this.setCircular();
        }
        super.readAdditionalSaveData(pCompound);
    }

    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }
}

