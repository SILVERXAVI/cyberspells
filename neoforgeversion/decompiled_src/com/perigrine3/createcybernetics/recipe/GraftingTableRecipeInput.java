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

public record GraftingTableRecipeInput(List<ItemStack> items) implements RecipeInput
{
    public GraftingTableRecipeInput {
        items = items == null ? List.of() : List.copyOf(items);
    }

    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (stack.isEmpty()) continue;
            return false;
        }
        return true;
    }
}

