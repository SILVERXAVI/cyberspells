/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item$Properties
 */
package io.redspace.ironsspellbooks.util;

import net.minecraft.world.item.Item;

public class ItemPropertiesHelper {
    public static Item.Properties equipment() {
        return new Item.Properties();
    }

    public static Item.Properties equipment(int stackSize) {
        return ItemPropertiesHelper.equipment().stacksTo(stackSize);
    }

    public static Item.Properties material() {
        return new Item.Properties();
    }

    public static Item.Properties material(int stackSize) {
        return ItemPropertiesHelper.material().stacksTo(stackSize);
    }

    public static Item.Properties hidden() {
        return new Item.Properties();
    }

    public static Item.Properties hidden(int stackSize) {
        return ItemPropertiesHelper.hidden().stacksTo(stackSize);
    }
}

