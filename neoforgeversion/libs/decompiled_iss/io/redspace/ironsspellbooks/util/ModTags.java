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
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.levelgen.structure.Structure
 *  net.minecraft.world.level.material.Fluid
 */
package io.redspace.ironsspellbooks.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static final TagKey<Item> SCHOOL_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"school_focus"));
    public static final TagKey<Item> FIRE_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_focus"));
    public static final TagKey<Item> ICE_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_focus"));
    public static final TagKey<Item> LIGHTNING_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"lightning_focus"));
    public static final TagKey<Item> ENDER_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ender_focus"));
    public static final TagKey<Item> HOLY_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"holy_focus"));
    public static final TagKey<Item> BLOOD_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"blood_focus"));
    public static final TagKey<Item> EVOCATION_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"evocation_focus"));
    public static final TagKey<Item> ELDRITCH_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"eldritch_focus"));
    public static final TagKey<Item> NATURE_FOCUS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"nature_focus"));
    public static final TagKey<Item> INSCRIBED_RUNES = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"inscribed_rune"));
    public static final TagKey<Item> MITHRIL_INGOT = ItemTags.create((ResourceLocation)ResourceLocation.parse((String)"c:ingots/mithril"));
    public static final TagKey<Item> CAN_BE_UPGRADED = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"upgrade_whitelist"));
    public static final TagKey<Item> CAN_BE_IMBUED = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"imbue_whitelist"));
    public static final TagKey<Item> BASE_WIZARD_HELMET = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wizard_base_helmet"));
    public static final TagKey<Item> BASE_WIZARD_CHESTPLATE = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wizard_base_chestplate"));
    public static final TagKey<Item> BASE_WIZARD_LEGGINGS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wizard_base_leggings"));
    public static final TagKey<Item> BASE_WIZARD_BOOTS = ItemTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wizard_base_boots"));
    public static final TagKey<Block> SPECTRAL_HAMMER_MINEABLE = BlockTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spectral_hammer_mineable"));
    public static final TagKey<Block> GUARDED_BY_WIZARDS = BlockTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"guarded_by_wizards"));
    public static final TagKey<Block> PREVENT_POCKET_DIMENSION_PLACEMENT = BlockTags.create((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"pocket_dimension_prevent_placement"));
    public static final TagKey<MobEffect> CLEANSE_IMMUNE = TagKey.create((ResourceKey)Registries.MOB_EFFECT, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cleanse_immune"));
    public static final TagKey<MobEffect> AFFECTED_BY_SPIDER_ASPECT = TagKey.create((ResourceKey)Registries.MOB_EFFECT, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"affected_by_spider_aspect"));
    public static final TagKey<Structure> WAYWARD_COMPASS_LOCATOR = TagKey.create((ResourceKey)Registries.STRUCTURE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wayward_compass_locator"));
    public static final TagKey<EntityType<?>> ALWAYS_HEAL = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"always_heal"));
    public static final TagKey<EntityType<?>> CANT_ROOT = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cant_root"));
    public static final TagKey<EntityType<?>> VILLAGE_ALLIES = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"village_allies"));
    public static final TagKey<EntityType<?>> CANT_USE_PORTAL = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cant_use_portal"));
    public static final TagKey<EntityType<?>> INFERNAL_ALLIES = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"infernal_allies"));
    public static final TagKey<EntityType<?>> GUIDING_BOLT_IMMUNE = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"guiding_bolt_immune"));
    public static final TagKey<EntityType<?>> CANT_PRODUCE_BLOOD = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cant_produce_blood"));
    public static final TagKey<EntityType<?>> CANT_PARRY = TagKey.create((ResourceKey)Registries.ENTITY_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cant_parry"));
    public static final TagKey<Biome> ICE_SPIDER_PATROLS = TagKey.create((ResourceKey)Registries.BIOME, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_spider_patrols"));
    public static final TagKey<Fluid> CAULDRON_FLUID_DISALLOW = TagKey.create((ResourceKey)Registries.FLUID, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"alchemist_cauldron_disallow"));

    private static TagKey<DamageType> create(String tag) {
        return TagKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)tag));
    }
}

