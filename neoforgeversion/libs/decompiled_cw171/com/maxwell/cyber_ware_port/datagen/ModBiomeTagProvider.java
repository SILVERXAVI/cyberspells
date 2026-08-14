/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.tags.BiomeTagsProvider
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BiomeTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.level.biome.Biome
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBiomeTagProvider
extends BiomeTagsProvider {
    public static final ResourceKey<Biome> HAS_CYBER_LAB = ResourceKey.create((ResourceKey)Registries.BIOME, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"has_structure/cyber_lab"));

    public ModBiomeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, "cyber_ware_port", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(TagKey.create((ResourceKey)Registries.BIOME, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"has_structure/cyber_lab"))).addTag(BiomeTags.IS_OVERWORLD).remove(BiomeTags.IS_OCEAN).remove(BiomeTags.IS_RIVER);
    }
}

