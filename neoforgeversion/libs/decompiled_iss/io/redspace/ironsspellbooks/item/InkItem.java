/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.material.Fluid
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;

public class InkItem
extends Item {
    private final SpellRarity rarity;
    private final Holder<Fluid> fluid;

    public InkItem(SpellRarity rarity, Holder<Fluid> fluid) {
        super(ItemPropertiesHelper.material());
        this.rarity = rarity;
        this.fluid = fluid;
    }

    public SpellRarity getRarity() {
        return this.rarity;
    }

    public static InkItem getInkForRarity(SpellRarity rarity) {
        return switch (rarity) {
            case SpellRarity.COMMON -> (InkItem)((Object)ItemRegistry.INK_COMMON.get());
            case SpellRarity.UNCOMMON -> (InkItem)((Object)ItemRegistry.INK_UNCOMMON.get());
            case SpellRarity.RARE -> (InkItem)((Object)ItemRegistry.INK_RARE.get());
            case SpellRarity.EPIC -> (InkItem)((Object)ItemRegistry.INK_EPIC.get());
            case SpellRarity.LEGENDARY -> (InkItem)((Object)ItemRegistry.INK_LEGENDARY.get());
            default -> (InkItem)((Object)ItemRegistry.INK_COMMON.get());
        };
    }

    public Holder<Fluid> fluid() {
        return this.fluid;
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> lines, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, lines, pIsAdvanced);
        lines.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.ink_tooltip", (Object[])new Object[]{this.rarity.getDisplayName()}).withStyle(ChatFormatting.GRAY));
    }
}

