/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.recipe;

import com.perigrine3.createcybernetics.recipe.EngineeringTableRecipe;
import com.perigrine3.createcybernetics.recipe.GraftingTableRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create((ResourceKey)Registries.RECIPE_SERIALIZER, (String)"createcybernetics");
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create((ResourceKey)Registries.RECIPE_TYPE, (String)"createcybernetics");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EngineeringTableRecipe>> ENGINEERING_TABLE_SERIALIZER = SERIALIZERS.register("engineering_table", EngineeringTableRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EngineeringTableRecipe>> ENGINEERING_TABLE_TYPE = TYPES.register("engineering_table", () -> new RecipeType<EngineeringTableRecipe>(){

        public String toString() {
            return "engineering_table";
        }
    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GraftingTableRecipe>> GRAFTING_TABLE_SERIALIZER = SERIALIZERS.register("grafting_table", GraftingTableRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<GraftingTableRecipe>> GRAFTING_TABLE_TYPE = TYPES.register("grafting_table", () -> new RecipeType<GraftingTableRecipe>(){

        public String toString() {
            return "grafting_table";
        }
    });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}

