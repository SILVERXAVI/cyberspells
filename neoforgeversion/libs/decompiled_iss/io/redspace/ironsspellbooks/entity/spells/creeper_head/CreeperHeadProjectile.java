/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.creeper_head;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.evocation.ChainCreeperSpell;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CreeperHeadProjectile
extends AbstractMagicProjectile {
    protected boolean chainOnKill;
    protected int chainCount;
    protected float speed;

    public CreeperHeadProjectile(EntityType<? extends CreeperHeadProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.chainOnKill = false;
    }

    public CreeperHeadProjectile(LivingEntity shooter, Level level, float speed, float damage) {
        super((EntityType<? extends Projectile>)((EntityType)EntityRegistry.CREEPER_HEAD_PROJECTILE.get()), level);
        this.setOwner((Entity)shooter);
        this.speed = speed;
        this.damage = damage;
        this.explosionRadius = 5.0f;
        this.shoot(shooter.getLookAngle());
    }

    public CreeperHeadProjectile(LivingEntity shooter, Level level, Vec3 speed, float damage) {
        super((EntityType<? extends Projectile>)((EntityType)EntityRegistry.CREEPER_HEAD_PROJECTILE.get()), level);
        this.setOwner((Entity)shooter);
        this.damage = damage;
        this.explosionRadius = 5.0f;
        this.speed = (float)speed.length();
        this.shoot(speed);
    }

    public void setChainOnKill(boolean chain) {
        this.chainOnKill = chain;
    }

    public void setChainCount(int count) {
        this.chainCount = count;
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
                LivingEntity livingOwner;
                Entity entity2;
                LivingEntity livingEntity;
                LivingEntity livingEntity2;
                double distance = entity.position().distanceTo(hitResult.getLocation());
                if (!(distance < (double)this.explosionRadius) || !this.canHitEntity(entity)) continue;
                if (entity instanceof LivingEntity && (livingEntity2 = (LivingEntity)entity).isDeadOrDying()) break;
                float damage = (float)((double)this.damage * (1.0 - Math.pow(distance / (double)this.explosionRadius, 2.0)));
                DamageSources.applyDamage(entity, damage, SpellRegistry.LOB_CREEPER_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
                if (!this.chainOnKill || !(entity instanceof LivingEntity) || !(livingEntity = (LivingEntity)entity).isDeadOrDying()) continue;
                ChainCreeperSpell.summonCreeperRing(this.level(), (entity2 = this.getOwner()) instanceof LivingEntity ? (livingOwner = (LivingEntity)entity2) : null, livingEntity.getEyePosition(), this.damage * 0.85f, this.chainCount);
            }
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.EXPLOSION, x, y, z, 3, 0.1, 0.1, 0.1, 0.3, true);
            MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(1.0f, 1.0f, 1.0f, this.explosionRadius * 1.2f), x, y, z, 1, 0.0, 0.0, 0.0, 0.0, true);
            this.playSound((SoundEvent)SoundEvents.GENERIC_EXPLODE.value(), 3.0f, Utils.random.nextFloat() * 0.2f + 0.9f);
            this.discardHelper(hitResult);
        }
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }
}

