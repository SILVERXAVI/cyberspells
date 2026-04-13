/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.common.synergy;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.synergy.CyberwareSynergies;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareSynergyDispatcher {
    private static final String NBT_ROOT = "cc_synergies";
    private static final int TICK_INTERVAL = 20;

    private CyberwareSynergyDispatcher() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            CyberwareSynergyDispatcher.clearAll(player);
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            CyberwareSynergyDispatcher.clearAll(player);
            return;
        }
        if (player.tickCount % 20 != 0) {
            return;
        }
        CyberwareSynergyDispatcher.evaluate(player, data);
    }

    private static void evaluate(Player player, PlayerCyberwareData data) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag state = persistent.getCompound(NBT_ROOT);
        for (CyberwareSynergies.Synergy synergy : CyberwareSynergies.ALL) {
            String key = synergy.id().toString();
            boolean shouldBeActive = synergy.isActive(player, data);
            boolean isActive = state.getBoolean(key);
            if (shouldBeActive && !isActive) {
                synergy.apply(player);
                state.putBoolean(key, true);
                continue;
            }
            if (shouldBeActive || !isActive) continue;
            synergy.remove(player);
            state.putBoolean(key, false);
        }
        persistent.put(NBT_ROOT, (Tag)state);
    }

    private static void clearAll(Player player) {
        for (CyberwareSynergies.Synergy synergy : CyberwareSynergies.ALL) {
            synergy.remove(player);
        }
        player.getPersistentData().remove(NBT_ROOT);
    }
}

