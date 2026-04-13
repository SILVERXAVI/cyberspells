/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
 *  net.neoforged.neoforge.network.registration.PayloadRegistrar
 */
package com.perigrine3.createcybernetics.network;

import com.perigrine3.createcybernetics.network.ModPayloads;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
public final class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";

    private ModNetworking() {
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("createcybernetics").versioned(PROTOCOL_VERSION);
        ModNetworking.registerAll(registrar);
    }

    private static void registerAll(PayloadRegistrar registrar) {
        ModPayloads.register(registrar);
    }
}

