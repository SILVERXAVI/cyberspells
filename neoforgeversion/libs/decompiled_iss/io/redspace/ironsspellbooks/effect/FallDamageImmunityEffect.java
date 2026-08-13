/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FallDamageImmunityEffect
extends MobEffect {
    public FallDamageImmunityEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.resetFallDistance();
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}

