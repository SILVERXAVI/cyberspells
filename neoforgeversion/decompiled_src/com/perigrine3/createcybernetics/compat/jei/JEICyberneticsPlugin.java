/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.JeiPlugin
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  mezz.jei.api.registration.IGuiHandlerRegistration
 *  mezz.jei.api.registration.IRecipeCatalystRegistration
 *  mezz.jei.api.registration.IRecipeCategoryRegistration
 *  mezz.jei.api.registration.IRecipeRegistration
 *  mezz.jei.api.registration.IRecipeTransferRegistration
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 */
package com.perigrine3.createcybernetics.compat.jei;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.compat.jei.EngineeringTableRecipeCategory;
import com.perigrine3.createcybernetics.compat.jei.GraftingTableRecipeCategory;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import com.perigrine3.createcybernetics.screen.custom.EngineeringTableMenu;
import com.perigrine3.createcybernetics.screen.custom.EngineeringTableScreen;
import com.perigrine3.createcybernetics.screen.custom.GraftingTableMenu;
import com.perigrine3.createcybernetics.screen.custom.GraftingTableScreen;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;

@JeiPlugin
public class JEICyberneticsPlugin
implements IModPlugin {
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"jei_plugin");
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new IRecipeCategory[]{new EngineeringTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()), new GraftingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper())});
    }

    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) {
            return;
        }
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List engineeringTableRecipes = recipeManager.getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType)ModRecipes.ENGINEERING_TABLE_TYPE.get());
        registration.addRecipes(EngineeringTableRecipeCategory.ENGINEERING_TABLE_RECIPE_TYPE, engineeringTableRecipes);
        List graftingTableRecipes = recipeManager.getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType)ModRecipes.GRAFTING_TABLE_TYPE.get());
        registration.addRecipes(GraftingTableRecipeCategory.GRAFTING_TABLE_RECIPE_TYPE, graftingTableRecipes);
    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack((ItemLike)ModBlocks.ENGINEERING_TABLE.get()), new RecipeType[]{EngineeringTableRecipeCategory.ENGINEERING_TABLE_RECIPE_TYPE});
        registration.addRecipeCatalyst(new ItemStack((ItemLike)ModBlocks.GRAFTING_TABLE.get()), new RecipeType[]{GraftingTableRecipeCategory.GRAFTING_TABLE_RECIPE_TYPE});
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EngineeringTableScreen.class, 157, 103, 16, 11, new RecipeType[]{EngineeringTableRecipeCategory.ENGINEERING_TABLE_RECIPE_TYPE});
        registration.addRecipeClickArea(GraftingTableScreen.class, 109, 38, 14, 9, new RecipeType[]{GraftingTableRecipeCategory.GRAFTING_TABLE_RECIPE_TYPE});
    }

    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(EngineeringTableMenu.class, (MenuType)ModMenuTypes.ENGINEERING_TABLE_MENU.get(), EngineeringTableRecipeCategory.ENGINEERING_TABLE_RECIPE_TYPE, 1, 25, 26, 36);
        registration.addRecipeTransferHandler(GraftingTableMenu.class, (MenuType)ModMenuTypes.GRAFTING_TABLE_MENU.get(), GraftingTableRecipeCategory.GRAFTING_TABLE_RECIPE_TYPE, 0, 7, 8, 36);
    }
}

