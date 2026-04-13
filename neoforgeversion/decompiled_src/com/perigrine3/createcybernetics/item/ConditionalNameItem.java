/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

public class ConditionalNameItem
extends Item {
    private final String normalKey;
    private final String northstarKey;

    public ConditionalNameItem(Item.Properties props, String normalTranslationKey, String northstarTranslationKey) {
        super(props);
        this.normalKey = normalTranslationKey;
        this.northstarKey = northstarTranslationKey;
    }

    public Component getName(ItemStack stack) {
        return ModList.get().isLoaded("northstar") ? Component.translatable((String)this.northstarKey) : Component.translatable((String)this.normalKey);
    }
}

