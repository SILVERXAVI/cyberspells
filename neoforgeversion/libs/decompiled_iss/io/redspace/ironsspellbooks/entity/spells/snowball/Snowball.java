/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.snowball;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.snowball.FrostField;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Snowball
extends AbstractMagicProjectile {
    public Snowball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Snowball(Level level, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.SNOWBALL.get()), level);
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
            this.level.addParticle(ParticleHelper.SNOW_DUST, d0 - random.x, d1 + 0.5 - random.y, d2 - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
        Vec3 random = Utils.getRandomVec3(0.2);
        this.level.addParticle(ParticleHelper.SNOWFLAKE, d0 - random.x, d1 + 0.5 - random.y, d2 - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOW_DUST, x, y, z, 50, 0.5, 0.5, 0.5, 0.2, true);
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, x, y, z, 50, 0.5, 0.5, 0.5, 0.2, false);
    }

    @Override
    public float getSpeed() {
        return 1.0f;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.createFrostField(Utils.moveToRelativeGroundLevel(this.level, hitresult.getLocation(), 2));
        float explosionRadius = this.getExplosionRadius();
        List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
        for (Entity entity : entities) {
            double distance = entity.distanceToSqr(hitresult.getLocation());
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity livingEntity = (LivingEntity)entity;
            if (!(distance < (double)(explosionRadius * explosionRadius)) || !this.canHitEntity(entity) || DamageSources.isFriendlyFireBetween(this.getOwner(), entity) || !Utils.hasLineOfSight(this.level, hitresult.getLocation(), entity.position().add(0.0, (double)(entity.getEyeHeight() * 0.5f), 0.0), true)) continue;
            livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED, (int)this.getDamage()));
        }
        this.discardHelper(hitresult);
    }

    public void createFrostField(Vec3 location) {
        if (!this.level.isClientSide) {
            FrostField fire = new FrostField(this.level);
            fire.setOwner(this.getOwner());
            fire.setDuration((int)this.getDamage());
            fire.setRadius(this.getExplosionRadius());
            fire.setCircular();
            fire.moveTo(location);
            this.level.addFreshEntity((Entity)fire);
        }
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 2.0f, 0.7f + Utils.random.nextFloat() * 0.2f);
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.ICE_SPIKE_EMERGE);
    }
}

