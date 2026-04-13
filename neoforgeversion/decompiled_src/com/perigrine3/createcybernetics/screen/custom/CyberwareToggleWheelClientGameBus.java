/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.InputEvent$MouseButton$Pre
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.screen.custom.CyberwareToggleWheelScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareToggleWheelClientGameBus {
    private CyberwareToggleWheelClientGameBus() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!CyberwareToggleWheelScreen.isWheelOpen()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            CyberwareToggleWheelScreen.closeWheel();
            return;
        }
        KeyMapping attack = mc.options.keyAttack;
        if (attack != null && attack.isDown()) {
            attack.setDown(false);
        }
    }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (!CyberwareToggleWheelScreen.isWheelOpen()) {
            return;
        }
        if (event.getAction() != 1) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (event.getButton() == 1) {
            event.setCanceled(true);
            KeyMapping attack = mc.options.keyAttack;
            if (attack != null) {
                attack.setDown(false);
            }
            CyberwareToggleWheelScreen.closeWheel();
            return;
        }
        if (event.getButton() == 0) {
            event.setCanceled(true);
            CyberwareToggleWheelScreen.toggleSelected();
        }
    }
}

