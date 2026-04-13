/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.level.Level
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.compat.ironsspells;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

public final class IronsSpellbooksCompat {
    public static final String MODID = "irons_spellbooks";
    public static final ResourceLocation DT_LIGHTNING_MAGIC = IronsSpellbooksCompat.rl("lightning_magic");
    public static final ResourceKey<DamageType> DTK_LIGHTNING_MAGIC = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)DT_LIGHTNING_MAGIC);
    public static final ResourceLocation ATTR_MAX_MANA = IronsSpellbooksCompat.rl("max_mana");
    public static final ResourceLocation ATTR_MANA_REGEN = IronsSpellbooksCompat.rl("mana_regen");
    public static final ResourceLocation ATTR_COOLDOWN_REDUCTION = IronsSpellbooksCompat.rl("cooldown_reduction");
    public static final ResourceLocation ATTR_SPELL_POWER = IronsSpellbooksCompat.rl("spell_power");
    public static final ResourceLocation ATTR_SPELL_RESIST = IronsSpellbooksCompat.rl("spell_resist");
    public static final ResourceLocation ATTR_CAST_TIME_REDUCTION = IronsSpellbooksCompat.rl("cast_time_reduction");
    public static final ResourceLocation ATTR_SUMMON_DAMAGE = IronsSpellbooksCompat.rl("summon_damage");
    public static final ResourceLocation ATTR_CASTING_MOVESPEED = IronsSpellbooksCompat.rl("casting_movespeed");
    public static final ResourceLocation ATTR_FIRE_MAGIC_RESIST = IronsSpellbooksCompat.rl("fire_magic_resist");
    public static final ResourceLocation ATTR_ICE_MAGIC_RESIST = IronsSpellbooksCompat.rl("ice_magic_resist");
    public static final ResourceLocation ATTR_LIGHTNING_MAGIC_RESIST = IronsSpellbooksCompat.rl("lightning_magic_resist");
    public static final ResourceLocation ATTR_HOLY_MAGIC_RESIST = IronsSpellbooksCompat.rl("holy_magic_resist");
    public static final ResourceLocation ATTR_ENDER_MAGIC_RESIST = IronsSpellbooksCompat.rl("ender_magic_resist");
    public static final ResourceLocation ATTR_BLOOD_MAGIC_RESIST = IronsSpellbooksCompat.rl("blood_magic_resist");
    public static final ResourceLocation ATTR_EVOCATION_MAGIC_RESIST = IronsSpellbooksCompat.rl("evocation_magic_resist");
    public static final ResourceLocation ATTR_NATURE_MAGIC_RESIST = IronsSpellbooksCompat.rl("nature_magic_resist");
    public static final ResourceLocation ATTR_ELDRITCH_MAGIC_RESIST = IronsSpellbooksCompat.rl("eldritch_magic_resist");
    public static final ResourceLocation ATTR_FIRE_SPELL_POWER = IronsSpellbooksCompat.rl("fire_spell_power");
    public static final ResourceLocation ATTR_ICE_SPELL_POWER = IronsSpellbooksCompat.rl("ice_spell_power");
    public static final ResourceLocation ATTR_LIGHTNING_SPELL_POWER = IronsSpellbooksCompat.rl("lightning_spell_power");
    public static final ResourceLocation ATTR_HOLY_SPELL_POWER = IronsSpellbooksCompat.rl("holy_spell_power");
    public static final ResourceLocation ATTR_ENDER_SPELL_POWER = IronsSpellbooksCompat.rl("ender_spell_power");
    public static final ResourceLocation ATTR_BLOOD_SPELL_POWER = IronsSpellbooksCompat.rl("blood_spell_power");
    public static final ResourceLocation ATTR_EVOCATION_SPELL_POWER = IronsSpellbooksCompat.rl("evocation_spell_power");
    public static final ResourceLocation ATTR_NATURE_SPELL_POWER = IronsSpellbooksCompat.rl("nature_spell_power");
    public static final ResourceLocation ATTR_ELDRITCH_SPELL_POWER = IronsSpellbooksCompat.rl("eldritch_spell_power");

    private IronsSpellbooksCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath((String)MODID, (String)path);
    }

    @Nullable
    public static Holder<Attribute> getAttributeHolder(ResourceLocation id) {
        if (!IronsSpellbooksCompat.isLoaded()) {
            return null;
        }
        Optional opt = BuiltInRegistries.ATTRIBUTE.getOptional(id);
        if (opt.isEmpty()) {
            return null;
        }
        return BuiltInRegistries.ATTRIBUTE.wrapAsHolder((Object)((Attribute)opt.get()));
    }

    public static Optional<Holder<DamageType>> resolveDamageTypeHolder(RegistryAccess access, ResourceLocation id) {
        if (!IronsSpellbooksCompat.isLoaded()) {
            return Optional.empty();
        }
        ResourceKey key = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)id);
        return access.registry(Registries.DAMAGE_TYPE).flatMap(reg -> reg.getHolder(key));
    }

    private static Holder<DamageType> fallbackLightningHolder(RegistryAccess access) {
        Registry reg = access.registryOrThrow(Registries.DAMAGE_TYPE);
        return reg.getHolderOrThrow(DamageTypes.LIGHTNING_BOLT);
    }

    public static DamageSource lightningMagic(Level level, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        ResourceKey key;
        ResourceKey resourceKey = key = IronsSpellbooksCompat.resolveDamageTypeHolder(level.registryAccess(), DT_LIGHTNING_MAGIC).isPresent() ? DTK_LIGHTNING_MAGIC : DamageTypes.LIGHTNING_BOLT;
        if (directEntity != null || causingEntity != null) {
            return level.damageSources().source(key, directEntity, causingEntity);
        }
        return level.damageSources().source(key);
    }

    public static boolean hurtLightningMagic(LivingEntity target, float amount, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        DamageSource src = IronsSpellbooksCompat.lightningMagic(target.level(), directEntity, causingEntity);
        return target.hurt(src, amount);
    }

    public static boolean isLightningMagic(DamageSource src) {
        if (src == null) {
            return false;
        }
        return src.typeHolder().unwrapKey().filter(DTK_LIGHTNING_MAGIC::equals).isPresent();
    }

    public static boolean isAnyLightning(DamageSource src) {
        if (src == null) {
            return false;
        }
        if (src.is(DamageTypes.LIGHTNING_BOLT)) {
            return true;
        }
        return IronsSpellbooksCompat.isLightningMagic(src);
    }
}

