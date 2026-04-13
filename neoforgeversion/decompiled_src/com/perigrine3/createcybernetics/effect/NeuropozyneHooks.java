/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics")
public final class NeuropozyneHooks {
    private static boolean hasNeuropozyne(Player player) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (!inst.is(ModEffects.NEUROPOZYNE)) continue;
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance inst = event.getEffectInstance();
        if (inst == null) {
            return;
        }
        if (!inst.is(ModEffects.NEUROPOZYNE)) {
            return;
        }
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            PlayerCyberwareData data;
            Player player = (Player)livingEntity;
            if (!player.level().isClientSide && (data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE)) != null) {
                data.clearHumanityBonus();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (!NeuropozyneHooks.hasNeuropozyne(player)) {
            data.clearHumanityBonus();
        }
    }
}

