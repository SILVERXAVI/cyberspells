/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Util
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.ArmorMaterial$Layer
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArmorMaterialRegistry {
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create((ResourceKey)Registries.ARMOR_MATERIAL, (String)"irons_spellbooks");
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> TARNISHED = ArmorMaterialRegistry.register("tarnished", ArmorMaterialRegistry.makeArmorMap(0, 0, 0, 0), 15, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of((TagKey)Tags.Items.INGOTS_IRON), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> WANDERING_MAGICIAN = ArmorMaterialRegistry.register("wandering_magician", ArmorMaterialRegistry.makeArmorMap(2, 6, 5, 2), 15, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of((TagKey)Tags.Items.LEATHERS), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> PUMPKIN = ArmorMaterialRegistry.register("pumpkin", ArmorMaterialRegistry.schoolArmorMap(), 15, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_TURTLE, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.HAY_BLOCK}), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> SCHOOL = ArmorMaterialRegistry.register("school_armor", ArmorMaterialRegistry.schoolArmorMap(), 20, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.MAGIC_CLOTH.get()}), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> NETHERITE_BATTLEMAGE = ArmorMaterialRegistry.register("netherite_battlemage", ArmorMaterialRegistry.schoolArmorMap(), 20, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of((TagKey)Tags.Items.INGOTS_NETHERITE), 3.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> PALADIN = ArmorMaterialRegistry.register("paladin", ArmorMaterialRegistry.schoolArmorMap(), 40, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(ModTags.MITHRIL_INGOT), 4.0f, 0.4f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> INFERNAL_SORCERER = ArmorMaterialRegistry.register("infernal_sorcerer", ArmorMaterialRegistry.schoolArmorMap(), 40, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(ModTags.MITHRIL_INGOT), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> BOOTS_OF_SPEED = ArmorMaterialRegistry.register("speed_boots", ArmorMaterialRegistry.schoolArmorMap(), 40, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(ModTags.MITHRIL_INGOT), 0.0f, 0.0f);
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> DEV = ArmorMaterialRegistry.register("dev", ArmorMaterialRegistry.makeArmorMap(20, 20, 20, 20), 20, (Holder<SoundEvent>)SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of((TagKey)Tags.Items.INGOTS_GOLD), 20.0f, 20.0f);

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(IronsSpellbooks.id(name)));
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial((Map)defense, enchantmentValue, equipSound, repairIngredient, list, toughness, knockbackResistance));
    }

    public static EnumMap<ArmorItem.Type, Integer> makeArmorMap(int helmet, int chestplate, int leggings, int boots) {
        return (EnumMap)Util.make(new EnumMap(ArmorItem.Type.class), p_266655_ -> {
            p_266655_.put(ArmorItem.Type.BOOTS, boots);
            p_266655_.put(ArmorItem.Type.LEGGINGS, leggings);
            p_266655_.put(ArmorItem.Type.CHESTPLATE, chestplate);
            p_266655_.put(ArmorItem.Type.HELMET, helmet);
        });
    }

    public static EnumMap<ArmorItem.Type, Integer> schoolArmorMap() {
        return ArmorMaterialRegistry.makeArmorMap(3, 8, 6, 3);
    }
}

