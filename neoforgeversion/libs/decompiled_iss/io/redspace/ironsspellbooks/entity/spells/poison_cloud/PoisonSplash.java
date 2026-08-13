/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.poison_cloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PoisonSplash
extends AoeEntity {
    boolean playedParticles;

    public PoisonSplash(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius((float)(this.getBoundingBox().getXsize() * 0.5));
    }

    public PoisonSplash(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.POISON_SPLASH.get()), level);
    }

    @Override
    public void tick() {
        if (!this.playedParticles) {
            this.playedParticles = true;
            if (this.level().isClientSide) {
                for (int i = 0; i < 150; ++i) {
                    Vec3 pos = new Vec3(Utils.getRandomScaled(0.5), Utils.getRandomScaled(0.2f), (double)(this.random.nextFloat() * this.getRadius())).yRot(this.random.nextFloat() * 360.0f);
                    Vec3 motion = new Vec3(Utils.getRandomScaled(0.06f), this.random.nextDouble() * -0.8 - 0.5, Utils.getRandomScaled(0.06f));
                    this.level().addParticle(ParticleHelper.ACID, this.getX() + pos.x, this.getY() + pos.y + this.getBoundingBox().getYsize(), this.getZ() + pos.z, motion.x, motion.y, motion.z);
                }
            } else {
                MagicManager.spawnParticles(this.level(), ParticleHelper.POISON_CLOUD, this.getX(), this.getY() + this.getBoundingBox().getYsize(), this.getZ(), 9, this.getRadius() * 0.7f, 0.2f, this.getRadius() * 0.7f, 1.0, true);
            }
        }
        if (this.tickCount == 4) {
            this.checkHits();
            if (!this.level().isClientSide) {
                MagicManager.spawnParticles(this.level(), ParticleHelper.POISON_CLOUD, this.getX(), this.getY(), this.getZ(), 9, this.getRadius() * 0.7f, 0.2f, this.getRadius() * 0.7f, 1.0, true);
            }
            this.createPoisonCloud();
        }
        if (this.tickCount > 6) {
            this.discard();
        }
    }

    public void createPoisonCloud() {
        if (!this.level().isClientSide) {
            PoisonCloud cloud = new PoisonCloud(this.level());
            cloud.setOwner(this.getOwner());
            cloud.setDuration(this.getEffectDuration());
            cloud.setDamage(this.getDamage() * 0.1f);
            cloud.moveTo(this.position());
            this.level().addFreshEntity((Entity)cloud);
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (DamageSources.applyDamage((Entity)target, this.getDamage(), SpellRegistry.POISON_SPLASH_SPELL.get().getDamageSource((Entity)this, this.getOwner()))) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, this.getEffectDuration(), 0));
        }
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}

