/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.gui.builder.IIngredientAcceptor
 *  mezz.jei.api.gui.builder.IRecipeLayoutBuilder
 *  mezz.jei.api.gui.builder.IRecipeSlotBuilder
 *  mezz.jei.api.gui.drawable.IDrawable
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.recipe.IFocusGroup
 *  mezz.jei.api.recipe.RecipeIngredientRole
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.jei.ScrollForgeRecipe;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.Arrays;
import java.util.List;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ScrollForgeRecipeCategory
implements IRecipeCategory<ScrollForgeRecipe> {
    public static final RecipeType<ScrollForgeRecipe> SCROLL_FORGE_RECIPE_RECIPE_TYPE = RecipeType.create((String)"irons_spellbooks", (String)"scroll_forge", ScrollForgeRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final String inkSlotName = "inkSlot";
    private final String paperSlotName = "paperSlot";
    private final String focusSlotName = "focusSlot";
    private final String outputSlotName = "outputSlot";

    public ScrollForgeRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = JeiPlugin.SCROLL_FORGE_GUI;
        this.background = guiHelper.drawableBuilder(location, 11, 16, 64, 49).addPadding(0, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack((ItemLike)BlockRegistry.SCROLL_FORGE_BLOCK.get()));
    }

    public RecipeType<ScrollForgeRecipe> getRecipeType() {
        return SCROLL_FORGE_RECIPE_RECIPE_TYPE;
    }

    public Component getTitle() {
        return ((Block)BlockRegistry.SCROLL_FORGE_BLOCK.get()).getName();
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ScrollForgeRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inkInputs = recipe.inkInputs();
        Ingredient paperInput = recipe.paperInput();
        Ingredient focusInput = recipe.focusInput();
        List<ItemStack> outputs = recipe.scrollOutputs();
        IRecipeSlotBuilder inkInputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(inkInputs)).setSlotName("inkSlot");
        IRecipeSlotBuilder paperInputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 24, 1).addItemStacks(Arrays.asList(paperInput.getItems()))).setSlotName("paperSlot");
        IRecipeSlotBuilder focusInputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 47, 1).addItemStacks(Arrays.asList(focusInput.getItems()))).setSlotName("focusSlot");
        IRecipeSlotBuilder outputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 24, 31).addItemStacks(outputs)).setSlotName("outputSlot");
        if (inkInputs.size() == outputs.size()) {
            builder.createFocusLink(new IIngredientAcceptor[]{inkInputSlot, outputSlot});
        }
    }
}

