/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NewRegistryEvent
 *  net.neoforged.neoforge.registries.RegistryBuilder
 */
package io.redspace.ironsspellbooks.api.registry;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class SchoolRegistry {
    public static final ResourceKey<Registry<SchoolType>> SCHOOL_REGISTRY_KEY = ResourceKey.createRegistryKey((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"schools"));
    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, (String)"irons_spellbooks");
    public static final Registry<SchoolType> REGISTRY = new RegistryBuilder(SCHOOL_REGISTRY_KEY).create();
    public static final ResourceLocation FIRE_RESOURCE = IronsSpellbooks.id("fire");
    public static final ResourceLocation ICE_RESOURCE = IronsSpellbooks.id("ice");
    public static final ResourceLocation LIGHTNING_RESOURCE = IronsSpellbooks.id("lightning");
    public static final ResourceLocation HOLY_RESOURCE = IronsSpellbooks.id("holy");
    public static final ResourceLocation ENDER_RESOURCE = IronsSpellbooks.id("ender");
    public static final ResourceLocation BLOOD_RESOURCE = IronsSpellbooks.id("blood");
    public static final ResourceLocation EVOCATION_RESOURCE = IronsSpellbooks.id("evocation");
    public static final ResourceLocation NATURE_RESOURCE = IronsSpellbooks.id("nature");
    public static final ResourceLocation ELDRITCH_RESOURCE = IronsSpellbooks.id("eldritch");
    public static final Supplier<SchoolType> FIRE = SchoolRegistry.registerSchool(new SchoolType(FIRE_RESOURCE, ModTags.FIRE_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.fire").withStyle(ChatFormatting.GOLD), (Holder<Attribute>)AttributeRegistry.FIRE_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.FIRE_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.FIRE_CAST, ISSDamageTypes.FIRE_MAGIC));
    public static final Supplier<SchoolType> ICE = SchoolRegistry.registerSchool(new SchoolType(ICE_RESOURCE, ModTags.ICE_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.ice").withStyle(Style.EMPTY.withColor(13695487)), (Holder<Attribute>)AttributeRegistry.ICE_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.ICE_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.ICE_CAST, ISSDamageTypes.ICE_MAGIC));
    public static final Supplier<SchoolType> LIGHTNING = SchoolRegistry.registerSchool(new SchoolType(LIGHTNING_RESOURCE, ModTags.LIGHTNING_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.lightning").withStyle(ChatFormatting.AQUA), (Holder<Attribute>)AttributeRegistry.LIGHTNING_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.LIGHTNING_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.LIGHTNING_CAST, ISSDamageTypes.LIGHTNING_MAGIC));
    public static final Supplier<SchoolType> HOLY = SchoolRegistry.registerSchool(new SchoolType(HOLY_RESOURCE, ModTags.HOLY_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.holy").withStyle(Style.EMPTY.withColor(16775380)), (Holder<Attribute>)AttributeRegistry.HOLY_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.HOLY_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.HOLY_CAST, ISSDamageTypes.HOLY_MAGIC));
    public static final Supplier<SchoolType> ENDER = SchoolRegistry.registerSchool(new SchoolType(ENDER_RESOURCE, ModTags.ENDER_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.ender").withStyle(ChatFormatting.LIGHT_PURPLE), (Holder<Attribute>)AttributeRegistry.ENDER_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.ENDER_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.ENDER_CAST, ISSDamageTypes.ENDER_MAGIC));
    public static final Supplier<SchoolType> BLOOD = SchoolRegistry.registerSchool(new SchoolType(BLOOD_RESOURCE, ModTags.BLOOD_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.blood").withStyle(ChatFormatting.DARK_RED), (Holder<Attribute>)AttributeRegistry.BLOOD_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.BLOOD_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.BLOOD_CAST, ISSDamageTypes.BLOOD_MAGIC));
    public static final Supplier<SchoolType> EVOCATION = SchoolRegistry.registerSchool(new SchoolType(EVOCATION_RESOURCE, ModTags.EVOCATION_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.evocation").withStyle(ChatFormatting.WHITE), (Holder<Attribute>)AttributeRegistry.EVOCATION_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.EVOCATION_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.EVOCATION_CAST, ISSDamageTypes.EVOCATION_MAGIC));
    public static final Supplier<SchoolType> NATURE = SchoolRegistry.registerSchool(new SchoolType(NATURE_RESOURCE, ModTags.NATURE_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.nature").withStyle(ChatFormatting.GREEN), (Holder<Attribute>)AttributeRegistry.NATURE_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.NATURE_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.NATURE_CAST, ISSDamageTypes.NATURE_MAGIC));
    public static final Supplier<SchoolType> ELDRITCH = SchoolRegistry.registerSchool(new SchoolType(ELDRITCH_RESOURCE, ModTags.ELDRITCH_FOCUS, (Component)Component.translatable((String)"school.irons_spellbooks.eldritch").withStyle(Style.EMPTY.withColor(1016732)), (Holder<Attribute>)AttributeRegistry.ELDRITCH_SPELL_POWER, (Holder<Attribute>)AttributeRegistry.ELDRITCH_MAGIC_RESIST, (Holder<SoundEvent>)SoundRegistry.EVOCATION_CAST, ISSDamageTypes.ELDRITCH_MAGIC, true, false));

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    public static void registerRegistry(NewRegistryEvent event) {
        IronsSpellbooks.LOGGER.debug("SchoolRegistry.registerRegistry");
        event.register(REGISTRY);
    }

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static SchoolType getSchool(ResourceLocation resourceLocation) {
        return (SchoolType)REGISTRY.get(resourceLocation);
    }

    @Nullable
    public static SchoolType getSchoolFromFocus(ItemStack focusStack) {
        for (SchoolType school : REGISTRY) {
            if (!school.isFocus(focusStack)) continue;
            return school;
        }
        return null;
    }

    public static List<SchoolType> getSchoolsFromFocus(ItemStack focusStack) {
        return REGISTRY.stream().filter(school -> school.isFocus(focusStack)).toList();
    }
}

