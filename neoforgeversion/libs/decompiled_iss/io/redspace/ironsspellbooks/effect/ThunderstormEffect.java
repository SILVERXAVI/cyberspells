/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ThunderstormEffect
extends MagicMobEffect {
    public ThunderstormEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration % 40 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int pAmplifier) {
        int radiusSqr = 400;
        entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(20.0, 12.0, 20.0), livingEntity -> livingEntity != entity && this.horizontalDistanceSqr((LivingEntity)livingEntity, entity) < (float)radiusSqr && livingEntity.isPickable() && !livingEntity.isSpectator() && !Utils.shouldHealEntity(entity, livingEntity) && Utils.hasLineOfSight(entity.level, (Entity)entity, (Entity)livingEntity, false)).forEach(targetEntity -> {
            LightningStrike lightningStrike = new LightningStrike(entity.level);
            lightningStrike.setOwner((Entity)entity);
            lightningStrike.setDamage(ThunderstormEffect.getDamageFromAmplifier(pAmplifier, entity));
            lightningStrike.setPos(targetEntity.position());
            entity.level.addFreshEntity((Entity)lightningStrike);
        });
        return true;
    }

    private float horizontalDistanceSqr(LivingEntity livingEntity, LivingEntity entity2) {
        double dx = livingEntity.getX() - entity2.getX();
        double dz = livingEntity.getZ() - entity2.getZ();
        return (float)(dx * dx + dz * dz);
    }

    public static float getDamageFromAmplifier(int effectAmplifier, @Nullable LivingEntity caster) {
        float power = caster == null ? 1.0f : SpellRegistry.THUNDERSTORM_SPELL.get().getEntityPowerMultiplier(caster);
        return (float)(effectAmplifier - 7) * power + 7.0f;
    }
}

