/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.magma_ball;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBomb
extends AbstractMagicProjectile {
    float aoeDamage;

    public FireBomb(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireBomb(Level level, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.FIRE_BOMB.get()), level);
        this.setOwner((Entity)shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        for (int i = 0; i < 4; ++i) {
            Vec3 random = Utils.getRandomVec3(0.2);
            this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d0 - random.x, d1 + 0.5 - random.y, d2 - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.LAVA, x, y, z, 30, 1.5, 0.1, 1.5, 1.0, false);
    }

    @Override
    public float getSpeed() {
        return 0.65f;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.createFireField(hitresult.getLocation());
        float explosionRadius = this.getExplosionRadius();
        List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
        for (Entity entity : entities) {
            double distance = entity.distanceToSqr(hitresult.getLocation());
            if (!(distance < (double)(explosionRadius * explosionRadius)) || !this.canHitEntity(entity) || !Utils.hasLineOfSight(this.level, hitresult.getLocation(), entity.position().add(0.0, (double)(entity.getEyeHeight() * 0.5f), 0.0), true)) continue;
            double p = 1.0 - Math.pow(Math.sqrt(distance) / (double)explosionRadius, 3.0);
            float damage = (float)((double)this.damage * p);
            DamageSources.applyDamage(entity, damage, SpellRegistry.MAGMA_BOMB_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        }
        this.discardHelper(hitresult);
    }

    public void createFireField(Vec3 location) {
        if (!this.level.isClientSide) {
            FireField fire = new FireField(this.level);
            fire.setOwner(this.getOwner());
            fire.setDuration(200);
            fire.setDamage(this.aoeDamage);
            fire.setRadius(this.getExplosionRadius());
            fire.setCircular();
            fire.moveTo(location);
            this.level.addFreshEntity((Entity)fire);
        }
    }

    public void setAoeDamage(float damage) {
        this.aoeDamage = damage;
    }

    public float getAoeDamage() {
        return this.aoeDamage;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("AoeDamage", this.aoeDamage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.aoeDamage = tag.getFloat("AoeDamage");
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 2.0f, 1.2f + Utils.random.nextFloat() * 0.2f);
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }
}

