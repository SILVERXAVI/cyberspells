/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Unit
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CastingItem
extends Item {
    public CastingItem(Item.Properties pProperties) {
        super(pProperties.component(ComponentRegistry.CASTING_IMPLEMENT, (Object)Unit.INSTANCE));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}

