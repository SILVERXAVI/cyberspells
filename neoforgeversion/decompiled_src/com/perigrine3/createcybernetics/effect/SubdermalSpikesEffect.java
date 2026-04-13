/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class SubdermalSpikesEffect
extends MobEffect {
    public SubdermalSpikesEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide) {
            return;
        }
        if (event.getNewDamage() <= 0.0f) {
            return;
        }
        DamageSource source = event.getSource();
        if (source.is(DamageTypes.THORNS)) {
            return;
        }
        if (!SubdermalSpikesEffect.isMeleeAttack(source)) {
            return;
        }
        MobEffectInstance inst = player.getEffect(ModEffects.SUBDERMAL_SPIKES_EFFECT);
        if (inst == null) {
            return;
        }
        int level = Mth.clamp((int)(inst.getAmplifier() + 1), (int)1, (int)255);
        Entity attackerEntity = source.getEntity();
        if (!(attackerEntity instanceof LivingEntity)) {
            return;
        }
        LivingEntity attacker = (LivingEntity)attackerEntity;
        RandomSource rand = player.getRandom();
        float chance = 1.05f * (float)level;
        if (rand.nextFloat() >= chance) {
            return;
        }
        int retaliateDamage = 1 + rand.nextInt(5);
        attacker.hurt(player.damageSources().thorns((Entity)player), (float)retaliateDamage);
        player.level().playSound(null, player.blockPosition(), SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private static boolean isMeleeAttack(DamageSource source) {
        if (source.is(DamageTypes.PLAYER_ATTACK)) {
            return true;
        }
        if (source.is(DamageTypes.MOB_ATTACK)) {
            return true;
        }
        return source.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}

