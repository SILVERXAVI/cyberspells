/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.item.armor.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UpgradeOrbItem
extends Item {
    public static final Component TOOLTIP_HEADER = Component.translatable((String)"tooltip.irons_spellbooks.upgrade_tooltip").withStyle(ChatFormatting.GRAY);

    @Deprecated(forRemoval=true)
    public UpgradeOrbItem(UpgradeType upgrade, Item.Properties pProperties) {
        this(pProperties);
    }

    public UpgradeOrbItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public Component getName(ItemStack pStack) {
        return super.getName(pStack);
    }
}

