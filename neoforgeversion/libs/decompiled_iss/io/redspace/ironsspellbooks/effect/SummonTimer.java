/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.IMobEffectEndCallback;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

@Deprecated(forRemoval=true)
public class SummonTimer
extends MobEffect
implements IMobEffectEndCallback {
    public SummonTimer(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof IMagicSummon) {
            IMagicSummon summon = (IMagicSummon)pLivingEntity;
            if (!pLivingEntity.isDeadOrDying() && !pLivingEntity.isRemoved()) {
                summon.onUnSummon();
            }
        }
    }
}

