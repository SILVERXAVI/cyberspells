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
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.level.ItemLike
 */
package com.maxwell.cyber_ware_port.compat.emi;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class EmiEngineeringRecipe
implements EmiRecipe {
    private final RecipeHolder<EngineeringRecipe> holder;
    private final EmiIngredient input;
    private final List<EmiStack> outputs;
    private final EmiStack blueprint;

    public EmiEngineeringRecipe(RecipeHolder<EngineeringRecipe> holder) {
        this.holder = holder;
        this.input = EmiIngredient.of((Ingredient)((EngineeringRecipe)holder.value()).input());
        this.outputs = new ArrayList<EmiStack>();
        for (EngineeringRecipe.OutputEntry entry : ((EngineeringRecipe)holder.value()).outputs()) {
            this.outputs.add(EmiStack.of((ItemStack)entry.stack()));
        }
        ItemStack bpStack = ItemStack.EMPTY;
        if (((EngineeringRecipe)holder.value()).input().getItems().length > 0) {
            bpStack = BlueprintItem.createBlueprintFor(((EngineeringRecipe)holder.value()).input().getItems()[0].getItem());
        }
        this.blueprint = EmiStack.of((ItemStack)bpStack);
    }

    public EmiRecipeCategory getCategory() {
        return CyberwareEmiPlugin.ENGINEERING_CATEGORY;
    }

    public ResourceLocation getId() {
        return this.holder.id();
    }

    public List<EmiIngredient> getInputs() {
        return List.of(this.input);
    }

    public List<EmiStack> getOutputs() {
        ArrayList<EmiStack> totalOutputs = new ArrayList<EmiStack>(this.outputs);
        if (!this.blueprint.isEmpty()) {
            totalOutputs.add(this.blueprint);
        }
        return totalOutputs;
    }

    public int getDisplayWidth() {
        return 176;
    }

    public int getDisplayHeight() {
        return 80;
    }

    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(new EmiTexture(CyberwareEmiPlugin.GUI_TEXTURE, 0, 0, 176, 80), 0, 0);
        widgets.addSlot(this.input, 14, 19).drawBack(false);
        widgets.addSlot((EmiIngredient)EmiStack.of((ItemLike)Items.PAPER), 14, 52).drawBack(false).appendTooltip((Component)Component.translatable((String)"gui.cyber_ware_port.need_paper").withStyle(ChatFormatting.GRAY));
        int gridX = 70;
        int gridY = 16;
        for (int i = 0; i < Math.min(this.outputs.size(), 6); ++i) {
            float chance = ((EngineeringRecipe)this.holder.value()).outputs().get(i).chance() * 100.0f;
            widgets.addSlot((EmiIngredient)this.outputs.get(i), gridX + i % 2 * 18, gridY + i / 2 * 18).drawBack(false).appendTooltip((Component)Component.literal((String)String.format("%.0f%% Chance", Float.valueOf(chance))).withStyle(ChatFormatting.YELLOW));
        }
        if (!this.blueprint.isEmpty()) {
            float bpChance = ((EngineeringRecipe)this.holder.value()).getBlueprintChance() * 100.0f;
            widgets.addSlot((EmiIngredient)this.blueprint, 114, 52).drawBack(false).appendTooltip((Component)Component.translatable((String)"gui.cyber_ware_port.blueprint_chance", (Object[])new Object[]{String.format("%.0f", Float.valueOf(bpChance))}).withStyle(ChatFormatting.BLUE));
        }
    }
}

