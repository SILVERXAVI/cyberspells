/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class SpiderAspectEffect
extends MagicMobEffect {
    public static final float DAMAGE_PER_LEVEL = 0.05f;

    public SpiderAspectEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void increaseDamage(LivingIncomingDamageEvent event) {
        boolean targetHasTagEffect;
        LivingEntity livingAttacker;
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity && (livingAttacker = (LivingEntity)attacker).hasEffect(MobEffectRegistry.SPIDER_ASPECT) && (targetHasTagEffect = event.getEntity().getActiveEffects().stream().anyMatch(instance -> instance.getEffect().is(ModTags.AFFECTED_BY_SPIDER_ASPECT)))) {
            int lvl = livingAttacker.getEffect(MobEffectRegistry.SPIDER_ASPECT).getAmplifier() + 1;
            float before = event.getAmount();
            float multiplier = 1.0f + 0.05f * (float)lvl;
            event.setAmount(event.getAmount() * multiplier);
        }
    }
}

