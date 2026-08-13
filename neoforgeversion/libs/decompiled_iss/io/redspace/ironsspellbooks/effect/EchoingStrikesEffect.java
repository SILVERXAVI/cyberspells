/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.spells.EchoingStrikeEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class EchoingStrikesEffect
extends MagicMobEffect {
    public EchoingStrikesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void createEcho(LivingDamageEvent.Post event) {
        DamageSource damageSource = event.getSource();
        Entity entity = damageSource.getEntity();
        if (entity instanceof LivingEntity) {
            MobEffectInstance effect;
            LivingEntity attacker = (LivingEntity)entity;
            if ((damageSource.getDirectEntity() == attacker || damageSource.getDirectEntity() instanceof AbstractArrow) && !(damageSource instanceof SpellDamageSource) && (effect = attacker.getEffect(MobEffectRegistry.ECHOING_STRIKES)) != null) {
                float percent = EchoingStrikesEffect.getDamageModifier(effect.getAmplifier(), attacker);
                EchoingStrikeEntity echo = new EchoingStrikeEntity(attacker.level, attacker, event.getNewDamage() * percent, 2.0f);
                echo.setTracking((Entity)event.getEntity());
                echo.setPos(event.getEntity().getBoundingBox().getCenter().subtract(0.0, (double)(echo.getBbHeight() * 0.5f), 0.0));
                attacker.level.addFreshEntity((Entity)echo);
            }
        }
    }

    public static float getDamageModifier(int effectAmplifier, @Nullable LivingEntity caster) {
        float power = caster == null ? 1.0f : SpellRegistry.ECHOING_STRIKES_SPELL.get().getEntityPowerMultiplier(caster);
        return (float)(effectAmplifier + 1) * power * 0.1f;
    }
}

