/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Explosion
 *  net.minecraft.world.level.Explosion$BlockInteraction
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.level.ExplosionEvent$Start
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.entity.spells.fireball;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class MagicFireball
extends AbstractMagicProjectile {
    public MagicFireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public MagicFireball(Level pLevel, LivingEntity pShooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.MAGIC_FIREBALL.get()), pLevel);
        this.setOwner((Entity)pShooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        int count = Mth.clamp((int)((int)(vec3.lengthSqr() * 2.0)), (int)1, (int)4);
        for (int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3(this.getBbHeight() * 0.2f);
            float f = (float)i / (float)count;
            double x = Mth.lerp((double)f, (double)d0, (double)(this.getX() + vec3.x));
            double y = Mth.lerp((double)f, (double)d1, (double)(this.getY() + vec3.y));
            double z = Mth.lerp((double)f, (double)d2, (double)(this.getZ() + vec3.z));
            this.level.addParticle(ParticleHelper.FIERY_SMOKE, true, x - random.x, y + (double)(this.getBbHeight() * 0.5f) - random.y, z - random.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 1.15f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level.isClientSide) {
            Explosion explosion;
            this.impactParticles(this.xOld, this.yOld, this.zOld);
            float explosionRadius = this.getExplosionRadius();
            float explosionRadiusSqr = explosionRadius * explosionRadius;
            List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(this.level, this.position(), this.position().add(0.0, 2.0, 0.0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (!(distanceSqr < (double)explosionRadiusSqr) || !this.canHitEntity(entity) || !Utils.hasLineOfSight(this.level, losPoint, entity.getBoundingBox().getCenter(), true)) continue;
                double p = 1.0 - distanceSqr / (double)explosionRadiusSqr;
                float damage = (float)((double)this.damage * p);
                DamageSources.applyDamage(entity, damage, SpellRegistry.FIREBALL_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            }
            if (((Boolean)ServerConfigs.SPELL_GREIFING.get()).booleanValue() && !((ExplosionEvent.Start)NeoForge.EVENT_BUS.post((Event)new ExplosionEvent.Start(this.level, explosion = new Explosion(this.level, null, (DamageSource)SpellRegistry.FIREBALL_SPELL.get().getDamageSource((Entity)this, this.getOwner()), null, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius() / 2.0f, true, Explosion.BlockInteraction.DESTROY, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, (Holder)SoundEvents.GENERIC_EXPLODE)))).isCanceled()) {
                explosion.explode();
                explosion.finalizeExplosion(false);
            }
            PacketDistributor.sendToPlayersTrackingEntity((Entity)this, (CustomPacketPayload)new FieryExplosionParticlesPacket(hitResult.getLocation().subtract(this.getDeltaMovement().scale(0.5)), this.getExplosionRadius()), (CustomPacketPayload[])new CustomPacketPayload[0]);
            this.playSound((SoundEvent)SoundEvents.GENERIC_EXPLODE.value(), 4.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f);
            this.discardHelper(hitResult);
        }
    }
}

