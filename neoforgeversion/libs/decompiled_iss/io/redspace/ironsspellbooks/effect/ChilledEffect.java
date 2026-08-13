/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ChilledEffect
extends MagicMobEffect {
    public ChilledEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getVehicle() instanceof IceTombEntity) {
            return false;
        }
        if (pLivingEntity.isFullyFrozen()) {
            IceTombEntity iceTombEntity = new IceTombEntity(pLivingEntity.level, null);
            iceTombEntity.moveTo(pLivingEntity.position());
            iceTombEntity.setDeltaMovement(pLivingEntity.getDeltaMovement());
            iceTombEntity.setEvil();
            iceTombEntity.setLifetime(100);
            pLivingEntity.level.addFreshEntity((Entity)iceTombEntity);
            pLivingEntity.startRiding((Entity)iceTombEntity, true);
            pLivingEntity.playSound((SoundEvent)SoundRegistry.FROSTBITE_FREEZE.get(), 2.0f, (float)Utils.random.nextInt(9, 11) * 0.1f);
            return false;
        }
        return true;
    }
}

