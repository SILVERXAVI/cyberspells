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
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.recipe_types.NoAdditionSmithingTransformRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeRegistry {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create((ResourceKey)Registries.RECIPE_TYPE, (String)"irons_spellbooks");
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create((ResourceKey)Registries.RECIPE_SERIALIZER, (String)"irons_spellbooks");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FillAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_FILL_SERIALIZER = RECIPE_SERIALIZERS.register("alchemist_cauldron_fill", FillAlchemistCauldronRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<FillAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_FILL_TYPE = RECIPE_TYPES.register("alchemist_cauldron_fill", registry -> new RecipeType<FillAlchemistCauldronRecipe>(){

        public String toString() {
            return registry.toString();
        }
    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmptyAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_EMPTY_SERIALIZER = RECIPE_SERIALIZERS.register("alchemist_cauldron_empty", EmptyAlchemistCauldronRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EmptyAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_EMPTY_TYPE = RECIPE_TYPES.register("alchemist_cauldron_empty", registry -> new RecipeType<EmptyAlchemistCauldronRecipe>(){

        public String toString() {
            return registry.toString();
        }
    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BrewAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_BREW_SERIALIZER = RECIPE_SERIALIZERS.register("alchemist_cauldron_brew", BrewAlchemistCauldronRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<BrewAlchemistCauldronRecipe>> ALCHEMIST_CAULDRON_BREW_TYPE = RECIPE_TYPES.register("alchemist_cauldron_brew", registry -> new RecipeType<BrewAlchemistCauldronRecipe>(){

        public String toString() {
            return registry.toString();
        }
    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NoAdditionSmithingTransformRecipe>> SMITHING_TRANSFORM_NO_ADDITION_SERIALIZER = RECIPE_SERIALIZERS.register("smithing_transform_no_addition", NoAdditionSmithingTransformRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}

