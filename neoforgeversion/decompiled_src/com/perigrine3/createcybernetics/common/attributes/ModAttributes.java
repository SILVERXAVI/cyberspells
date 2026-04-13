/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.RangedAttribute
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.common.attributes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create((ResourceKey)Registries.ATTRIBUTE, (String)"createcybernetics");
    public static final DeferredHolder<Attribute, Attribute> XP_GAIN_MULTIPLIER = ATTRIBUTES.register("xp_gain_multiplier", () -> new RangedAttribute("attribute.createcybernetics.xp_gain_multiplier", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ORE_DROP_MULTIPLIER = ATTRIBUTES.register("ore_drop_multiplier", () -> new RangedAttribute("attribute.createcybernetics.ore_drop_multiplier", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> HAGGLING = ATTRIBUTES.register("haggling", () -> new RangedAttribute("attribute.createcybernetics.haggling", 1.0, 0.0, 2.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ENDER_PEARL_DAMAGE = ATTRIBUTES.register("ender_pearl_damage", () -> new RangedAttribute("attribute.createcybernetics.ender_pearl_damage", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ARROW_INACCURACY = ATTRIBUTES.register("arrow_inaccuracy", () -> new RangedAttribute("attribute.createcybernetics.arrow_inaccuracy", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BREEDING_MULTIPLIER = ATTRIBUTES.register("breeding_multiplier", () -> new RangedAttribute("attribute.createcybernetics.breeding_multiplier", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> CROP_MULTIPLIER = ATTRIBUTES.register("crop_multiplier", () -> new RangedAttribute("attribute.createcybernetics.crop_multiplier", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ELYTRA_SPEED = ATTRIBUTES.register("elytra_speed", () -> new RangedAttribute("attribute.createcybernetics.elytra_speed", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ELYTRA_HANDLING = ATTRIBUTES.register("elytra_handling", () -> new RangedAttribute("attribute.createcybernetics.elytra_handling", 1.0, 0.0, 16.0).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> INSOMNIA = ATTRIBUTES.register("insomnia", () -> new RangedAttribute("attribute.createcybernetics.insomnia", 3.0, 0.0, 16.0).setSyncable(true));

    private ModAttributes() {
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}

