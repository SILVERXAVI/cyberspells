/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.common.loot.IGlobalLootModifier
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package com.perigrine3.createcybernetics.loot;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.loot.AddItemModifier;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create((ResourceKey)NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, (String)"createcybernetics");
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}

