/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.PotionBrewing
 *  net.minecraft.world.item.alchemy.PotionBrewing$Mix
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronJeiRecipe;
import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public final class AlchemistCauldronRecipeMaker {
    public static List<AlchemistCauldronJeiRecipe> recipes = List.of();

    private AlchemistCauldronRecipeMaker() {
    }

    static List<AlchemistCauldronJeiRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        recipes = Stream.of(AlchemistCauldronRecipeMaker.getScrollRecipes(vanillaRecipeFactory, itemFinder), AlchemistCauldronRecipeMaker.getCauldronRecipes(vanillaRecipeFactory, itemFinder), AlchemistCauldronRecipeMaker.getPotionRecipes(vanillaRecipeFactory, itemFinder)).flatMap(Function.identity()).toList();
        return recipes;
    }

    private static Stream<AlchemistCauldronJeiRecipe> getScrollRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        return Arrays.stream(SpellRarity.values()).map(AlchemistCauldronRecipeMaker::enumerateSpellsForRarity);
    }

    private static Stream<AlchemistCauldronJeiRecipe> getCauldronRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        if (Minecraft.getInstance().level == null) {
            return Stream.of(new AlchemistCauldronJeiRecipe[0]);
        }
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        return manager.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_BREW_TYPE.get()).stream().map(RecipeHolder::value).map(recipe -> new AlchemistCauldronJeiRecipe(recipe.reagent(), recipe.fluidIn(), recipe.results(), recipe.byproduct().orElse(ItemStack.EMPTY)));
    }

    private static Stream<Item> getBrewingReagents(PotionBrewing potionBrewing) {
        return Stream.concat(potionBrewing.containerMixes.stream(), potionBrewing.potionMixes.stream()).map(PotionBrewing.Mix::ingredient).flatMap(i -> Arrays.stream(i.getItems())).map(ItemStack::getItem).distinct();
    }

    private static Stream<AlchemistCauldronJeiRecipe> getPotionRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        if (!((Boolean)ServerConfigs.ALLOW_CAULDRON_BREWING.get()).booleanValue()) {
            return Stream.of(new AlchemistCauldronJeiRecipe[0]);
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return Stream.of(new AlchemistCauldronJeiRecipe[0]);
        }
        PotionBrewing potionBrewing = Minecraft.getInstance().level.potionBrewing();
        Stream brewablePotions = BuiltInRegistries.POTION.holders().flatMap(potion -> Stream.of(PotionContents.createItemStack((Item)Items.POTION, (Holder)potion), PotionContents.createItemStack((Item)Items.SPLASH_POTION, (Holder)potion), PotionContents.createItemStack((Item)Items.LINGERING_POTION, (Holder)potion)));
        return brewablePotions.flatMap(potion -> AlchemistCauldronRecipeMaker.getBrewingReagents(potionBrewing).filter(reagent -> potionBrewing.hasMix(potion, reagent.getDefaultInstance())).map(reagent -> new AlchemistCauldronJeiRecipe(Ingredient.of((ItemLike[])new ItemLike[]{reagent}), PotionFluid.from(potion), List.of(PotionFluid.from(level.potionBrewing().mix(reagent.getDefaultInstance(), potion))), ItemStack.EMPTY)));
    }

    private static AlchemistCauldronJeiRecipe enumerateSpellsForRarity(SpellRarity spellRarity) {
        ItemStack scrollStack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
        Stream scrolls = SpellRegistry.getEnabledSpells().stream().flatMap(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).filter(spellLevel -> spell.getRarity(spellLevel) == spellRarity).mapToObj(i -> AlchemistCauldronRecipeMaker.getScrollStack(scrollStack, spell, i)));
        FluidStack ink = new FluidStack(InkItem.getInkForRarity(spellRarity).fluid(), 250);
        FluidStack water = new FluidStack((Fluid)Fluids.WATER, 250);
        return new AlchemistCauldronJeiRecipe(Ingredient.of(scrolls), water, List.of(ink), ItemStack.EMPTY);
    }

    private static ItemStack getScrollStack(ItemStack stack, AbstractSpell spell, int spellLevel) {
        ItemStack scrollStack = stack.copy();
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }

    private static boolean isIngredient(ItemStack itemStack) {
        try {
            return Minecraft.getInstance().level.potionBrewing().isIngredient(itemStack);
        }
        catch (LinkageError | RuntimeException e) {
            IronsSpellbooks.LOGGER.error("Failed to check if item is a potion reagent {}.", (Object)itemStack.toString(), (Object)e);
            return false;
        }
    }
}

