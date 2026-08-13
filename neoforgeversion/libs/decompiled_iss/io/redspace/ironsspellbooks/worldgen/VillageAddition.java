/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.ProcessorLists
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool$Projection
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.server.ServerAboutToStartEvent
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.datafixers.util.Pair;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import java.util.ArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber
public class VillageAddition {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create((ResourceKey)Registries.PROCESSOR_LIST, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"minecraft", (String)"empty"));

    private static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        Holder.Reference emptyProcessorList = processorListRegistry.getHolderOrThrow(ProcessorLists.MOSSIFY_70_PERCENT);
        StructureTemplatePool pool = (StructureTemplatePool)templatePoolRegistry.get(poolRL);
        if (pool == null) {
            return;
        }
        SinglePoolElement piece = (SinglePoolElement)SinglePoolElement.legacy((String)nbtPieceRL, (Holder)emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);
        for (int i = 0; i < weight; ++i) {
            pool.templates.add((Object)piece);
        }
        ArrayList<Pair> listOfPieceEntries = new ArrayList<Pair>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair((Object)piece, (Object)weight));
        pool.rawTemplates = listOfPieceEntries;
    }

    @SubscribeEvent
    public static void addNewVillageBuilding(ServerAboutToStartEvent event) {
        Registry templatePoolRegistry = (Registry)event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();
        Registry processorListRegistry = (Registry)event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
        int weight = (Integer)ServerConfigs.PRIEST_TOWER_SPAWNRATE.get();
        if (weight <= 0) {
            return;
        }
        VillageAddition.addBuildingToPool((Registry<StructureTemplatePool>)templatePoolRegistry, (Registry<StructureProcessorList>)processorListRegistry, ResourceLocation.parse((String)"minecraft:village/plains/houses"), "irons_spellbooks:priest_house", weight);
        VillageAddition.addBuildingToPool((Registry<StructureTemplatePool>)templatePoolRegistry, (Registry<StructureProcessorList>)processorListRegistry, ResourceLocation.parse((String)"minecraft:village/taiga/houses"), "irons_spellbooks:priest_house_taiga", weight);
    }
}

