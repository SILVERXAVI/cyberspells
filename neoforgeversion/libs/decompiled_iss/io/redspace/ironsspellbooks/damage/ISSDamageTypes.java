/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.damagesource.DamageScaling
 *  net.minecraft.world.damagesource.DamageType
 */
package io.redspace.ironsspellbooks.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ISSDamageTypes {
    public static final ResourceKey<DamageType> FIRE_MAGIC = ISSDamageTypes.register("fire_magic");
    public static final ResourceKey<DamageType> ICE_MAGIC = ISSDamageTypes.register("ice_magic");
    public static final ResourceKey<DamageType> LIGHTNING_MAGIC = ISSDamageTypes.register("lightning_magic");
    public static final ResourceKey<DamageType> HOLY_MAGIC = ISSDamageTypes.register("holy_magic");
    public static final ResourceKey<DamageType> ENDER_MAGIC = ISSDamageTypes.register("ender_magic");
    public static final ResourceKey<DamageType> BLOOD_MAGIC = ISSDamageTypes.register("blood_magic");
    public static final ResourceKey<DamageType> EVOCATION_MAGIC = ISSDamageTypes.register("evocation_magic");
    public static final ResourceKey<DamageType> ELDRITCH_MAGIC = ISSDamageTypes.register("eldritch_magic");
    public static final ResourceKey<DamageType> NATURE_MAGIC = ISSDamageTypes.register("nature_magic");
    public static final ResourceKey<DamageType> CAULDRON = ISSDamageTypes.register("blood_cauldron");
    public static final ResourceKey<DamageType> HEARTSTOP = ISSDamageTypes.register("heartstop");
    public static final ResourceKey<DamageType> DRAGON_BREATH_POOL = ISSDamageTypes.register("dragon_breath_pool");
    public static final ResourceKey<DamageType> FIRE_FIELD = ISSDamageTypes.register("fire_field");
    public static final ResourceKey<DamageType> POISON_CLOUD = ISSDamageTypes.register("poison_cloud");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name));
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(FIRE_MAGIC, (Object)new DamageType(FIRE_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(ICE_MAGIC, (Object)new DamageType(ICE_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(LIGHTNING_MAGIC, (Object)new DamageType(LIGHTNING_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(HOLY_MAGIC, (Object)new DamageType(HOLY_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(ENDER_MAGIC, (Object)new DamageType(ENDER_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(BLOOD_MAGIC, (Object)new DamageType(BLOOD_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(EVOCATION_MAGIC, (Object)new DamageType(EVOCATION_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(ELDRITCH_MAGIC, (Object)new DamageType(ELDRITCH_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(NATURE_MAGIC, (Object)new DamageType(NATURE_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(CAULDRON, (Object)new DamageType(CAULDRON.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(HEARTSTOP, (Object)new DamageType(HEARTSTOP.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(DRAGON_BREATH_POOL, (Object)new DamageType(DRAGON_BREATH_POOL.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(FIRE_FIELD, (Object)new DamageType(FIRE_FIELD.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
        context.register(POISON_CLOUD, (Object)new DamageType(POISON_CLOUD.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
    }
}

