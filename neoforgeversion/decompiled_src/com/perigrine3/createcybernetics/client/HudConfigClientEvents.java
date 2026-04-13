/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingIn
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.client.HudConfigClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class HudConfigClientEvents {
    private HudConfigClientEvents() {
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p != null) {
            HudConfigClient.reload(p.getUUID());
        }
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p != null) {
            HudConfigClient.invalidate(p.getUUID());
        }
    }
}

