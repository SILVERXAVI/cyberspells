/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.item.cyberware.MetalDetectorItem;
import com.perigrine3.createcybernetics.sound.MetalDetectorLoopSound;
import com.perigrine3.createcybernetics.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class MetalDetectorClientTicker {
    private static MetalDetectorLoopSound activeLoop;

    private MetalDetectorClientTicker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof LocalPlayer)) {
            return;
        }
        LocalPlayer player2 = (LocalPlayer)player;
        Minecraft mc = Minecraft.getInstance();
        Level level = player2.level();
        if (!MetalDetectorItem.isAnyMetalDetectorPowered((Player)player2)) {
            MetalDetectorClientTicker.stop(mc);
            return;
        }
        MetalDetectorItem.DetectionResult r = MetalDetectorItem.scanForMetal(level, (Player)player2);
        if (!r.detected()) {
            MetalDetectorClientTicker.stop(mc);
            return;
        }
        MetalDetectorClientTicker.startIfNeeded(mc, player2);
        float maxVol = 1.0f;
        float minVol = 0.2f;
        float t = 1.0f - (float)r.dy() / 15.0f;
        float volume = minVol + (maxVol - minVol) * t;
        if (!r.direct()) {
            volume *= 0.5f;
        }
        if (activeLoop != null) {
            activeLoop.setTargetVolume(volume);
        }
    }

    private static void startIfNeeded(Minecraft mc, LocalPlayer player) {
        if (mc.getSoundManager() == null) {
            return;
        }
        if (activeLoop != null && mc.getSoundManager().isActive((SoundInstance)activeLoop)) {
            return;
        }
        activeLoop = new MetalDetectorLoopSound((Player)player, ModSounds.METAL_DETECTOR_BEEPS.get());
        mc.getSoundManager().play((SoundInstance)activeLoop);
    }

    private static void stop(Minecraft mc) {
        if (mc.getSoundManager() == null) {
            return;
        }
        if (activeLoop != null) {
            mc.getSoundManager().stop((SoundInstance)activeLoop);
            activeLoop = null;
        }
    }
}

