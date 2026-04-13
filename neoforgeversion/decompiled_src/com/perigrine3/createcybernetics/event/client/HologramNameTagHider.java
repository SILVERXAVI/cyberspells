/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  net.neoforged.neoforge.common.util.TriState
 */
package com.perigrine3.createcybernetics.event.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class HologramNameTagHider {
    private HologramNameTagHider() {
    }

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (event == null) {
            return;
        }
        if (event.getEntity() == null) {
            return;
        }
        if (event.getEntity().getPersistentData().getBoolean("cc_holo_snapshot")) {
            event.setCanRender(TriState.FALSE);
        }
    }
}

