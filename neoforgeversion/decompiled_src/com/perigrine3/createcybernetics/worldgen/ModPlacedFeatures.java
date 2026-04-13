/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.levelgen.VerticalAnchor
 *  net.minecraft.world.level.levelgen.feature.ConfiguredFeature
 *  net.minecraft.world.level.levelgen.placement.HeightRangePlacement
 *  net.minecraft.world.level.levelgen.placement.PlacedFeature
 *  net.minecraft.world.level.levelgen.placement.PlacementModifier
 */
package com.perigrine3.createcybernetics.worldgen;

import com.perigrine3.createcybernetics.worldgen.ModConfiguredFeatures;
import com.perigrine3.createcybernetics.worldgen.ModOrePlacement;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> TITANIUMORE_PLACED_KEY = ModPlacedFeatures.registerKey("titaniumore_placed_key");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        ModPlacedFeatures.register(context, TITANIUMORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.TITANIUMORE_KEY), ModOrePlacement.commonOrePlacement(12, (PlacementModifier)HeightRangePlacement.uniform((VerticalAnchor)VerticalAnchor.absolute((int)-64), (VerticalAnchor)VerticalAnchor.absolute((int)50))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create((ResourceKey)Registries.PLACED_FEATURE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, (Object)new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}

