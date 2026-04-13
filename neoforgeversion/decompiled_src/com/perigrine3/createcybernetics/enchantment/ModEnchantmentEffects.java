/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.enchantment;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.enchantment.custom.HarvesterEnchantmentEffect;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS = DeferredRegister.create((ResourceKey)Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, (String)"createcybernetics");
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> HARVESTER = ENTITY_ENCHANTMENT_EFFECTS.register("harvester", () -> HarvesterEnchantmentEffect.CODEC);

    public static void register(IEventBus eventBus) {
        ENTITY_ENCHANTMENT_EFFECTS.register(eventBus);
    }
}

