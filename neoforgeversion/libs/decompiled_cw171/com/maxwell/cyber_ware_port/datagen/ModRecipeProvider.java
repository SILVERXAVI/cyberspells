/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.Criterion
 *  net.minecraft.advancements.critereon.InventoryChangeTrigger$TriggerInstance
 *  net.minecraft.advancements.critereon.ItemPredicate
 *  net.minecraft.advancements.critereon.ItemPredicate$Builder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.recipes.RecipeCategory
 *  net.minecraft.data.recipes.RecipeOutput
 *  net.minecraft.data.recipes.RecipeProvider
 *  net.minecraft.data.recipes.ShapedRecipeBuilder
 *  net.minecraft.data.recipes.ShapelessRecipeBuilder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.common.conditions.IConditionBuilder
 */
package com.maxwell.cyber_ware_port.datagen;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public class ModRecipeProvider
extends RecipeProvider
implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    protected void buildRecipes(RecipeOutput pWriter) {
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.SURGERY_CHAMBER.get())).pattern("IDI").pattern("IBI").pattern("IRI").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('D'), (ItemLike)Items.IRON_TRAPDOOR).define(Character.valueOf('B'), ItemTags.BEDS).define(Character.valueOf('R'), (ItemLike)Items.REDSTONE).unlockedBy("has_iron", this.internalHas((ItemLike)Items.IRON_INGOT)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.CYBERWARE_WORKBENCH.get())).pattern("III").pattern("ICI").pattern("IPI").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('C'), (ItemLike)Items.CRAFTING_TABLE).define(Character.valueOf('P'), (ItemLike)Items.PAPER).unlockedBy("has_iron", this.internalHas((ItemLike)Items.IRON_INGOT)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.SCANNER.get())).pattern("I I").pattern("IGR").pattern("I I").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('G'), (ItemLike)Items.GLASS_PANE).define(Character.valueOf('R'), (ItemLike)Items.REDSTONE).unlockedBy("has_redstone", this.internalHas((ItemLike)Items.REDSTONE)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.BLUEPRINT_CHEST.get())).pattern("III").pattern("ICI").pattern("ILI").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('C'), Tags.Items.CHESTS).define(Character.valueOf('L'), (ItemLike)Items.LAPIS_LAZULI).unlockedBy("has_lapis", this.internalHas((ItemLike)Items.LAPIS_LAZULI)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.COMPONENT_BOX.get())).pattern(" I ").pattern("ICI").pattern(" I ").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('C'), Tags.Items.CHESTS).unlockedBy("has_iron", this.internalHas((ItemLike)Items.IRON_INGOT)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.RADIO_TOWER_COMPONENT.get()), (int)4).pattern("I I").pattern("XBX").pattern("I I").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('B'), (ItemLike)Items.IRON_BARS).define(Character.valueOf('X'), (ItemLike)Items.REDSTONE).unlockedBy("has_iron", this.internalHas((ItemLike)Items.IRON_INGOT)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.MISC, (ItemLike)((ItemLike)ModBlocks.RADIO_KIT_BLOCK.get())).pattern(" A ").pattern("IRI").pattern("III").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('R'), (ItemLike)Items.REDSTONE).define(Character.valueOf('A'), (ItemLike)Items.IRON_BARS).unlockedBy("has_redstone", this.internalHas((ItemLike)Items.REDSTONE)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.COMBAT, (ItemLike)((ItemLike)ModItems.KATANA.get())).pattern(" I ").pattern(" I ").pattern(" S ").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('S'), (ItemLike)Items.STICK).unlockedBy("has_iron", this.internalHas((ItemLike)Items.IRON_INGOT)).save(pWriter);
        ShapelessRecipeBuilder.shapeless((RecipeCategory)RecipeCategory.MISC, (ItemLike)((ItemLike)ModItems.NEUROPOZYNE.get()), (int)1).requires((ItemLike)Items.SPIDER_EYE).requires((ItemLike)Items.SUGAR).requires((ItemLike)Items.IRON_NUGGET).unlockedBy("has_spider_eye", this.internalHas((ItemLike)Items.SPIDER_EYE)).save(pWriter);
        ShapedRecipeBuilder.shaped((RecipeCategory)RecipeCategory.DECORATIONS, (ItemLike)((ItemLike)ModBlocks.RADIO_TOWER_CORE.get())).pattern("ICI").pattern("CDC").pattern("ICI").define(Character.valueOf('I'), (ItemLike)Items.IRON_INGOT).define(Character.valueOf('C'), (ItemLike)ModBlocks.RADIO_TOWER_COMPONENT.get()).define(Character.valueOf('D'), (ItemLike)Items.DIAMOND).unlockedBy("has_radio_component", this.internalHas((ItemLike)ModBlocks.RADIO_TOWER_COMPONENT.get())).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CYBER_EYE.get()).requires((Item)ModItems.COMPONENT_PLATING.get(), 2).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2).requires((Item)ModItems.COMPONENT_SSC.get(), 1).requires(Items.GLASS_PANE, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.LOW_LIGHT_VISION.get()).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).requires(Items.GLOWSTONE_DUST, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.LIQUID_REFRACTION.get()).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.PRISMARINE_SHARD, 1).requires(Items.GLASS_PANE, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.HUDJACK.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 2).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.TARGETING_OVERLAY.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 1).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.REDSTONE, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.DISTANCE_ENHANCER.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.GLASS_PANE, 2).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CORTICAL_STACK.get()).requires((Item)ModItems.COMPONENT_STORAGE.get(), 2).requires((Item)ModItems.COMPONENT_SSC.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.ENDER_JAMMER.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 1).requires(Items.ENDER_PEARL, 1).requires(Items.OBSIDIAN, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CONSCIOUSNESS_TRANSMITTER.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 2).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.ENDER_EYE, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.NEURAL_CONTEXTUALIZER.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 2).requires((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.THREAT_MATRIX.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 2).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).requires(Items.SPIDER_EYE, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CRANIAL_BROADCASTER.get()).requires((Item)ModItems.COMPONENT_SSC.get(), 1).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.NOTE_BLOCK, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CARDIOMECHANIC_PUMP.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.INTERNAL_DEFIBRILLATOR.get()).requires((Item)ModItems.COMPONENT_STORAGE.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 2).requires(Items.REDSTONE_BLOCK, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.PLATELET_DISPATCHER.get()).requires((Item)ModItems.COMPONENT_REACTOR.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).requires(Items.SLIME_BALL, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.STEM_CELL_SYNTHESIZER.get()).requires((Item)ModItems.COMPONENT_REACTOR.get(), 2).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).requires(Items.GHAST_TEAR, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CARDIOVASCULAR_COUPLER.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 1).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.COMPRESSED_OXYGEN.get()).requires((Item)ModItems.COMPONENT_PLATING.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.HYPER_OXYGENATION.get()).requires((Item)ModItems.COMPONENT_REACTOR.get(), 1).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).requires((Item)ModItems.COMPONENT_FULLERENE.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.LIVER_FILTER.get()).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires((Item)ModItems.COMPONENT_FULLERENE.get(), 1).requires(Items.CHARCOAL, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.METABOLIC_GENERATOR.get()).requires((Item)ModItems.COMPONENT_REACTOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires(Items.PISTON, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.INTERNAL_BATTERY.get()).requires((Item)ModItems.COMPONENT_STORAGE.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.ADRENALINE_PUMP.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).requires(Items.SUGAR, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.SOLARSKIN.get()).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 2).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1).requires(Items.QUARTZ, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.SUBDERMAL_SPIKES.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.SYNTHETIC_SKIN.get()).requires((Item)ModItems.COMPONENT_FULLERENE.get(), 2).requires((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1).requires(Items.LEATHER, 2).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.TARGETED_IMMUNOSUPPRESSANT.get()).requires((Item)ModItems.COMPONENT_REACTOR.get(), 1).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.WIRED_REFLEXES.get()).requires((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 2).requires((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.MYOMER_MUSCLE.get()).requires((Item)ModItems.COMPONENT_FULLERENE.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.BONELACING.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 2).requires((Item)ModItems.COMPONENT_FULLERENE.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CITRATE_ENHANCEMENT.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 1).requires((Item)ModItems.COMPONENT_REACTOR.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.MARROW_BATTERY.get()).requires((Item)ModItems.COMPONENT_STORAGE.get(), 2).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CYBER_ARM_LEFT.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 3).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CYBER_ARM_RIGHT.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 3).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CYBER_LEG_LEFT.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 3).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.CYBER_LEG_RIGHT.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 3).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.RAPID_FIRE_FLYWHEEL.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires(Items.BOW, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.IMPLANTED_SPURS.get()).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires(Items.IRON_NUGGET, 4).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.FINE_MANIPULATORS.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 4).requires((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2).requires(Items.CRAFTING_TABLE, 1).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.RAPID_FIRE_FLYWHEEL.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput(Items.BOW, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.IMPLANTED_SPURS.get()).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput(Items.IRON_NUGGET, 4, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.FINE_MANIPULATORS.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 4, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2, 1.0f).addOutput(Items.CRAFTING_TABLE, 1, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.RETRACTABLE_CLAWS.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.REINFORCED_FIST.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 1).requires((Item)ModItems.COMPONENT_PLATING.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.LINEAR_ACTUATORS.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires(Items.PISTON, 1).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.FALL_BRACERS.get()).requires((Item)ModItems.COMPONENT_TITANIUM.get(), 2).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 1).requires(Items.WHITE_WOOL, 2).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.AQUATIC_PROPULSION.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).requires(Items.IRON_INGOT, 2).save(pWriter);
        new AssemblyRecipeBuilder((Item)ModItems.DEPLOYABLE_WHEELS.get()).requires((Item)ModItems.COMPONENT_ACTUATOR.get(), 2).requires((Item)ModItems.COMPONENT_PLATING.get(), 1).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CYBER_EYE.get()).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_SSC.get(), 1, 1.0f).addOutput(Items.GLASS_PANE, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.LOW_LIGHT_VISION.get()).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).addOutput(Items.GLOWSTONE_DUST, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.LIQUID_REFRACTION.get()).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.PRISMARINE_SHARD, 1, 1.0f).addOutput(Items.GLASS_PANE, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.HUDJACK.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.TARGETING_OVERLAY.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.REDSTONE, 1, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.DISTANCE_ENHANCER.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.GLASS_PANE, 2, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CORTICAL_STACK.get()).addOutput((Item)ModItems.COMPONENT_STORAGE.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_SSC.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.1f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.ENDER_JAMMER.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 1, 1.0f).addOutput(Items.ENDER_PEARL, 1, 0.0f).addOutput(Items.OBSIDIAN, 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CONSCIOUSNESS_TRANSMITTER.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.ENDER_EYE, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.NEURAL_CONTEXTUALIZER.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.THREAT_MATRIX.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).addOutput(Items.SPIDER_EYE, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CRANIAL_BROADCASTER.get()).addOutput((Item)ModItems.COMPONENT_SSC.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.NOTE_BLOCK, 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CARDIOMECHANIC_PUMP.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.INTERNAL_DEFIBRILLATOR.get()).addOutput((Item)ModItems.COMPONENT_STORAGE.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 2, 1.0f).addOutput(Items.REDSTONE_BLOCK, 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.PLATELET_DISPATCHER.get()).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).addOutput(Items.SLIME_BALL, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.STEM_CELL_SYNTHESIZER.get()).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).addOutput(Items.GHAST_TEAR, 1, 0.2f).setBlueprintChance(0.1f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CARDIOVASCULAR_COUPLER.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.COMPRESSED_OXYGEN.get()).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.HYPER_OXYGENATION.get()).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_FULLERENE.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.LIVER_FILTER.get()).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_FULLERENE.get(), 1, 1.0f).addOutput(Items.CHARCOAL, 1, 0.5f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.METABOLIC_GENERATOR.get()).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput(Items.PISTON, 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.INTERNAL_BATTERY.get()).addOutput((Item)ModItems.COMPONENT_STORAGE.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.ADRENALINE_PUMP.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).addOutput(Items.SUGAR, 1, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.SOLARSKIN.get()).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 1, 1.0f).addOutput(Items.QUARTZ, 1, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.SUBDERMAL_SPIKES.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.SYNTHETIC_SKIN.get()).addOutput((Item)ModItems.COMPONENT_FULLERENE.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 1, 1.0f).addOutput(Items.LEATHER, 2, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.TARGETED_IMMUNOSUPPRESSANT.get()).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.WIRED_REFLEXES.get()).addOutput((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_MICROELECTRIC.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.MYOMER_MUSCLE.get()).addOutput((Item)ModItems.COMPONENT_FULLERENE.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.BONELACING.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_FULLERENE.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CITRATE_ENHANCEMENT.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_REACTOR.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.MARROW_BATTERY.get()).addOutput((Item)ModItems.COMPONENT_STORAGE.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CYBER_ARM_LEFT.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 3, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).setBlueprintChance(0.2f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CYBER_ARM_RIGHT.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 3, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).setBlueprintChance(0.2f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CYBER_LEG_LEFT.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 3, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).setBlueprintChance(0.2f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.CYBER_LEG_RIGHT.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 3, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).setBlueprintChance(0.2f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.RETRACTABLE_CLAWS.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.REINFORCED_FIST.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 1, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.LINEAR_ACTUATORS.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput(Items.PISTON, 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.FALL_BRACERS.get()).addOutput((Item)ModItems.COMPONENT_TITANIUM.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 1, 1.0f).addOutput(Items.WHITE_WOOL, 2, 0.8f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.AQUATIC_PROPULSION.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).addOutput(Items.IRON_INGOT, 2, 1.0f).setBlueprintChance(0.15f).save(pWriter);
        new EngineeringRecipeBuilder((Item)ModItems.DEPLOYABLE_WHEELS.get()).addOutput((Item)ModItems.COMPONENT_ACTUATOR.get(), 2, 1.0f).addOutput((Item)ModItems.COMPONENT_PLATING.get(), 1, 1.0f).setBlueprintChance(0.15f).save(pWriter);
    }

    private Criterion<InventoryChangeTrigger.TriggerInstance> internalHas(ItemLike pItem) {
        return ModRecipeProvider.inventoryTrigger((ItemPredicate[])new ItemPredicate[]{ItemPredicate.Builder.item().of(new ItemLike[]{pItem}).build()});
    }

    public static class AssemblyRecipeBuilder {
        private final Item result;
        private final List<AssemblyRecipe.SizedIngredient> ingredients = new ArrayList<AssemblyRecipe.SizedIngredient>();

        public AssemblyRecipeBuilder(Item result) {
            this.result = result;
        }

        public AssemblyRecipeBuilder requires(Item item, int count) {
            this.ingredients.add(new AssemblyRecipe.SizedIngredient(Ingredient.of((ItemLike[])new ItemLike[]{item}), count));
            return this;
        }

        public void save(RecipeOutput consumer) {
            consumer.accept(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)(this.getItemName(this.result) + "_assembly")), (Recipe)new AssemblyRecipe(this.ingredients, new ItemStack((ItemLike)this.result)), null);
        }

        private String getItemName(Item item) {
            return item.builtInRegistryHolder().key().location().getPath();
        }
    }

    public static class EngineeringRecipeBuilder {
        private final Item input;
        private final List<OutputEntry> outputs = new ArrayList<OutputEntry>();
        private float blueprintChance = 0.0f;

        public EngineeringRecipeBuilder(Item input) {
            this.input = input;
        }

        public EngineeringRecipeBuilder addOutput(Item item, int count, float chance) {
            this.outputs.add(new OutputEntry(item, count, chance));
            return this;
        }

        public EngineeringRecipeBuilder setBlueprintChance(float chance) {
            this.blueprintChance = chance;
            return this;
        }

        public void save(RecipeOutput consumer) {
            consumer.accept(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)(this.getItemName(this.input) + "_engineering")), (Recipe)new EngineeringRecipe(Ingredient.of((ItemLike[])new ItemLike[]{this.input}), this.outputs.stream().map(e -> new EngineeringRecipe.OutputEntry(new ItemStack((ItemLike)e.item), e.chance)).toList(), this.blueprintChance), null);
        }

        private String getItemName(Item item) {
            return item.builtInRegistryHolder().key().location().getPath();
        }

        private record OutputEntry(Item item, int count, float chance) {
        }
    }
}

