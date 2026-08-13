/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTabs
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.MOD)
public class CreativeTabRegistry {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (String)"irons_spellbooks");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT_TAB = TABS.register("spellbook_equipment", () -> CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.irons_spellbooks.spell_equipment_tab")).icon(() -> new ItemStack((ItemLike)ItemRegistry.IRON_SPELL_BOOK.get())).displayItems((enabledFeatures, entries) -> {
        entries.accept((ItemLike)ItemRegistry.NETHERITE_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.DIAMOND_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.GOLD_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.IRON_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.COPPER_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.EVOKER_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.NECRONOMICON.get());
        entries.accept((ItemLike)ItemRegistry.ROTTEN_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.BLAZE_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.DRAGONSKIN_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.VILLAGER_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.DRUIDIC_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.CURSED_DOLL_SPELLBOOK.get());
        entries.accept((ItemLike)ItemRegistry.ICE_SPELL_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.BLOOD_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.GRAYBEARD_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.ICE_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.ARTIFICER_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.LIGHTNING_ROD_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.PYRIUM_STAFF.get());
        entries.accept((ItemLike)ItemRegistry.MAGEHUNTER.get());
        entries.accept((ItemLike)ItemRegistry.SPELLBREAKER.get());
        entries.accept((ItemLike)ItemRegistry.AMETHYST_RAPIER.get());
        entries.accept((ItemLike)ItemRegistry.ICE_GREATSWORD.get());
        entries.accept((ItemLike)ItemRegistry.TWILIGHT_GALE.get());
        entries.accept((ItemLike)ItemRegistry.KEEPER_FLAMBERGE.get());
        entries.accept((ItemLike)ItemRegistry.LEGIONNAIRE_FLAMBERGE.get());
        entries.accept((ItemLike)ItemRegistry.DECREPIT_SCYTHE.get());
        entries.accept((ItemLike)ItemRegistry.HELLRAZOR.get());
        entries.accept((ItemLike)ItemRegistry.AUTOLOADER_CROSSBOW.get());
        entries.accept((ItemLike)ItemRegistry.WAYWARD_COMPASS.get());
        entries.accept((ItemLike)ItemRegistry.WANDERING_MAGICIAN_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.WANDERING_MAGICIAN_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.WANDERING_MAGICIAN_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.WANDERING_MAGICIAN_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.PUMPKIN_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.PUMPKIN_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.PUMPKIN_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.PUMPKIN_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.PYROMANCER_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.PYROMANCER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.PYROMANCER_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.PYROMANCER_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.ELECTROMANCER_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.ELECTROMANCER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.ELECTROMANCER_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.ELECTROMANCER_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.ARCHEVOKER_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.ARCHEVOKER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.ARCHEVOKER_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.ARCHEVOKER_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.CULTIST_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.CULTIST_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.CULTIST_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.CULTIST_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.CRYOMANCER_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.CRYOMANCER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.CRYOMANCER_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.CRYOMANCER_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.SHADOWWALKER_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.SHADOWWALKER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.SHADOWWALKER_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.SHADOWWALKER_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.PRIEST_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.PRIEST_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.PRIEST_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.PRIEST_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.PLAGUED_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.PLAGUED_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.PLAGUED_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.PLAGUED_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.NETHERITE_MAGE_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.NETHERITE_MAGE_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.NETHERITE_MAGE_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.NETHERITE_MAGE_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.WIZARD_HELMET.get());
        entries.accept((ItemLike)ItemRegistry.WIZARD_HAT.get());
        entries.accept((ItemLike)ItemRegistry.WIZARD_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.WIZARD_LEGGINGS.get());
        entries.accept((ItemLike)ItemRegistry.WIZARD_BOOTS.get());
        entries.accept((ItemLike)ItemRegistry.INFERNAL_SORCERER_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.PALADIN_CHESTPLATE.get());
        entries.accept((ItemLike)ItemRegistry.BOOTS_OF_SPEED.get());
        entries.accept((ItemLike)ItemRegistry.TARNISHED_CROWN.get());
        entries.accept((ItemLike)ItemRegistry.HITHER_THITHER_WAND.get());
        entries.accept((ItemLike)ItemRegistry.MANA_RING.get());
        entries.accept((ItemLike)ItemRegistry.SILVER_RING.get());
        entries.accept((ItemLike)ItemRegistry.COOLDOWN_RING.get());
        entries.accept((ItemLike)ItemRegistry.CAST_TIME_RING.get());
        entries.accept((ItemLike)ItemRegistry.HEAVY_CHAIN.get());
        entries.accept((ItemLike)ItemRegistry.EMERALD_STONEPLATE_RING.get());
        entries.accept((ItemLike)ItemRegistry.FIREWARD_RING.get());
        entries.accept((ItemLike)ItemRegistry.FROSTWARD_RING.get());
        entries.accept((ItemLike)ItemRegistry.POISONWARD_RING.get());
        entries.accept((ItemLike)ItemRegistry.CONJURERS_TALISMAN.get());
        entries.accept((ItemLike)ItemRegistry.GREATER_CONJURERS_TALISMAN.get());
        entries.accept((ItemLike)ItemRegistry.AFFINITY_RING.get());
        entries.accept((ItemLike)ItemRegistry.CONCENTRATION_AMULET.get());
        entries.accept((ItemLike)ItemRegistry.AMETHYST_RESONANCE_NECKLACE.get());
        entries.accept((ItemLike)ItemRegistry.EXPULSION_RING.get());
        entries.accept((ItemLike)ItemRegistry.VISIBILITY_RING.get());
        entries.accept((ItemLike)ItemRegistry.TELEPORTATION_AMULET.get());
        entries.accept((ItemLike)ItemRegistry.SIGNET_OF_THE_BETRAYER.get());
        entries.accept((ItemLike)ItemRegistry.INVISIBILITY_RING.get());
    }).withTabsBefore(new ResourceKey[]{CreativeModeTabs.SPAWN_EGGS}).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATERIALS_TAB = TABS.register("spellbook_materials", () -> CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.irons_spellbooks.spell_materials_tab")).icon(() -> new ItemStack((ItemLike)ItemRegistry.DIVINE_PEARL.get())).displayItems((enabledFeatures, entries) -> {
        entries.accept((ItemLike)ItemRegistry.INK_COMMON.get());
        entries.accept((ItemLike)ItemRegistry.INK_UNCOMMON.get());
        entries.accept((ItemLike)ItemRegistry.INK_RARE.get());
        entries.accept((ItemLike)ItemRegistry.INK_EPIC.get());
        entries.accept((ItemLike)ItemRegistry.INK_LEGENDARY.get());
        entries.accept((ItemLike)ItemRegistry.LESSER_SPELL_SLOT_UPGRADE.get());
        entries.accept((ItemLike)ItemRegistry.UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.FIRE_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.ICE_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.LIGHTNING_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.HOLY_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.ENDER_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.BLOOD_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.EVOCATION_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.NATURE_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.MANA_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.COOLDOWN_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.PROTECTION_UPGRADE_ORB.get());
        entries.accept((ItemLike)ItemRegistry.LIGHTNING_BOTTLE.get());
        entries.accept((ItemLike)ItemRegistry.FROZEN_BONE_SHARD.get());
        entries.accept((ItemLike)ItemRegistry.BLOOD_VIAL.get());
        entries.accept((ItemLike)ItemRegistry.ICE_VENOM_VIAL.get());
        entries.accept((ItemLike)ItemRegistry.DIVINE_PEARL.get());
        entries.accept((ItemLike)ItemRegistry.MAGIC_CLOTH.get());
        entries.accept((ItemLike)ItemRegistry.HOGSKIN.get());
        entries.accept((ItemLike)ItemRegistry.BLOODY_VELLUM.get());
        entries.accept((ItemLike)ItemRegistry.DRAGONSKIN.get());
        entries.accept((ItemLike)ItemRegistry.ARCANE_ESSENCE.get());
        entries.accept((ItemLike)ItemRegistry.RUINED_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.CHAINED_BOOK.get());
        entries.accept((ItemLike)ItemRegistry.THE_CHRONICLE.get());
        entries.accept((ItemLike)ItemRegistry.CINDER_ESSENCE.get());
        entries.accept((ItemLike)ItemRegistry.TIMELESS_SLURRY.get());
        entries.accept((ItemLike)ItemRegistry.MITHRIL_INGOT.get());
        entries.accept((ItemLike)ItemRegistry.MITHRIL_SCRAP.get());
        entries.accept((ItemLike)ItemRegistry.RAW_MITHRIL.get());
        entries.accept((ItemLike)ItemRegistry.WEAPON_PARTS.get());
        entries.accept((ItemLike)ItemRegistry.MITHRIL_WEAVE.get());
        entries.accept((ItemLike)ItemRegistry.DIVINE_SOULSHARD.get());
        entries.accept((ItemLike)ItemRegistry.PYRIUM_INGOT.get());
        entries.accept((ItemLike)ItemRegistry.ARCANE_INGOT.get());
        entries.accept((ItemLike)ItemRegistry.SHRIVING_STONE.get());
        entries.accept((ItemLike)ItemRegistry.ELDRITCH_PAGE.get());
        entries.accept((ItemLike)ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.get());
        entries.accept((ItemLike)ItemRegistry.ICY_FANG.get());
        entries.accept((ItemLike)ItemRegistry.ICE_CRYSTAL.get());
        entries.accept((ItemLike)ItemRegistry.FROSTED_HELVE.get());
        entries.accept((ItemLike)ItemRegistry.ENERGIZED_CORE.get());
        entries.accept(FurledMapItem.of(IronsSpellbooks.id("evoker_fort"), (ResourceKey<Level>)ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"overworld")), Component.translatable((String)"item.irons_spellbooks.evoker_fort_battle_plans"), false));
        entries.accept(FurledMapItem.of(IronsSpellbooks.id("mangrove_hut"), (ResourceKey<Level>)ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"overworld")), Component.translatable((String)"item.irons_spellbooks.alchemical_trade_route"), false));
        entries.accept((ItemLike)ItemRegistry.ICE_SPIDER_FURLED_MAP.get());
        entries.accept((ItemLike)ItemRegistry.CITADEL_FURLED_MAP.get());
        entries.accept((ItemLike)ItemRegistry.DECREPIT_KEY.get());
        entries.accept((ItemLike)ItemRegistry.CINDEROUS_SOULCALLER.get());
        entries.accept((ItemLike)ItemRegistry.BLANK_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.FIRE_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.ICE_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.LIGHTNING_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.ENDER_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.HOLY_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.BLOOD_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.EVOCATION_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.MANA_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.COOLDOWN_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.PROTECTION_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.NATURE_RUNE.get());
        entries.accept((ItemLike)ItemRegistry.OAKSKIN_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.GREATER_OAKSKIN_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.GREATER_HEALING_POTION.get());
        entries.accept((ItemLike)ItemRegistry.INVISIBILITY_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.EVASION_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.GREATER_EVASION_ELIXIR.get());
        entries.accept((ItemLike)ItemRegistry.FIRE_ALE.get());
        entries.accept((ItemLike)ItemRegistry.NETHERWARD_TINCTURE.get());
        entries.accept((ItemLike)ItemRegistry.MUSIC_DISC_DEAD_KING_LULLABY.get());
        entries.accept((ItemLike)ItemRegistry.MUSIC_DISC_FLAME_STILL_BURNS.get());
        entries.accept((ItemLike)ItemRegistry.FLAME_STILL_BURNS_FRAGMENT.get());
        entries.accept((ItemLike)ItemRegistry.MUSIC_DISC_WHISPERS_OF_ICE.get());
        entries.accept((ItemLike)ItemRegistry.KEEPER_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.DEAD_KING_CORPSE_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.ARCHEVOKER_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.NECROMANCER_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.CRYOMANCER_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.PYROMANCER_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.PRIEST_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.APOTHECARIST_SPAWN_EGG.get());
        entries.accept((ItemLike)ItemRegistry.ICE_SPIDER_SPAWN_EGG.get());
    }).withTabsBefore(new ResourceKey[]{EQUIPMENT_TAB.getKey()}).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS_TAB = TABS.register("spellbook_blocks", () -> CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.irons_spellbooks.blocks_tab")).icon(() -> new ItemStack((ItemLike)ItemRegistry.INSCRIPTION_TABLE_BLOCK_ITEM.get())).displayItems((enabledFeatures, entries) -> ItemRegistry.getIronsItems().forEach(holder -> {
        if (holder.get() instanceof BlockItem) {
            entries.accept((ItemLike)holder.get());
        }
    })).withTabsBefore(new ResourceKey[]{MATERIALS_TAB.getKey()}).build());
    public static final Supplier<CreativeModeTab> SCROLLS_TAB = TABS.register("spellbook_scrolls", () -> CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.irons_spellbooks.spellbook_scrolls_tab")).icon(() -> new ItemStack((ItemLike)ItemRegistry.SCROLL.get())).withTabsBefore(new ResourceKey[]{MATERIALS_TAB.getKey()}).build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    @SubscribeEvent
    public static void fillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept((ItemLike)ItemRegistry.INSCRIPTION_TABLE_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.SCROLL_FORGE_BLOCK.get());
            event.accept((ItemLike)ItemRegistry.ACANE_ANVIL_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.PEDESTAL_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.ARMOR_PILE_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.ALCHEMIST_CAULDRON_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.FIREFLY_JAR_ITEM.get());
            event.accept((ItemLike)ItemRegistry.PORTAL_FRAME_ITEM.get());
        }
        if (event.getTab() == SCROLLS_TAB.get()) {
            SpellRegistry.getEnabledSpells().stream().filter(spellType -> spellType != SpellRegistry.none()).forEach(spell -> {
                for (int i = spell.getMinLevel(); i <= spell.getMaxLevel(); ++i) {
                    ItemStack itemstack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                    ISpellContainer spellList = ISpellContainer.createScrollContainer(spell, i, itemstack);
                    event.accept(itemstack);
                }
            });
        }
        if (event.getTab() == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.NATURAL_BLOCKS)) {
            event.accept((ItemLike)ItemRegistry.MITHRIL_ORE_BLOCK_ITEM.get());
            event.accept((ItemLike)ItemRegistry.MITHRIL_ORE_DEEPSLATE_BLOCK_ITEM.get());
        }
    }
}

