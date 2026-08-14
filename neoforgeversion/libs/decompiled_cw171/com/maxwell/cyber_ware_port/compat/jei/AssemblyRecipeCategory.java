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
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.compat.jei;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.init.ModBlocks;
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
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class AssemblyRecipeCategory
implements IRecipeCategory<AssemblyRecipe> {
    public static final RecipeType<AssemblyRecipe> RECIPE_TYPE = RecipeType.create((String)"cyber_ware_port", (String)"assembly", AssemblyRecipe.class);
    private static final ResourceLocation BACKGROUND_LOC = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/engineering.png");
    private final IDrawable background;
    private final IDrawable icon;

    public AssemblyRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND_LOC, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)new ItemStack((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get()));
    }

    @NotNull
    public RecipeType<AssemblyRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @NotNull
    public Component getTitle() {
        return Component.translatable((String)"gui.cyber_ware_port.assemble");
    }

    @NotNull
    public IDrawable getBackground() {
        return this.background;
    }

    @NotNull
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AssemblyRecipe recipe, IFocusGroup focuses) {
        RegistryAccess registries = Minecraft.getInstance().level.registryAccess();
        int gridStartX = 71;
        int gridStartY = 17;
        for (int i = 0; i < Math.min(recipe.getInputs().size(), 6); ++i) {
            AssemblyRecipe.SizedIngredient input = recipe.getInputs().get(i);
            int x = gridStartX + i % 2 * 18;
            int y = gridStartY + i / 2 * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(input.ingredient());
        }
        ItemStack result = recipe.getResultItem((HolderLookup.Provider)registries);
        ItemStack blueprint = BlueprintItem.createBlueprintFor(result.getItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 115, 53).addItemStack(blueprint);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 141, 21).addItemStack(result);
    }
}

