/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.Monster
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
 */
package com.maxwell.cyber_ware_port.common.entity;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.MOD)
public class EntitiesAttributeEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put((EntityType)ModEntities.CYBER_ZOMBIE.get(), Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ARMOR, 2.0).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
        event.put((EntityType)ModEntities.CYBER_SKELETON.get(), AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 25.0).add(Attributes.MOVEMENT_SPEED, 0.3).build());
        event.put((EntityType)ModEntities.CYBER_CREEPER.get(), Creeper.createAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).build());
        event.put((EntityType)ModEntities.CYBER_WITHER.get(), CyberWitherBoss.createAttributes().build());
        event.put((EntityType)ModEntities.CYBER_WITHER_SKELETON.get(), AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 8.0).build());
    }
}

