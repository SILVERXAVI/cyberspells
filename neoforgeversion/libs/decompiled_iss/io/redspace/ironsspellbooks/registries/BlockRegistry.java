/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.RotatedPillarBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.entity.BlockEntityType$Builder
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.material.PushReaction
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.block.ArmorPileBlock;
import io.redspace.ironsspellbooks.block.BloodCauldronBlock;
import io.redspace.ironsspellbooks.block.BookStackBlock;
import io.redspace.ironsspellbooks.block.BrazierBlock;
import io.redspace.ironsspellbooks.block.FireflyJar;
import io.redspace.ironsspellbooks.block.VoidstoneBlock;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronBlock;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import io.redspace.ironsspellbooks.block.arcane_anvil.ArcaneAnvilBlock;
import io.redspace.ironsspellbooks.block.chiseled_bookshelf.WisewoodChiseledBookShelfBlockEntity;
import io.redspace.ironsspellbooks.block.chiseled_bookshelf.WisewoodChiseledBookshelfBlock;
import io.redspace.ironsspellbooks.block.ice_spider_egg.IceSpiderEggBlock;
import io.redspace.ironsspellbooks.block.inscription_table.InscriptionTableBlock;
import io.redspace.ironsspellbooks.block.pedestal.PedestalBlock;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import io.redspace.ironsspellbooks.block.portal_frame.PocketDimensionPortalFrameBlock;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlock;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeBlock;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import java.util.Collection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create((ResourceKey)Registries.BLOCK, (String)"irons_spellbooks");
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create((ResourceKey)Registries.BLOCK_ENTITY_TYPE, (String)"irons_spellbooks");
    public static final DeferredHolder<Block, Block> INSCRIPTION_TABLE_BLOCK = BLOCKS.register("inscription_table", InscriptionTableBlock::new);
    public static final DeferredHolder<Block, Block> SCROLL_FORGE_BLOCK = BLOCKS.register("scroll_forge", ScrollForgeBlock::new);
    public static final DeferredHolder<Block, Block> PEDESTAL_BLOCK = BLOCKS.register("pedestal", PedestalBlock::new);
    public static final DeferredHolder<Block, Block> BLOOD_CAULDRON_BLOCK = BLOCKS.register("blood_cauldron", BloodCauldronBlock::new);
    public static final DeferredHolder<Block, Block> ARCANE_ANVIL_BLOCK = BLOCKS.register("arcane_anvil", ArcaneAnvilBlock::new);
    public static final DeferredHolder<Block, Block> ARMOR_PILE_BLOCK = BLOCKS.register("armor_pile", ArmorPileBlock::new);
    public static final DeferredHolder<Block, Block> ALCHEMIST_CAULDRON = BLOCKS.register("alchemist_cauldron", AlchemistCauldronBlock::new);
    public static final DeferredHolder<Block, Block> FIREFLY_JAR = BLOCKS.register("firefly_jar", FireflyJar::new);
    public static final DeferredHolder<Block, Block> PORTAL_FRAME = BLOCKS.register("portal_frame", () -> new PortalFrameBlock());
    public static final DeferredHolder<Block, Block> BRAZIER_FIRE = BLOCKS.register("brazier", () -> new BrazierBlock(false));
    public static final DeferredHolder<Block, Block> BRAZIER_SOUL = BLOCKS.register("brazier_soul", () -> new BrazierBlock(true));
    public static final DeferredHolder<Block, Block> CINDEROUS_KEYSTONE = BLOCKS.register("cinderous_soul_rune", () -> new Block(BlockBehaviour.Properties.of().lightLevel(state -> 15).pushReaction(PushReaction.BLOCK).sound(SoundType.DEEPSLATE).noLootTable().strength(40.0f, 1200.0f)));
    public static final DeferredHolder<Block, Block> MITHRIL_ORE = BLOCKS.register("mithril_ore", () -> new Block(BlockBehaviour.Properties.of().lightLevel(state -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops().strength(20.0f, 1200.0f).sound(SoundType.ANCIENT_DEBRIS)));
    public static final DeferredHolder<Block, Block> MITHRIL_ORE_DEEPSLATE = BLOCKS.register("deepslate_mithril_ore", () -> new Block(BlockBehaviour.Properties.of().lightLevel(state -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops().strength(20.0f, 1200.0f).sound(SoundType.ANCIENT_DEBRIS)));
    public static final DeferredHolder<Block, Block> ICE_SPIDER_EGG = BLOCKS.register("ice_spider_egg", () -> new IceSpiderEggBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY).strength(3.0f, 1.0f).noOcclusion()));
    public static final DeferredHolder<Block, Block> BOOK_STACK = BLOCKS.register("book_stack", BookStackBlock::new);
    public static final DeferredHolder<Block, Block> WISEWOOD_PLANKS = BLOCKS.register("wisewood_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> WISEWOOD_BOOKSHELF = BLOCKS.register("wisewood_bookshelf", () -> new Block(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.BOOKSHELF)));
    public static final DeferredHolder<Block, Block> GRIMY_TILES = BLOCKS.register("grimy_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.DEEPSLATE)));
    public static final DeferredHolder<Block, Block> WISEWOOD_CHISELLED_BOOKSHELF = BLOCKS.register("wisewood_chiseled_bookshelf", () -> new WisewoodChiseledBookshelfBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.CHISELED_BOOKSHELF)));
    public static final DeferredHolder<Block, Block> NETHER_BRICK_PILLAR = BLOCKS.register("nether_brick_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.NETHER_BRICKS)));
    public static final DeferredHolder<Block, Block> VOIDSTONE = BLOCKS.register("voidstone", VoidstoneBlock::new);
    public static final DeferredHolder<Block, Block> POCKET_PORTAL_FRAME = BLOCKS.register("pocket_dimension_portal_frame", PocketDimensionPortalFrameBlock::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ScrollForgeTile>> SCROLL_FORGE_TILE = BLOCK_ENTITIES.register("scroll_forge", () -> BlockEntityType.Builder.of(ScrollForgeTile::new, (Block[])new Block[]{(Block)SCROLL_FORGE_BLOCK.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalTile>> PEDESTAL_TILE = BLOCK_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalTile::new, (Block[])new Block[]{(Block)PEDESTAL_BLOCK.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemistCauldronTile>> ALCHEMIST_CAULDRON_TILE = BLOCK_ENTITIES.register("alchemist_cauldron", () -> BlockEntityType.Builder.of(AlchemistCauldronTile::new, (Block[])new Block[]{(Block)ALCHEMIST_CAULDRON.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PortalFrameBlockEntity>> PORTAL_FRAME_BLOCK_ENTITY = BLOCK_ENTITIES.register("portal_frame", () -> BlockEntityType.Builder.of(PortalFrameBlockEntity::new, (Block[])new Block[]{(Block)PORTAL_FRAME.get(), (Block)POCKET_PORTAL_FRAME.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WisewoodChiseledBookShelfBlockEntity>> WISEWOOD_CHISELED_BOOKSHELF_ENTITY = BLOCK_ENTITIES.register("wisewood_chiseled_bookshelf", () -> BlockEntityType.Builder.of(WisewoodChiseledBookShelfBlockEntity::new, (Block[])new Block[]{(Block)WISEWOOD_CHISELLED_BOOKSHELF.get()}).build(null));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }

    public static Collection<DeferredHolder<Block, ? extends Block>> blocks() {
        return BLOCKS.getEntries();
    }
}

