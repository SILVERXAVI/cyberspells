/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.common.energy;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.network.payload.EnergyHudSnapshotPayload;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class EnergyHudSync {
    private static final Map<UUID, Mini> LAST = new HashMap<UUID, Mini>();

    private EnergyHudSync() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Mini prev;
        int net;
        int cap;
        Player p = event.getEntity();
        if (!(p instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)p;
        if (sp.isSpectator()) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        int stored = data.getEnergyStored();
        Mini now = new Mini(stored, cap = data.getTotalEnergyCapacity((Player)sp), net = 0);
        if (now.equals(prev = LAST.get(sp.getUUID()))) {
            return;
        }
        LAST.put(sp.getUUID(), now);
        PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new EnergyHudSnapshotPayload(stored, cap, net), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            LAST.remove(sp.getUUID());
        }
    }

    private record Mini(int stored, int cap, int net) {
    }
}

