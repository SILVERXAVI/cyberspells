/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.data.PackOutput
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.SweetBerryBushBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.client.model.generators.BlockModelBuilder
 *  net.neoforged.neoforge.client.model.generators.BlockStateProvider
 *  net.neoforged.neoforge.client.model.generators.ConfiguredModel
 *  net.neoforged.neoforge.client.model.generators.ModelFile
 *  net.neoforged.neoforge.client.model.generators.ModelFile$UncheckedModelFile
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.registries.DeferredBlock
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.block.DaturaBushBlock;
import com.perigrine3.createcybernetics.block.ModBlocks;
import java.util.function.Function;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider
extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, "createcybernetics", exFileHelper);
    }

    protected void registerStatesAndModels() {
        this.blockWithItem(ModBlocks.TITANIUMORE_BLOCK);
        this.blockWithItem(ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK);
        this.blockWithItem(ModBlocks.RAW_TITANIUM_BLOCK);
        this.blockWithItem(ModBlocks.TITANIUM_BLOCK);
        this.blockWithItem(ModBlocks.SMOOTH_TITANIUM);
        this.cutoutMippedBlockWithItem(ModBlocks.TITANIUM_GRATE);
        this.blockWithItem(ModBlocks.ETCHED_TITANIUM_COPPER);
        this.stairsBlock((StairBlock)ModBlocks.SMOOTH_TITANIUM_STAIRS.get(), this.blockTexture((Block)ModBlocks.SMOOTH_TITANIUM.get()));
        this.stairsBlock((StairBlock)ModBlocks.ETCHED_TITANIUM_COPPER_STAIRS.get(), this.blockTexture((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get()));
        this.stairsBlock((StairBlock)ModBlocks.TITANIUM_CLAD_COPPER_STAIRS.get(), this.blockTexture((Block)ModBlocks.TITANIUM_CLAD_COPPER.get()));
        this.slabBlock((SlabBlock)ModBlocks.SMOOTH_TITANIUM_SLAB.get(), this.blockTexture((Block)ModBlocks.SMOOTH_TITANIUM.get()), this.blockTexture((Block)ModBlocks.SMOOTH_TITANIUM.get()));
        this.slabBlock((SlabBlock)ModBlocks.ETCHED_TITANIUM_COPPER_SLAB.get(), this.blockTexture((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get()), this.blockTexture((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get()));
        this.slabBlock((SlabBlock)ModBlocks.TITANIUM_CLAD_COPPER_SLAB.get(), this.blockTexture((Block)ModBlocks.TITANIUM_CLAD_COPPER.get()), this.blockTexture((Block)ModBlocks.TITANIUM_CLAD_COPPER.get()));
        this.blockItem(ModBlocks.SMOOTH_TITANIUM_STAIRS);
        this.blockItem(ModBlocks.SMOOTH_TITANIUM_SLAB);
        this.blockItem(ModBlocks.ETCHED_TITANIUM_COPPER_STAIRS);
        this.blockItem(ModBlocks.ETCHED_TITANIUM_COPPER_SLAB);
        this.blockItem(ModBlocks.TITANIUM_CLAD_COPPER_STAIRS);
        this.blockItem(ModBlocks.TITANIUM_CLAD_COPPER_SLAB);
        this.blockWithItem(ModBlocks.ROBOSURGEON);
        this.makeBush((SweetBerryBushBlock)ModBlocks.DATURA_BUSH.get(), "datura_bush_stage", "datura_bush_stage");
    }

    public void makeBush(SweetBerryBushBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> this.states((BlockState)state, modelName, textureName);
        this.getVariantBuilder((Block)block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[]{new ConfiguredModel((ModelFile)((BlockModelBuilder)this.models().cross(modelName + String.valueOf(state.getValue((Property)DaturaBushBlock.AGE)), ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)("block/" + textureName + String.valueOf(state.getValue((Property)DaturaBushBlock.AGE)))))).renderType("cutout"))};
        return models;
    }

    private void cutoutBlockWithItem(DeferredBlock<?> deferredBlock) {
        this.simpleBlockWithItem((Block)deferredBlock.get(), (ModelFile)((BlockModelBuilder)this.models().cubeAll(ModBlockStateProvider.name((Block)deferredBlock.get()), this.blockTexture((Block)deferredBlock.get()))).renderType("cutout"));
    }

    private void cutoutMippedBlockWithItem(DeferredBlock<?> deferredBlock) {
        this.simpleBlockWithItem((Block)deferredBlock.get(), (ModelFile)((BlockModelBuilder)this.models().cubeAll(ModBlockStateProvider.name((Block)deferredBlock.get()), this.blockTexture((Block)deferredBlock.get()))).renderType("cutout_mipped"));
    }

    private static String name(Block b) {
        return BuiltInRegistries.BLOCK.getKey((Object)b).getPath();
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        this.simpleBlockWithItem((Block)deferredBlock.get(), this.cubeAll((Block)deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        this.simpleBlockItem((Block)deferredBlock.get(), (ModelFile)new ModelFile.UncheckedModelFile("createcybernetics:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        this.simpleBlockItem((Block)deferredBlock.get(), (ModelFile)new ModelFile.UncheckedModelFile("createcybernetics:block/" + deferredBlock.getId().getPath() + appendix));
    }
}

