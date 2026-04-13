/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.Item
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Pre
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class CrouchSprintClientHandler {
    private static int lastForwardTapTick = -40;
    private static boolean wasForwardPressed = false;

    private CrouchSprintClientHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (!data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_BLACK.get())) {
            return;
        }
        KeyMapping sprintKey = mc.options.keySprint;
        KeyMapping forwardKey = mc.options.keyUp;
        boolean forwardPressed = forwardKey.isDown();
        if (forwardPressed && !wasForwardPressed) {
            int tick;
            int n = tick = mc.level.getGameTime() > Integer.MAX_VALUE ? 0 : (int)mc.level.getGameTime();
            if (tick - lastForwardTapTick <= 7 && player.isCrouching()) {
                player.setSprinting(true);
            }
            lastForwardTapTick = tick;
        }
        wasForwardPressed = forwardPressed;
        if (player.isCrouching() && sprintKey.isDown() && player.input.forwardImpulse > 0.8f) {
            player.setSprinting(true);
        }
    }
}

