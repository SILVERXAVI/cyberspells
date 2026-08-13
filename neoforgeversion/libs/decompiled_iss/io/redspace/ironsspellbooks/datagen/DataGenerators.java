/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.DetectedVersion
 *  net.minecraft.data.DataGenerator
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.loot.LootTableProvider
 *  net.minecraft.data.loot.LootTableProvider$SubProviderEntry
 *  net.minecraft.data.metadata.PackMetadataGenerator
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.packs.PackType
 *  net.minecraft.server.packs.metadata.pack.PackMetadataSection
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.data.event.GatherDataEvent
 */
package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.datagen.IronLootTableProviders;
import io.redspace.ironsspellbooks.datagen.IronRecipeProvider;
import io.redspace.ironsspellbooks.datagen.RegistryDataGenerator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.DetectedVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture provider = event.getLookupProvider();
        RegistryDataGenerator datapackProvider = new RegistryDataGenerator(output, provider);
        CompletableFuture lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), (DataProvider)datapackProvider);
        generator.addProvider(event.includeServer(), (DataProvider)new DamageTypeTagGenerator(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), (DataProvider)new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(IronLootTableProviders.Block::new, LootContextParamSets.BLOCK)), provider));
        generator.addProvider(event.includeServer(), (DataProvider)new IronRecipeProvider(output, lookupProvider));
        generator.addProvider(true, (DataProvider)new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, (Object)new PackMetadataSection((Component)Component.literal((String)"Resources for Iron's Spells N Spellbooks"), DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES), Optional.empty())));
    }
}

