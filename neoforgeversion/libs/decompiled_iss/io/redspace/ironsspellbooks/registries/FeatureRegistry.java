/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.HolderSet
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.data.worldgen.features.FeatureUtils
 *  net.minecraft.data.worldgen.placement.PlacementUtils
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BiomeTags
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.GenerationStep$Decoration
 *  net.minecraft.world.level.levelgen.VerticalAnchor
 *  net.minecraft.world.level.levelgen.feature.ConfiguredFeature
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
 *  net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
 *  net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration$TargetBlockState
 *  net.minecraft.world.level.levelgen.placement.BiomeFilter
 *  net.minecraft.world.level.levelgen.placement.CountPlacement
 *  net.minecraft.world.level.levelgen.placement.HeightRangePlacement
 *  net.minecraft.world.level.levelgen.placement.InSquarePlacement
 *  net.minecraft.world.level.levelgen.placement.PlacedFeature
 *  net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
 *  net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.common.world.BiomeModifier
 *  net.neoforged.neoforge.common.world.BiomeModifiers$AddFeaturesBiomeModifier
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.worldgen.features.ExposedAirFeature;
import io.redspace.ironsspellbooks.worldgen.features.StructureFeatureConfiguration;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FeatureRegistry {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create((ResourceKey)Registries.FEATURE, (String)"irons_spellbooks");
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create((ResourceKey)Registries.CONFIGURED_FEATURE, (String)"irons_spellbooks");
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create((ResourceKey)Registries.PLACED_FEATURE, (String)"irons_spellbooks");
    public static final DeferredHolder<Feature<?>, ExposedAirFeature> EXPOSED_AIR_FEATURE = FEATURES.register("exposed_air_feature", () -> new ExposedAirFeature(StructureFeatureConfiguration.CODEC));
    public static final ResourceKey<ConfiguredFeature<?, ?>> MITHRIL_ORE_FEATURE = FeatureRegistry.configuredFeatureResourceKey("ore_mithril_feature");
    public static final ResourceKey<PlacedFeature> MITHRIL_ORE_PLACEMENT = FeatureRegistry.placedFeatureResourceKey("ore_mithril_placement");
    public static final ResourceKey<BiomeModifier> ADD_MITHRIL_TO_BIOMES = FeatureRegistry.biomeModifierResourceKey("add_mithril_ore");

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
        FEATURES.register(eventBus);
    }

    public static void bootstrapConfiguredFeature(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        TagMatchTest deepslateTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        TagMatchTest stoneTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> arcaneDebrisList = List.of(OreConfiguration.target((RuleTest)stoneTest, (BlockState)((Block)BlockRegistry.MITHRIL_ORE.get()).defaultBlockState()), OreConfiguration.target((RuleTest)deepslateTest, (BlockState)((Block)BlockRegistry.MITHRIL_ORE_DEEPSLATE.get()).defaultBlockState()));
        FeatureUtils.register(context, MITHRIL_ORE_FEATURE, (Feature)Feature.ORE, (FeatureConfiguration)new OreConfiguration(arcaneDebrisList, 3, 1.0f));
    }

    public static void bootstrapPlacedFeature(BootstrapContext<PlacedFeature> context) {
        HolderGetter holdergetter = context.lookup(CONFIGURED_FEATURES.getRegistryKey());
        Holder.Reference holderArcaneDebris = holdergetter.getOrThrow(MITHRIL_ORE_FEATURE);
        List<BiomeFilter> list = List.of(CountPlacement.of((int)7), InSquarePlacement.spread(), HeightRangePlacement.uniform((VerticalAnchor)VerticalAnchor.absolute((int)-63), (VerticalAnchor)VerticalAnchor.absolute((int)-38)), BiomeFilter.biome());
        PlacementUtils.register(context, MITHRIL_ORE_PLACEMENT, (Holder)holderArcaneDebris, list);
    }

    public static void bootstrapBiomeModifier(BootstrapContext<BiomeModifier> context) {
        HolderGetter biomes = context.lookup(Registries.BIOME);
        HolderGetter features = context.lookup(PLACED_FEATURES.getRegistryKey());
        context.register(ADD_MITHRIL_TO_BIOMES, (Object)new BiomeModifiers.AddFeaturesBiomeModifier(FeatureRegistry.tag((HolderGetter<Biome>)biomes, (TagKey<Biome>)BiomeTags.IS_OVERWORLD), FeatureRegistry.feature((HolderGetter<PlacedFeature>)features, MITHRIL_ORE_PLACEMENT), GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey(String name) {
        return ResourceKey.create((ResourceKey)CONFIGURED_FEATURES.getRegistryKey(), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name));
    }

    private static ResourceKey<PlacedFeature> placedFeatureResourceKey(String name) {
        return ResourceKey.create((ResourceKey)PLACED_FEATURES.getRegistryKey(), (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name));
    }

    private static ResourceKey<BiomeModifier> biomeModifierResourceKey(String name) {
        return ResourceKey.create((ResourceKey)NeoForgeRegistries.Keys.BIOME_MODIFIERS, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name));
    }

    private static HolderSet<Biome> tag(HolderGetter<Biome> holderGetter, TagKey<Biome> key) {
        return holderGetter.getOrThrow(key);
    }

    private static HolderSet<PlacedFeature> feature(HolderGetter<PlacedFeature> holderGetter, ResourceKey<PlacedFeature> feature) {
        return HolderSet.direct((Holder[])new Holder[]{holderGetter.getOrThrow(feature)});
    }
}

