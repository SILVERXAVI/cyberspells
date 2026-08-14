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
package com.maxwell.cyber_ware_port.init;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create((ResourceKey)Registries.RECIPE_SERIALIZER, (String)"cyber_ware_port");
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create((ResourceKey)Registries.RECIPE_TYPE, (String)"cyber_ware_port");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EngineeringRecipe>> ENGINEERING_SERIALIZER = SERIALIZERS.register("engineering", () -> EngineeringRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EngineeringRecipe>> ENGINEERING_TYPE = TYPES.register("engineering", () -> new RecipeType<EngineeringRecipe>(){

        public String toString() {
            return "cyber_ware_port:engineering";
        }
    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AssemblyRecipe>> ASSEMBLY_SERIALIZER = SERIALIZERS.register("assembly", () -> AssemblyRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<AssemblyRecipe>> ASSEMBLY_TYPE = TYPES.register("assembly", () -> new RecipeType<AssemblyRecipe>(){

        public String toString() {
            return "cyber_ware_port:assembly";
        }
    });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}

