/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.HolderSet
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.EnchantmentTags
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.Enchantment$Builder
 *  net.minecraft.world.item.enchantment.Enchantment$Cost
 *  net.minecraft.world.item.enchantment.Enchantment$EnchantmentDefinition
 *  net.minecraft.world.item.enchantment.EnchantmentEffectComponents
 *  net.minecraft.world.item.enchantment.EnchantmentTarget
 */
package com.perigrine3.createcybernetics.enchantment;

import com.perigrine3.createcybernetics.enchantment.custom.HarvesterEnchantmentEffect;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> HARVESTER = ResourceKey.create((ResourceKey)Registries.ENCHANTMENT, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"harvester"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter items = context.lookup(Registries.ITEM);
        ModEnchantments.register(context, HARVESTER, Enchantment.enchantment((Enchantment.EnchantmentDefinition)Enchantment.definition((HolderSet)items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE), (HolderSet)items.getOrThrow(ItemTags.SWORD_ENCHANTABLE), (int)3, (int)3, (Enchantment.Cost)Enchantment.dynamicCost((int)5, (int)7), (Enchantment.Cost)Enchantment.dynamicCost((int)25, (int)8), (int)2, (EquipmentSlotGroup[])new EquipmentSlotGroup[]{EquipmentSlotGroup.MAINHAND})).exclusiveWith((HolderSet)enchantments.getOrThrow(EnchantmentTags.ON_RANDOM_LOOT)).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, (Object)new HarvesterEnchantmentEffect()));
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, (Object)builder.build(key.location()));
    }
}

