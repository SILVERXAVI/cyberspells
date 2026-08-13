/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.gui.builder.IIngredientAcceptor
 *  mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension
 *  net.minecraft.world.item.crafting.Ingredient
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.recipe_types.NoAdditionSmithingTransformRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.world.item.crafting.Ingredient;

public class NoAdditionSmithingExtension
implements ISmithingCategoryExtension<NoAdditionSmithingTransformRecipe> {
    public <T extends IIngredientAcceptor<T>> void setTemplate(NoAdditionSmithingTransformRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getTemplate());
    }

    public <T extends IIngredientAcceptor<T>> void setBase(NoAdditionSmithingTransformRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getBase());
    }

    public <T extends IIngredientAcceptor<T>> void setAddition(NoAdditionSmithingTransformRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(Ingredient.EMPTY);
    }

    public <T extends IIngredientAcceptor<T>> void setOutput(NoAdditionSmithingTransformRecipe recipe, T ingredientAcceptor) {
        ingredientAcceptor.addIngredients(recipe.getResult());
    }
}

