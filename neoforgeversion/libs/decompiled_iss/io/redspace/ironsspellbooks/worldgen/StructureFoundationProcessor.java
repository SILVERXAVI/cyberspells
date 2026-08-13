/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.WorldGenRegion
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class StructureFoundationProcessor
extends StructureProcessor {
    public static final MapCodec<StructureFoundationProcessor> CODEC = BlockState.CODEC.fieldOf("block").xmap(StructureFoundationProcessor::new, proc -> proc.block);
    public final BlockState block;

    private StructureFoundationProcessor(BlockState block) {
        this.block = block;
    }

    public StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader levelReader, @NotNull BlockPos jigsawPiecePos, @NotNull BlockPos jigsawPieceBottomCenterPos, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData) {
        WorldGenRegion worldGenRegion;
        if (!(blockInfoLocal.pos().getY() != 0 || blockInfoGlobal.state().is(Blocks.AIR) || levelReader instanceof WorldGenRegion && !(worldGenRegion = (WorldGenRegion)levelReader).getCenter().equals((Object)new ChunkPos(blockInfoGlobal.pos())))) {
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos().mutable().move(Direction.DOWN);
            BlockState currentState = levelReader.getBlockState((BlockPos)mutable);
            while (mutable.getY() > levelReader.getMinBuildHeight() && mutable.getY() < levelReader.getMaxBuildHeight() && (currentState.isAir() || !levelReader.getFluidState((BlockPos)mutable).isEmpty())) {
                levelReader.getChunk((BlockPos)mutable).setBlockState((BlockPos)mutable, this.block, false);
                mutable.move(Direction.DOWN);
                currentState = levelReader.getBlockState((BlockPos)mutable);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.STRUCTURE_FOUNDATION_PROCESSOR.get();
    }
}

