/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
 */
package com.maxwell.cyber_ware_port.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid="cyber_ware_port", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
public class KeyInit {
    public static final KeyMapping MENU_KEY = new KeyMapping("key.cyber_ware_port.menu", InputConstants.Type.KEYSYM, 82, "key.categories.cyber_ware_port");

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(MENU_KEY);
    }
}

