/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WitherSkullProjectile
extends AbstractMagicProjectile {
    float speed = 1.0f;

    public WitherSkullProjectile(EntityType<? extends AbstractMagicProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WitherSkullProjectile(LivingEntity shooter, Level level, float speed, float damage) {
        super((EntityType<? extends Projectile>)((EntityType)EntityRegistry.WITHER_SKULL_PROJECTILE.get()), level);
        this.setOwner((Entity)shooter);
        this.speed = speed;
        this.damage = damage;
        this.explosionRadius = 2.0f;
        this.shoot(shooter.getLookAngle());
        this.setNoGravity(true);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.getBoundingBox().getCenter();
        this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            List entities = this.level().getEntities((Entity)this, this.getBoundingBox().inflate((double)this.explosionRadius));
            for (Entity entity : entities) {
                double distance = entity.distanceToSqr(hitResult.getLocation());
                if (!(distance < (double)(this.explosionRadius * this.explosionRadius)) || !this.canHitEntity(entity)) continue;
                float damage = (float)((double)this.damage * (1.0 - distance / (double)(this.explosionRadius * this.explosionRadius)));
                AbstractSpell spell = SpellRegistry.WITHER_SKULL_SPELL.get();
                DamageSources.applyDamage(entity, damage, spell.getDamageSource((Entity)this, this.getOwner()));
            }
            this.level.explode((Entity)this, this.getX(), this.getY(), this.getZ(), 0.0f, false, Level.ExplosionInteraction.NONE);
            this.discardHelper(hitResult);
        }
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }
}

