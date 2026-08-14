/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 */
package com.maxwell.cyber_ware_port.common.item.base;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME)
public class CyberwareAttributeHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        CyberwareAttributeHandler.updateAttributes(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        CyberwareAttributeHandler.updateAttributes(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        CyberwareAttributeHandler.updateAttributes(event.getEntity());
    }

    private static void updateAttributes(Player player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            CyberwareUserData data = (CyberwareUserData)serverPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            data.recalculateCapacity(serverPlayer);
        }
    }
}

