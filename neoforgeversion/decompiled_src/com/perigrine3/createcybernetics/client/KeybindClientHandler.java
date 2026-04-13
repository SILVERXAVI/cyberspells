/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.lwjgl.glfw.GLFW
 */
package com.perigrine3.createcybernetics.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.perigrine3.createcybernetics.client.ModKeyMappings;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.generic.InfologTextData;
import com.perigrine3.createcybernetics.network.payload.OpenArmCannonPayload;
import com.perigrine3.createcybernetics.network.payload.OpenHeatEnginePayload;
import com.perigrine3.createcybernetics.network.payload.OpenSpinalInjectorPayload;
import com.perigrine3.createcybernetics.screen.custom.ArmCannonWheelScreen;
import com.perigrine3.createcybernetics.screen.custom.CyberwareToggleWheelScreen;
import com.perigrine3.createcybernetics.screen.custom.InfologEditScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class KeybindClientHandler {
    private KeybindClientHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Record payload;
        PlayerCyberwareData data;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        while (((KeyMapping)ModKeyMappings.CYBERWARE_WHEEL.get()).consumeClick()) {
            if (CyberwareToggleWheelScreen.isWheelOpen()) {
                CyberwareToggleWheelScreen.closeWheel();
                if (mc.screen == null) continue;
                mc.setScreen(null);
                continue;
            }
            mc.setScreen((Screen)new CyberwareToggleWheelScreen());
        }
        while (((KeyMapping)ModKeyMappings.ARM_CANNON_WHEEL.get()).consumeClick()) {
            if (ArmCannonWheelScreen.isOpen()) {
                ArmCannonWheelScreen.close();
                if (mc.screen == null) continue;
                mc.setScreen(null);
                continue;
            }
            ArmCannonWheelScreen.open(4);
            if (mc.player != null && mc.player.hasData(ModAttachments.CYBERWARE) && (data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE)) != null) {
                ArmCannonWheelScreen.setPreselectedIndex(data.getArmCannonSelected());
            }
            mc.setScreen((Screen)new ArmCannonWheelScreen());
        }
        while (((KeyMapping)ModKeyMappings.SPINAL_INJECTOR.get()).consumeClick()) {
            if (mc.screen != null) continue;
            payload = new OpenSpinalInjectorPayload();
            PacketDistributor.sendToServer((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        while (((KeyMapping)ModKeyMappings.ARM_CANNON.get()).consumeClick()) {
            if (mc.screen != null) continue;
            payload = new OpenArmCannonPayload();
            PacketDistributor.sendToServer((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        while (((KeyMapping)ModKeyMappings.HEAT_ENGINE.get()).consumeClick()) {
            if (mc.screen != null) continue;
            payload = new OpenHeatEnginePayload();
            PacketDistributor.sendToServer((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        while (((KeyMapping)ModKeyMappings.INFOLOG.get()).consumeClick()) {
            if (mc.screen != null || mc.player == null || !mc.player.hasData(ModAttachments.CYBERWARE) || (data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE)) == null) continue;
            int found = -1;
            ItemStack foundStack = ItemStack.EMPTY;
            for (int i = 0; i < 2; ++i) {
                ItemStack st = data.getChipwareStack(i);
                if (st.isEmpty() || !st.is((Item)ModItems.DATA_SHARD_INFOLOG.get())) continue;
                found = i;
                foundStack = st;
                break;
            }
            if (found == -1) continue;
            String initial = InfologTextData.getText(foundStack);
            mc.setScreen((Screen)new InfologEditScreen(found, initial));
        }
    }

    private static void passthroughMovementKeys(Minecraft mc) {
        KeybindClientHandler.passthrough(mc, mc.options.keyUp);
        KeybindClientHandler.passthrough(mc, mc.options.keyDown);
        KeybindClientHandler.passthrough(mc, mc.options.keyLeft);
        KeybindClientHandler.passthrough(mc, mc.options.keyRight);
        KeybindClientHandler.passthrough(mc, mc.options.keyJump);
        KeybindClientHandler.passthrough(mc, mc.options.keySprint);
        KeybindClientHandler.passthrough(mc, mc.options.keyShift);
    }

    private static void passthrough(Minecraft mc, KeyMapping key) {
        long window = mc.getWindow().getWindow();
        InputConstants.Key k = key.getKey();
        boolean down = k.getType() == InputConstants.Type.MOUSE ? GLFW.glfwGetMouseButton((long)window, (int)k.getValue()) == 1 : InputConstants.isKeyDown((long)window, (int)k.getValue());
        if (key.isDown() != down) {
            key.setDown(down);
        }
    }
}

