/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.common.data.BlockTagsProvider
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.datagen;

import com.maxwell.cyber_ware_port.init.ModBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider
extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "cyber_ware_port", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.FENCES).add((Object)((Block)ModBlocks.RADIO_TOWER_COMPONENT.get()));
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add((Object)((Block)ModBlocks.RADIO_TOWER_COMPONENT.get())).add((Object)((Block)ModBlocks.RADIO_TOWER_CORE.get())).add((Object)((Block)ModBlocks.ROBO_SURGEON.get())).add((Object)((Block)ModBlocks.SURGERY_CHAMBER.get())).add((Object)((Block)ModBlocks.CYBERWARE_WORKBENCH.get())).add((Object)((Block)ModBlocks.SCANNER.get())).add((Object)((Block)ModBlocks.BLUEPRINT_CHEST.get())).add((Object)((Block)ModBlocks.RADIO_KIT_BLOCK.get())).add((Object)((Block)ModBlocks.CHARGER.get())).add((Object)((Block)ModBlocks.COMPONENT_BOX.get())).add((Object)((Block)ModBlocks.CYBER_WITHER_SKELETON_SKULL.get())).add((Object)((Block)ModBlocks.CYBER_WITHER_SKELETON_WALL_SKULL.get()));
        this.tag(BlockTags.NEEDS_IRON_TOOL).add((Object)((Block)ModBlocks.RADIO_TOWER_COMPONENT.get())).add((Object)((Block)ModBlocks.RADIO_TOWER_CORE.get())).add((Object)((Block)ModBlocks.ROBO_SURGEON.get())).add((Object)((Block)ModBlocks.SURGERY_CHAMBER.get())).add((Object)((Block)ModBlocks.CYBERWARE_WORKBENCH.get())).add((Object)((Block)ModBlocks.SCANNER.get())).add((Object)((Block)ModBlocks.CHARGER.get())).add((Object)((Block)ModBlocks.BLUEPRINT_CHEST.get())).add((Object)((Block)ModBlocks.RADIO_KIT_BLOCK.get()));
    }
}

