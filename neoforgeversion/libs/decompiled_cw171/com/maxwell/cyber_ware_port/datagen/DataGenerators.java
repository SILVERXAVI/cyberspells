/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.DataGenerator
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.data.event.GatherDataEvent
 */
package com.maxwell.cyber_ware_port.datagen;

import com.maxwell.cyber_ware_port.datagen.ModBlockTagProvider;
import com.maxwell.cyber_ware_port.datagen.ModItemTagProvider;
import com.maxwell.cyber_ware_port.datagen.ModLanguageProvider;
import com.maxwell.cyber_ware_port.datagen.ModRecipeProvider;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), output -> new ModRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), (DataProvider)new ModLanguageProvider(packOutput, "zh_cn"));
        generator.addProvider(event.includeServer(), (DataProvider)new ModLanguageProvider(packOutput, "ru_ru"));
        generator.addProvider(event.includeServer(), (DataProvider)new ModLanguageProvider(packOutput, "fr_fr"));
        generator.addProvider(event.includeServer(), (DataProvider)new ModLanguageProvider(packOutput, "en_us"));
        generator.addProvider(event.includeServer(), (DataProvider)new ModLanguageProvider(packOutput, "ja_jp"));
        ModBlockTagProvider blockTags = (ModBlockTagProvider)generator.addProvider(event.includeServer(), (DataProvider)new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), (DataProvider)new ModItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }
}

