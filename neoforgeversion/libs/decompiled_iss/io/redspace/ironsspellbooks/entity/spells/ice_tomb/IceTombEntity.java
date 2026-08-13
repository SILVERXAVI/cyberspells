/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.fluids.FluidType
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.entity.spells.ice_tomb;

import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.ICritablePartEntity;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class IceTombEntity
extends Entity
implements PreventDismount,
AntiMagicSusceptible,
ICritablePartEntity {
    @Nullable
    private Entity cachedOwner;
    @Nullable
    private UUID ownerUUID;
    private boolean evil;
    private float health = 1.0f;
    private int lifetime = -1;
    private float healing;

    public IceTombEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public IceTombEntity(Level level, Entity owner) {
        super((EntityType)EntityRegistry.ICE_TOMB.get(), level);
        this.setOwner(owner);
    }

    public boolean skipAttackInteraction(Entity entity) {
        return this.isPassengerOfSameVehicle(entity);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    public void setEvil() {
        this.evil = true;
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setHealing(float healing) {
        this.healing = healing;
    }

    public boolean hasIndirectPassenger(Entity pEntity) {
        return this.evil;
    }

    @Nullable
    public Entity getOwner() {
        Level level;
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && (level = this.level) instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        return null;
    }

    public void tick() {
        super.tick();
        if (this.evil) {
            this.getPassengers().forEach(this::doNegativeEffects);
        } else if (this.tickCount % 20 == 0) {
            this.getPassengers().forEach(this::doPositiveEffects);
        }
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.7));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.95, 1.0, 0.95));
        }
        if (this.lifetime >= 0 && this.tickCount > this.lifetime) {
            this.destroyTomb();
        }
    }

    protected double getDefaultGravity() {
        return 0.08;
    }

    public void doPositiveEffects(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            NeoForge.EVENT_BUS.post((Event)new SpellHealEvent(livingEntity, livingEntity, this.healing, SchoolRegistry.ICE.get()));
            livingEntity.heal(this.healing);
        }
    }

    public void doNegativeEffects(Entity entity) {
        entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() * 3, entity.getTicksFrozen() + 10));
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide && this.health > 0.0f) {
            if (DamageSources.isFriendlyFireBetween(source.getEntity(), this.getFirstPassenger())) {
                return false;
            }
            if (!(this.isInvulnerableTo(source) || source.getEntity() != null && this.isPassengerOfSameVehicle(source.getEntity()))) {
                this.health -= amount;
                if (this.health <= 0.0f) {
                    this.die(source, amount);
                }
                return true;
            }
        }
        return super.hurt(source, amount);
    }

    public void die(DamageSource damageSource, float amount) {
        List entities = this.getPassengers();
        this.destroyTomb();
        if (this.evil) {
            entities.forEach(entity -> entity.hurt(damageSource, amount * 2.0f));
        }
    }

    public void kill() {
        this.destroyTomb();
    }

    @Override
    public boolean canEntityDismount(Entity entity) {
        return entity.getUUID().equals(this.ownerUUID);
    }

    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        this.refreshDimensions();
    }

    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.destroyTomb();
    }

    public void destroyTomb() {
        if (!this.level.isClientSide) {
            this.ejectPassengers();
            this.playSound(SoundEvents.GLASS_BREAK, 2.0f, 1.0f);
            MagicManager.spawnParticles(this.level, ParticleHelper.SNOW_DUST, this.getX(), this.getY() + 1.0, this.getZ(), 50, 0.2, 0.2, 0.2, 0.2, false);
            MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, this.getX(), this.getY() + 1.0, this.getZ(), 50, 0.2, 0.2, 0.2, 0.2, false);
            this.discard();
        }
    }

    public Vec3 getPassengerRidingPosition(Entity pEntity) {
        return this.position();
    }

    public void positionRider(Entity passenger, Entity.MoveFunction p_19958_) {
        passenger.setPos(this.getX(), this.getY(), this.getZ());
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putInt("age", this.tickCount);
        compound.putInt("lifetime", this.lifetime);
        compound.putBoolean("evil", this.evil);
        compound.putFloat("health", this.health);
        compound.putFloat("healing", this.healing);
    }

    public boolean dismountsUnderwater() {
        return false;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
            this.cachedOwner = null;
        }
        this.tickCount = compound.getInt("age");
        this.lifetime = compound.getInt("lifetime");
        this.evil = compound.getBoolean("evil");
        this.health = compound.getFloat("health");
        this.healing = compound.getFloat("healing");
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public EntityDimensions getDimensions(Pose pPose) {
        Object object;
        List passengers = this.getPassengers();
        float hScale = 1.0f;
        float vScale = 1.0f;
        if (!passengers.isEmpty() && (object = passengers.getFirst()) instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)object;
            hScale = livingEntity.getBbWidth() + 0.4f;
            vScale = (livingEntity.getBbHeight() + 0.2f) / 2.0f;
            vScale = (vScale + hScale) * 0.5f;
        }
        return super.getDimensions(pPose).scale(hScale * 0.9f, vScale * 0.9f);
    }

    public boolean canCollideWith(@NotNull Entity pEntity) {
        return true;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPickable() {
        return true;
    }

    public void push(@NotNull Entity pEntity) {
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.destroyTomb();
    }
}

