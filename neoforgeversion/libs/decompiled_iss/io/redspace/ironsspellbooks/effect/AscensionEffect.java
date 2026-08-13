/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AscensionEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public AscensionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.resetFallDistance();
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    public static void ambientParticles(ClientLevel level, LivingEntity entity) {
        RandomSource random = entity.getRandom();
        for (int i = 0; i < 2; ++i) {
            Vec3 motion = new Vec3((double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f));
            motion = motion.scale((double)0.04f);
            level.addParticle(ParticleHelper.ELECTRICITY, entity.getRandomX((double)0.4f), entity.getRandomY(), entity.getRandomZ((double)0.4f), motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void clientTick(LivingEntity livingEntity, MobEffectInstance instance) {
        Level level = livingEntity.level;
        if (level instanceof ClientLevel) {
            ClientLevel level2 = (ClientLevel)level;
            AscensionEffect.ambientParticles(level2, livingEntity);
        }
    }
}

