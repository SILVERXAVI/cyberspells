/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.SpawnPlacementTypes
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions
 *  net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
 *  net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
 *  net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent
 *  net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent$Operation
 */
package com.perigrine3.createcybernetics.event;

import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.client.CyberskeletonModel;
import com.perigrine3.createcybernetics.entity.client.CyberzombieModel;
import com.perigrine3.createcybernetics.entity.client.SmasherModel;
import com.perigrine3.createcybernetics.entity.custom.CyberskeletonEntity;
import com.perigrine3.createcybernetics.entity.custom.CyberzombieEntity;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SmasherModel.LAYER_LOCATION, SmasherModel::createBodyLayer);
        event.registerLayerDefinition(CyberzombieModel.LAYER_LOCATION, CyberzombieModel::createBodyLayer);
        event.registerLayerDefinition(CyberskeletonModel.LAYER_LOCATION, CyberskeletonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SMASHER.get(), SmasherEntity.createAttributes().build());
        event.put(ModEntities.CYBERZOMBIE.get(), CyberzombieEntity.createAttributes().build());
        event.put(ModEntities.CYBERSKELETON.get(), CyberskeletonEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.SMASHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CYBERZOMBIE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CYBERSKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
    }
}

