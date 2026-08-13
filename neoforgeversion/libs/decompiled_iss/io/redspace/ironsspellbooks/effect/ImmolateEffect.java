/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ImmolateEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public static final int STACKS_REQUIRED = 3;
    public static final int STACKS_REQUIRED_AMPLIFIER = 2;
    private static final Map<LivingEntity, Entity> EFFECT_CREDIT = new WeakHashMap<LivingEntity, Entity>();
    private static final Map<MobEffectInstance, Integer> DELAYED_INSTANCES = new WeakHashMap<MobEffectInstance, Integer>();
    static int duration;

    public ImmolateEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public static MobEffectInstance addImmolateStack(LivingEntity entity, @Nullable Entity afflicter) {
        MobEffectInstance previous = entity.getEffect(MobEffectRegistry.IMMOLATE);
        MobEffectInstance inst = previous != null ? new MobEffectInstance(MobEffectRegistry.IMMOLATE, 300, previous.getAmplifier() + 1, previous.isAmbient(), previous.isVisible(), previous.showIcon()) : new MobEffectInstance(MobEffectRegistry.IMMOLATE, 300, 0, false, false, true);
        if (afflicter != null) {
            EFFECT_CREDIT.put(entity, afflicter);
        }
        entity.addEffect(inst);
        return inst;
    }

    @Override
    public void clientTick(LivingEntity livingEntity, MobEffectInstance instance) {
        int amplifier = instance.getAmplifier();
        SimpleParticleType particle = ParticleTypes.SMOKE;
        if (amplifier >= 1) {
            particle = ParticleHelper.FIRE;
        }
        RandomSource random = livingEntity.getRandom();
        for (int i = 0; i < 2; ++i) {
            Vec3 motion = new Vec3((double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f));
            motion = motion.scale((double)0.04f);
            livingEntity.level.addParticle((ParticleOptions)particle, livingEntity.getRandomX((double)0.4f), livingEntity.getRandomY(), livingEntity.getRandomZ((double)0.4f), motion.x, motion.y, motion.z);
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MobEffectInstance self = livingEntity.getEffect(MobEffectRegistry.IMMOLATE);
        if (DELAYED_INSTANCES.containsKey(self) && DELAYED_INSTANCES.get(self) - duration <= 4) {
            return true;
        }
        float explosionRadius = 6.0f;
        Level level = livingEntity.level;
        if (level.isClientSide) {
            return true;
        }
        @Nullable Entity attacker = EFFECT_CREDIT.remove(livingEntity);
        double baseDamage = ImmolateEffect.damageFor(attacker);
        DamageSource source = new DamageSource((Holder)level.damageSources().damageTypes.getHolderOrThrow(ISSDamageTypes.FIRE_MAGIC), attacker);
        float explosionRadiusSqr = explosionRadius * explosionRadius;
        List entities = level.getEntities(null, livingEntity.getBoundingBox().inflate((double)explosionRadius));
        Vec3 losPoint = Utils.raycastForBlock(level, livingEntity.position(), livingEntity.position().add(0.0, 1.0, 0.0), ClipContext.Fluid.NONE).getLocation();
        for (Entity entity : entities) {
            double p;
            float damage;
            double distanceSqr = entity.distanceToSqr(livingEntity.position());
            if (!(distanceSqr < (double)explosionRadiusSqr) || !entity.canBeHitByProjectile() || DamageSources.isFriendlyFireBetween(attacker, entity) || !Utils.hasLineOfSight(level, losPoint, entity.getBoundingBox().getCenter(), true) || !entity.hurt(source, damage = (float)(baseDamage * (p = 1.0 - distanceSqr / (double)explosionRadiusSqr))) || !(entity instanceof LivingEntity)) continue;
            LivingEntity livingVictim = (LivingEntity)entity;
            MobEffectInstance inst = ImmolateEffect.addImmolateStack(livingVictim, attacker);
            DELAYED_INSTANCES.put(inst, inst.getDuration());
        }
        PacketDistributor.sendToPlayersTrackingEntity((Entity)livingEntity, (CustomPacketPayload)new FieryExplosionParticlesPacket(livingEntity.getBoundingBox().getCenter(), 1.5f), (CustomPacketPayload[])new CustomPacketPayload[0]);
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)SoundEvents.GENERIC_EXPLODE.value(), livingEntity.getSoundSource(), 4.0f, (1.0f + (level.random.nextFloat() - level.random.nextFloat()) * 0.2f) * 0.7f);
        return false;
    }

    public static double damageFor(@Nullable Entity entity) {
        double baseDamage = 10.0;
        if (entity instanceof LivingEntity) {
            LivingEntity livingAttacker = (LivingEntity)entity;
            baseDamage = baseDamage * livingAttacker.getAttributeValue(AttributeRegistry.SPELL_POWER) * livingAttacker.getAttributeValue(AttributeRegistry.FIRE_SPELL_POWER);
        }
        return baseDamage;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        ImmolateEffect.duration = duration;
        return amplifier >= 2;
    }
}

