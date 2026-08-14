/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 */
package com.maxwell.cyber_ware_port.common.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> CYBERWARE = Items.create("cyberware");
        public static final TagKey<Item> CYBERWARE_EYES = Items.create("cyberware/eyes");
        public static final TagKey<Item> CYBERWARE_BRAIN = Items.create("cyberware/brain");
        public static final TagKey<Item> CYBERWARE_HEART = Items.create("cyberware/heart");
        public static final TagKey<Item> CYBERWARE_LUNGS = Items.create("cyberware/lungs");
        public static final TagKey<Item> CYBERWARE_STOMACH = Items.create("cyberware/stomach");
        public static final TagKey<Item> CYBERWARE_SKIN = Items.create("cyberware/skin");
        public static final TagKey<Item> CYBERWARE_MUSCLE = Items.create("cyberware/muscle");
        public static final TagKey<Item> CYBERWARE_BONES = Items.create("cyberware/bones");
        public static final TagKey<Item> CYBERWARE_ARMS = Items.create("cyberware/arms");
        public static final TagKey<Item> CYBERWARE_HANDS = Items.create("cyberware/hands");
        public static final TagKey<Item> CYBERWARE_LEGS = Items.create("cyberware/legs");
        public static final TagKey<Item> CYBERWARE_BOOTS = Items.create("cyberware/boots");

        private static TagKey<Item> create(String name) {
            return TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)name));
        }
    }
}

