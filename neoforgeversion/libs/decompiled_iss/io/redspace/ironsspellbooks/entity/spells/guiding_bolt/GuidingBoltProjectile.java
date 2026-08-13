/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 */
package io.redspace.ironsspellbooks.entity.spells.guiding_bolt;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class GuidingBoltProjectile
extends AbstractMagicProjectile {
    public GuidingBoltProjectile(EntityType<? extends GuidingBoltProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public GuidingBoltProjectile(EntityType<? extends GuidingBoltProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner((Entity)shooter);
    }

    public GuidingBoltProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends GuidingBoltProjectile>)((EntityType)EntityRegistry.GUIDING_BOLT.get()), levelIn, shooter);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.WISP, x, y, z, 25, 0.0, 0.0, 0.0, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 1.3f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.GUIDING_BOLT_IMPACT);
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 2.0f, 0.9f + Utils.random.nextFloat() * 0.4f);
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity;
        super.onHitEntity(entityHitResult);
        if (DamageSources.applyDamage(entityHitResult.getEntity(), this.damage, SpellRegistry.GUIDING_BOLT_SPELL.get().getDamageSource((Entity)this, this.getOwner())) && (entity = entityHitResult.getEntity()) instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GUIDING_BOLT, 500));
        }
        this.pierceOrDiscard();
    }

    @Override
    public void trailParticles() {
    }
}

