/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.WrappedGoal
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class TrueInvisibilityEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public TrueInvisibilityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int pAmplifier) {
        super.onEffectAdded(livingEntity, pAmplifier);
        TargetingConditions targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> ((Mob)e).getTarget() == livingEntity);
        livingEntity.level.getNearbyEntities(Mob.class, targetingCondition, livingEntity, livingEntity.getBoundingBox().inflate(40.0)).forEach(entityTargetingCaster -> {
            entityTargetingCaster.setTarget(null);
            entityTargetingCaster.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
            entityTargetingCaster.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        });
    }

    @SubscribeEvent
    public static void onDealDamage(LivingIncomingDamageEvent event) {
        LivingEntity livingAttacker;
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity && (livingAttacker = (LivingEntity)entity).hasEffect(MobEffectRegistry.TRUE_INVISIBILITY)) {
            livingAttacker.removeEffect(MobEffectRegistry.TRUE_INVISIBILITY);
        }
    }
}

