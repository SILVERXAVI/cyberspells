/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.mixin.LivingEntityAccessor;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.UUID;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class VoltStrikeEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public VoltStrikeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level;
        if (level.isClientSide) {
            return true;
        }
        List list = level.getEntities((Entity)livingEntity, livingEntity.getBoundingBox().inflate(0.25, 0.5, 0.25));
        boolean hit = false;
        UUID ignore = null;
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!DamageSources.applyDamage(entity, amplifier, SpellRegistry.VOLT_STRIKE_SPELL.get().getDamageSource((Entity)livingEntity))) continue;
                entity.invulnerableTime = 20;
                hit = true;
                ignore = entity.getUUID();
            }
        }
        if (!hit && !level.noCollision(livingEntity.getBoundingBox().move(livingEntity.getDeltaMovement()).move(livingEntity.getDeltaMovement().normalize().scale(0.1)).deflate(0.1))) {
            hit = true;
        }
        if (hit) {
            float explosionRadius = 4.0f;
            float explosionRadiusSqr = explosionRadius * explosionRadius;
            List entities = level.getEntities((Entity)livingEntity, livingEntity.getBoundingBox().inflate((double)explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(level, livingEntity.position(), livingEntity.position().add(0.0, 1.0, 0.0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(livingEntity.position());
                if (ignore == entity.getUUID() || !(distanceSqr < (double)explosionRadiusSqr) || !entity.canBeHitByProjectile() || !Utils.hasLineOfSight(level, losPoint, entity.getBoundingBox().getCenter(), true)) continue;
                double p = 1.0 - distanceSqr / (double)explosionRadiusSqr;
                float damage = (float)((double)amplifier * p * 0.5);
                DamageSources.applyDamage(entity, damage, SpellRegistry.VOLT_STRIKE_SPELL.get().getDamageSource((Entity)livingEntity));
            }
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().normalize().scale(-0.5).add(0.0, 0.5, 0.0));
            livingEntity.hurtMarked = true;
            double x = livingEntity.getX();
            double y = livingEntity.getY() + 1.0;
            double z = livingEntity.getZ();
            MagicManager.spawnParticles(level, ParticleHelper.ELECTRIC_SPARKS, x, y, z, 25, 0.08, 0.08, 0.08, 0.3, false);
            MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, x, y, z, 75, 0.1, 0.1, 0.1, 0.5, false);
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(0.7f, 1.0f, 1.0f), explosionRadius * 2.0f), x, y + (double)0.15f, z, 1, 0.0, 0.0, 0.0, 0.0, true);
            level.playSound(null, x, y, z, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), livingEntity.getSoundSource(), 4.0f, 0.8f);
            return false;
        }
        livingEntity.fallDistance = 0.0f;
        return true;
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        Vec3 random;
        int i;
        Level level = entity.level;
        for (i = 0; i < 2; ++i) {
            random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.ELECTRIC_SPARKS, entity.getRandomX(0.75), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(0.75), random.x, random.y, random.z);
        }
        for (i = 0; i < 4; ++i) {
            random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.ELECTRICITY, entity.getRandomX(0.75), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(0.75), random.x, random.y, random.z);
        }
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor)pLivingEntity).setLivingEntityFlagInvoker(4, true);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectRemoved(pLivingEntity, pAmplifier);
        ((LivingEntityAccessor)pLivingEntity).setLivingEntityFlagInvoker(4, false);
    }
}

