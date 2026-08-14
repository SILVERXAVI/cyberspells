/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.emi.api.EmiEntrypoint
 *  dev.emi.emi.api.EmiPlugin
 *  dev.emi.emi.api.EmiRegistry
 *  dev.emi.emi.api.recipe.EmiRecipe
 *  dev.emi.emi.api.recipe.EmiRecipeCategory
 *  dev.emi.emi.api.render.EmiRenderable
 *  dev.emi.emi.api.render.EmiTexture
 *  dev.emi.emi.api.stack.EmiIngredient
 *  dev.emi.emi.api.stack.EmiStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 */
package com.maxwell.cyber_ware_port.compat.emi;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import com.maxwell.cyber_ware_port.compat.emi.EmiAssemblyRecipe;
import com.maxwell.cyber_ware_port.compat.emi.EmiEngineeringRecipe;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModRecipes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

@EmiEntrypoint
public class CyberwareEmiPlugin
implements EmiPlugin {
    public static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/engineering.png");
    public static final EmiRecipeCategory ASSEMBLY_CATEGORY = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"assembly"), (EmiRenderable)EmiStack.of((ItemLike)((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get())), (EmiRenderable)new EmiTexture(GUI_TEXTURE, 0, 0, 16, 16));
    public static final EmiRecipeCategory ENGINEERING_CATEGORY = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"engineering"), (EmiRenderable)EmiStack.of((ItemLike)((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get())), (EmiRenderable)new EmiTexture(GUI_TEXTURE, 16, 0, 16, 16));

    public void register(EmiRegistry registry) {
        registry.addCategory(ASSEMBLY_CATEGORY);
        registry.addCategory(ENGINEERING_CATEGORY);
        registry.addWorkstation(ASSEMBLY_CATEGORY, (EmiIngredient)EmiStack.of((ItemLike)((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get())));
        registry.addWorkstation(ENGINEERING_CATEGORY, (EmiIngredient)EmiStack.of((ItemLike)((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get())));
        RecipeManager rm = registry.getRecipeManager();
        for (RecipeHolder recipe : rm.getAllRecipesFor((RecipeType)ModRecipes.ASSEMBLY_TYPE.get())) {
            registry.addRecipe((EmiRecipe)new EmiAssemblyRecipe((RecipeHolder<AssemblyRecipe>)recipe));
        }
        for (RecipeHolder recipe : rm.getAllRecipesFor((RecipeType)ModRecipes.ENGINEERING_TYPE.get())) {
            registry.addRecipe((EmiRecipe)new EmiEngineeringRecipe((RecipeHolder<EngineeringRecipe>)recipe));
        }
    }
}

