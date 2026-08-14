/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.item;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class ExpCapsuleItem
extends Item {
    public ExpCapsuleItem(Item.Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        CompoundTag tag;
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (!pLevel.isClientSide && customData != null && (tag = customData.copyTag()).contains("xp")) {
            int xp = tag.getInt("xp");
            pPlayer.giveExperiencePoints(xp);
            stack.shrink(1);
            return InteractionResultHolder.consume((Object)stack);
        }
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)pLevel.isClientSide());
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag tag;
        CustomData customData = (CustomData)pStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && (tag = customData.copyTag()).contains("xp")) {
            pTooltipComponents.add((Component)Component.literal((String)(tag.getInt("xp") + " XP Stored")));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}

