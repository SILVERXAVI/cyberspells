/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.ball_lightning;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BallLightning
extends AbstractMagicProjectile {
    public static final int lifetime = 100;
    int bounces;
    HashMap<UUID, Integer> victims = new HashMap();

    public BallLightning(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public BallLightning(Level level, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.BALL_LIGHTNING.get()), level);
        this.setOwner((Entity)shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 pos = this.getBoundingBox().getCenter().add(this.getDeltaMovement());
        Vec3 random = Utils.getRandomVec3(0.28);
        pos = pos.add(this.getDeltaMovement());
        this.level.addParticle(ParticleHelper.ELECTRICITY, pos.x, pos.y, pos.z, random.x, random.y, random.z);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRIC_SPARKS, x, y, z, 12, 0.08, 0.08, 0.08, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 0.6f;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget) && this.canHitVictim(pTarget);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            this.discard();
            if (!this.level.isClientSide) {
                this.impactParticles(this.getX(), this.getBoundingBox().getCenter().y, this.getZ());
            }
        }
    }

    @Override
    public void handleHitDetection() {
        Vec3 vec32;
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 pos = this.position();
        BlockHitResult hitresult = this.level.clip(new ClipContext(pos, vec32 = pos.add(vec3), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit((HitResult)hitresult);
        } else {
            List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate(0.25), this::canHitEntity);
            for (Entity entity : entities) {
                this.onHit((HitResult)new EntityHitResult(entity, this.getBoundingBox().getCenter().add(entity.getBoundingBox().getCenter()).scale(0.5)));
            }
        }
    }

    public boolean canHitVictim(Entity entity) {
        Integer timestamp = this.victims.get(entity.getUUID());
        return timestamp == null || entity.tickCount - timestamp >= 10;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity target = pResult.getEntity();
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)target;
            DamageSources.ignoreNextKnockback(livingEntity);
        }
        DamageSources.applyDamage(target, this.getDamage(), SpellRegistry.BALL_LIGHTNING_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        this.victims.put(target.getUUID(), target.tickCount);
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        switch (pResult.getDirection()) {
            case UP: 
            case DOWN: {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, this.isNoGravity() ? -1.0 : (double)-0.8f, 1.0));
                break;
            }
            case EAST: 
            case WEST: {
                this.setDeltaMovement(this.getDeltaMovement().multiply(-1.0, 1.0, 1.0));
                break;
            }
            case NORTH: 
            case SOUTH: {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 1.0, -1.0));
            }
        }
        if (++this.bounces >= 6) {
            this.discard();
        }
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.CHAIN_LIGHTNING_CHAIN);
    }
}

