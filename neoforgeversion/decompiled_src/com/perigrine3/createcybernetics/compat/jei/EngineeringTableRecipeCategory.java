/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.constants.VanillaTypes
 *  mezz.jei.api.gui.builder.IRecipeLayoutBuilder
 *  mezz.jei.api.gui.drawable.IDrawable
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.ingredients.IIngredientType
 *  mezz.jei.api.recipe.IFocusGroup
 *  mezz.jei.api.recipe.RecipeIngredientRole
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.compat.jei;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.recipe.EngineeringTableRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class EngineeringTableRecipeCategory
implements IRecipeCategory<RecipeHolder<EngineeringTableRecipe>> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"engineering_table");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/engineering_gui.png");
    public static final RecipeType<RecipeHolder<EngineeringTableRecipe>> ENGINEERING_TABLE_RECIPE_TYPE = new RecipeType(UID, RecipeHolder.class);
    private final IDrawable background;
    private final IDrawable icon;

    public EngineeringTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 59, 60, 139, 98);
        this.icon = ModBlocks.ENGINEERING_TABLE != null ? helper.createDrawableIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)new ItemStack((ItemLike)ModBlocks.ENGINEERING_TABLE.get())) : null;
    }

    public RecipeType<RecipeHolder<EngineeringTableRecipe>> getRecipeType() {
        return ENGINEERING_TABLE_RECIPE_TYPE;
    }

    public Component getTitle() {
        return Component.translatable((String)"gui.engineeringtable.title");
    }

    @Nullable
    public IDrawable getIcon() {
        return this.icon;
    }

    @Nullable
    public IDrawable getBackground() {
        return this.background;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EngineeringTableRecipe> holder, IFocusGroup focuses) {
        EngineeringTableRecipe recipe = (EngineeringTableRecipe)holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 5).addIngredients((Ingredient)recipe.getIngredients().get(0));
        int startX = 7;
        int startY = 5;
        int spacing = 18;
        int ingredientIndex = 1;
        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col) {
                if (row == 0 && col == 0) continue;
                int x = 7 + col * 18;
                int y = 5 + row * 18;
                if (ingredientIndex < recipe.getIngredients().size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients((Ingredient)recipe.getIngredients().get(ingredientIndex));
                } else {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y);
                }
                ++ingredientIndex;
            }
        }
        ItemStack result = ItemStack.EMPTY;
        if (Minecraft.getInstance().level != null) {
            result = recipe.getResultItem((HolderLookup.Provider)Minecraft.getInstance().level.registryAccess());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 41).addItemStack(result);
    }
}

