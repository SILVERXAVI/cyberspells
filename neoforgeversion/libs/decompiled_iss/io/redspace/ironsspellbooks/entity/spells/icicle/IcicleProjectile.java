/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 */
package io.redspace.ironsspellbooks.entity.spells.icicle;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class IcicleProjectile
extends AbstractMagicProjectile {
    public IcicleProjectile(EntityType<? extends IcicleProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInfinitePiercing();
    }

    public IcicleProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends IcicleProjectile>)((EntityType)EntityRegistry.ICICLE_PROJECTILE.get()), levelIn);
        this.setOwner((Entity)shooter);
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        DamageSources.applyDamage(entityHitResult.getEntity(), this.getDamage(), SpellRegistry.ICICLE_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        this.pierceOrDiscard();
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; ++i) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dy = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dz = Utils.random.nextDouble() * 2.0 * speed - speed;
            this.level.addParticle((ParticleOptions)(Utils.random.nextDouble() < 0.3 ? ParticleHelper.SNOWFLAKE : ParticleTypes.SNOWFLAKE), this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, x, y, z, 15, 0.1, 0.1, 0.1, 0.1, true);
    }

    @Override
    public float getSpeed() {
        return 1.4f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.ICE_IMPACT);
    }
}

