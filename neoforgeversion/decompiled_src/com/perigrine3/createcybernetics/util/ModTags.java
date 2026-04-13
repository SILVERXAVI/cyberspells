/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 */
package com.perigrine3.createcybernetics.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> C_FOODS_RAW_MEATS = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"c", (String)"foods/raw_meats"));
        public static final TagKey<Item> FD_KNIVES = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"c", (String)"tools/knives"));
        public static final TagKey<Item> C_TITANIUM = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"c", (String)"ingots/titanium"));
        public static final TagKey<Item> TOGGLEABLE_CYBERWARE = Items.createTag("toggleable_cyberware");
        public static final TagKey<Item> ENERGY_GENERATING_CYBERWARE = Items.createTag("energy_generating_cyberware");
        public static final TagKey<Item> ARM_CANNON_AMMO = Items.createTag("arm_cannon_ammo");
        public static final TagKey<Item> DATA_SHARDS = Items.createTag("data_shards");
        public static final TagKey<Item> CYBERWARE_ITEM = Items.createTag("cyberware_item");
        public static final TagKey<Item> WETWARE_ITEM = Items.createTag("wetware_item");
        public static final TagKey<Item> BODYPART_DROPS = Items.createTag("bodypart_drops");
        public static final TagKey<Item> HUMANOID_BODYPART_DROPS = Items.createTag("humanoid_bodypart_drops");
        public static final TagKey<Item> GRASSFED_BODYPART_DROPS = Items.createTag("grassfed_bodypart_drops");
        public static final TagKey<Item> FISH_BODYPART_DROPS = Items.createTag("fish_bodypart_drops");
        public static final TagKey<Item> OFFAL = Items.createTag("offal");
        public static final TagKey<Item> BODY_PARTS = Items.createTag("body_parts");
        public static final TagKey<Item> BASE_CYBERWARE = Items.createTag("base_cyberware");
        public static final TagKey<Item> EYE_UPGRADES = Items.createTag("eye_upgrades");
        public static final TagKey<Item> ARM_UPGRADES = Items.createTag("arm_upgrades");
        public static final TagKey<Item> LEG_UPGRADES = Items.createTag("leg_upgrades");
        public static final TagKey<Item> BONE_UPGRADES = Items.createTag("bone_upgrades");
        public static final TagKey<Item> BRAIN_UPGRADES = Items.createTag("brain_upgrades");
        public static final TagKey<Item> HEART_UPGRADES = Items.createTag("heart_upgrades");
        public static final TagKey<Item> LUNG_UPGRADES = Items.createTag("lung_upgrades");
        public static final TagKey<Item> ORGAN_UPGRADES = Items.createTag("organ_upgrades");
        public static final TagKey<Item> SKIN_UPGRADES = Items.createTag("skin_upgrades");
        public static final TagKey<Item> MUSCLE_UPGRADES = Items.createTag("muscle_upgrades");
        public static final TagKey<Item> SCAVENGED_CYBERWARE = Items.createTag("scavenged_cyberware");
        public static final TagKey<Item> BRAIN_ITEMS = Items.createTag("brain_items");
        public static final TagKey<Item> EYE_ITEMS = Items.createTag("eye_items");
        public static final TagKey<Item> SKIN_ITEMS = Items.createTag("skin_items");
        public static final TagKey<Item> MUSCLE_ITEMS = Items.createTag("muscle_items");
        public static final TagKey<Item> BONE_ITEMS = Items.createTag("bone_items");
        public static final TagKey<Item> HEART_ITEMS = Items.createTag("heart_items");
        public static final TagKey<Item> LUNGS_ITEMS = Items.createTag("lungs_items");
        public static final TagKey<Item> LIVER_ITEMS = Items.createTag("liver_items");
        public static final TagKey<Item> INTESTINES_ITEMS = Items.createTag("intestines_items");
        public static final TagKey<Item> LEFTARM_ITEMS = Items.createTag("leftarm_items");
        public static final TagKey<Item> RIGHTARM_ITEMS = Items.createTag("rightarm_items");
        public static final TagKey<Item> LEFTLEG_ITEMS = Items.createTag("leftleg_items");
        public static final TagKey<Item> RIGHTLEG_ITEMS = Items.createTag("rightleg_items");
        public static final TagKey<Item> BRAIN_REPLACEMENTS = Items.createTag("brain_replacements");
        public static final TagKey<Item> EYE_REPLACEMENTS = Items.createTag("eye_replacements");
        public static final TagKey<Item> SKIN_REPLACEMENTS = Items.createTag("skin_replacements");
        public static final TagKey<Item> MUSCLE_REPLACEMENTS = Items.createTag("muscle_replacements");
        public static final TagKey<Item> BONE_REPLACEMENTS = Items.createTag("bone_replacements");
        public static final TagKey<Item> HEART_REPLACEMENTS = Items.createTag("heart_replacements");
        public static final TagKey<Item> LIVER_REPLACEMENTS = Items.createTag("liver_replacements");
        public static final TagKey<Item> LEFTARM_REPLACEMENTS = Items.createTag("leftarm_replacements");
        public static final TagKey<Item> LEFT_CYBERARM = Items.createTag("left_cyberarm");
        public static final TagKey<Item> RIGHTARM_REPLACEMENTS = Items.createTag("rightarm_replacements");
        public static final TagKey<Item> RIGHT_CYBERARM = Items.createTag("right_cyberarm");
        public static final TagKey<Item> LEFTLEG_REPLACEMENTS = Items.createTag("leftleg_replacements");
        public static final TagKey<Item> LEFT_CYBERLEG = Items.createTag("left_cyberleg");
        public static final TagKey<Item> RIGHTLEG_REPLACEMENTS = Items.createTag("rightleg_replacements");
        public static final TagKey<Item> RIGHT_CYBERLEG = Items.createTag("right_cyberleg");
        public static final TagKey<Item> MEAT_LIMBS = Items.createTag("meat_limbs");
        public static final TagKey<Item> MEAT_ARMS = Items.createTag("meat_arms");
        public static final TagKey<Item> MEAT_LEGS = Items.createTag("meat_legs");
        public static final TagKey<Item> DEFAULTS_FAIL_AS_MISSING_WHEN_UNPOWERED = Items.createTag("defaults_fail_as_missing_when_unpowered");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTABLE = Blocks.createTag("metal_detectable");
        public static final TagKey<Block> C_TITANIUM = BlockTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"c", (String)"ores/titanium"));

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
        }
    }
}

