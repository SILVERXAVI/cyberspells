/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class FrostbiteEffect
extends MagicMobEffect {
    public FrostbiteEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void handleFrostbiteDeathEffects(LivingDeathEvent event) {
        LivingEntity attacker;
        MobEffectInstance effect;
        Entity entity;
        DamageSource damageSource = event.getSource();
        LivingEntity entity2 = event.getEntity();
        if (damageSource != null && (entity = damageSource.getEntity()) instanceof LivingEntity && (effect = (attacker = (LivingEntity)entity).getEffect(MobEffectRegistry.FROSTBITTEN_STRIKES)) != null && entity2.isFullyFrozen()) {
            FrozenHumanoid iceClone = new FrozenHumanoid(entity2.level, entity2);
            iceClone.setSummoner(attacker);
            iceClone.setShatterDamage(FrostbiteEffect.getDamageForAmplifier(effect.getAmplifier(), attacker));
            iceClone.setDeathTimer(100);
            entity2.level.addFreshEntity((Entity)iceClone);
            entity2.deathTime = 1000;
            iceClone.playSound((SoundEvent)SoundRegistry.FROSTBITE_FREEZE.get(), 2.0f, (float)Utils.random.nextInt(9, 11) * 0.1f);
        }
    }

    public static float getDamageForAmplifier(int effectAmplifier, @Nullable LivingEntity caster) {
        float power = caster == null ? 1.0f : SpellRegistry.FROSTBITE_SPELL.get().getEntityPowerMultiplier(caster);
        return (float)(1 + effectAmplifier) * power;
    }
}

