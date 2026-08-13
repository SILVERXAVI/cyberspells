/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.SlabType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class DegradeSlabsStairsProcessor
extends StructureProcessor {
    public static final MapCodec<DegradeSlabsStairsProcessor> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group((App)Codec.DOUBLE.fieldOf("chance_stairs").forGetter(obj -> obj.chanceStairs), (App)Codec.DOUBLE.fieldOf("chance_slabs").forGetter(obj -> obj.chanceSlabs)).apply((Applicative)builder, (a, b) -> new DegradeSlabsStairsProcessor(a.floatValue(), b.floatValue())));
    private final float chanceStairs;
    private final float chanceSlabs;

    public DegradeSlabsStairsProcessor(float stairs, float slabs) {
        this.chanceStairs = stairs;
        this.chanceSlabs = slabs;
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader pLevel, BlockPos pOffset, BlockPos pPos, StructureTemplate.StructureBlockInfo pBlockInfo, StructureTemplate.StructureBlockInfo pRelativeBlockInfo, StructurePlaceSettings pSettings) {
        RandomSource randomsource = pSettings.getRandom(pRelativeBlockInfo.pos());
        BlockState blockstate = pRelativeBlockInfo.state();
        BlockPos blockpos = pRelativeBlockInfo.pos();
        BlockState blockstate1 = null;
        if (blockstate.is(BlockTags.STAIRS)) {
            blockstate1 = this.maybeReplaceStairs(randomsource, pRelativeBlockInfo.state());
        } else if (blockstate.is(BlockTags.SLABS)) {
            blockstate1 = this.maybeReplaceSlab(randomsource);
        }
        return blockstate1 != null ? new StructureTemplate.StructureBlockInfo(blockpos, blockstate1, pRelativeBlockInfo.nbt()) : pRelativeBlockInfo;
    }

    @Nullable
    private BlockState maybeReplaceStairs(RandomSource pRandom, BlockState pState) {
        Half half = (Half)pState.getValue((Property)StairBlock.HALF);
        if (pRandom.nextFloat() >= this.chanceStairs) {
            return null;
        }
        Optional<Block> block = this.tryGetSlab(pState.getBlock());
        if (block.isPresent()) {
            return (BlockState)block.get().defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)(half == Half.TOP ? SlabType.TOP : SlabType.BOTTOM));
        }
        return null;
    }

    private Optional<Block> tryGetSlab(Block original) {
        try {
            String stringKey = BuiltInRegistries.BLOCK.getKey((Object)original).toString().replace("_stairs", "_slab");
            Block block = (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.parse((String)stringKey));
            if (block.equals(Blocks.AIR)) {
                return Optional.empty();
            }
            return Optional.of(block);
        }
        catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @Nullable
    private BlockState maybeReplaceSlab(RandomSource pRandom) {
        return pRandom.nextFloat() >= this.chanceSlabs ? Blocks.AIR.defaultBlockState() : null;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.DEGRADE_SLABS_STAIRS.get();
    }
}

