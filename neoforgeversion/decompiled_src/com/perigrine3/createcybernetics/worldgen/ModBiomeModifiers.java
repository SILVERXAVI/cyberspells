/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.HolderSet
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BiomeTags
 *  net.minecraft.world.level.biome.MobSpawnSettings$SpawnerData
 *  net.minecraft.world.level.levelgen.GenerationStep$Decoration
 *  net.neoforged.neoforge.common.world.BiomeModifier
 *  net.neoforged.neoforge.common.world.BiomeModifiers$AddFeaturesBiomeModifier
 *  net.neoforged.neoforge.common.world.BiomeModifiers$AddSpawnsBiomeModifier
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package com.perigrine3.createcybernetics.worldgen;

import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.worldgen.ModPlacedFeatures;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_TITANIUMORE = ModBiomeModifiers.registerKey("add_titaniumore");
    public static final ResourceKey<BiomeModifier> SPAWN_CYBERZOMBIE = ModBiomeModifiers.registerKey("spawn_cyberzombie");
    public static final ResourceKey<BiomeModifier> SPAWN_CYBERSKELETON = ModBiomeModifiers.registerKey("spawn_cyberskeleton");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter biomes = context.lookup(Registries.BIOME);
        context.register(ADD_TITANIUMORE, (Object)new BiomeModifiers.AddFeaturesBiomeModifier((HolderSet)biomes.getOrThrow(BiomeTags.IS_OVERWORLD), (HolderSet)HolderSet.direct((Holder[])new Holder[]{placedFeatures.getOrThrow(ModPlacedFeatures.TITANIUMORE_PLACED_KEY)}), GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(SPAWN_CYBERZOMBIE, (Object)new BiomeModifiers.AddSpawnsBiomeModifier((HolderSet)biomes.getOrThrow(BiomeTags.IS_OVERWORLD), List.of(new MobSpawnSettings.SpawnerData(ModEntities.CYBERZOMBIE.get(), 10, 1, 3))));
        context.register(SPAWN_CYBERSKELETON, (Object)new BiomeModifiers.AddSpawnsBiomeModifier((HolderSet)biomes.getOrThrow(BiomeTags.IS_OVERWORLD), List.of(new MobSpawnSettings.SpawnerData(ModEntities.CYBERSKELETON.get(), 10, 1, 3))));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create((ResourceKey)NeoForgeRegistries.Keys.BIOME_MODIFIERS, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
    }
}

