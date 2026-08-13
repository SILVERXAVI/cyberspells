/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingHealEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class BlightEffect
extends MagicMobEffect {
    public static final float DAMAGE_PER_LEVEL = -0.05f;
    public static final float HEALING_PER_LEVEL = -0.1f;

    public BlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void reduceHealing(LivingHealEvent event) {
        MobEffectInstance effect = event.getEntity().getEffect(MobEffectRegistry.BLIGHT);
        if (effect != null) {
            int lvl = effect.getAmplifier() + 1;
            float healingMult = 1.0f + -0.1f * (float)lvl;
            float before = event.getAmount();
            event.setAmount(event.getAmount() * healingMult);
        }
    }

    @SubscribeEvent
    public static void reduceDamageOutput(LivingIncomingDamageEvent event) {
        LivingEntity livingAttacker;
        MobEffectInstance effect;
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity && (effect = (livingAttacker = (LivingEntity)attacker).getEffect(MobEffectRegistry.BLIGHT)) != null) {
            int lvl = effect.getAmplifier() + 1;
            float before = event.getAmount();
            float multiplier = 1.0f + -0.05f * (float)lvl;
            event.setAmount(event.getAmount() * multiplier);
        }
    }
}

