/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public interface ISyncedMobEffect {
    default public void clientTick(LivingEntity livingEntity, MobEffectInstance instance) {
    }
}

