/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.WorldGenLevel
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager
 */
package io.redspace.ironsspellbooks.worldgen.features;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.worldgen.features.StructureFeatureConfiguration;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class ExposedAirFeature
extends Feature<StructureFeatureConfiguration> {
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    public ExposedAirFeature(Codec<StructureFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<StructureFeatureConfiguration> context) {
        Predicate cannotReplacePredicate = Feature.isReplaceable((TagKey)BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        int xsize = ((StructureFeatureConfiguration)context.config()).xsize();
        int ysize = ((StructureFeatureConfiguration)context.config()).ysize();
        int zsize = ((StructureFeatureConfiguration)context.config()).zsize();
        int minX = -xsize / 2;
        int maxX = xsize / 2;
        int minY = -1;
        int maxY = ysize - 1;
        int minZ = -zsize / 2;
        int maxZ = zsize / 2;
        int sideOpenings = 0;
        for (int dx = minX; dx <= maxX; ++dx) {
            for (int dy = minY; dy <= maxY; ++dy) {
                for (int dz = minZ; dz <= maxZ; ++dz) {
                    BlockPos currentPos = origin.offset(dx, dy, dz);
                    boolean isSolid = level.getBlockState(currentPos).isSolid();
                    if (dy == minY && !isSolid) {
                        return false;
                    }
                    if (dy == maxY && isSolid) {
                        return false;
                    }
                    if (dx != minX && dx != maxX && dz != minZ && dz != maxZ || dy != 0 || !level.isEmptyBlock(currentPos) || !level.isEmptyBlock(currentPos.above())) continue;
                    ++sideOpenings;
                }
            }
        }
        int perimeter = xsize * 2 + zsize * 2;
        if ((double)sideOpenings < (double)perimeter * 0.25) {
            return false;
        }
        StructureTemplateManager structureTemplateManager = level.getServer().getStructureManager();
        StructureTemplate structureTemplate = structureTemplateManager.getOrCreate(((StructureFeatureConfiguration)context.config()).structureTemplateLocation());
        StructurePlaceSettings placementSettings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE);
        BlockPos configuredOffset = ((StructureFeatureConfiguration)context.config()).offset();
        BlockPos structurePos = origin.offset(minX, -1, minZ).offset((Vec3i)configuredOffset);
        structureTemplate.placeInWorld((ServerLevelAccessor)level, structurePos, structurePos, placementSettings, level.getRandom(), 2);
        return true;
    }
}

