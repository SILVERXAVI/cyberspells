/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlock;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class ClearPortalFrameDataProcessor
extends StructureProcessor {
    public static final MapCodec<ClearPortalFrameDataProcessor> CODEC = MapCodec.unit(ClearPortalFrameDataProcessor::new);

    public StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader levelReader, @NotNull BlockPos jigsawPiecePos, @NotNull BlockPos jigsawPieceBottomCenterPos, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() instanceof PortalFrameBlock) {
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), blockInfoGlobal.state(), null);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.CLEAR_PORTAL_FRAME_DATA.get();
    }
}

