/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.spells.dragon_breath;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class DragonBreathPool
extends AoeEntity {
    private DamageSource damageSource;

    public DragonBreathPool(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
        this.setRadius(1.8f);
        this.radiusOnUse = -0.15f;
        this.radiusPerTick = -0.02f;
    }

    public DragonBreathPool(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.DRAGON_BREATH_POOL.get()), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (this.damageSource == null) {
            this.damageSource = new DamageSource(DamageSources.getHolderFromResource((Entity)target, ISSDamageTypes.DRAGON_BREATH_POOL), (Entity)this, this.getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(this.damageSource, this.getDamage());
    }

    @Override
    public float getParticleCount() {
        return 3.0f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleTypes.DRAGON_BREATH);
    }
}

