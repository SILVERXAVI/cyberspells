/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.entity;

import com.perigrine3.createcybernetics.entity.custom.CyberskeletonEntity;
import com.perigrine3.createcybernetics.entity.custom.CyberzombieEntity;
import com.perigrine3.createcybernetics.entity.custom.GuardianBeamEntity;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import com.perigrine3.createcybernetics.entity.projectile.EmpGrenadeProjectile;
import com.perigrine3.createcybernetics.entity.projectile.NuggetProjectile;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create((Registry)BuiltInRegistries.ENTITY_TYPE, (String)"createcybernetics");
    public static final Supplier<EntityType<GuardianBeamEntity>> GUARDIAN_BEAM = ENTITY_TYPES.register("guardian_beam", () -> EntityType.Builder.of(GuardianBeamEntity::new, (MobCategory)MobCategory.MISC).sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1).build("guardian_beam"));
    public static final Supplier<EntityType<NuggetProjectile>> NUGGET_PROJECTILE = ENTITY_TYPES.register("nugget_projectile", () -> EntityType.Builder.of(NuggetProjectile::new, (MobCategory)MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("nugget_projectile"));
    public static final Supplier<EntityType<EmpGrenadeProjectile>> EMP_GRENADE_PROJECTILE = ENTITY_TYPES.register("emp_grenade_projectile", () -> EntityType.Builder.of(EmpGrenadeProjectile::new, (MobCategory)MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(8).updateInterval(10).build("emp_grenade_projectile"));
    public static final Supplier<EntityType<SmasherEntity>> SMASHER = ENTITY_TYPES.register("smasher", () -> EntityType.Builder.of(SmasherEntity::new, (MobCategory)MobCategory.MONSTER).sized(1.1f, 2.5f).build("smasher"));
    public static final Supplier<EntityType<CyberzombieEntity>> CYBERZOMBIE = ENTITY_TYPES.register("cyberzombie", () -> EntityType.Builder.of(CyberzombieEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).build("cyberzombie"));
    public static final Supplier<EntityType<CyberskeletonEntity>> CYBERSKELETON = ENTITY_TYPES.register("cyberskeleton", () -> EntityType.Builder.of(CyberskeletonEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).build("cyberskeleton"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

