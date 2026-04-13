/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.neoforged.neoforge.common.data.BlockTagsProvider
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider
extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "createcybernetics", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add((Object)((Block)ModBlocks.TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.SMOOTH_TITANIUM.get())).add((Object)((Block)ModBlocks.TITANIUM_GRATE.get())).add((Object)((Block)ModBlocks.TITANIUM_CLAD_COPPER.get())).add((Object)((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get())).add((Object)((Block)ModBlocks.RAW_TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.TITANIUMORE_BLOCK.get())).add((Object)((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get())).add((Object)((Block)ModBlocks.CHARGING_BLOCK.get()));
        this.tag(BlockTags.NEEDS_IRON_TOOL).add((Object)((Block)ModBlocks.TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.SMOOTH_TITANIUM.get())).add((Object)((Block)ModBlocks.TITANIUM_GRATE.get())).add((Object)((Block)ModBlocks.TITANIUM_CLAD_COPPER.get())).add((Object)((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get())).add((Object)((Block)ModBlocks.RAW_TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.TITANIUMORE_BLOCK.get())).add((Object)((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get()));
        this.tag(ModTags.Blocks.METAL_DETECTABLE).add((Object)((Block)ModBlocks.TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.SMOOTH_TITANIUM.get())).add((Object)((Block)ModBlocks.TITANIUM_GRATE.get())).add((Object)((Block)ModBlocks.TITANIUM_CLAD_COPPER.get())).add((Object)((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get())).add((Object)((Block)ModBlocks.RAW_TITANIUM_BLOCK.get())).add((Object)((Block)ModBlocks.TITANIUMORE_BLOCK.get())).add((Object)((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get())).add((Object)Blocks.IRON_ORE).add((Object)Blocks.DEEPSLATE_IRON_ORE).add((Object)Blocks.GOLD_ORE).add((Object)Blocks.DEEPSLATE_GOLD_ORE).add((Object)Blocks.COPPER_ORE).add((Object)Blocks.DEEPSLATE_COPPER_ORE).add((Object)Blocks.NETHER_GOLD_ORE).add((Object)Blocks.IRON_BLOCK).add((Object)Blocks.GOLD_BLOCK).add((Object)Blocks.COPPER_BLOCK).add((Object)Blocks.RAW_IRON_BLOCK).add((Object)Blocks.RAW_GOLD_BLOCK).add((Object)Blocks.RAW_COPPER_BLOCK).add((Object)Blocks.NETHERITE_BLOCK).addOptional(ResourceLocation.fromNamespaceAndPath((String)"create", (String)"zinc_ore")).addOptional(ResourceLocation.fromNamespaceAndPath((String)"create", (String)"zinc_block")).addOptional(ResourceLocation.fromNamespaceAndPath((String)"create", (String)"raw_zinc_block")).addOptional(ResourceLocation.fromNamespaceAndPath((String)"create", (String)"brass_block")).addOptional(ResourceLocation.fromNamespaceAndPath((String)"create", (String)"industrial_iron_block"));
        this.tag(ModTags.Blocks.C_TITANIUM).add((Object)((Block)ModBlocks.TITANIUMORE_BLOCK.get())).add((Object)((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get()));
    }
}

