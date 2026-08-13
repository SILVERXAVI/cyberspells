/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class HandleLitBlocksProcessor
extends StructureProcessor {
    public static final MapCodec<HandleLitBlocksProcessor> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group((App)Codec.DOUBLE.fieldOf("chanceLit").forGetter(obj -> obj.chanceLit), (App)Codec.unboundedMap((Codec)ResourceLocation.CODEC, (Codec)Codec.DOUBLE).optionalFieldOf("byBlock", Map.of()).forGetter(obj -> obj.byBlock)).apply((Applicative)builder, HandleLitBlocksProcessor::new));
    public final double chanceLit;
    public final Map<ResourceLocation, Double> byBlock;

    private HandleLitBlocksProcessor(double chanceLit, Map<ResourceLocation, Double> byBlock) {
        this.chanceLit = chanceLit;
        this.byBlock = byBlock;
    }

    public StructureTemplate.StructureBlockInfo processBlock(@NotNull LevelReader levelReader, @NotNull BlockPos jigsawPiecePos, @NotNull BlockPos jigsawPieceBottomCenterPos, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().hasProperty((Property)BlockStateProperties.LIT)) {
            double chanceToBeLit = this.byBlock.getOrDefault(BuiltInRegistries.BLOCK.getKey((Object)blockInfoGlobal.state().getBlock()), this.chanceLit);
            RandomSource random = structurePlacementData.getRandom(blockInfoGlobal.pos());
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), (BlockState)blockInfoGlobal.state().setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf((double)random.nextFloat() < chanceToBeLit)), blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.HANDLE_LIT_BLOCKS_PROCESSOR.get();
    }
}

