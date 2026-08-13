/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.mixin.LivingEntityAccessor;
import java.util.List;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BurningDashEffect
extends MagicMobEffect {
    public BurningDashEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        List list = livingEntity.level.getEntities((Entity)livingEntity, livingEntity.getBoundingBox().inflate(0.25, 0.5, 0.25));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!(entity instanceof LivingEntity)) continue;
                DamageSources.applyDamage(entity, amplifier, SpellRegistry.BURNING_DASH_SPELL.get().getDamageSource((Entity)livingEntity));
                entity.invulnerableTime = 20;
            }
        } else if (livingEntity.horizontalCollision) {
            return false;
        }
        livingEntity.fallDistance = 0.0f;
        return true;
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

