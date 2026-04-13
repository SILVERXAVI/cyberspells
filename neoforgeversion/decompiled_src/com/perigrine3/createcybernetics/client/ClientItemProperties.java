/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.item.ItemProperties
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class ClientItemProperties {
    private ClientItemProperties() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register((Item)((Item)ModItems.DATA_SHARD_INFOLOG.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)ModItems.BASECYBERWARE_LEFTARM.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)ModItems.BASECYBERWARE_RIGHTARM.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)ModItems.BASECYBERWARE_LEFTLEG.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)ModItems.BASECYBERWARE_RIGHTLEG.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
            ItemProperties.register((Item)((Item)ModItems.BASECYBERWARE_CYBEREYES.get()), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dyed"), (stack, level, entity, seed) -> stack.has(DataComponents.DYED_COLOR) ? 1.0f : 0.0f);
        });
    }
}

