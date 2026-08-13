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
package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegistryDataGenerator
extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, FeatureRegistry::bootstrapBiomeModifier).add(Registries.CONFIGURED_FEATURE, FeatureRegistry::bootstrapConfiguredFeature).add(Registries.PLACED_FEATURE, FeatureRegistry::bootstrapPlacedFeature).add(Registries.DAMAGE_TYPE, ISSDamageTypes::bootstrap).add(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, UpgradeOrbTypeRegistry::bootstrap);

    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", "irons_spellbooks"));
    }
}

