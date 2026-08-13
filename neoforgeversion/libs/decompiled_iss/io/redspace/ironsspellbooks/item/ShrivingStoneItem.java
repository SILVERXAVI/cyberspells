/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ShrivingStoneItem
extends Item {
    private static final Component description = Component.translatable((String)"item.irons_spellbooks.shriving_stone_desc").withStyle(ChatFormatting.GRAY);

    public ShrivingStoneItem() {
        super(ItemPropertiesHelper.material());
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> lines, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, lines, pIsAdvanced);
        lines.add(description);
    }
}

