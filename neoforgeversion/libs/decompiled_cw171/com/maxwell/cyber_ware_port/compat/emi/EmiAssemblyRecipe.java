/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.emi.api.recipe.EmiRecipe
 *  dev.emi.emi.api.recipe.EmiRecipeCategory
 *  dev.emi.emi.api.render.EmiTexture
 *  dev.emi.emi.api.stack.EmiIngredient
 *  dev.emi.emi.api.stack.EmiStack
 *  dev.emi.emi.api.widget.WidgetHolder
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 */
package com.maxwell.cyber_ware_port.compat.emi;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.compat.emi.CyberwareEmiPlugin;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiAssemblyRecipe
implements EmiRecipe {
    private final RecipeHolder<AssemblyRecipe> holder;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    private final EmiStack blueprint;

    public EmiAssemblyRecipe(RecipeHolder<AssemblyRecipe> holder) {
        this.holder = holder;
        this.inputs = new ArrayList<EmiIngredient>();
        for (AssemblyRecipe.SizedIngredient input : ((AssemblyRecipe)holder.value()).getInputs()) {
            this.inputs.add(EmiIngredient.of((Ingredient)input.ingredient(), (long)input.count()));
        }
        ItemStack result = ((AssemblyRecipe)holder.value()).getResultItem((HolderLookup.Provider)Minecraft.getInstance().level.registryAccess());
        this.output = EmiStack.of((ItemStack)result);
        this.blueprint = EmiStack.of((ItemStack)BlueprintItem.createBlueprintFor(result.getItem()));
    }

    public EmiRecipeCategory getCategory() {
        return CyberwareEmiPlugin.ASSEMBLY_CATEGORY;
    }

    public ResourceLocation getId() {
        return this.holder.id();
    }

    public List<EmiIngredient> getInputs() {
        ArrayList<EmiIngredient> totalInputs = new ArrayList<EmiIngredient>(this.inputs);
        totalInputs.add((EmiIngredient)this.blueprint);
        return totalInputs;
    }

    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    public int getDisplayWidth() {
        return 176;
    }

    public int getDisplayHeight() {
        return 80;
    }

    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(new EmiTexture(CyberwareEmiPlugin.GUI_TEXTURE, 0, 0, 176, 80), 0, 0);
        int gridX = 70;
        int gridY = 16;
        for (int i = 0; i < Math.min(this.inputs.size(), 6); ++i) {
            widgets.addSlot(this.inputs.get(i), gridX + i % 2 * 18, gridY + i / 2 * 18).drawBack(false);
        }
        widgets.addSlot((EmiIngredient)this.blueprint, 114, 52).drawBack(false);
        widgets.addSlot((EmiIngredient)this.output, 140, 20).drawBack(false).recipeContext((EmiRecipe)this);
    }
}

