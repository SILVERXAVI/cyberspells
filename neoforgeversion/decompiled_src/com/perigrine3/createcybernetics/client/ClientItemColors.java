/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.component.DyedItemColor
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterColorHandlersEvent$Item
 */
package com.perigrine3.createcybernetics.client;

import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class ClientItemColors {
    private ClientItemColors() {
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) {
                return -1;
            }
            DyedItemColor dyed = (DyedItemColor)stack.get(DataComponents.DYED_COLOR);
            if (dyed == null) {
                return 0xFFFFFF;
            }
            return 0xFF000000 | dyed.rgb() & 0xFFFFFF;
        }, new ItemLike[]{(ItemLike)ModItems.DATA_SHARD_INFOLOG.get(), (ItemLike)ModItems.BASECYBERWARE_LEFTARM.get(), (ItemLike)ModItems.BASECYBERWARE_RIGHTARM.get(), (ItemLike)ModItems.BASECYBERWARE_LEFTLEG.get(), (ItemLike)ModItems.BASECYBERWARE_RIGHTLEG.get(), (ItemLike)ModItems.BASECYBERWARE_CYBEREYES.get(), (ItemLike)ModItems.SKINUPGRADES_METALPLATING.get(), (ItemLike)ModItems.LEGUPGRADES_OCELOTPAWS.get()});
    }
}

