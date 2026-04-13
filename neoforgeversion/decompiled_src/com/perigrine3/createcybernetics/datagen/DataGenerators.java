/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.DataGenerator
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.loot.LootTableProvider
 *  net.minecraft.data.loot.LootTableProvider$SubProviderEntry
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.data.event.GatherDataEvent
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.datagen.FarmersDelightCompatRecipeProvider;
import com.perigrine3.createcybernetics.datagen.ModAdvancementProvider;
import com.perigrine3.createcybernetics.datagen.ModBlockLootTableProvider;
import com.perigrine3.createcybernetics.datagen.ModBlockStateProvider;
import com.perigrine3.createcybernetics.datagen.ModBlockTagProvider;
import com.perigrine3.createcybernetics.datagen.ModDataMapProvider;
import com.perigrine3.createcybernetics.datagen.ModDatapackProvider;
import com.perigrine3.createcybernetics.datagen.ModGlobalLootModifierProvider;
import com.perigrine3.createcybernetics.datagen.ModItemModelProvider;
import com.perigrine3.createcybernetics.datagen.ModItemTagProvider;
import com.perigrine3.createcybernetics.datagen.ModRecipeProvider;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), (DataProvider)new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(event.includeServer(), (DataProvider)new ModRecipeProvider(packOutput, lookupProvider));
        ModBlockTagProvider blockTagsProvider = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), (DataProvider)blockTagsProvider);
        generator.addProvider(event.includeServer(), (DataProvider)new ModItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeClient(), (DataProvider)new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), (DataProvider)new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), (DataProvider)new ModDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), (DataProvider)new ModGlobalLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), (DataProvider)new ModDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), (DataProvider)new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), (DataProvider)new FarmersDelightCompatRecipeProvider(packOutput));
    }
}

