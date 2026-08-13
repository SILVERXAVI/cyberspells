/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
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
package io.redspace.ironsspellbooks.entity.spells.comet;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
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

public class Comet
extends AbstractMagicProjectile {
    public Comet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public Comet(Level pLevel, LivingEntity pShooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.COMET.get()), pLevel);
        this.setOwner((Entity)pShooter);
    }

    public void shoot(Vec3 rotation, float innaccuracy) {
        Vec3 offset = Utils.getRandomVec3(1.0).normalize().scale((double)innaccuracy);
        super.shoot(rotation.add(offset));
    }

    @Override
    public void trailParticles() {
        Vec3 vec = this.getDeltaMovement();
        double length = vec.length();
        int count = (int)Math.min(20L, Math.round(length) * 4L) + 1;
        float f = (float)length / (float)count;
        for (int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3(0.04);
            Vec3 p = vec.scale((double)(f * (float)i));
            this.level.addParticle(ParticleHelper.UNSTABLE_ENDER, this.getX() + random.x + p.x, this.getY() + random.y + p.y, this.getZ() + random.z + p.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(SpellRegistry.STARFALL_SPELL.get().getSchoolType().getTargetingColor(), 1.25f), x, y, z, 1, 0.0, 0.0, 0.0, 0.0, true);
    }

    @Override
    public float getSpeed() {
        return 1.85f;
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 0.8f, 1.35f + Utils.random.nextFloat() * 0.3f);
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level.isClientSide) {
            this.impactParticles(this.xOld, this.yOld, this.zOld);
            this.getImpactSound().ifPresent(this::doImpactSound);
            float explosionRadius = this.getExplosionRadius();
            List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
            for (Entity entity : entities) {
                double distance = entity.distanceToSqr(hitResult.getLocation());
                if (!(distance < (double)(explosionRadius * explosionRadius)) || !this.canHitEntity(entity)) continue;
                DamageSources.applyDamage(entity, this.damage, SpellRegistry.STARFALL_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            }
            this.discardHelper(hitResult);
        }
    }
}

