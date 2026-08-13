/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AirborneEffect
extends MobEffect {
    public static final float damage_per_amp = 0.5f;

    public AirborneEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        double d11;
        float f1;
        if (!livingEntity.level.isClientSide && livingEntity.horizontalCollision && (f1 = (float)((d11 = livingEntity.getDeltaMovement().horizontalDistance()) * 10.0 - 1.0)) > 0.0f) {
            livingEntity.playSound(SoundEvents.HOSTILE_BIG_FALL, 2.0f, 1.5f);
            livingEntity.hurt(livingEntity.damageSources().flyIntoWall(), AirborneEffect.getDamageFromLevel(pAmplifier + 1));
            return false;
        }
        return true;
    }

    public static float getDamageFromLevel(int level) {
        return 4.0f + (float)level * 0.5f;
    }
}

