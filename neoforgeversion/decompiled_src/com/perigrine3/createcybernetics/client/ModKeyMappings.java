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
 *  net.neoforged.neoforge.common.util.Lazy
 */
package com.perigrine3.createcybernetics.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class ModKeyMappings {
    public static final String CATEGORY = "key.categories.createcybernetics";
    public static final Lazy<KeyMapping> CYBERWARE_WHEEL = Lazy.of(() -> new KeyMapping("key.createcybernetics.cyberware_wheel", InputConstants.Type.KEYSYM, 67, CATEGORY));
    public static final Lazy<KeyMapping> SPINAL_INJECTOR = Lazy.of(() -> new KeyMapping("key.createcybernetics.spinal_injector", InputConstants.Type.KEYSYM, 78, CATEGORY));
    public static final Lazy<KeyMapping> ARM_CANNON = Lazy.of(() -> new KeyMapping("key.createcybernetics.arm_cannon", InputConstants.Type.KEYSYM, 66, CATEGORY));
    public static final Lazy<KeyMapping> ARM_CANNON_WHEEL = Lazy.of(() -> new KeyMapping("key.createcybernetics.arm_cannon_wheel", InputConstants.Type.KEYSYM, 86, CATEGORY));
    public static final Lazy<KeyMapping> HEAT_ENGINE = Lazy.of(() -> new KeyMapping("key.createcybernetics.heat_engine", InputConstants.Type.KEYSYM, 72, CATEGORY));
    public static final Lazy<KeyMapping> INFOLOG = Lazy.of(() -> new KeyMapping("key.createcybernetics.infolog_gui", InputConstants.Type.KEYSYM, 74, CATEGORY));

    private ModKeyMappings() {
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register((KeyMapping)CYBERWARE_WHEEL.get());
        event.register((KeyMapping)SPINAL_INJECTOR.get());
        event.register((KeyMapping)ARM_CANNON.get());
        event.register((KeyMapping)ARM_CANNON_WHEEL.get());
        event.register((KeyMapping)HEAT_ENGINE.get());
        event.register((KeyMapping)INFOLOG.get());
    }
}

