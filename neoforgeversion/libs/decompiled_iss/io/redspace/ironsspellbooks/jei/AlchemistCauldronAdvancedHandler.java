/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.constants.VanillaTypes
 *  mezz.jei.api.ingredients.IIngredientType
 *  mezz.jei.api.ingredients.ITypedIngredient
 *  mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronJeiRecipe;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronRecipeMaker;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class AlchemistCauldronAdvancedHandler
implements ISimpleRecipeManagerPlugin<AlchemistCauldronJeiRecipe> {
    public boolean isHandledInput(ITypedIngredient<?> input) {
        ItemStack stack = (ItemStack)input.getCastIngredient((IIngredientType)VanillaTypes.ITEM_STACK);
        if (stack == null || Minecraft.getInstance().level == null) {
            return false;
        }
        if (((Boolean)ServerConfigs.ALLOW_CAULDRON_BREWING.get()).booleanValue() && stack.has(DataComponents.POTION_CONTENTS)) {
            return true;
        }
        RecipeManager m = Minecraft.getInstance().level.getRecipeManager();
        return m.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get()).stream().anyMatch(empty -> ((FillAlchemistCauldronRecipe)empty.value()).input().test(stack));
    }

    public boolean isHandledOutput(ITypedIngredient<?> output) {
        ItemStack stack = (ItemStack)output.getCastIngredient((IIngredientType)VanillaTypes.ITEM_STACK);
        if (stack == null || Minecraft.getInstance().level == null) {
            return false;
        }
        if (((Boolean)ServerConfigs.ALLOW_CAULDRON_BREWING.get()).booleanValue() && stack.has(DataComponents.POTION_CONTENTS)) {
            return true;
        }
        RecipeManager m = Minecraft.getInstance().level.getRecipeManager();
        return m.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get()).stream().anyMatch(empty -> ItemStack.isSameItemSameComponents((ItemStack)stack, (ItemStack)((EmptyAlchemistCauldronRecipe)empty.value()).result()));
    }

    public List<AlchemistCauldronJeiRecipe> getRecipesForInput(ITypedIngredient<?> input) {
        ItemStack stack = (ItemStack)input.getCastIngredient((IIngredientType)VanillaTypes.ITEM_STACK);
        if (stack == null || Minecraft.getInstance().level == null) {
            return List.of();
        }
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        Optional<FluidStack> fluidConversion = manager.getRecipeFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get(), (RecipeInput)new SingleRecipeInput(stack), (Level)Minecraft.getInstance().level).map(RecipeHolder::value).map(FillAlchemistCauldronRecipe::result);
        if (fluidConversion.isEmpty()) {
            fluidConversion = Optional.of(PotionFluid.from(stack));
        }
        return fluidConversion.map(inputFluid -> AlchemistCauldronRecipeMaker.recipes.stream().filter(recipe -> FluidStack.isSameFluidSameComponents((FluidStack)recipe.fluidIn(), (FluidStack)inputFluid)).toList()).orElse(List.of());
    }

    public List<AlchemistCauldronJeiRecipe> getRecipesForOutput(ITypedIngredient<?> output) {
        ItemStack stack = (ItemStack)output.getCastIngredient((IIngredientType)VanillaTypes.ITEM_STACK);
        if (stack == null || Minecraft.getInstance().level == null) {
            return List.of();
        }
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        Optional<FluidStack> fluidConversion = manager.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get()).stream().map(RecipeHolder::value).filter(emptyAlchemistCauldronRecipe -> ItemStack.isSameItemSameComponents((ItemStack)emptyAlchemistCauldronRecipe.result(), (ItemStack)stack)).map(EmptyAlchemistCauldronRecipe::fluid).findFirst();
        if (fluidConversion.isEmpty() && ((Boolean)ServerConfigs.ALLOW_CAULDRON_BREWING.get()).booleanValue() && !PotionFluid.from(stack).isEmpty()) {
            fluidConversion = Optional.of(PotionFluid.from(stack));
        }
        return fluidConversion.map(outputFluid -> AlchemistCauldronRecipeMaker.recipes.stream().filter(recipe -> recipe.results().stream().anyMatch(result -> FluidStack.isSameFluidSameComponents((FluidStack)result, (FluidStack)outputFluid))).toList()).orElse(List.of());
    }

    public List<AlchemistCauldronJeiRecipe> getAllRecipes() {
        return AlchemistCauldronRecipeMaker.recipes;
    }
}

