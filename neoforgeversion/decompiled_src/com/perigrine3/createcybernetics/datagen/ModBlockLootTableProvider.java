/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.critereon.StatePropertiesPredicate$Builder
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.HolderLookup$RegistryLookup
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.loot.BlockLootSubProvider
 *  net.minecraft.world.flag.FeatureFlags
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SweetBerryBushBlock
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.storage.loot.LootPool
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.LootTable$Builder
 *  net.minecraft.world.level.storage.loot.entries.LootItem
 *  net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer$Builder
 *  net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
 *  net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunction$Builder
 *  net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
 *  net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition$Builder
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProvider
 *  net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.Set;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModBlockLootTableProvider
extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    protected void generate() {
        this.dropSelf((Block)ModBlocks.SURGERY_CHAMBER_BOTTOM.get());
        this.dropSelf((Block)ModBlocks.SURGERY_CHAMBER_TOP.get());
        this.dropSelf((Block)ModBlocks.ROBOSURGEON.get());
        this.dropSelf((Block)ModBlocks.ENGINEERING_TABLE.get());
        this.dropSelf((Block)ModBlocks.GRAFTING_TABLE.get());
        this.dropSelf((Block)ModBlocks.CHARGING_BLOCK.get());
        this.dropSelf((Block)ModBlocks.HOLOPROJECTOR.get());
        this.dropSelf((Block)ModBlocks.TITANIUM_BLOCK.get());
        this.dropSelf((Block)ModBlocks.SMOOTH_TITANIUM.get());
        this.dropSelf((Block)ModBlocks.SMOOTH_TITANIUM_STAIRS.get());
        this.add((Block)ModBlocks.SMOOTH_TITANIUM_SLAB.get(), block -> this.createSlabItemTable((Block)ModBlocks.SMOOTH_TITANIUM_SLAB.get()));
        this.dropSelf((Block)ModBlocks.TITANIUM_GRATE.get());
        this.dropSelf((Block)ModBlocks.TITANIUM_CLAD_COPPER.get());
        this.dropSelf((Block)ModBlocks.TITANIUM_CLAD_COPPER_STAIRS.get());
        this.add((Block)ModBlocks.TITANIUM_CLAD_COPPER_SLAB.get(), block -> this.createSlabItemTable((Block)ModBlocks.TITANIUM_CLAD_COPPER_SLAB.get()));
        this.dropSelf((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get());
        this.dropSelf((Block)ModBlocks.ETCHED_TITANIUM_COPPER_STAIRS.get());
        this.add((Block)ModBlocks.ETCHED_TITANIUM_COPPER_SLAB.get(), block -> this.createSlabItemTable((Block)ModBlocks.ETCHED_TITANIUM_COPPER_SLAB.get()));
        this.dropSelf((Block)ModBlocks.RAW_TITANIUM_BLOCK.get());
        this.add((Block)ModBlocks.TITANIUMORE_BLOCK.get(), block -> this.createOreDrop((Block)ModBlocks.TITANIUMORE_BLOCK.get(), (Item)ModItems.RAWTITANIUM.get()));
        this.add((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get(), block -> this.createMultipleOreDrops((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get(), (Item)ModItems.RAWTITANIUM.get(), 2.0f, 5.0f));
        HolderLookup.RegistryLookup registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add((Block)ModBlocks.DATURA_BUSH.get(), block -> (LootTable.Builder)this.applyExplosionDecay((ItemLike)block, (FunctionUserBuilder)LootTable.lootTable().withPool(LootPool.lootPool().when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties((Block)((Block)ModBlocks.DATURA_BUSH.get())).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SweetBerryBushBlock.AGE, 3))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)((ItemLike)ModItems.DATURA_FLOWER.get()))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between((float)2.0f, (float)3.0f))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)registrylookup.getOrThrow(Enchantments.FORTUNE)))).withPool(LootPool.lootPool().when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties((Block)((Block)ModBlocks.DATURA_BUSH.get())).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SweetBerryBushBlock.AGE, 2))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)((ItemLike)ModItems.DATURA_FLOWER.get()))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between((float)1.0f, (float)2.0f))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)registrylookup.getOrThrow(Enchantments.FORTUNE))))));
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock, (LootPoolEntryContainer.Builder)this.applyExplosionDecay((ItemLike)pBlock, (FunctionUserBuilder)LootItem.lootTableItem((ItemLike)item).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between((float)minDrops, (float)maxDrops))).apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)registryLookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}

