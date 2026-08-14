/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.JeiPlugin
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  mezz.jei.api.registration.IRecipeCategoryRegistration
 *  mezz.jei.api.registration.IRecipeRegistration
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.compat.jei;

import com.maxwell.cyber_ware_port.compat.jei.AssemblyRecipeCategory;
import com.maxwell.cyber_ware_port.compat.jei.EngineeringRecipeCategory;
import com.maxwell.cyber_ware_port.init.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CyberwareJeiPlugin
implements IModPlugin {
    @NotNull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"jei_plugin");
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new IRecipeCategory[]{new AssemblyRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
        registration.addRecipeCategories(new IRecipeCategory[]{new EngineeringRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
    }

    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(AssemblyRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor((RecipeType)ModRecipes.ASSEMBLY_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(EngineeringRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor((RecipeType)ModRecipes.ENGINEERING_TYPE.get()).stream().map(RecipeHolder::value).toList());
    }
}

