/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.item.alchemy.Potion
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.potion;

import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create((Registry)BuiltInRegistries.POTION, (String)"createcybernetics");
    public static final Holder<Potion> NEUROPOZYNE = POTIONS.register("neuropozyne_potion", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(ModEffects.NEUROPOZYNE, 3600, 0)}));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}

