/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Remove
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class SandevistanEffect
extends MobEffect {
    public SandevistanEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player)) {
            return true;
        }
        Player player = (Player)entity;
        if (player.level().isClientSide()) {
            return true;
        }
        SandevistanEffect.applyAll(player);
        return true;
    }

    private static void applyAll(Player player) {
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "sandevistan_speed");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "sandevistan_stepheight");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "sandevistan_jump");
    }

    private static void removeAll(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "sandevistan_speed");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "sandevistan_stepheight");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "sandevistan_jump");
    }

    private static boolean isSandevistan(MobEffect effect) {
        return effect == ModEffects.SANDEVISTAN_EFFECT;
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        MobEffectInstance inst = event.getEffectInstance();
        if (inst == null) {
            return;
        }
        if (SandevistanEffect.isSandevistan((MobEffect)inst.getEffect().value())) {
            SandevistanEffect.removeAll(player);
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        MobEffectInstance inst = event.getEffectInstance();
        if (inst == null) {
            return;
        }
        if (SandevistanEffect.isSandevistan((MobEffect)inst.getEffect().value())) {
            SandevistanEffect.removeAll(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof Player)) {
            return;
        }
        Player player2 = player;
        if (player2.level().isClientSide()) {
            return;
        }
        if (!player2.hasEffect(ModEffects.SANDEVISTAN_EFFECT)) {
            SandevistanEffect.removeAll(player2);
        }
    }
}

