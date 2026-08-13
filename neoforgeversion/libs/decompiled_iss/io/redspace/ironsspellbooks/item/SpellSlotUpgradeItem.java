/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

public class SpellSlotUpgradeItem
extends Item {
    private final int maxSlots;
    private final Component description;

    public SpellSlotUpgradeItem(int maxSlotsToUpgradeTo) {
        super(ItemPropertiesHelper.material().rarity(Rarity.RARE));
        this.maxSlots = maxSlotsToUpgradeTo;
        this.description = Component.translatable((String)"item.irons_spellbooks.spell_slot_upgrade_desc", (Object[])new Object[]{maxSlotsToUpgradeTo}).withStyle(ChatFormatting.GRAY);
    }

    public int maxSlots() {
        return this.maxSlots;
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> lines, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, lines, pIsAdvanced);
        lines.add(this.description);
    }
}

