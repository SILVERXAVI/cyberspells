/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.control.FlyingMoveControl
 *  net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.spells.firefly_swarm;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FireflySwarmProjectile
extends PathfinderMob
implements AntiMagicSusceptible {
    static final int maxLife = 200;
    public static final float radius = 2.0f;
    UUID targetUUID;
    Entity cachedTarget;
    UUID ownerUUID;
    Entity cachedOwner;
    Entity nextTarget;
    float damage;

    public FireflySwarmProjectile(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl((Mob)this, 15, true);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public FireflySwarmProjectile(Level level, @javax.annotation.Nullable Entity owner, @javax.annotation.Nullable Entity target, float damage) {
        this((EntityType<? extends PathfinderMob>)((EntityType)EntityRegistry.FIREFLY_SWARM.get()), level);
        this.setOwner(owner);
        this.setTarget(target);
        this.damage = damage;
    }

    public boolean isPickable() {
        return false;
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new FlyingPathNavigation((Mob)this, pLevel);
    }

    public void tick() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                Vec3 motion = Utils.getRandomVec3(0.05f).add(this.getDeltaMovement());
                Vec3 spawn = Utils.getRandomVec3(0.25);
                this.level.addParticle(ParticleHelper.FIREFLY, this.getX() + spawn.x, this.getY() + (double)(this.getBbHeight() * 0.5f) + spawn.z, this.getZ() + spawn.z, motion.x, motion.y, motion.z);
            }
        }
        super.tick();
        if (this.tickCount > 200) {
            this.discard();
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.navigation.moveTo((Entity)target, 7.0);
        }
        if (this.tickCount % 8 == 0) {
            if (this.level.collidesWithSuffocatingBlock((Entity)this, this.getBoundingBox().move(0.0, -1.0, 0.0))) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.02, 0.0));
            } else {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.008, 0.0));
            }
        }
        if (!this.moveControl.hasWanted()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.95f, 1.0, (double)0.95f));
        }
        if (this.tickCount % 7 == 0) {
            float fade = 1.0f - Mth.clamp((float)((float)(this.tickCount - 200 + 40) / 200.0f), (float)0.0f, (float)1.0f);
            this.playSound((SoundEvent)SoundRegistry.FIREFLY_SWARM_IDLE.get(), 0.25f * fade, 0.95f + Utils.random.nextFloat() * 0.1f);
        }
        if (this.tickCount % 15 == 0) {
            float inflate = 2.0f - this.getBbWidth() * 0.5f;
            this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)inflate), this::canHitEntity).forEach(entity -> {
                boolean hit;
                if (this.canHitEntity((Entity)entity) && (hit = DamageSources.applyDamage(entity, this.damage, SpellRegistry.FIREFLY_SWARM_SPELL.get().getDamageSource((Entity)this, this.getOwner())))) {
                    this.playSound((SoundEvent)SoundRegistry.FIREFLY_SWARM_ATTACK.get(), 0.75f, 0.9f + Utils.random.nextFloat() * 0.2f);
                    if (target == null) {
                        this.setTarget((Entity)entity);
                    } else if (target != entity) {
                        this.nextTarget = entity;
                    }
                }
            });
            if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
                this.setTarget(this.nextTarget);
                if (this.nextTarget != null && this.nextTarget.isRemoved()) {
                    this.nextTarget = null;
                }
            }
        }
    }

    protected boolean canHitEntity(Entity target) {
        if (!target.isSpectator() && target.isAlive() && target.isPickable()) {
            Entity owner = this.getOwner();
            return owner != target && !DamageSources.isFriendlyFireBetween(owner, target);
        }
        return false;
    }

    public void setOwner(@javax.annotation.Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    @javax.annotation.Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        return null;
    }

    @Nullable
    public LivingEntity getTarget() {
        LivingEntity livingEntity;
        Entity entity = this.getFireflyTarget();
        return entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
    }

    public void setTarget(@javax.annotation.Nullable Entity target) {
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    @javax.annotation.Nullable
    public Entity getFireflyTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        }
        if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel)this.level).getEntity(this.targetUUID);
            return this.cachedTarget;
        }
        return null;
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.targetUUID != null) {
            pCompound.putUUID("Target", this.targetUUID);
        }
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.putInt("Age", this.tickCount);
        pCompound.putFloat("Damage", this.damage);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Target")) {
            this.targetUUID = pCompound.getUUID("Target");
        }
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.tickCount = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}

