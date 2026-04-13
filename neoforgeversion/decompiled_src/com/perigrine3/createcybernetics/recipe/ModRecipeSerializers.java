/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.recipe;

import com.perigrine3.createcybernetics.recipe.CyberwarePrimaryDyeRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create((ResourceKey)Registries.RECIPE_SERIALIZER, (String)"createcybernetics");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CyberwarePrimaryDyeRecipe>> CYBERWARE_PRIMARY_DYE = SERIALIZERS.register("cyberware_primary_dye", () -> new SimpleCraftingRecipeSerializer(CyberwarePrimaryDyeRecipe::new));

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}

