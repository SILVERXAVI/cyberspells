/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareRejectionController {
    private static final int THRESHOLD_HUMANITY = 25;
    private static final int REFRESH_EVERY_TICKS = 60;
    private static final int DURATION = 200;
    private static final int NEUROPOZYNE_HUMANITY_PER_LEVEL = 25;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        int neuroBonus;
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (player.tickCount % 60 != 0) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        int baseHumanity = data.getHumanityBase();
        int effectiveHumanity = baseHumanity + (neuroBonus = CyberwareRejectionController.getNeuropozyneBonus(player));
        if (effectiveHumanity <= 25) {
            MobEffectInstance existing = player.getEffect(ModEffects.CYBERWARE_REJECTION);
            int amp = existing != null ? existing.getAmplifier() : 0;
            player.addEffect(new MobEffectInstance(ModEffects.CYBERWARE_REJECTION, 200, amp, false, true, true));
        }
    }

    private static int getNeuropozyneBonus(Player player) {
        MobEffectInstance inst = player.getEffect(ModEffects.NEUROPOZYNE);
        if (inst == null) {
            return 0;
        }
        return (inst.getAmplifier() + 1) * 25;
    }

    private CyberwareRejectionController() {
    }
}

