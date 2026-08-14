/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.constants.VanillaTypes
 *  mezz.jei.api.gui.builder.IRecipeLayoutBuilder
 *  mezz.jei.api.gui.builder.IRecipeSlotBuilder
 *  mezz.jei.api.gui.drawable.IDrawable
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.ingredients.IIngredientType
 *  mezz.jei.api.recipe.IFocusGroup
 *  mezz.jei.api.recipe.RecipeIngredientRole
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.compat.jei;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class EngineeringRecipeCategory
implements IRecipeCategory<EngineeringRecipe> {
    public static final RecipeType<EngineeringRecipe> RECIPE_TYPE = RecipeType.create((String)"cyber_ware_port", (String)"engineering", EngineeringRecipe.class);
    private static final ResourceLocation BACKGROUND_LOC = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/engineering.png");
    private final IDrawable background;
    private final IDrawable icon;

    public EngineeringRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND_LOC, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)new ItemStack((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get()));
    }

    @NotNull
    public RecipeType<EngineeringRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @NotNull
    public Component getTitle() {
        return Component.translatable((String)"gui.cyber_ware_port.deconstruct");
    }

    @NotNull
    public IDrawable getBackground() {
        return this.background;
    }

    @NotNull
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, EngineeringRecipe recipe, IFocusGroup focuses) {
        ItemStack blueprint;
        Ingredient inputIng = (Ingredient)recipe.getIngredients().get(0);
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 20).addIngredients(inputIng);
        ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.CATALYST, 15, 53).addItemStack(new ItemStack((ItemLike)Items.PAPER))).addTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable((String)"gui.cyber_ware_port.need_paper").withStyle(ChatFormatting.GRAY)));
        int outputX = 71;
        int outputY = 17;
        for (int i = 0; i < Math.min(recipe.outputs().size(), 6); ++i) {
            EngineeringRecipe.OutputEntry entry = recipe.outputs().get(i);
            int x = outputX + i % 2 * 18;
            int y = outputY + i / 2 * 18;
            ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(entry.stack())).addTooltipCallback((view, tooltip) -> {
                float chance = entry.chance() * 100.0f;
                tooltip.add(Component.literal((String)String.format("%.0f%% Chance", Float.valueOf(chance))).withStyle(ChatFormatting.YELLOW));
            });
        }
        float bpChance = recipe.getBlueprintChance();
        if (bpChance > 0.0f && inputIng.getItems().length > 0 && !(blueprint = BlueprintItem.createBlueprintFor(inputIng.getItems()[0].getItem())).isEmpty()) {
            ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 53).addItemStack(blueprint)).addTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable((String)"gui.cyber_ware_port.blueprint_chance", (Object[])new Object[]{String.format("%.0f", Float.valueOf(bpChance * 100.0f))}).withStyle(ChatFormatting.BLUE)));
        }
    }
}

