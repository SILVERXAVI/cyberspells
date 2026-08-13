/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.lightning_lance;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LightningLanceProjectile
extends AbstractMagicProjectile {
    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(this.getDeltaMovement());
        this.level.addParticle(ParticleHelper.ELECTRICITY, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, x, y, z, 75, 0.1, 0.1, 0.1, 2.0, true);
        MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, x, y, z, 75, 0.1, 0.1, 0.1, 0.5, false);
    }

    @Override
    public float getSpeed() {
        return 3.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    public LightningLanceProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(false);
    }

    public LightningLanceProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.LIGHTNING_LANCE_PROJECTILE.get()), levelIn);
        this.setOwner((Entity)shooter);
    }

    protected void onHitBlock(BlockHitResult pResult) {
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        DamageSources.applyDamage(entityHitResult.getEntity(), this.damage, SpellRegistry.LIGHTNING_LANCE_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (!this.level.isClientSide) {
            this.playSound((SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 6.0f, 0.65f);
        }
        super.onHit(pResult);
        this.discardHelper(pResult);
    }

    public int getAge() {
        return this.tickCount;
    }
}

