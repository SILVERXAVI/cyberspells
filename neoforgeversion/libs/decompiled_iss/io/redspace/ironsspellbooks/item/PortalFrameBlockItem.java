/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.block.Block
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class PortalFrameBlockItem
extends BlockItem {
    private static final Component DESCRIPTION = Component.translatable((String)"block.irons_spellbooks.portal_frame.desc").withStyle(ChatFormatting.GRAY);

    public PortalFrameBlockItem() {
        super((Block)BlockRegistry.PORTAL_FRAME.get(), new Item.Properties().fireResistant().rarity(Rarity.RARE));
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(DESCRIPTION);
    }
}

