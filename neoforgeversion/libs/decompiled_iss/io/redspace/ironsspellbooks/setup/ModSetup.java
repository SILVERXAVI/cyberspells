/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.neoforged.neoforge.common.NeoForge
 */
package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.capabilities.magic.MagicEvents;
import io.redspace.ironsspellbooks.compat.CompatHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ModSetup {
    public static void setup() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(MagicEvents::onWorldTick);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CompatHandler.init());
    }
}

