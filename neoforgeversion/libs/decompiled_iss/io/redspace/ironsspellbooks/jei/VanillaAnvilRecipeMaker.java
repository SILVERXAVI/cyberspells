/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe
 *  mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SmithingRecipe
 *  net.minecraft.world.level.ItemLike
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.recipe_types.NoAdditionSmithingTransformRecipe;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.ItemLike;

public class VanillaAnvilRecipeMaker {
    static List<IJeiAnvilRecipe> getAnvilRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        return Stream.concat(VanillaAnvilRecipeMaker.getArmorRepairRecipes(vanillaRecipeFactory, itemFinder), VanillaAnvilRecipeMaker.getItemRepairRecipes(vanillaRecipeFactory, itemFinder)).toList();
    }

    static List<RecipeHolder<SmithingRecipe>> getCustomSmithingRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        if (Minecraft.getInstance().level == null) {
            return List.of();
        }
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING).stream().filter(holder -> holder.value() instanceof NoAdditionSmithingTransformRecipe).toList();
    }

    static Stream<IJeiAnvilRecipe> getItemRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        return itemFinder.ironsTieredItems.stream().mapMulti((item, consumer) -> {
            ItemStack damagedThreeQuarters = new ItemStack((ItemLike)item);
            damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
            ItemStack damagedHalf = new ItemStack((ItemLike)item);
            damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);
            IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
            consumer.accept(repairWithSame);
            List<ItemStack> repairMaterials = Arrays.stream(item.getTier().getRepairIngredient().getItems()).toList();
            ItemStack damagedFully = new ItemStack((ItemLike)item);
            damagedFully.setDamageValue(damagedFully.getMaxDamage());
            IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
            consumer.accept(repairWithMaterial);
        });
    }

    static Stream<IJeiAnvilRecipe> getArmorRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        return itemFinder.ironsArmorItems.stream().mapMulti((item, consumer) -> {
            ItemStack damagedThreeQuarters = new ItemStack((ItemLike)item);
            damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
            ItemStack damagedHalf = new ItemStack((ItemLike)item);
            damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);
            IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
            consumer.accept(repairWithSame);
            List<ItemStack> repairMaterials = Arrays.stream(((Ingredient)((ArmorMaterial)item.getMaterial().value()).repairIngredient().get()).getItems()).toList();
            ItemStack damagedFully = new ItemStack((ItemLike)item);
            damagedFully.setDamageValue(damagedFully.getMaxDamage());
            IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
            consumer.accept(repairWithMaterial);
        });
    }
}

