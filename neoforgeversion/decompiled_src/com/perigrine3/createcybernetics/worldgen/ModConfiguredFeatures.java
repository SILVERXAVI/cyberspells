/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.feature.ConfiguredFeature
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
 *  net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
 *  net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration$TargetBlockState
 *  net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
 *  net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
 */
package com.perigrine3.createcybernetics.worldgen;

import com.perigrine3.createcybernetics.block.ModBlocks;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> TITANIUMORE_KEY = ModConfiguredFeatures.registerKey("titaniumore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_TITANIUMORE_KEY = ModConfiguredFeatures.registerKey("deepslate_titaniumore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        TagMatchTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> overworldTitaniumOres = List.of(OreConfiguration.target((RuleTest)stoneReplaceables, (BlockState)((Block)ModBlocks.TITANIUMORE_BLOCK.get()).defaultBlockState()), OreConfiguration.target((RuleTest)deepslateReplaceables, (BlockState)((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get()).defaultBlockState()));
        ModConfiguredFeatures.register(context, TITANIUMORE_KEY, Feature.ORE, new OreConfiguration(overworldTitaniumOres, 7));
        ModConfiguredFeatures.register(context, DEEPSLATE_TITANIUMORE_KEY, Feature.ORE, new OreConfiguration(overworldTitaniumOres, 7));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create((ResourceKey)Registries.CONFIGURED_FEATURE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, (Object)new ConfiguredFeature(feature, configuration));
    }
}

