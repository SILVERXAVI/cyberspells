/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.spells.snowball;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class FrostField
extends AoeEntity {
    public FrostField(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 1;
    }

    public FrostField(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.FROST_FIELD.get()), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (!DamageSources.isFriendlyFireBetween(this.getOwner(), (Entity)target)) {
            Utils.addFreezeTicks(target, 10);
        }
    }

    @Override
    public float getParticleCount() {
        return 0.2f * this.getRadius();
    }

    @Override
    protected float particleYOffset() {
        return 0.25f;
    }

    @Override
    protected float getParticleSpeedModifier() {
        return 1.4f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void ambientParticles() {
        if (!this.level.isClientSide) {
            return;
        }
        this.ambientParticles(ParticleHelper.SNOWFLAKE);
        this.ambientParticles(ParticleHelper.SNOW_DUST);
    }
}

