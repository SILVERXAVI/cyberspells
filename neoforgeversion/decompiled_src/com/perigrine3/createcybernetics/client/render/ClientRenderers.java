/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterRenderers
 */
package com.perigrine3.createcybernetics.client.render;

import com.perigrine3.createcybernetics.block.entity.HoloprojectorBlockEntityRenderer;
import com.perigrine3.createcybernetics.block.entity.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
public final class ClientRenderers {
    private ClientRenderers() {
    }

    @SubscribeEvent
    public static void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HOLOPROJECTOR_BLOCKENTITY.get(), HoloprojectorBlockEntityRenderer::new);
    }
}

