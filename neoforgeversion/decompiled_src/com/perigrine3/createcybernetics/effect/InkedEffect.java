/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 */
package com.perigrine3.createcybernetics.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class InkedEffect
extends MobEffect {
    private static final ResourceLocation FOLLOW_RANGE_MOD_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"inked_follow_range");

    public InkedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(Attributes.FOLLOW_RANGE, FOLLOW_RANGE_MOD_ID, -0.9, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Mob) {
            Mob mob = (Mob)entity;
            mob.getNavigation().stop();
            if (mob.getTarget() != null) {
                mob.setTarget(null);
            }
        }
        return true;
    }
}

