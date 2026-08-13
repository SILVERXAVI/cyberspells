/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.JeiPlugin
 *  mezz.jei.api.constants.RecipeTypes
 *  mezz.jei.api.constants.VanillaTypes
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.helpers.IJeiHelpers
 *  mezz.jei.api.ingredients.IIngredientType
 *  mezz.jei.api.ingredients.IIngredientTypeWithSubtypes
 *  mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension
 *  mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory
 *  mezz.jei.api.registration.IAdvancedRegistration
 *  mezz.jei.api.registration.IGuiHandlerRegistration
 *  mezz.jei.api.registration.IRecipeCatalystRegistration
 *  mezz.jei.api.registration.IRecipeCategoryRegistration
 *  mezz.jei.api.registration.IRecipeRegistration
 *  mezz.jei.api.registration.IRecipeTransferRegistration
 *  mezz.jei.api.registration.ISubtypeRegistration
 *  mezz.jei.api.registration.IVanillaCategoryExtensionRegistration
 *  mezz.jei.api.runtime.IIngredientManager
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TieredItem
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.material.Fluid
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilMenu;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilScreen;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeScreen;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronAdvancedHandler;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronRecipeCategory;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronRecipeMaker;
import io.redspace.ironsspellbooks.jei.ArcaneAnvilRecipeCategory;
import io.redspace.ironsspellbooks.jei.ArcaneAnvilRecipeMaker;
import io.redspace.ironsspellbooks.jei.FluidSubtype;
import io.redspace.ironsspellbooks.jei.FurledMapJeiInterpreter;
import io.redspace.ironsspellbooks.jei.NoAdditionSmithingExtension;
import io.redspace.ironsspellbooks.jei.PotionFluidInterpreter;
import io.redspace.ironsspellbooks.jei.ScrollForgeRecipeCategory;
import io.redspace.ironsspellbooks.jei.ScrollForgeRecipeMaker;
import io.redspace.ironsspellbooks.jei.ScrollJeiInterpreter;
import io.redspace.ironsspellbooks.jei.VanillaAnvilRecipeMaker;
import io.redspace.ironsspellbooks.recipe_types.NoAdditionSmithingTransformRecipe;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

@mezz.jei.api.JeiPlugin
public class JeiPlugin
implements IModPlugin {
    public static final ResourceLocation RECIPE_GUI_VANILLA = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/gui_vanilla.png");
    public static final ResourceLocation ALCHEMIST_CAULDRON_GUI = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/jei_alchemist_cauldron.png");
    public static final ResourceLocation SCROLL_FORGE_GUI = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/scroll_forge.png");

    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"jei_plugin");
    }

    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter((Item)ItemRegistry.SCROLL.get(), (ISubtypeInterpreter)new ScrollJeiInterpreter());
        registration.registerSubtypeInterpreter((Item)ItemRegistry.FURLED_MAP.get(), (ISubtypeInterpreter)new FurledMapJeiInterpreter());
        registration.registerSubtypeInterpreter((Item)ItemRegistry.ANCIENT_FURLED_MAP.get(), (ISubtypeInterpreter)new FurledMapJeiInterpreter());
        registration.registerSubtypeInterpreter((IIngredientTypeWithSubtypes)new FluidSubtype(), (Object)((Fluid)FluidRegistry.POTION_FLUID.get()), (ISubtypeInterpreter)new PotionFluidInterpreter());
    }

    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new IRecipeCategory[]{new ArcaneAnvilRecipeCategory(guiHelper)});
        registration.addRecipeCategories(new IRecipeCategory[]{new ScrollForgeRecipeCategory(guiHelper)});
        registration.addRecipeCategories(new IRecipeCategory[]{new AlchemistCauldronRecipeCategory(guiHelper)});
    }

    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
        ItemFinder itemFinder = new ItemFinder(ingredientManager);
        registration.addRecipes(ArcaneAnvilRecipeCategory.ARCANE_ANVIL_RECIPE_RECIPE_TYPE, ArcaneAnvilRecipeMaker.getRecipes(vanillaRecipeFactory, itemFinder));
        registration.addRecipes(ScrollForgeRecipeCategory.SCROLL_FORGE_RECIPE_RECIPE_TYPE, ScrollForgeRecipeMaker.getRecipes(vanillaRecipeFactory, itemFinder));
        registration.addRecipes(AlchemistCauldronRecipeCategory.ALCHEMIST_CAULDRON_RECIPE_TYPE, AlchemistCauldronRecipeMaker.getRecipes(vanillaRecipeFactory, itemFinder));
        registration.addRecipes(RecipeTypes.ANVIL, VanillaAnvilRecipeMaker.getAnvilRepairRecipes(vanillaRecipeFactory, itemFinder));
        SpellRegistry.REGISTRY.stream().forEach(spell -> {
            if (spell.isEnabled() && spell != SpellRegistry.none()) {
                ArrayList list = new ArrayList();
                IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).forEach(spellLevel -> {
                    ItemStack scrollStack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                    ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
                    list.add(scrollStack);
                });
                registration.addIngredientInfo(list, (IIngredientType)VanillaTypes.ITEM_STACK, new Component[]{Component.translatable((String)String.format("%s.guide", spell.getComponentId()))});
            }
        });
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.LIGHTNING_BOTTLE.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.lightning_bottle.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.BLOOD_VIAL.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.blood_vial.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.FROZEN_BONE_SHARD.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.frozen_bone.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.HOGSKIN.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.hogskin.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.DRAGONSKIN.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.dragonskin.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.RUINED_BOOK.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.ruined_book.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.CINDER_ESSENCE.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.cinder_essence.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.LIGHTNING_ROD_STAFF.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.lightning_rod.guide")});
        registration.addItemStackInfo(new ItemStack((ItemLike)ItemRegistry.CURSED_DOLL_SPELLBOOK.get()), new Component[]{Component.translatable((String)"item.irons_spellbooks.cursed_doll_spell_book.guide")});
        registration.addIngredientInfo((ItemLike)ItemRegistry.FURLED_MAP.get(), new Component[]{Component.translatable((String)"item.irons_spellbooks.furled_map.guide")});
        registration.addIngredientInfo((ItemLike)ItemRegistry.ANCIENT_FURLED_MAP.get(), new Component[]{Component.translatable((String)"item.irons_spellbooks.furled_map.guide")});
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneAnvilScreen.class, 102, 48, 22, 15, new RecipeType[]{ArcaneAnvilRecipeCategory.ARCANE_ANVIL_RECIPE_RECIPE_TYPE});
        registration.addRecipeClickArea(ScrollForgeScreen.class, 1, 1, 76, 14, new RecipeType[]{ScrollForgeRecipeCategory.SCROLL_FORGE_RECIPE_RECIPE_TYPE});
    }

    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneAnvilMenu.class, MenuRegistry.ARCANE_ANVIL_MENU.get(), ArcaneAnvilRecipeCategory.ARCANE_ANVIL_RECIPE_RECIPE_TYPE, 0, 2, 3, 36);
    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack((ItemLike)BlockRegistry.ARCANE_ANVIL_BLOCK.get()), new RecipeType[]{ArcaneAnvilRecipeCategory.ARCANE_ANVIL_RECIPE_RECIPE_TYPE});
        registration.addRecipeCatalyst(new ItemStack((ItemLike)BlockRegistry.SCROLL_FORGE_BLOCK.get()), new RecipeType[]{ScrollForgeRecipeCategory.SCROLL_FORGE_RECIPE_RECIPE_TYPE});
        registration.addRecipeCatalyst(new ItemStack((ItemLike)BlockRegistry.ALCHEMIST_CAULDRON.get()), new RecipeType[]{AlchemistCauldronRecipeCategory.ALCHEMIST_CAULDRON_RECIPE_TYPE});
    }

    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addTypedRecipeManagerPlugin(AlchemistCauldronRecipeCategory.ALCHEMIST_CAULDRON_RECIPE_TYPE, (ISimpleRecipeManagerPlugin)new AlchemistCauldronAdvancedHandler());
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getSmithingCategory().addExtension(NoAdditionSmithingTransformRecipe.class, (ISmithingCategoryExtension)new NoAdditionSmithingExtension());
    }

    static class ItemFinder {
        Collection<ItemStack> allItemStacks;
        List<ArmorItem> ironsArmorItems;
        List<TieredItem> ironsTieredItems;
        List<InkItem> inkItems;
        List<Item> imbueable;
        List<Item> upgradeable;

        ItemFinder(IIngredientManager ingredientManager) {
            this.allItemStacks = ingredientManager.getAllItemStacks();
            this.ironsArmorItems = new ArrayList<ArmorItem>();
            this.ironsTieredItems = new ArrayList<TieredItem>();
            this.inkItems = new ArrayList<InkItem>();
            this.imbueable = new ArrayList<Item>();
            this.upgradeable = new ArrayList<Item>();
            this.allItemStacks.forEach(stack -> {
                Item item = stack.getItem();
                if (BuiltInRegistries.ITEM.getKey((Object)item).getNamespace().equals("irons_spellbooks")) {
                    if (item instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem)item;
                        this.ironsArmorItems.add(armorItem);
                    } else if (item instanceof TieredItem) {
                        TieredItem tieredItem = (TieredItem)item;
                        this.ironsTieredItems.add(tieredItem);
                    }
                }
                if (item instanceof InkItem) {
                    InkItem inkItem = (InkItem)item;
                    this.inkItems.add(inkItem);
                }
                if (Utils.canImbue(stack)) {
                    this.imbueable.add(item);
                }
                if (Utils.canBeUpgraded(stack)) {
                    this.upgradeable.add(item);
                }
            });
        }
    }
}

