/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.RegistrySetBuilder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.PackOutput
 *  net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.enchantment.ModEnchantments;
import com.perigrine3.createcybernetics.worldgen.ModBiomeModifiers;
import com.perigrine3.createcybernetics.worldgen.ModConfiguredFeatures;
import com.perigrine3.createcybernetics.worldgen.ModPlacedFeatures;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDatapackProvider
extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.ENCHANTMENT, ModEnchantments::bootstrap).add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap).add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap).add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("createcybernetics"));
    }
}

