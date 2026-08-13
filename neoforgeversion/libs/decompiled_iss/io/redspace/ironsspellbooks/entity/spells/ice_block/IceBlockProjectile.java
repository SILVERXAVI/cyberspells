/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.ice_block;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IceBlockProjectile
extends AbstractMagicProjectile
implements GeoEntity,
IEntityWithComplexSpawn {
    private UUID targetUUID;
    private Entity cachedTarget;
    private List<Entity> victims;
    int airTime;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public IceBlockProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.victims = new ArrayList<Entity>();
        this.setNoGravity(true);
    }

    public IceBlockProjectile(Level pLevel, LivingEntity owner, LivingEntity target) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.ICE_BLOCK_PROJECTILE.get()), pLevel);
        this.setOwner((Entity)owner);
        this.setTarget((Entity)target);
    }

    public void setAirTime(int airTimeInTicks) {
        this.airTime = airTimeInTicks;
    }

    public void setTarget(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.targetUUID = pOwner.getUUID();
            this.cachedTarget = pOwner;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        }
        if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel)this.level).getEntity(this.targetUUID);
            return this.cachedTarget;
        }
        return null;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.targetUUID != null) {
            tag.putUUID("Target", this.targetUUID);
        }
        if (this.airTime > 0) {
            tag.putInt("airTime", this.airTime);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
        }
        if (tag.contains("airTime")) {
            this.airTime = tag.getInt("airTime");
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; ++i) {
            Vec3 random = new Vec3(Utils.getRandomScaled(this.getBbWidth() * 0.5f), 0.0, Utils.getRandomScaled(this.getBbWidth() * 0.5f));
            this.level.addParticle((ParticleOptions)ParticleTypes.SNOWFLAKE, this.getX() + random.x, this.getY(), this.getZ() + random.z, 0.0, -0.05, 0.0);
        }
    }

    private void doFallingDamage(Entity target) {
        if (this.level.isClientSide) {
            return;
        }
        if (!this.canHitEntity(target) || this.victims.contains(target)) {
            return;
        }
        boolean flag = DamageSources.applyDamage(target, this.getDamage() / 2.0f, SpellRegistry.ICE_BLOCK_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        if (flag) {
            this.victims.add(target);
        }
    }

    private void doImpactDamage() {
        float explosionRadius = 3.5f;
        this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius)).forEach(entity -> {
            double distance;
            if (this.canHitEntity((Entity)entity) && (distance = entity.distanceToSqr(this.position())) < (double)(explosionRadius * explosionRadius)) {
                double p = 1.0 - Math.pow(Math.sqrt(distance) / (double)explosionRadius, 3.0);
                float damage = (float)((double)this.damage * p);
                DamageSources.applyDamage(entity, damage, SpellRegistry.ICE_BLOCK_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            }
        });
    }

    @Override
    public void tick() {
        this.firstTick = false;
        if (this.airTime-- > 0) {
            this.handleFloating();
        } else {
            this.handleFalling();
        }
        Entity target = this.getTarget();
        if (target != null) {
            Vec3 diff = target.position().subtract(this.position());
            double distance = diff.horizontalDistanceSqr();
            double factor = Math.clamp((double)(distance / 16.0), (double)0.0, (double)1.0);
            if (diff.horizontalDistanceSqr() > 0.1) {
                this.setDeltaMovement(this.getDeltaMovement().add(diff.multiply(1.0, 0.0, 1.0).normalize().scale((double)0.025f * ((double)(this.airTime <= 0 ? 2 : 1) + factor * 2.0))));
            }
        }
        if (this.noPhysics) {
            this.noPhysics = this.level.noBlockCollision((Entity)this, this.getBoundingBox());
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void handleFloating() {
        BlockHitResult ground;
        boolean tooHigh = false;
        this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.95f, 0.75, (double)0.95f));
        Entity target = this.getTarget();
        if (target != null) {
            if (this.getY() - target.getY() > 3.5 + (double)(target.getBbHeight() * 0.5f)) {
                tooHigh = true;
            }
        } else if (this.airTime % 3 == 0 && (ground = Utils.raycastForBlock(this.level, this.position(), this.position().subtract(0.0, 3.5, 0.0), ClipContext.Fluid.ANY)).getType() == HitResult.Type.MISS) {
            tooHigh = true;
        }
        if (tooHigh) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
        }
        if (this.airTime == 0) {
            this.setDeltaMovement(0.0, 0.5, 0.0);
        }
    }

    private void handleFalling() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.15, 0.0));
        if (!this.level.isClientSide) {
            if (this.onGround()) {
                this.doImpactDamage();
                this.playSound((SoundEvent)SoundRegistry.ICE_BLOCK_IMPACT.get(), 2.5f, 0.8f + this.random.nextFloat() * 0.4f);
                this.impactParticles(this.getX(), this.getY(), this.getZ());
                this.discard();
            } else {
                this.level.getEntities((Entity)this, this.getBoundingBox().inflate(0.35)).forEach(this::doFallingDamage);
            }
        }
    }

    public boolean canCollideWith(Entity entity) {
        return super.canCollideWith(entity) && !(entity instanceof IceTombEntity);
    }

    public void setXRot(float pXRot) {
    }

    public void setYRot(float pYRot) {
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return pTarget != this.getOwner() && super.canHitEntity(pTarget);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.SNOWFLAKE, x, y, z, 50, 0.8, 0.1, 0.8, 0.2, false);
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, x, y, z, 25, 0.5, 0.1, 0.5, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 0.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.airTime);
        buffer.writeInt(this.cachedTarget == null ? -1 : this.cachedTarget.getId());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.airTime = additionalData.readInt();
        int id = additionalData.readInt();
        if (id >= 0) {
            this.setTarget(this.level.getEntity(id));
        }
    }
}

