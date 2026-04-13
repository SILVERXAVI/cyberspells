/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeInput
 */
package com.perigrine3.createcybernetics.recipe;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record EngineeringTableRecipeInput(List<ItemStack> items) implements RecipeInput
{
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    public int size() {
        return this.items.size();
    }
}

