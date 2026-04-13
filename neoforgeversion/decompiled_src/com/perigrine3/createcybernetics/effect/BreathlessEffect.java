/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 */
package com.perigrine3.createcybernetics.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BreathlessEffect
extends MobEffect {
    public BreathlessEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }
}

