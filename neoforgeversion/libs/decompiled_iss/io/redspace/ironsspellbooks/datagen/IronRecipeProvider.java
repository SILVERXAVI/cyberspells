/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.recipes.RecipeCategory
 *  net.minecraft.data.recipes.RecipeOutput
 *  net.minecraft.data.recipes.RecipeProvider
 *  net.minecraft.data.recipes.ShapedRecipeBuilder
 *  net.minecraft.data.recipes.ShapelessRecipeBuilder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.SmithingTransformRecipe
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class IronRecipeProvider
extends RecipeProvider {
    public IronRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    protected void buildRecipes(RecipeOutput recipeOutput) {
        this.quadRingSalvageRecipe(recipeOutput, ItemRegistry.FIREWARD_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.CINDER_ESSENCE.get()}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.FROSTWARD_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.ICE_CRYSTAL.get()}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.POISONWARD_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.NATURE_RUNE.get()}));
        this.quadRingSalvageRecipe(recipeOutput, ItemRegistry.COOLDOWN_RING.get(), Ingredient.of((TagKey)Tags.Items.INGOTS_COPPER));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.CAST_TIME_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.AMETHYST_SHARD}));
        this.simpleNecklaceSalvageRecipe(recipeOutput, ItemRegistry.HEAVY_CHAIN.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.CHAIN}), Ingredient.of((ItemLike[])new ItemLike[]{Items.CHAIN}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.EMERALD_STONEPLATE_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.EXPERIENCE_BOTTLE}));
        this.simpleNecklaceSalvageRecipe(recipeOutput, ItemRegistry.CONJURERS_TALISMAN.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.SKELETON_SKULL}), Ingredient.of((ItemLike[])new ItemLike[]{Items.STRING}));
        this.simpleNecklaceSalvageRecipe(recipeOutput, ItemRegistry.CONCENTRATION_AMULET.get(), Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.MITHRIL_INGOT.get()}), Ingredient.of((ItemLike[])new ItemLike[]{Items.CHAIN}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.AFFINITY_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.BUCKET}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.EXPULSION_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.WIND_CHARGE}));
        this.simpleRingSalvageRecipe(recipeOutput, ItemRegistry.VISIBILITY_RING.get(), Ingredient.of((ItemLike[])new ItemLike[]{Items.SPYGLASS}));
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.FIRE.get(), "pyromancer");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.ICE.get(), "cryomancer");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.LIGHTNING.get(), "electromancer");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.HOLY.get(), "priest");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.BLOOD.get(), "cultist");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.ENDER.get(), "shadowwalker");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.EVOCATION.get(), "archevoker");
        IronRecipeProvider.schoolArmorSmithing(recipeOutput, SchoolRegistry.NATURE.get(), "plagued");
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.FIRE_RUNE.get(), (Item)ItemRegistry.FIRE_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.ICE_RUNE.get(), (Item)ItemRegistry.ICE_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.LIGHTNING_RUNE.get(), (Item)ItemRegistry.LIGHTNING_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.EVOCATION_RUNE.get(), (Item)ItemRegistry.EVOCATION_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.HOLY_RUNE.get(), (Item)ItemRegistry.HOLY_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.ENDER_RUNE.get(), (Item)ItemRegistry.ENDER_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.BLOOD_RUNE.get(), (Item)ItemRegistry.BLOOD_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.NATURE_RUNE.get(), (Item)ItemRegistry.NATURE_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.COOLDOWN_RUNE.get(), (Item)ItemRegistry.COOLDOWN_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.PROTECTION_RUNE.get(), (Item)ItemRegistry.PROTECTION_UPGRADE_ORB.get());
        IronRecipeProvider.upgradeOrbRecipe(recipeOutput, (Item)ItemRegistry.MANA_RUNE.get(), (Item)ItemRegistry.MANA_UPGRADE_ORB.get());
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.BLOOD_VIAL, FluidRegistry.BLOOD);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INK_COMMON, FluidRegistry.COMMON_INK);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INK_UNCOMMON, FluidRegistry.UNCOMMON_INK);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INK_RARE, FluidRegistry.RARE_INK);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INK_EPIC, FluidRegistry.EPIC_INK);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INK_LEGENDARY, FluidRegistry.LEGENDARY_INK);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.OAKSKIN_ELIXIR, FluidRegistry.OAKSKIN_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.GREATER_OAKSKIN_ELIXIR, FluidRegistry.GREATER_OAKSKIN_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.EVASION_ELIXIR, FluidRegistry.EVASION_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.GREATER_EVASION_ELIXIR, FluidRegistry.GREATER_EVASION_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.INVISIBILITY_ELIXIR, FluidRegistry.INVISIBILITY_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.GREATER_INVISIBILITY_ELIXIR, FluidRegistry.GREATER_INVISIBILITY_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.GREATER_HEALING_POTION, FluidRegistry.GREATER_HEALING_ELIXIR_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.TIMELESS_SLURRY, FluidRegistry.TIMELESS_SLURRY_FLUID);
        IronRecipeProvider.cauldronBottledInteraction(recipeOutput, ItemRegistry.ICE_VENOM_VIAL, FluidRegistry.ICE_VENOM_FLUID);
        new FillAlchemistCauldronRecipe.Builder().withInput(Items.WATER_BUCKET).withReturnItem(Items.BUCKET).withFluid(new FluidStack((Fluid)Fluids.WATER, 1000)).withSound(SoundEvents.BUCKET_EMPTY).mustFitAll(false).save(recipeOutput);
        new EmptyAlchemistCauldronRecipe.Builder().withInput(Items.BUCKET).withReturnItem(Items.WATER_BUCKET).withFluid(new FluidStack((Fluid)Fluids.WATER, 1000)).withSound(SoundEvents.BUCKET_FILL).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.COMMON_INK, 1000).withReagent((TagKey<Item>)Tags.Items.INGOTS_COPPER).withResult((Holder<Fluid>)FluidRegistry.UNCOMMON_INK, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.UNCOMMON_INK, 1000).withReagent((TagKey<Item>)Tags.Items.INGOTS_IRON).withResult((Holder<Fluid>)FluidRegistry.RARE_INK, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.RARE_INK, 1000).withReagent((TagKey<Item>)Tags.Items.INGOTS_GOLD).withResult((Holder<Fluid>)FluidRegistry.EPIC_INK, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.EPIC_INK, 1000).withReagent((TagKey<Item>)Tags.Items.GEMS_AMETHYST).withResult((Holder<Fluid>)FluidRegistry.LEGENDARY_INK, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(PotionFluid.of(500, (Holder<Potion>)Potions.STRONG_HEALING, PotionFluid.BottleType.REGULAR)).withReagent(Items.OAK_LOG).withResult((Holder<Fluid>)FluidRegistry.OAKSKIN_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.OAKSKIN_ELIXIR_FLUID, 500).withReagent(Items.AMETHYST_SHARD).withResult((Holder<Fluid>)FluidRegistry.GREATER_OAKSKIN_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(PotionFluid.of(1000, PotionRegistry.INSTANT_MANA_THREE, PotionFluid.BottleType.REGULAR)).withReagent(Items.ENDER_PEARL).withResult((Holder<Fluid>)FluidRegistry.EVASION_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.EVASION_ELIXIR_FLUID, 250).withReagent(Items.DRAGON_BREATH).withResult((Holder<Fluid>)FluidRegistry.GREATER_EVASION_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(PotionFluid.of(1000, (Holder<Potion>)Potions.LONG_INVISIBILITY, PotionFluid.BottleType.REGULAR)).withReagent((Item)ItemRegistry.SHRIVING_STONE.get()).withResult((Holder<Fluid>)FluidRegistry.INVISIBILITY_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.INVISIBILITY_ELIXIR_FLUID, 250).withReagent(Items.AMETHYST_CLUSTER).withResult((Holder<Fluid>)FluidRegistry.GREATER_INVISIBILITY_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(PotionFluid.of(1000, (Holder<Potion>)Potions.STRONG_HEALING, PotionFluid.BottleType.REGULAR)).withReagent(Items.AMETHYST_SHARD).withResult((Holder<Fluid>)FluidRegistry.GREATER_HEALING_ELIXIR_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.EVASION_ELIXIR_FLUID, 500).withReagent(Items.OBSIDIAN).withByproduct(Items.CRYING_OBSIDIAN).saveSoak(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput((Holder<Fluid>)FluidRegistry.BLOOD, 1000).withReagent((Item)ItemRegistry.HOGSKIN.get()).withByproduct((Holder<Item>)ItemRegistry.BLOODY_VELLUM).saveSoak(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(PotionFluid.of(250, (Holder<Potion>)Potions.MUNDANE, PotionFluid.BottleType.REGULAR)).withReagent(Items.ECHO_SHARD).withResult((Holder<Fluid>)FluidRegistry.TIMELESS_SLURRY_FLUID, 250).save(recipeOutput);
        BrewAlchemistCauldronRecipe.builder().withInput(new FluidStack((Fluid)Fluids.WATER, 250)).withReagent((Item)ItemRegistry.ICY_FANG.get()).withResult((Holder<Fluid>)FluidRegistry.ICE_VENOM_FLUID, 250).save(recipeOutput);
    }

    public static void schoolArmorSmithing(RecipeOutput output, SchoolType school, String armorName) {
        TagKey[] base = new TagKey[]{ModTags.BASE_WIZARD_BOOTS, ModTags.BASE_WIZARD_LEGGINGS, ModTags.BASE_WIZARD_CHESTPLATE, ModTags.BASE_WIZARD_HELMET};
        ArmorItem.Type[] slots = new ArmorItem.Type[]{ArmorItem.Type.BOOTS, ArmorItem.Type.LEGGINGS, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.HELMET};
        ResourceLocation schoolId = SchoolRegistry.REGISTRY.getKey((Object)school);
        for (int i = 0; i < 4; ++i) {
            TagKey tag = base[i];
            Ingredient baseArmor = Ingredient.of((TagKey)tag);
            ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath((String)schoolId.getNamespace(), (String)String.format("%s_%s", armorName, slots[i].getName()));
            Item rune = (Item)BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath((String)schoolId.getNamespace(), (String)String.format("%s_rune", schoolId.getPath())));
            ItemStack result = ((Item)BuiltInRegistries.ITEM.get(itemId)).getDefaultInstance();
            Item essence = (Item)ItemRegistry.ARCANE_ESSENCE.get();
            output.accept(itemId.withSuffix("_smithing"), (Recipe)new SmithingTransformRecipe(Ingredient.of((ItemLike[])new ItemLike[]{rune}), baseArmor, Ingredient.of((ItemLike[])new ItemLike[]{essence}), result), null);
            ShapelessRecipeBuilder.shapeless((RecipeCategory)RecipeCategory.MISC, (ItemStack)result).requires(baseArmor).requires((ItemLike)rune).requires((ItemLike)essence).unlockedBy("unlocked", IronRecipeProvider.has((TagKey)tag)).save(output, itemId.withSuffix("_crafting"));
        }
    }

    public static void upgradeOrbRecipe(RecipeOutput output, Item rune, Item result) {
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.MISC, (ItemLike)result).define(Character.valueOf('R'), (ItemLike)rune).define(Character.valueOf('O'), (ItemLike)ItemRegistry.UPGRADE_ORB.get()).pattern("RRR").pattern("ROR").pattern("RRR").unlockedBy("orb", IronRecipeProvider.has((ItemLike)((ItemLike)ItemRegistry.UPGRADE_ORB.get()))).save(output);
    }

    public static void cauldronBottledInteraction(RecipeOutput output, Holder<Item> item, Holder<Fluid> fluid) {
        IronRecipeProvider.cauldronTwoWayInteraction(output, item, (Holder<Item>)Holder.direct((Object)Items.GLASS_BOTTLE), fluid, 250);
    }

    public static void cauldronTwoWayInteraction(RecipeOutput output, Holder<Item> item, Holder<Item> vessel, Holder<Fluid> fluid, int amount) {
        new FillAlchemistCauldronRecipe.Builder().withFluid(fluid, amount).withInput((Item)item.value()).withReturnItem((Item)vessel.value()).save(output);
        new EmptyAlchemistCauldronRecipe.Builder().withInput((Item)vessel.value()).withReturnItem((Item)item.value()).withFluid(fluid, amount).save(output);
    }

    protected void simpleRingSalvageRecipe(RecipeOutput output, Item result, Ingredient modifier) {
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.MISC, (ItemLike)result).define(Character.valueOf('M'), modifier).define(Character.valueOf('X'), (ItemLike)ItemRegistry.MITHRIL_SCRAP.get()).pattern("M ").pattern(" X").unlockedBy("mithril_scrap", IronRecipeProvider.has((ItemLike)((ItemLike)ItemRegistry.MITHRIL_SCRAP.get()))).save(output);
    }

    protected void simpleNecklaceSalvageRecipe(RecipeOutput output, Item result, Ingredient modifier, Ingredient strap) {
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.MISC, (ItemLike)result).define(Character.valueOf('M'), modifier).define(Character.valueOf('X'), (ItemLike)ItemRegistry.MITHRIL_SCRAP.get()).define(Character.valueOf('S'), strap).pattern(" S ").pattern("SXS").pattern(" M ").unlockedBy("mithril_scrap", IronRecipeProvider.has((ItemLike)((ItemLike)ItemRegistry.MITHRIL_SCRAP.get()))).save(output);
    }

    protected void quadRingSalvageRecipe(RecipeOutput output, Item result, Ingredient modifier) {
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.MISC, (ItemLike)result).define(Character.valueOf('M'), modifier).define(Character.valueOf('X'), (ItemLike)ItemRegistry.MITHRIL_SCRAP.get()).pattern(" M ").pattern("MXM").pattern(" M ").unlockedBy("mithril_scrap", IronRecipeProvider.has((ItemLike)((ItemLike)ItemRegistry.MITHRIL_SCRAP.get()))).save(output);
    }
}

