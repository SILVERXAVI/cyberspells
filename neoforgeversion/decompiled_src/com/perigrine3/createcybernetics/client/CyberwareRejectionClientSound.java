/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class CyberwareRejectionClientSound {
    private static final int MIN_INTERVAL_TICKS = 60;
    private static final int MAX_INTERVAL_TICKS = 140;
    private static final int CHANCE_DENOM = 3;
    private static int nextAttemptTick = -1;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof LocalPlayer)) {
            return;
        }
        LocalPlayer player2 = (LocalPlayer)player;
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused()) {
            return;
        }
        if (!CyberwareRejectionClientSound.hasEffect(player2, ModEffects.CYBERWARE_REJECTION)) {
            nextAttemptTick = -1;
            return;
        }
        int now = player2.tickCount;
        if (nextAttemptTick < 0) {
            nextAttemptTick = now + Mth.nextInt((RandomSource)player2.getRandom(), (int)60, (int)140);
            return;
        }
        if (now < nextAttemptTick) {
            return;
        }
        nextAttemptTick = now + Mth.nextInt((RandomSource)player2.getRandom(), (int)60, (int)140);
        if (player2.getRandom().nextInt(3) != 0) {
            return;
        }
        player2.level().playLocalSound(player2.getX(), player2.getY(), player2.getZ(), ModSounds.GLITCHY.get(), SoundSource.PLAYERS, 0.9f, 0.9f + player2.getRandom().nextFloat() * 0.2f, false);
    }

    private static boolean hasEffect(LocalPlayer player, Holder<MobEffect> effect) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (inst == null || !inst.is(effect)) continue;
            return true;
        }
        return false;
    }

    private CyberwareRejectionClientSound() {
    }
}

