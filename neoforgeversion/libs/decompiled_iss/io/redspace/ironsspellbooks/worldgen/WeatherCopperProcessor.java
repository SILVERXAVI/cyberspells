/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.WeatheringCopper
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class WeatherCopperProcessor
extends StructureProcessor {
    public static final MapCodec<WeatherCopperProcessor> CODEC = Codec.FLOAT.fieldOf("bias").xmap(WeatherCopperProcessor::new, obj -> Float.valueOf(obj.bias));
    float bias;

    public WeatherCopperProcessor(float bias) {
        this.bias = bias;
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo process(@Nonnull LevelReader level, @Nonnull BlockPos jigsawPiecePos, @Nonnull BlockPos jigsawPieceBottomCenterPos, @Nonnull StructureTemplate.StructureBlockInfo blockInfoLocal, @Nonnull StructureTemplate.StructureBlockInfo blockInfoGlobal, @Nonnull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        Block block = blockInfoGlobal.state().getBlock();
        if (block instanceof WeatheringCopper) {
            WeatheringCopper copperBlock = (WeatheringCopper)block;
            float f = Mth.lerp((float)Utils.random.nextFloat(), (float)this.bias, (float)1.0f);
            int weatherStage = (int)(f * 4.0f);
            BlockState state = blockInfoGlobal.state();
            for (int i = 0; i < weatherStage; ++i) {
                Optional nextState = copperBlock.getNext(state);
                if (!nextState.isPresent()) continue;
                state = ((BlockState)nextState.get()).getBlock().withPropertiesOf(blockInfoGlobal.state());
            }
            return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), state, blockInfoGlobal.nbt());
        }
        return blockInfoGlobal;
    }

    @Nonnull
    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.WEATHER_COPPER.get();
    }
}

