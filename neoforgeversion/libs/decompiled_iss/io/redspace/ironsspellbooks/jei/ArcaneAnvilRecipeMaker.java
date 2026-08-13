/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.jei.ArcaneAnvilJeiRecipe;
import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ArcaneAnvilRecipeMaker {
    private ArcaneAnvilRecipeMaker() {
    }

    static List<ArcaneAnvilJeiRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        return Stream.of(ArcaneAnvilRecipeMaker.getScrollRecipes(itemFinder), ArcaneAnvilRecipeMaker.getImbueRecipes(itemFinder), ArcaneAnvilRecipeMaker.getUpgradeRecipes(itemFinder), ArcaneAnvilRecipeMaker.getAffinityAttuneRecipes(itemFinder)).flatMap(x -> x).toList();
    }

    private static Stream<ArcaneAnvilJeiRecipe> getScrollRecipes(JeiPlugin.ItemFinder itemFinder) {
        if (!ServerConfigs.SPEC.isLoaded() || ((Boolean)ServerConfigs.SCROLL_MERGING.get()).booleanValue()) {
            return SpellRegistry.getEnabledSpells().stream().sorted(Comparator.comparing(AbstractSpell::getSpellId)).flatMap(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel() - 1).mapToObj(i -> new ArcaneAnvilJeiRecipe((AbstractSpell)spell, i)));
        }
        return Stream.empty();
    }

    private static Stream<ArcaneAnvilJeiRecipe> getImbueRecipes(JeiPlugin.ItemFinder itemFinder) {
        return itemFinder.imbueable.stream().map(item -> new ArcaneAnvilJeiRecipe((Item)item, (AbstractSpell)null));
    }

    private static Stream<ArcaneAnvilJeiRecipe> getUpgradeRecipes(JeiPlugin.ItemFinder itemFinder) {
        return BuiltInRegistries.ITEM.stream().filter(item -> item.components().has((DataComponentType)ComponentRegistry.UPGRADE_ORB_TYPE.get())).flatMap(upgradeOrb -> itemFinder.upgradeable.stream().map(item -> new ArcaneAnvilJeiRecipe((Item)item, (Item)upgradeOrb)));
    }

    private static Stream<ArcaneAnvilJeiRecipe> getAffinityAttuneRecipes(JeiPlugin.ItemFinder itemFinder) {
        return SpellRegistry.getEnabledSpells().stream().sorted(Comparator.comparing(AbstractSpell::getSpellId)).map(ArcaneAnvilJeiRecipe::new);
    }

    private static ItemStack getScrollStack(ItemStack stack, AbstractSpell spell, int spellLevel) {
        ItemStack scrollStack = stack.copy();
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }
}

