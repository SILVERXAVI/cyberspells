/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EchoingStrikeEntity
extends AoeEntity {
    private Entity toTrack;
    public final int waitTime = 20;

    public EchoingStrikeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
    }

    public EchoingStrikeEntity(Level level, LivingEntity owner, float damage, float radius) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.ECHOING_STRIKE.get()), level);
        this.setOwner((Entity)owner);
        this.setRadius(radius);
        this.setDamage(damage);
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    public void setTracking(Entity entity) {
        this.toTrack = entity;
    }

    @Override
    public void tick() {
        if (this.toTrack != null && this.tickCount < 10) {
            this.setPos(this.toTrack.position());
        } else if (this.tickCount == 20) {
            this.playSound((SoundEvent)SoundRegistry.ECHOING_STRIKE.get(), 1.0f, (float)Utils.random.nextIntBetweenInclusive(8, 12) * 0.1f);
            if (!this.level.isClientSide) {
                Vec3 center = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(this.level, ParticleHelper.UNSTABLE_ENDER, center.x, center.y, center.z, 25, 0.0, 0.0, 0.0, 0.18, false);
                MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(SpellRegistry.ECHOING_STRIKES_SPELL.get().getSchoolType().getTargetingColor(), this.getRadius() * 0.9f), center.x, center.y, center.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                float explosionRadius = this.getRadius();
                float explosionRadiusSqr = explosionRadius * explosionRadius;
                List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
                Vec3 losCenter = Utils.moveToRelativeGroundLevel(this.level, center, 2);
                losCenter = Utils.raycastForBlock(this.level, losCenter, losCenter.add(0.0, 3.0, 0.0), ClipContext.Fluid.NONE).getLocation().add(losCenter).scale(0.5);
                for (Entity entity : entities) {
                    double distanceSqr = entity.distanceToSqr(center);
                    if (!(distanceSqr < (double)explosionRadiusSqr) || !this.canHitEntity(entity) || !Utils.hasLineOfSight(this.level, losCenter, entity.getBoundingBox().getCenter(), true)) continue;
                    double p = Mth.clamp((double)(1.0 - distanceSqr / (double)explosionRadiusSqr + (double)0.4f), (double)0.0, (double)1.0);
                    float damage = (float)((double)this.damage * p);
                    DamageSources.applyDamage(entity, damage, SpellRegistry.ECHOING_STRIKES_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
                }
            }
        } else if (this.tickCount > 20) {
            this.discard();
        }
        if (this.level.isClientSide && this.tickCount < 10) {
            Vec3 position = this.getBoundingBox().getCenter();
            for (int i = 0; i < 3; ++i) {
                Vec3 vec3 = Utils.getRandomVec3(1.0);
                vec3 = vec3.multiply(vec3).multiply((double)Mth.sign((double)vec3.x), (double)Mth.sign((double)vec3.y), (double)Mth.sign((double)vec3.z)).scale((double)this.getRadius()).add(position);
                Vec3 motion = position.subtract(vec3).scale(0.125);
                this.level.addParticle(ParticleHelper.UNSTABLE_ENDER, vec3.x, vec3.y - 0.5, vec3.z, motion.x, motion.y, motion.z);
            }
        }
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)(this.getRadius() * 2.0f));
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}

