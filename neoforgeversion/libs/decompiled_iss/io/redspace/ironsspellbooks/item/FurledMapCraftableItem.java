/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponentPatch
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.component.ItemLore
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.List;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber
public class FurledMapCraftableItem
extends FurledMapItem {
    final boolean ancient;
    final FurledMapItem.FurledMapData mapData;

    public FurledMapCraftableItem(boolean ancient, FurledMapItem.FurledMapData mapData) {
        this.ancient = ancient;
        this.mapData = mapData;
    }

    public String getDescriptionId() {
        return this.ancient ? ((Item)ItemRegistry.ANCIENT_FURLED_MAP.get()).getDescriptionId() : ((Item)ItemRegistry.FURLED_MAP.get()).getDescriptionId();
    }

    @SubscribeEvent
    public static void setMapData(ModifyDefaultComponentsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!(item instanceof FurledMapCraftableItem)) continue;
            FurledMapCraftableItem map = (FurledMapCraftableItem)item;
            map.modifyDefaultComponentsFrom(DataComponentPatch.builder().set((DataComponentType)ComponentRegistry.FURLED_MAP_COMPONENT.get(), (Object)map.mapData).build());
            map.mapData.descriptionOverride().ifPresent(desc -> map.modifyDefaultComponentsFrom(DataComponentPatch.builder().set(DataComponents.LORE, (Object)new ItemLore(List.of(desc))).build()));
        }
    }
}

