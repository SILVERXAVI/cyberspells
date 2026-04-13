/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.config.ModConfig
 *  net.neoforged.fml.event.config.ModConfigEvent$Loading
 *  net.neoforged.fml.event.config.ModConfigEvent$Reloading
 */
package com.perigrine3.createcybernetics;

import com.perigrine3.createcybernetics.Config;
import com.perigrine3.createcybernetics.ConfigValues;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
public final class ConfigEvents {
    private ConfigEvents() {
    }

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading e) {
        ConfigEvents.bake(e.getConfig());
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading e) {
        ConfigEvents.bake(e.getConfig());
    }

    private static void bake(ModConfig config) {
        if (config.getSpec() != Config.SPEC) {
            return;
        }
        ConfigValues.BASE_HUMANITY = (Integer)Config.HUMANITY.get();
        ConfigValues.KEEP_CYBERWARE = (Boolean)Config.KEEP_CYBERWARE.get();
        ConfigValues.SURGERY_DAMAGE_SCALING = (Boolean)Config.SURGERY_DAMAGE_SCALING.get();
    }
}

