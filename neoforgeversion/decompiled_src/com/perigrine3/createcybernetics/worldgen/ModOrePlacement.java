/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.levelgen.placement.BiomeFilter
 *  net.minecraft.world.level.levelgen.placement.CountPlacement
 *  net.minecraft.world.level.levelgen.placement.InSquarePlacement
 *  net.minecraft.world.level.levelgen.placement.PlacementModifier
 *  net.minecraft.world.level.levelgen.placement.RarityFilter
 */
package com.perigrine3.createcybernetics.worldgen;

import java.util.List;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class ModOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return ModOrePlacement.orePlacement((PlacementModifier)CountPlacement.of((int)pCount), pHeightRange);
    }

    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return ModOrePlacement.orePlacement((PlacementModifier)RarityFilter.onAverageOnceEvery((int)pChance), pHeightRange);
    }
}

