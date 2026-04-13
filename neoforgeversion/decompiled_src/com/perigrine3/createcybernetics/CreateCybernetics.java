/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.client.renderer.entity.EntityRenderers
 *  net.minecraft.client.renderer.entity.ThrownItemRenderer
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTab$TabVisibility
 *  net.minecraft.world.item.CreativeModeTabs
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.StairBlock
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModContainer
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.config.IConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 *  net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
 *  net.neoforged.neoforge.event.server.ServerStartingEvent
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$Blocks
 *  net.neoforged.neoforge.registries.DeferredRegister$Items
 *  org.slf4j.Logger
 */
package com.perigrine3.createcybernetics;

import com.mojang.logging.LogUtils;
import com.perigrine3.createcybernetics.Config;
import com.perigrine3.createcybernetics.advancement.ModCriteria;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.entity.ModBlockEntities;
import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.compat.CompatBootstrap;
import com.perigrine3.createcybernetics.compat.ironsspells.IronsSpellbooksCyberwareAttributes;
import com.perigrine3.createcybernetics.component.ModDataComponents;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.effect.PneumaticCalvesEffect;
import com.perigrine3.createcybernetics.enchantment.ModEnchantmentEffects;
import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.client.CyberskeletonRenderer;
import com.perigrine3.createcybernetics.entity.client.CyberzombieRenderer;
import com.perigrine3.createcybernetics.entity.client.GuardianBeamRenderer;
import com.perigrine3.createcybernetics.entity.client.NuggetProjectileRenderer;
import com.perigrine3.createcybernetics.entity.client.SmasherRenderer;
import com.perigrine3.createcybernetics.item.ModCreativeModeTabs;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.loot.ModLootModifiers;
import com.perigrine3.createcybernetics.potion.ModPotions;
import com.perigrine3.createcybernetics.recipe.ModRecipeSerializers;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import com.perigrine3.createcybernetics.screen.custom.ArmCannonScreen;
import com.perigrine3.createcybernetics.screen.custom.ChipwareMiniScreen;
import com.perigrine3.createcybernetics.screen.custom.EngineeringTableScreen;
import com.perigrine3.createcybernetics.screen.custom.ExpandedInventoryScreen;
import com.perigrine3.createcybernetics.screen.custom.GraftingTableScreen;
import com.perigrine3.createcybernetics.screen.custom.HeatEngineScreen;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonScreen;
import com.perigrine3.createcybernetics.screen.custom.SpinalInjectorScreen;
import com.perigrine3.createcybernetics.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(value="createcybernetics")
public class CreateCybernetics {
    public static final String MODID = "createcybernetics";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks((String)"createcybernetics");
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems((String)"createcybernetics");
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (String)"createcybernetics");

    public CreateCybernetics(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register((Object)this);
        NeoForge.EVENT_BUS.addListener(PneumaticCalvesEffect.Events::onLivingJump);
        NeoForge.EVENT_BUS.register(PneumaticCalvesEffect.Events.class);
        eventBus.addListener(this::addCreative);
        ModCreativeModeTabs.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModSounds.register(eventBus);
        ModEntities.register(eventBus);
        ModEffects.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModEnchantmentEffects.register(eventBus);
        ModPotions.register(eventBus);
        ModLootModifiers.register(eventBus);
        ModCriteria.register(eventBus);
        ModDataComponents.register(eventBus);
        ModRecipes.register(eventBus);
        ModRecipeSerializers.register(eventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec)Config.SPEC);
        CompatBootstrap.bootstrap();
        ModAttachments.register(eventBus);
        ModAttributes.register(eventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        IronsSpellbooksCyberwareAttributes.register();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.insertAfter(Items.PITCHER_POD.getDefaultInstance(), ((Item)ModItems.DATURA_SEED_POD.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Blocks.DEEPSLATE_IRON_ORE.asItem().getDefaultInstance(), ((Block)ModBlocks.TITANIUMORE_BLOCK.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUMORE_BLOCK.asItem().getDefaultInstance(), ((Block)ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Blocks.RAW_IRON_BLOCK.asItem().getDefaultInstance(), ((Block)ModBlocks.RAW_TITANIUM_BLOCK.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.insertAfter(Items.CHAIN.getDefaultInstance(), ((Block)ModBlocks.TITANIUM_BLOCK.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUM_BLOCK.asItem().getDefaultInstance(), ((Block)ModBlocks.SMOOTH_TITANIUM.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.SMOOTH_TITANIUM.asItem().getDefaultInstance(), ((Block)ModBlocks.TITANIUM_GRATE.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUM_GRATE.asItem().getDefaultInstance(), ((Block)ModBlocks.TITANIUM_CLAD_COPPER.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUM_CLAD_COPPER.asItem().getDefaultInstance(), ((Block)ModBlocks.ETCHED_TITANIUM_COPPER.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.SMOOTH_TITANIUM.asItem().getDefaultInstance(), ((StairBlock)ModBlocks.SMOOTH_TITANIUM_STAIRS.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.SMOOTH_TITANIUM_STAIRS.asItem().getDefaultInstance(), ((SlabBlock)ModBlocks.SMOOTH_TITANIUM_SLAB.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUM_CLAD_COPPER.asItem().getDefaultInstance(), ((StairBlock)ModBlocks.TITANIUM_CLAD_COPPER_STAIRS.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.TITANIUM_CLAD_COPPER_STAIRS.asItem().getDefaultInstance(), ((SlabBlock)ModBlocks.TITANIUM_CLAD_COPPER_SLAB.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.ETCHED_TITANIUM_COPPER.asItem().getDefaultInstance(), ((StairBlock)ModBlocks.ETCHED_TITANIUM_COPPER_STAIRS.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.ETCHED_TITANIUM_COPPER_STAIRS.asItem().getDefaultInstance(), ((SlabBlock)ModBlocks.ETCHED_TITANIUM_COPPER_SLAB.get()).asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.SMASHER_SPAWN_EGG);
            event.accept(ModItems.CYBERZOMBIE_SPAWN_EGG);
            event.accept(ModItems.CYBERSKELETON_SPAWN_EGG);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.PHANTOM_MEMBRANE.getDefaultInstance(), ((Item)ModItems.DATURA_FLOWER.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.insertAfter(Items.COOKED_RABBIT.getDefaultInstance(), ((Item)ModItems.BODYPART_BRAIN.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.BODYPART_BRAIN.get()).getDefaultInstance(), ((Item)ModItems.COOKED_BRAIN.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.COOKED_BRAIN.get()).getDefaultInstance(), ((Item)ModItems.BODYPART_HEART.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.BODYPART_HEART.get()).getDefaultInstance(), ((Item)ModItems.COOKED_HEART.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.COOKED_HEART.get()).getDefaultInstance(), ((Item)ModItems.BODYPART_LIVER.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.BODYPART_LIVER.get()).getDefaultInstance(), ((Item)ModItems.COOKED_LIVER.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(((Item)ModItems.COOKED_LIVER.get()).getDefaultInstance(), ((Item)ModItems.BONE_MARROW.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            if (ModItems.ANDOUILLE_SAUSAGE != null && ModItems.ROASTED_ANDOUILLE != null && ModItems.GROUND_OFFAL != null && ModItems.BRAIN_STEW != null) {
                event.insertAfter(((Item)ModItems.BONE_MARROW.get()).getDefaultInstance(), ((Item)ModItems.ANDOUILLE_SAUSAGE.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(((Item)ModItems.ANDOUILLE_SAUSAGE.get()).getDefaultInstance(), ((Item)ModItems.ROASTED_ANDOUILLE.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(((Item)ModItems.ROASTED_ANDOUILLE.get()).getDefaultInstance(), ((Item)ModItems.GROUND_OFFAL.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(((Item)ModItems.GROUND_OFFAL.get()).getDefaultInstance(), ((Item)ModItems.BRAIN_STEW.get()).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.NUGGET_PROJECTILE.get(), NuggetProjectileRenderer::new);
            EntityRenderers.register(ModEntities.EMP_GRENADE_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.SMASHER.get(), SmasherRenderer::new);
            EntityRenderers.register(ModEntities.CYBERZOMBIE.get(), CyberzombieRenderer::new);
            EntityRenderers.register(ModEntities.CYBERSKELETON.get(), CyberskeletonRenderer::new);
            EntityRenderers.register(ModEntities.GUARDIAN_BEAM.get(), GuardianBeamRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register((MenuType)ModMenuTypes.ROBOSURGEON_MENU.get(), RobosurgeonScreen::new);
            event.register((MenuType)ModMenuTypes.ENGINEERING_TABLE_MENU.get(), EngineeringTableScreen::new);
            event.register((MenuType)ModMenuTypes.GRAFTING_TABLE_MENU.get(), GraftingTableScreen::new);
            event.register((MenuType)ModMenuTypes.EXPANDED_INVENTORY_MENU.get(), ExpandedInventoryScreen::new);
            event.register((MenuType)ModMenuTypes.CHIPWARE_MINI_MENU.get(), ChipwareMiniScreen::new);
            event.register((MenuType)ModMenuTypes.SPINAL_INJECTOR_MENU.get(), SpinalInjectorScreen::new);
            event.register((MenuType)ModMenuTypes.ARM_CANNON_MENU.get(), ArmCannonScreen::new);
            event.register((MenuType)ModMenuTypes.HEAT_ENGINE_MENU.get(), HeatEngineScreen::new);
        }
    }
}

