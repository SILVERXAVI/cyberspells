/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class InsomniaHandler {
    private static final String NBT_LAST_GAME_TIME = "cc_insomnia_last_gt";
    private static final String NBT_ELAPSED_TICKS = "cc_insomnia_elapsed";
    private static final String NBT_LAST_WRITTEN = "cc_insomnia_last_written";
    private static final int VANILLA_THRESHOLD = 72000;
    private static final int UPDATE_INTERVAL_TICKS = 20;
    private static final int MAX_CATCHUP_TICKS = 100;

    private InsomniaHandler() {
    }

    @SubscribeEvent
    public static void onWake(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        long now = sp.serverLevel().getGameTime();
        CompoundTag pd = sp.getPersistentData();
        pd.putLong(NBT_LAST_GAME_TIME, now);
        pd.putInt(NBT_ELAPSED_TICKS, 0);
        pd.putInt(NBT_LAST_WRITTEN, Integer.MIN_VALUE);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        double factor;
        double scaledD;
        int desiredTicks;
        long deltaL;
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        long now = sp.serverLevel().getGameTime();
        if (now % 20L != 0L) {
            return;
        }
        AttributeInstance inst = sp.getAttribute(ModAttributes.INSOMNIA);
        if (inst == null) {
            return;
        }
        double days = inst.getValue();
        if (!Double.isFinite(days)) {
            return;
        }
        CompoundTag pd = sp.getPersistentData();
        long last = pd.getLong(NBT_LAST_GAME_TIME);
        if (last == 0L) {
            last = now;
        }
        if ((deltaL = now - last) < 0L) {
            deltaL = 0L;
        }
        if (deltaL > 100L) {
            deltaL = 100L;
        }
        pd.putLong(NBT_LAST_GAME_TIME, now);
        int elapsed = pd.getInt(NBT_ELAPSED_TICKS);
        if (!sp.isSleeping()) {
            elapsed = InsomniaHandler.safeAdd(elapsed, (int)deltaL);
            pd.putInt(NBT_ELAPSED_TICKS, elapsed);
        }
        if (days <= 0.0) {
            desiredTicks = 0;
        } else {
            double raw = days * 24000.0;
            int n = desiredTicks = raw >= 2.147483647E9 ? Integer.MAX_VALUE : (int)Math.floor(raw);
        }
        int scaled = desiredTicks <= 0 ? 72000 : (desiredTicks == 72000 ? elapsed : ((scaledD = (double)elapsed * (factor = 72000.0 / (double)desiredTicks)) >= 2.147483647E9 ? Integer.MAX_VALUE : (scaledD <= 0.0 ? 0 : (int)Math.floor(scaledD))));
        int lastWritten = pd.getInt(NBT_LAST_WRITTEN);
        if (lastWritten == scaled) {
            return;
        }
        sp.getStats().setValue((Player)sp, Stats.CUSTOM.get((Object)Stats.TIME_SINCE_REST), scaled);
        pd.putInt(NBT_LAST_WRITTEN, scaled);
    }

    private static int safeAdd(int a, int b) {
        long v = (long)a + (long)b;
        if (v > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (v < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)v;
    }
}

