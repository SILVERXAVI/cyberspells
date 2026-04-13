/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 */
package com.perigrine3.createcybernetics.client.toggle;

import com.mojang.blaze3d.platform.InputConstants;
import com.perigrine3.createcybernetics.client.ModKeyMappings;
import com.perigrine3.createcybernetics.screen.custom.CyberwareToggleWheelScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class CyberwareToggleWheelClientEvents {
    private CyberwareToggleWheelClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (((KeyMapping)ModKeyMappings.CYBERWARE_WHEEL.get()).consumeClick()) {
            if (mc.screen instanceof CyberwareToggleWheelScreen) {
                mc.setScreen(null);
            } else {
                mc.setScreen((Screen)new CyberwareToggleWheelScreen());
            }
        }
        if (mc.screen instanceof CyberwareToggleWheelScreen) {
            CyberwareToggleWheelClientEvents.passthroughMovement(mc);
        }
    }

    private static void passthroughMovement(Minecraft mc) {
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyUp);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyDown);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyLeft);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyRight);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyJump);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keySprint);
        CyberwareToggleWheelClientEvents.passthrough(mc.options.keyShift);
    }

    private static void passthrough(KeyMapping key) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        int code = key.getKey().getValue();
        boolean logicalDown = key.isDown();
        boolean physicalDown = InputConstants.isKeyDown((long)window, (int)code);
        if (physicalDown != logicalDown) {
            key.setDown(physicalDown);
        }
    }
}

