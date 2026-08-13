/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import com.google.common.collect.ImmutableList;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval=true)
public class AlchemistCauldronRecipeRegistry {
    @Deprecated(forRemoval=true)
    public static AlchemistCauldronRecipe registerRecipe(ResourceLocation resourceLocation, AlchemistCauldronRecipe recipe) {
        IronsSpellbooks.LOGGER.warn("Mod {} is trying to register an Alchemist Cauldron recipe, which no longer works!", (Object)resourceLocation.getNamespace());
        return recipe;
    }

    @Deprecated(forRemoval=true)
    public static ItemStack getOutput(ItemStack input, ItemStack ingredient, boolean consumeOnSucces) {
        return ItemStack.EMPTY;
    }

    @Deprecated(forRemoval=true)
    public static ItemStack getOutput(ItemStack input, ItemStack ingredient, boolean ignoreCount, boolean consumeOnSucces) {
        return ItemStack.EMPTY;
    }

    @Deprecated(forRemoval=true)
    public static boolean isValidIngredient(ItemStack itemStack) {
        return false;
    }

    @Deprecated(forRemoval=true)
    public static boolean hasOutput(ItemStack input, ItemStack ingredient) {
        return false;
    }

    @Deprecated(forRemoval=true)
    @Nullable
    public static AlchemistCauldronRecipe getRecipeForResult(ItemStack result) {
        return null;
    }

    @Deprecated(forRemoval=true)
    @Nullable
    public static AlchemistCauldronRecipe getRecipeForInputs(ItemStack base, ItemStack reagent) {
        return null;
    }

    @Deprecated(forRemoval=true)
    public static ImmutableList<AlchemistCauldronRecipe> getRecipes() {
        return ImmutableList.of();
    }
}

