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
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.compat.jei;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.recipe.GraftingTableRecipe;
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
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public final class GraftingTableRecipeCategory
implements IRecipeCategory<RecipeHolder<GraftingTableRecipe>> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"grafting_table");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/grafting_table_gui.png");
    public static final RecipeType<RecipeHolder<GraftingTableRecipe>> GRAFTING_TABLE_RECIPE_TYPE = new RecipeType(UID, RecipeHolder.class);
    private final IDrawable background;
    private final IDrawable icon;

    public GraftingTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 3, 150, 78);
        this.icon = helper.createDrawableIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)new ItemStack((ItemLike)ModBlocks.GRAFTING_TABLE.get()));
    }

    public RecipeType<RecipeHolder<GraftingTableRecipe>> getRecipeType() {
        return GRAFTING_TABLE_RECIPE_TYPE;
    }

    public Component getTitle() {
        return Component.translatable((String)"gui.createcybernetics.grafting_table.title");
    }

    @Nullable
    public IDrawable getIcon() {
        return this.icon;
    }

    @Nullable
    public IDrawable getBackground() {
        return this.background;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<GraftingTableRecipe> holder, IFocusGroup focuses) {
        GraftingTableRecipe recipe = (GraftingTableRecipe)holder.value();
        NonNullList<Ingredient> wetware = recipe.getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 23).addIngredients(wetware.size() > 0 ? (Ingredient)wetware.get(0) : Ingredient.EMPTY);
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 23).addIngredients(wetware.size() > 1 ? (Ingredient)wetware.get(1) : Ingredient.EMPTY);
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 41).addIngredients(wetware.size() > 2 ? (Ingredient)wetware.get(2) : Ingredient.EMPTY);
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 41).addIngredients(wetware.size() > 3 ? (Ingredient)wetware.get(3) : Ingredient.EMPTY);
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 23).addItemStack(new ItemStack((ItemLike)ModItems.COMPONENT_MESH.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 41).addItemStack(new ItemStack((ItemLike)Items.STRING));
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 32).addItemStack(new ItemStack((ItemLike)Items.GHAST_TEAR));
        ItemStack out = ItemStack.EMPTY;
        if (Minecraft.getInstance().level != null) {
            out = recipe.getResultItem((HolderLookup.Provider)Minecraft.getInstance().level.registryAccess());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 32).addItemStack(out);
    }
}

