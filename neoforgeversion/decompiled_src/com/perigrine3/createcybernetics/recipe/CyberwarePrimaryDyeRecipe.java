/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.DyeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.DyedItemColor
 *  net.minecraft.world.item.crafting.CraftingBookCategory
 *  net.minecraft.world.item.crafting.CraftingInput
 *  net.minecraft.world.item.crafting.CustomRecipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.recipe;

import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.item.generic.InfologDataShardItem;
import com.perigrine3.createcybernetics.recipe.ModRecipeSerializers;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class CyberwarePrimaryDyeRecipe
extends CustomRecipe {
    public CyberwarePrimaryDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, Level level) {
        ItemStack target = ItemStack.EMPTY;
        boolean hasDye = false;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof DyeItem) {
                hasDye = true;
                continue;
            }
            if (!target.isEmpty()) {
                return false;
            }
            if (!CyberwarePrimaryDyeRecipe.isValidTarget(stack)) {
                return false;
            }
            target = stack;
        }
        return !target.isEmpty() && hasDye;
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack target = ItemStack.EMPTY;
        ArrayList<DyeItem> dyes = new ArrayList<DyeItem>();
        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            if (item instanceof DyeItem) {
                DyeItem dye = (DyeItem)item;
                dyes.add(dye);
                continue;
            }
            if (!target.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (!CyberwarePrimaryDyeRecipe.isValidTarget(stack)) {
                return ItemStack.EMPTY;
            }
            target = stack;
        }
        if (target.isEmpty() || dyes.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack out = target.copy();
        out.setCount(1);
        int rgb = CyberwarePrimaryDyeRecipe.cc$mixDyeRgb(out, dyes);
        out.set(DataComponents.DYED_COLOR, (Object)new DyedItemColor(rgb, true));
        return out;
    }

    private static boolean isValidTarget(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ICyberwareItem) {
            ICyberwareItem cyberwareItem = (ICyberwareItem)item;
            return cyberwareItem.isDyeable(stack);
        }
        item = stack.getItem();
        if (item instanceof InfologDataShardItem) {
            InfologDataShardItem dataShardItem = (InfologDataShardItem)item;
            return dataShardItem.isDyeable(stack);
        }
        return false;
    }

    private static int cc$mixDyeRgb(ItemStack base, List<DyeItem> dyes) {
        int rTotal = 0;
        int gTotal = 0;
        int bTotal = 0;
        int maxTotal = 0;
        int count = 0;
        DyedItemColor existing = (DyedItemColor)base.get(DataComponents.DYED_COLOR);
        if (existing != null) {
            int rgb = existing.rgb();
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            rTotal += r;
            gTotal += g;
            bTotal += b;
            maxTotal += Math.max(r, Math.max(g, b));
            ++count;
        }
        for (DyeItem dye : dyes) {
            int rgb = dye.getDyeColor().getTextureDiffuseColor();
            int r = (rgb &= 0xFFFFFF) >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            rTotal += r;
            gTotal += g;
            bTotal += b;
            maxTotal += Math.max(r, Math.max(g, b));
            ++count;
        }
        if (count <= 0) {
            return 0xFFFFFF;
        }
        int rAvg = rTotal / count;
        int gAvg = gTotal / count;
        int bAvg = bTotal / count;
        float brightnessAvg = (float)maxTotal / (float)count;
        float maxAvg = Math.max(rAvg, Math.max(gAvg, bAvg));
        if (maxAvg > 0.0f) {
            rAvg = (int)((float)rAvg * (brightnessAvg / maxAvg));
            gAvg = (int)((float)gAvg * (brightnessAvg / maxAvg));
            bAvg = (int)((float)bAvg * (brightnessAvg / maxAvg));
        }
        rAvg = Math.max(0, Math.min(255, rAvg));
        gAvg = Math.max(0, Math.min(255, gAvg));
        bAvg = Math.max(0, Math.min(255, bAvg));
        return rAvg << 16 | gAvg << 8 | bAvg;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)ModRecipeSerializers.CYBERWARE_PRIMARY_DYE.get();
    }
}

