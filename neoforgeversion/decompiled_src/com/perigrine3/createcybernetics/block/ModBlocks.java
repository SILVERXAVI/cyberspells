/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredBlock
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$Blocks
 */
package com.perigrine3.createcybernetics.block;

import com.perigrine3.createcybernetics.block.DaturaBushBlock;
import com.perigrine3.createcybernetics.block.EngineeringTableBlock;
import com.perigrine3.createcybernetics.block.GraftingTableBlock;
import com.perigrine3.createcybernetics.block.HoloprojectorBlock;
import com.perigrine3.createcybernetics.block.RobosurgeonBlock;
import com.perigrine3.createcybernetics.block.SurgeryChamberBlockBottom;
import com.perigrine3.createcybernetics.block.SurgeryChamberBlockTop;
import com.perigrine3.createcybernetics.block.TitaniumCladCopperBlock;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks((String)"createcybernetics");
    public static final DeferredBlock<Block> TITANIUMORE_BLOCK = ModBlocks.registerBlock("titaniumore_block", () -> new Block(BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)), true);
    public static final DeferredBlock<Block> DEEPSLATE_TITANIUMORE_BLOCK = ModBlocks.registerBlock("deepslate_titaniumore_block", () -> new Block(BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)), true);
    public static final DeferredBlock<Block> TITANIUM_BLOCK = ModBlocks.registerBlock("titanium_block", () -> new Block(BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> RAW_TITANIUM_BLOCK = ModBlocks.registerBlock("raw_titanium_block", () -> new Block(BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)), true);
    public static final DeferredBlock<Block> SMOOTH_TITANIUM = ModBlocks.registerBlock("smooth_titanium", () -> new Block(BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> TITANIUM_GRATE = ModBlocks.registerBlock("titanium_grate", () -> new Block(BlockBehaviour.Properties.of().noOcclusion().strength(6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> TITANIUM_CLAD_COPPER = ModBlocks.registerBlock("titanium_clad_copper", () -> new TitaniumCladCopperBlock(BlockBehaviour.Properties.of().strength(5.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> ETCHED_TITANIUM_COPPER = ModBlocks.registerBlock("etched_titanium_copper", () -> new Block(BlockBehaviour.Properties.of().strength(5.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), true);
    public static final DeferredBlock<StairBlock> SMOOTH_TITANIUM_STAIRS = ModBlocks.registerBlock("smooth_titanium_stairs", () -> new StairBlock(((Block)SMOOTH_TITANIUM.get()).defaultBlockState(), BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<SlabBlock> SMOOTH_TITANIUM_SLAB = ModBlocks.registerBlock("smooth_titanium_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<StairBlock> TITANIUM_CLAD_COPPER_STAIRS = ModBlocks.registerBlock("titanium_clad_copper_stairs", () -> new StairBlock(((Block)SMOOTH_TITANIUM.get()).defaultBlockState(), BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<SlabBlock> TITANIUM_CLAD_COPPER_SLAB = ModBlocks.registerBlock("titanium_clad_copper_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<StairBlock> ETCHED_TITANIUM_COPPER_STAIRS = ModBlocks.registerBlock("etched_titanium_copper_stairs", () -> new StairBlock(((Block)SMOOTH_TITANIUM.get()).defaultBlockState(), BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<SlabBlock> ETCHED_TITANIUM_COPPER_SLAB = ModBlocks.registerBlock("etched_titanium_copper_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().strength(6.0f).requiresCorrectToolForDrops()), true);
    public static final DeferredBlock<Block> SURGERY_CHAMBER_BOTTOM = ModBlocks.registerBlock("surgery_chamber", () -> new SurgeryChamberBlockBottom(BlockBehaviour.Properties.of().strength(6.0f, 8.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> SURGERY_CHAMBER_TOP = ModBlocks.registerBlock("surgery_chamber_top", () -> new SurgeryChamberBlockTop(BlockBehaviour.Properties.of().strength(6.0f, 8.0f).noOcclusion().sound(SoundType.METAL)), false);
    public static final DeferredBlock<Block> ROBOSURGEON = ModBlocks.registerBlock("robosurgeon", () -> new RobosurgeonBlock(BlockBehaviour.Properties.of().strength(6.0f, 8.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> CHARGING_BLOCK = ModBlocks.registerBlock("charging_block", () -> new Block(BlockBehaviour.Properties.of().strength(6.0f, 8.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> ENGINEERING_TABLE = ModBlocks.registerBlock("engineering_table", () -> new EngineeringTableBlock(BlockBehaviour.Properties.of().strength(6.0f, 6.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> GRAFTING_TABLE = ModBlocks.registerBlock("grafting_table", () -> new GraftingTableBlock(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> HOLOPROJECTOR = ModBlocks.registerBlock("holoprojector", () -> new HoloprojectorBlock(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).noOcclusion().sound(SoundType.METAL)), true);
    public static final DeferredBlock<Block> DATURA_BUSH = BLOCKS.register("datura_bush", () -> new DaturaBushBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.SWEET_BERRY_BUSH)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, boolean registerItem) {
        DeferredBlock toReturn = BLOCKS.register(name, block);
        ModBlocks.registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem((Block)block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

