/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 */
package com.maxwell.cyber_ware_port.common.item;

import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;

public class BlueprintItem
extends Item {
    public BlueprintItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack createBlueprintFor(Item targetItem) {
        ItemStack stack = new ItemStack((ItemLike)ModItems.BLUEPRINT.get());
        ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)targetItem);
        stack.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, customData -> customData.update(tag -> tag.putString("targetItem", key.toString())));
        return stack;
    }

    public static Item getTargetItem(ItemStack stack) {
        CompoundTag tag;
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && (tag = customData.copyTag()).contains("targetItem")) {
            return (Item)BuiltInRegistries.ITEM.get(ResourceLocation.parse((String)tag.getString("targetItem")));
        }
        return null;
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        Item target = BlueprintItem.getTargetItem(pStack);
        if (target != null) {
            pTooltipComponents.add((Component)Component.translatable((String)"cyberware.tooltip.blueprint.schematic_for", (Object[])new Object[]{target.getDescription()}));
        } else {
            pTooltipComponents.add((Component)Component.translatable((String)"cyberware.tooltip.blueprint.blank"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}

