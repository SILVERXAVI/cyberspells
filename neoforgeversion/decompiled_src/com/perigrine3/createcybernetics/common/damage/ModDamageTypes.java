/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.damagesource.DamageType
 */
package com.perigrine3.createcybernetics.common.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> SURGERY = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"surgery"));
    public static final ResourceKey<DamageType> CYBERWARE_REJECTION = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_rejection"));
    public static final ResourceKey<DamageType> BRAIN_DAMAGE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"brain_damage"));
    public static final ResourceKey<DamageType> HEART_ATTACK = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"heart_attack"));
    public static final ResourceKey<DamageType> LIVER_FAILURE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"liver_failure"));
    public static final ResourceKey<DamageType> MISSING_SKIN = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_skin"));
    public static final ResourceKey<DamageType> MISSING_LUNGS = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_lungs"));
    public static final ResourceKey<DamageType> BONELESS = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"boneless"));
    public static final ResourceKey<DamageType> MISSING_MUSCLE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_muscle"));
    public static final ResourceKey<DamageType> DAVIDS_DEMISE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"davids_demise"));

    private ModDamageTypes() {
    }
}

