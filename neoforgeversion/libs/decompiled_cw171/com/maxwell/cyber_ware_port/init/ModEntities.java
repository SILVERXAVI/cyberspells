/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.maxwell.cyber_ware_port.init;

import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberskeleton.CyberSkeletonEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton.CyberWitherSkeletonEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberzombie.CyberZombieEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create((ResourceKey)Registries.ENTITY_TYPE, (String)"cyber_ware_port");
    public static final DeferredHolder<EntityType<?>, EntityType<CyberZombieEntity>> CYBER_ZOMBIE = ENTITIES.register("cyber_zombie", () -> EntityType.Builder.of(CyberZombieEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.95f).build("cyber_zombie"));
    public static final DeferredHolder<EntityType<?>, EntityType<CyberSkeletonEntity>> CYBER_SKELETON = ENTITIES.register("cyber_skeleton", () -> EntityType.Builder.of(CyberSkeletonEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.99f).build("cyber_skeleton"));
    public static final DeferredHolder<EntityType<?>, EntityType<CyberWitherSkeletonEntity>> CYBER_WITHER_SKELETON = ENTITIES.register("cyber_wither_skeleton", () -> EntityType.Builder.of(CyberWitherSkeletonEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.7f, 2.4f).build("cyber_wither_skeleton"));
    public static final DeferredHolder<EntityType<?>, EntityType<CyberCreeperEntity>> CYBER_CREEPER = ENTITIES.register("cyber_creeper", () -> EntityType.Builder.of(CyberCreeperEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.7f, 1.4f).build("cyber_creeper"));
    public static final DeferredHolder<EntityType<?>, EntityType<CyberWitherBoss>> CYBER_WITHER = ENTITIES.register("cyber_wither", () -> EntityType.Builder.of(CyberWitherBoss::new, (MobCategory)MobCategory.MONSTER).sized(0.7f, 2.4f).build("cyber_wither"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}

