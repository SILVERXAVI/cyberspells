/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.item.CastingItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StaffItem
extends CastingItem {
    public StaffItem(Item.Properties properties) {
        super(properties);
    }

    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 20;
    }

    public boolean hasCustomRendering() {
        return false;
    }
}

