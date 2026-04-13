/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemNameBlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.fml.ModList
 *  net.neoforged.neoforge.common.DeferredSpawnEggItem
 *  net.neoforged.neoforge.registries.DeferredItem
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$Items
 */
package com.perigrine3.createcybernetics.item;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.item.ConditionalNameItem;
import com.perigrine3.createcybernetics.item.ModFoods;
import com.perigrine3.createcybernetics.item.cyberware.AdrenalPumpItem;
import com.perigrine3.createcybernetics.item.cyberware.AerostasisItem;
import com.perigrine3.createcybernetics.item.cyberware.AnkleBracerItem;
import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.item.cyberware.ArmSpinneretteItem;
import com.perigrine3.createcybernetics.item.cyberware.ArterialTurbineItem;
import com.perigrine3.createcybernetics.item.cyberware.BlubberItem;
import com.perigrine3.createcybernetics.item.cyberware.BoneflexItem;
import com.perigrine3.createcybernetics.item.cyberware.BonelacingItem;
import com.perigrine3.createcybernetics.item.cyberware.CardiovascularCouplerItem;
import com.perigrine3.createcybernetics.item.cyberware.CerebralProcessingUnitItem;
import com.perigrine3.createcybernetics.item.cyberware.ChipwareSlotsItem;
import com.perigrine3.createcybernetics.item.cyberware.CorticalStackItem;
import com.perigrine3.createcybernetics.item.cyberware.CraftingHandsItem;
import com.perigrine3.createcybernetics.item.cyberware.CreeperheartItem;
import com.perigrine3.createcybernetics.item.cyberware.CyberarmItem;
import com.perigrine3.createcybernetics.item.cyberware.CybereyeItem;
import com.perigrine3.createcybernetics.item.cyberware.CyberlegItem;
import com.perigrine3.createcybernetics.item.cyberware.DenseBatteryItem;
import com.perigrine3.createcybernetics.item.cyberware.DeployableElytraItem;
import com.perigrine3.createcybernetics.item.cyberware.DiamondWaferstackItem;
import com.perigrine3.createcybernetics.item.cyberware.DualisticConverterItem;
import com.perigrine3.createcybernetics.item.cyberware.EnderJammerItem;
import com.perigrine3.createcybernetics.item.cyberware.EyeOfDefenderItem;
import com.perigrine3.createcybernetics.item.cyberware.FirestarterItem;
import com.perigrine3.createcybernetics.item.cyberware.GillsItem;
import com.perigrine3.createcybernetics.item.cyberware.GrassfedStomachItem;
import com.perigrine3.createcybernetics.item.cyberware.GuardianEyeItem;
import com.perigrine3.createcybernetics.item.cyberware.HUDjackItem;
import com.perigrine3.createcybernetics.item.cyberware.HUDlensItem;
import com.perigrine3.createcybernetics.item.cyberware.HeatEngineItem;
import com.perigrine3.createcybernetics.item.cyberware.HyperoxygenationBoostItem;
import com.perigrine3.createcybernetics.item.cyberware.IDEMItem;
import com.perigrine3.createcybernetics.item.cyberware.IgniphorusGlandItem;
import com.perigrine3.createcybernetics.item.cyberware.ImmunosuppressorItem;
import com.perigrine3.createcybernetics.item.cyberware.InterchangeableFaceplateItem;
import com.perigrine3.createcybernetics.item.cyberware.InternalBatteryItem;
import com.perigrine3.createcybernetics.item.cyberware.InternalDefibrillatorItem;
import com.perigrine3.createcybernetics.item.cyberware.IntestineSpinneretteItem;
import com.perigrine3.createcybernetics.item.cyberware.IsothermalSkinItem;
import com.perigrine3.createcybernetics.item.cyberware.LinearFrameItem;
import com.perigrine3.createcybernetics.item.cyberware.LiverFilterItem;
import com.perigrine3.createcybernetics.item.cyberware.MagicCatalystItem;
import com.perigrine3.createcybernetics.item.cyberware.ManaBatteryItem;
import com.perigrine3.createcybernetics.item.cyberware.ManaSkinItem;
import com.perigrine3.createcybernetics.item.cyberware.MarrowBatteryItem;
import com.perigrine3.createcybernetics.item.cyberware.MechanicalHeartItem;
import com.perigrine3.createcybernetics.item.cyberware.MetabolicConverterItem;
import com.perigrine3.createcybernetics.item.cyberware.MetalDetectorItem;
import com.perigrine3.createcybernetics.item.cyberware.MetalSkinItem;
import com.perigrine3.createcybernetics.item.cyberware.NavigationChipItem;
import com.perigrine3.createcybernetics.item.cyberware.NeedlecasterItem;
import com.perigrine3.createcybernetics.item.cyberware.NeuralContextualizerItem;
import com.perigrine3.createcybernetics.item.cyberware.NightVisionModuleItem;
import com.perigrine3.createcybernetics.item.cyberware.OcelotPawsItem;
import com.perigrine3.createcybernetics.item.cyberware.OpticZoomModuleItem;
import com.perigrine3.createcybernetics.item.cyberware.OxygenTankItem;
import com.perigrine3.createcybernetics.item.cyberware.PiezoelectricEnergyGeneratorItem;
import com.perigrine3.createcybernetics.item.cyberware.PlateletDispatcherItem;
import com.perigrine3.createcybernetics.item.cyberware.PneumaticCalvesItem;
import com.perigrine3.createcybernetics.item.cyberware.PneumaticWristItem;
import com.perigrine3.createcybernetics.item.cyberware.PolarBearFurItem;
import com.perigrine3.createcybernetics.item.cyberware.PropellersItem;
import com.perigrine3.createcybernetics.item.cyberware.QuickdrawFlywheelItem;
import com.perigrine3.createcybernetics.item.cyberware.RavagerTendonsItem;
import com.perigrine3.createcybernetics.item.cyberware.ReinforcedKnucklesItem;
import com.perigrine3.createcybernetics.item.cyberware.RetractableClawsItem;
import com.perigrine3.createcybernetics.item.cyberware.SandevistanItem;
import com.perigrine3.createcybernetics.item.cyberware.SculkLungsItem;
import com.perigrine3.createcybernetics.item.cyberware.SolarskinItem;
import com.perigrine3.createcybernetics.item.cyberware.SpellJammerItem;
import com.perigrine3.createcybernetics.item.cyberware.SpiderEyesItem;
import com.perigrine3.createcybernetics.item.cyberware.SpinalInjectorItem;
import com.perigrine3.createcybernetics.item.cyberware.SpursItem;
import com.perigrine3.createcybernetics.item.cyberware.StemCellsItem;
import com.perigrine3.createcybernetics.item.cyberware.SubdermalArmorItem;
import com.perigrine3.createcybernetics.item.cyberware.SubdermalSpikesItem;
import com.perigrine3.createcybernetics.item.cyberware.SweatGlandsItem;
import com.perigrine3.createcybernetics.item.cyberware.SynthMuscleItem;
import com.perigrine3.createcybernetics.item.cyberware.SynthSkinItem;
import com.perigrine3.createcybernetics.item.cyberware.SyntheticChromatophoresItem;
import com.perigrine3.createcybernetics.item.cyberware.SyntheticSetulesItem;
import com.perigrine3.createcybernetics.item.cyberware.TacticalInkSacItem;
import com.perigrine3.createcybernetics.item.cyberware.TargetingModuleItem;
import com.perigrine3.createcybernetics.item.cyberware.ThreatMatrixItem;
import com.perigrine3.createcybernetics.item.cyberware.TitaniumSkullItem;
import com.perigrine3.createcybernetics.item.cyberware.TrajectoryCalculatorModuleItem;
import com.perigrine3.createcybernetics.item.cyberware.UnderwaterVisionModuleItem;
import com.perigrine3.createcybernetics.item.cyberware.WiredReflexesItem;
import com.perigrine3.createcybernetics.item.cyberware.upgrade_items.ArmUpgradeItem;
import com.perigrine3.createcybernetics.item.cyberware.upgrade_items.BrainUpgradeItem;
import com.perigrine3.createcybernetics.item.generic.BiochipDataShardItem;
import com.perigrine3.createcybernetics.item.generic.DataShardItem;
import com.perigrine3.createcybernetics.item.generic.EmpGrenadeItem;
import com.perigrine3.createcybernetics.item.generic.FaceplateMaskItem;
import com.perigrine3.createcybernetics.item.generic.HoloProjectionChipItem;
import com.perigrine3.createcybernetics.item.generic.InfologDataShardItem;
import com.perigrine3.createcybernetics.item.generic.NeuropozyneAutoinjector;
import com.perigrine3.createcybernetics.item.generic.XPCapsuleItem;
import com.perigrine3.createcybernetics.item.organs.BrainItem;
import com.perigrine3.createcybernetics.item.organs.EyeballItem;
import com.perigrine3.createcybernetics.item.organs.HeartItem;
import com.perigrine3.createcybernetics.item.organs.IntestinesItem;
import com.perigrine3.createcybernetics.item.organs.LeftArmItem;
import com.perigrine3.createcybernetics.item.organs.LeftLegItem;
import com.perigrine3.createcybernetics.item.organs.LiverItem;
import com.perigrine3.createcybernetics.item.organs.LungsItem;
import com.perigrine3.createcybernetics.item.organs.MuscleItem;
import com.perigrine3.createcybernetics.item.organs.RightArmItem;
import com.perigrine3.createcybernetics.item.organs.RightLegItem;
import com.perigrine3.createcybernetics.item.organs.SkeletonItem;
import com.perigrine3.createcybernetics.item.organs.SkinItem;
import com.perigrine3.createcybernetics.sound.ModSounds;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems((String)"createcybernetics");
    public static final DeferredItem<Item> RAWTITANIUM = ITEMS.register("rawtitanium", () -> new ConditionalNameItem(new Item.Properties(), "item.createcybernetics.rawtitanium", "item.createcybernetics.rawtitanium.northstar"));
    public static final DeferredItem<Item> TITANIUMINGOT = ITEMS.register("titaniumingot", () -> new ConditionalNameItem(new Item.Properties(), "item.createcybernetics.titaniumingot", "item.createcybernetics.titaniumingot.northstar"));
    public static final DeferredItem<Item> CRUSHEDTITANIUM = ITEMS.register("crushedtitanium", () -> new ConditionalNameItem(new Item.Properties(), "item.createcybernetics.crushedtitanium", "item.createcybernetics.crushedtitanium.northstar"));
    public static final DeferredItem<Item> TITANIUMNUGGET = ITEMS.register("titaniumnugget", () -> new ConditionalNameItem(new Item.Properties(), "item.createcybernetics.titaniumnugget", "item.createcybernetics.titaniumnugget.northstar"));
    public static final DeferredItem<Item> TITANIUMSHEET = ITEMS.register("titaniumsheet", () -> new ConditionalNameItem(new Item.Properties(), "item.createcybernetics.titaniumsheet", "item.createcybernetics.titaniumsheet.northstar"));
    public static final DeferredItem<Item> EYEUPGRADEBASE = ITEMS.register("eyeupgradebase", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TITANIUM_HAND = ITEMS.register("titanium_hand", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOLOIMPRINT_CHIP = ITEMS.register("holoimprint_chip", () -> new HoloProjectionChipItem(new Item.Properties()));
    public static final DeferredItem<Item> XP_CAPSULE = ITEMS.register("expcapsule", () -> new XPCapsuleItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FACEPLATE = ITEMS.register("faceplate", () -> new FaceplateMaskItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> NETHERITE_QPU = ITEMS.register("netherite_qpu", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legendarycomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EMP_GRENADE = ITEMS.register("emp_grenade", () -> new EmpGrenadeItem(new Item.Properties()));
    public static final DeferredItem<Item> DATURA_FLOWER = ITEMS.register("datura_flower", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DATURA_SEED_POD = ITEMS.register("datura_seed_pod", () -> new ItemNameBlockItem((Block)ModBlocks.DATURA_BUSH.get(), new Item.Properties()));
    public static final DeferredItem<Item> FRONTAL_LOBE = ITEMS.register("frontal_lobe", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PARIETAL_LOBE = ITEMS.register("parietal_lobe", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TEMPORAL_LOBE = ITEMS.register("temporal_lobe", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OCCIPITAL_LOBE = ITEMS.register("occipital_lobe", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CEREBELLUM = ITEMS.register("cerebellum", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BONE_SAW = ITEMS.register("bone_saw", () -> new SwordItem((Tier)Tiers.IRON, new Item.Properties().stacksTo(1).durability(128)));
    public static final DeferredItem<Item> QUICKHACK_BURNING = ITEMS.register("quickhack_burning", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> QUICKHACK_REBOOT = ITEMS.register("quickhack_reboot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> QUICKHACK_ITEM = ITEMS.register("quickhack_item", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NEUROPOZYNE_AUTOINJECTOR = ITEMS.register("neuropozyne_autoinjector", () -> new NeuropozyneAutoinjector(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> EMPTY_AUTOINJECTOR = ITEMS.register("empty_autoinjector", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MUSIC_DISC_CYBERPSYCHO = ITEMS.register("music_disc_cyberpsycho", () -> new Item(new Item.Properties().stacksTo(1).jukeboxPlayable(ModSounds.CYBERPSYCHO_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_NEON_OVERLORDS = ITEMS.register("music_disc_neon_overlords", () -> new Item(new Item.Properties().stacksTo(1).jukeboxPlayable(ModSounds.NEON_OVERLORDS_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_NEUROHACK = ITEMS.register("music_disc_neurohack", () -> new Item(new Item.Properties().stacksTo(1).jukeboxPlayable(ModSounds.NEUROHACK_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_THE_GRID = ITEMS.register("music_disc_the_grid", () -> new Item(new Item.Properties().stacksTo(1).jukeboxPlayable(ModSounds.THE_GRID_KEY)));
    public static final DeferredItem<Item> SMASHER_SPAWN_EGG = ITEMS.register("smasher_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.SMASHER, 0x7F7B7B, 16007990, new Item.Properties()));
    public static final DeferredItem<Item> CYBERZOMBIE_SPAWN_EGG = ITEMS.register("cyberzombie_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CYBERZOMBIE, 44975, -65476, new Item.Properties()));
    public static final DeferredItem<Item> CYBERSKELETON_SPAWN_EGG = ITEMS.register("cyberskeleton_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CYBERSKELETON, 0xC1C1C1, -65476, new Item.Properties()));
    public static final DeferredItem<Item> DATA_SHARD_RED = ITEMS.register("data_shard_red", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_red.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)14223364))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_red.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_ORANGE = ITEMS.register("data_shard_orange", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_orange.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)14247684))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_orange.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_YELLOW = ITEMS.register("data_shard_yellow", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_yellow.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)15255042))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_yellow.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_GREEN = ITEMS.register("data_shard_green", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_green.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)570114))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_green.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_CYAN = ITEMS.register("data_shard_cyan", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_cyan.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)182650))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_cyan.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_BLUE = ITEMS.register("data_shard_blue", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_blue.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)146912))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_blue.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_PURPLE = ITEMS.register("data_shard_purple", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_purple.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)10093270))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_purple.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_PINK = ITEMS.register("data_shard_pink", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_pink.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)14680749))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_pink.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_BROWN = ITEMS.register("data_shard_brown", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_brown.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)7880473))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_brown.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_GRAY = ITEMS.register("data_shard_gray", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_gray.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)0x5E5E5E))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_gray.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_BLACK = ITEMS.register("data_shard_black", () -> new DataShardItem(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_black.title").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)0x292929))));
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_black.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_BIOCHIP = ITEMS.register("data_shard_biochip", () -> new BiochipDataShardItem(new Item.Properties().stacksTo(1)){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_biochip.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> DATA_SHARD_INFOLOG = ITEMS.register("data_shard_infolog", () -> new InfologDataShardItem(new Item.Properties().stacksTo(1)){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_infolog.desc"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COOKED_BRAIN = ITEMS.register("cooked_brain", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.COOKED_BRAIN)));
    public static final DeferredItem<Item> COOKED_LIVER = ITEMS.register("cooked_liver", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.COOKED_LIVER)));
    public static final DeferredItem<Item> BONE_MARROW = ITEMS.register("bone_marrow", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.BONE_MARROW)));
    public static final DeferredItem<Item> COOKED_HEART = ITEMS.register("cooked_heart", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.COOKED_HEART)));
    public static final DeferredItem<Item> ANDOUILLE_SAUSAGE = ModItems.registerIfLoaded("farmersdelight", "andouille_sausage", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.ANDOUILLE_SAUSAGE)));
    public static final DeferredItem<Item> ROASTED_ANDOUILLE = ModItems.registerIfLoaded("farmersdelight", "roasted_andouille", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.ROASTED_ANDOUILLE)));
    public static final DeferredItem<Item> GROUND_OFFAL = ModItems.registerIfLoaded("farmersdelight", "ground_offal", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.GROUND_OFFAL)));
    public static final DeferredItem<Item> BRAIN_STEW = ModItems.registerIfLoaded("farmersdelight", "brain_stew", () -> new Item(new Item.Properties().stacksTo(64).food(ModFoods.BRAIN_STEW)));
    public static final DeferredItem<Item> COMPONENT_ACTUATOR = ITEMS.register("component_actuator", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_FIBEROPTICS = ITEMS.register("component_fiberoptics", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_WIRING = ITEMS.register("component_wiring", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_DIODES = ITEMS.register("component_diodes", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_PLATING = ITEMS.register("component_plating", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_GRAPHICSCARD = ITEMS.register("component_graphicscard", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_SSD = ITEMS.register("component_ssd", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_STORAGE = ITEMS.register("component_storage", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_SYNTHNERVES = ITEMS.register("component_synthnerves", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_MESH = ITEMS.register("component_mesh", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_LED = ModItems.registerIfNotLoaded("create", "component_led", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COMPONENT_TITANIUMROD = ModItems.registerIfNotLoaded("create", "component_titaniumrod", () -> new Item(new Item.Properties()){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basiccomponent_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_RIGHTLEG = ITEMS.register("bodypart_rightleg", () -> new RightLegItem(new Item.Properties().stacksTo(1), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_LEFTLEG = ITEMS.register("bodypart_leftleg", () -> new LeftLegItem(new Item.Properties().stacksTo(1), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_RIGHTARM = ITEMS.register("bodypart_rightarm", () -> new RightArmItem(new Item.Properties().stacksTo(1), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_LEFTARM = ITEMS.register("bodypart_leftarm", () -> new LeftArmItem(new Item.Properties().stacksTo(1), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_SKELETON = ITEMS.register("bodypart_skeleton", () -> new SkeletonItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_BRAIN = ITEMS.register("bodypart_brain", () -> new BrainItem(new Item.Properties().stacksTo(16).food(ModFoods.RAW_BRAIN), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_EYEBALLS = ITEMS.register("bodypart_eyeballs", () -> new EyeballItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_HEART = ITEMS.register("bodypart_heart", () -> new HeartItem(new Item.Properties().stacksTo(16).food(ModFoods.RAW_HEART), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_LUNGS = ITEMS.register("bodypart_lungs", () -> new LungsItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_LIVER = ITEMS.register("bodypart_liver", () -> new LiverItem(new Item.Properties().stacksTo(16).food(ModFoods.RAW_LIVER), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_INTESTINES = ITEMS.register("bodypart_intestines", () -> new IntestinesItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_MUSCLE = ITEMS.register("bodypart_muscle", () -> new MuscleItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_SKIN = ITEMS.register("bodypart_skin", () -> new SkinItem(new Item.Properties().stacksTo(16), 0){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_GUARDIANRETINA = ITEMS.register("bodypart_guardianretina", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_WARDENESOPHAGUS = ITEMS.register("bodypart_wardenesophagus", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_GYROSCOPICBLADDER = ITEMS.register("bodypart_gyroscopicbladder", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_SPINNERETTE = ITEMS.register("bodypart_spinnerette", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_FIREGLAND = ITEMS.register("bodypart_firegland", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BODYPART_GILLS = ITEMS.register("bodypart_gills", () -> new Item(new Item.Properties().stacksTo(16)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> COPPER_UPGRADE_TEMPLATE = ITEMS.register("copper_upgrade_template", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRON_UPGRADE_TEMPLATE = ITEMS.register("iron_upgrade_template", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOLD_UPGRADE_TEMPLATE = ITEMS.register("gold_upgrade_template", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTLEG = ITEMS.register("basecyberware_rightleg", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTLEG = ITEMS.register("basecyberware_leftleg", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTARM = ITEMS.register("basecyberware_rightarm", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTARM = ITEMS.register("basecyberware_leftarm", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LINEARFRAME = ITEMS.register("basecyberware_linearframe", () -> new LinearFrameItem(new Item.Properties().stacksTo(1), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_linearframe.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_CYBEREYES = ITEMS.register("basecyberware_cybereyes", () -> new CybereyeItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_cybereyes.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTLEG_COPPERPLATED = ITEMS.register("basecyberware_rightleg_copperplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTLEG_COPPERPLATED = ITEMS.register("basecyberware_leftleg_copperplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTARM_COPPERPLATED = ITEMS.register("basecyberware_rightarm_copperplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTARM_COPPERPLATED = ITEMS.register("basecyberware_leftarm_copperplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTLEG_IRONPLATED = ITEMS.register("basecyberware_rightleg_ironplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTLEG_IRONPLATED = ITEMS.register("basecyberware_leftleg_ironplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTARM_IRONPLATED = ITEMS.register("basecyberware_rightarm_ironplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTARM_IRONPLATED = ITEMS.register("basecyberware_leftarm_ironplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTLEG_GOLDPLATED = ITEMS.register("basecyberware_rightleg_goldplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTLEG_GOLDPLATED = ITEMS.register("basecyberware_leftleg_goldplated", () -> new CyberlegItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LLEG){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftleg.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_RIGHTARM_GOLDPLATED = ITEMS.register("basecyberware_rightarm_goldplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.RARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_rightarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BASECYBERWARE_LEFTARM_GOLDPLATED = ITEMS.register("basecyberware_leftarm_goldplated", () -> new CyberarmItem(new Item.Properties().stacksTo(1), 5, CyberwareSlot.LARM){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_leftarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.basecyberware_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_HUDLENS = ITEMS.register("eyeupgrades_hudlens", () -> new HUDlensItem(new Item.Properties().stacksTo(64), 1){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_hudlens.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_hudlens.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_NAVIGATIONCHIP = ModItems.registerIfLoaded("xaerominimap", "eyeupgrades_navigationchip", () -> new NavigationChipItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_navigationchip.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_navigationchip.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_HUDJACK = ITEMS.register("eyeupgrades_hudjack", () -> new HUDjackItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_hudjack.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_hudjack.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_NIGHTVISION = ITEMS.register("eyeupgrades_nightvision", () -> new NightVisionModuleItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_nightvision.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_nightvision.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_TARGETING = ITEMS.register("eyeupgrades_targeting", () -> new TargetingModuleItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_targeting.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_targeting.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_UNDERWATERVISION = ITEMS.register("eyeupgrades_underwatervision", () -> new UnderwaterVisionModuleItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_underwatervision.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_underwatervision.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_ZOOM = ITEMS.register("eyeupgrades_zoom", () -> new OpticZoomModuleItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_zoom.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_zoom.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> EYEUPGRADES_TRAJECTORYCALCULATOR = ITEMS.register("eyeupgrades_trajectorycalculator", () -> new TrajectoryCalculatorModuleItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_trajectorycalculator.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_trajectorycalculator.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_ARMCANNON = ITEMS.register("armupgrades_armcannon", () -> new ArmCannonItem(new Item.Properties().stacksTo(16), 7){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_armcannon.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_FLYWHEEL = ITEMS.register("armupgrades_flywheel", () -> new QuickdrawFlywheelItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_flywheel.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_CLAWS = ITEMS.register("armupgrades_claws", () -> new RetractableClawsItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_claws.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_claws.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_CRAFTHANDS = ITEMS.register("armupgrades_crafthands", () -> new CraftingHandsItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_crafthands.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_DRILLFIST = ITEMS.register("armupgrades_drillfist", () -> new ArmUpgradeItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_drillfist.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_drillfist.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_FIRESTARTER = ITEMS.register("armupgrades_firestarter", () -> new FirestarterItem(new Item.Properties().stacksTo(16), 1){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_firestarter.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_PNEUMATICWRIST = ITEMS.register("armupgrades_pneumaticwrist", () -> new PneumaticWristItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_pneumaticwrist.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_pneumaticwrist.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ARMUPGRADES_REINFORCEDKNUCKLES = ITEMS.register("armupgrades_reinforcedknuckles", () -> new ReinforcedKnucklesItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_reinforcedknuckles.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_METALDETECTOR = ITEMS.register("legupgrades_metaldetector", () -> new MetalDetectorItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_metaldetector.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_metaldetector.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_ANKLEBRACERS = ITEMS.register("legupgrades_anklebracers", () -> new AnkleBracerItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_anklebracers.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_anklebracers.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_JUMPBOOST = ITEMS.register("legupgrades_jumpboost", () -> new PneumaticCalvesItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_jumpboost.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_jumpboost.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_PROPELLERS = ITEMS.register("legupgrades_propellers", () -> new PropellersItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_propellers.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_propellers.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_SPURS = ITEMS.register("legupgrades_spurs", () -> new SpursItem(new Item.Properties().stacksTo(16), 2){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_spurs.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LEGUPGRADES_OCELOTPAWS = ITEMS.register("legupgrades_ocelotpaws", () -> new OcelotPawsItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_ocelotpaws.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_ocelotpaws.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_BONEBATTERY = ITEMS.register("boneupgrades_bonebattery", () -> new MarrowBatteryItem(new Item.Properties().stacksTo(16), 2){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_bonebattery.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_bonebattery.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_BONEFLEX = ITEMS.register("boneupgrades_boneflex", () -> new BoneflexItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_boneflex.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_boneflex.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_BONELACING = ITEMS.register("boneupgrades_bonelacing", () -> new BonelacingItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_bonelacing.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_bonelacing.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_ELYTRA = ModItems.registerIfLoaded("caelus", "boneupgrades_elytra", () -> new DeployableElytraItem(new Item.Properties().stacksTo(1), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_elytra.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_elytra.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_PIEZO = ITEMS.register("boneupgrades_piezo", () -> new PiezoelectricEnergyGeneratorItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_piezo.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_piezo.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_SPINALINJECTOR = ITEMS.register("boneupgrades_spinalinjector", () -> new SpinalInjectorItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_spinalinjector.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_spinalinjector.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_SANDEVISTAN = ITEMS.register("boneupgrades_sandevistan", () -> new SandevistanItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_sandevistan.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BONEUPGRADES_CYBERSKULL = ITEMS.register("boneupgrades_cyberskull", () -> new TitaniumSkullItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_cyberskull.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_CYBERBRAIN = ITEMS.register("brainupgrades_cyberbrain", () -> new CerebralProcessingUnitItem(new Item.Properties().stacksTo(16), 12){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_cyberbrain.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_cyberbrain.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_EYEOFDEFENDER = ITEMS.register("brainupgrades_eyeofdefender", () -> new EyeOfDefenderItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_eyeofdefender.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_eyeofdefender.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER = ModItems.registerIfLoaded("create_enchantment_industry", "brainupgrades_consciousnesstransmitter", () -> new NeedlecasterItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_consciousnesstransmitter.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_consciousnesstransmitter.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_CORTICALSTACK = ModItems.registerIfLoaded("create_enchantment_industry", "brainupgrades_corticalstack", () -> new CorticalStackItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_corticalstack.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_corticalstack.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_ENDERJAMMER = ITEMS.register("brainupgrades_enderjammer", () -> new EnderJammerItem(new Item.Properties().stacksTo(16), 2){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_enderjammer.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_MATRIX = ITEMS.register("brainupgrades_matrix", () -> new ThreatMatrixItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_matrix.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_matrix.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_NEURALCONTEXTUALIZER = ITEMS.register("brainupgrades_neuralcontextualizer", () -> new NeuralContextualizerItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_neuralcontextualizer.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_neuralcontextualizer.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_CYBERDECK = ITEMS.register("brainupgrades_cyberdeck", () -> new BrainUpgradeItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_cyberdeck.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_cyberdeck.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_IDEM = ITEMS.register("brainupgrades_idem", () -> new IDEMItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_idem.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_idem.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_CHIPWARESLOTS = ITEMS.register("brainupgrades_chipwareslots", () -> new ChipwareSlotsItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_chipwareslots.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_chipwareslots.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> BRAINUPGRADES_SPELLJAMMER = ModItems.registerIfLoaded("irons_spellbooks", "brainupgrades_spelljammer", () -> new SpellJammerItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_spelljammer.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_spelljammer.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrade_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_CYBERHEART = ITEMS.register("heartupgrades_cyberheart", () -> new MechanicalHeartItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_cyberheart.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_COUPLER = ITEMS.register("heartupgrades_coupler", () -> new CardiovascularCouplerItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_coupler.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_coupler.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_CREEPERHEART = ITEMS.register("heartupgrades_creeperheart", () -> new CreeperheartItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_creeperheart.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_creeperheart.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_DEFIBRILLATOR = ITEMS.register("heartupgrades_defibrillator", () -> new InternalDefibrillatorItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_defibrillator.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_defibrillator.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_STEMCELL = ITEMS.register("heartupgrades_stemcell", () -> new StemCellsItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_stemcell.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> HEARTUPGRADES_PLATELETS = ITEMS.register("heartupgrades_platelets", () -> new PlateletDispatcherItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_platelets.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LUNGSUPGRADES_HYPEROXYGENATION = ITEMS.register("lungsupgrades_hyperoxygenation", () -> new HyperoxygenationBoostItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_hyperoxygenation.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_hyperoxygenation.tooltip2"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_hyperoxygenation.tooltip3"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> LUNGSUPGRADES_OXYGEN = ITEMS.register("lungsupgrades_oxygen", () -> new OxygenTankItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_oxygen.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_oxygen.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_ADRENALINE = ITEMS.register("organsupgrades_adrenaline", () -> new AdrenalPumpItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_adrenaline.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_BATTERY = ITEMS.register("organsupgrades_battery", () -> new InternalBatteryItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_battery.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_battery.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_DIAMONDWAFERSTACK = ITEMS.register("organsupgrades_diamondwaferstack", () -> new DiamondWaferstackItem(new Item.Properties().stacksTo(16), 1){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_diamondwaferstack.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_diamondwaferstack.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_DUALISTICCONVERTER = ITEMS.register("organsupgrades_dualisticconverter", () -> new DualisticConverterItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_dualisticconverter.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_LIVERFILTER = ITEMS.register("organsupgrades_liverfilter", () -> new LiverFilterItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_liverfilter.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_MAGICCATALYST = ITEMS.register("organsupgrades_magiccatalyst", () -> new MagicCatalystItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_magiccatalyst.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_METABOLIC = ITEMS.register("organsupgrades_metabolic", () -> new MetabolicConverterItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_metabolic.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_metabolic.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_DENSEBATTERY = ITEMS.register("organsupgrade_densebattery", () -> new DenseBatteryItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrade_densebattery.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_HEATENGINE = ITEMS.register("organsupgrades_heatengine", () -> new HeatEngineItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_heatengine.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> ORGANSUPGRADES_MANABATTERY = ModItems.registerIfLoaded("irons_spellbooks", "organsupgrades_manabattery", () -> new ManaBatteryItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_manabattery.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_ARTERIALTURBINE = ITEMS.register("skinupgrades_arterialturbine", () -> new ArterialTurbineItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_arterialturbine.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_arterialturbine.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_CHROMATOPHORES = ITEMS.register("skinupgrades_chromatophores", () -> new SyntheticChromatophoresItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_chromatophores.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SYNTHSKIN = ITEMS.register("skinupgrades_synthskin", () -> new SynthSkinItem(new Item.Properties().stacksTo(16), 1){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_synthskin.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_IMMUNO = ITEMS.register("skinupgrades_immuno", () -> new ImmunosuppressorItem(new Item.Properties().stacksTo(16), -25){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_immuno.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_FACEPLATE = ITEMS.register("skinupgrades_faceplate", () -> new InterchangeableFaceplateItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_faceplate.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_NETHERITEPLATING = ITEMS.register("skinupgrades_netheriteplating", () -> new IsothermalSkinItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_netheriteplating.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SOLARSKIN = ITEMS.register("skinupgrades_solarskin", () -> new SolarskinItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_solarskin.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.boneupgrades_bonebattery.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SUBDERMALARMOR = ITEMS.register("skinupgrades_subdermalarmor", () -> new SubdermalArmorItem(new Item.Properties().stacksTo(16), 6){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_subdermalarmor.tooltip1"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_subdermalarmor.tooltip2"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SUBDERMALSPIKES = ITEMS.register("skinupgrades_subdermalspikes", () -> new SubdermalSpikesItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_subdermalspikes.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SYNTHETICSETULES = ITEMS.register("skinupgrades_syntheticsetules", () -> new SyntheticSetulesItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_syntheticsetules.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_METALPLATING = ITEMS.register("skinupgrades_metalplating", () -> new MetalSkinItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_metalplating.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_MANASKIN = ModItems.registerIfLoaded("irons_spellbooks", "skinupgrades_manaskin", () -> new ManaSkinItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_manaskin.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SKINUPGRADES_SWEAT = ModItems.registerIfLoaded("cold_sweat", "skinupgrades_sweat", () -> new SweatGlandsItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_sweat.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> MUSCLEUPGRADES_SYNTHMUSCLE = ITEMS.register("muscleupgrades_synthmuscle", () -> new SynthMuscleItem(new Item.Properties().stacksTo(16), 5){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_synthmuscle.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> MUSCLEUPGRADES_WIREDREFLEXES = ITEMS.register("muscleupgrades_wiredreflexes", () -> new WiredReflexesItem(new Item.Properties().stacksTo(16), 4){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_wiredreflexes.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_BLUBBER = ModItems.registerIfLoaded("cold_sweat", "wetware_blubber", () -> new BlubberItem(new Item.Properties().stacksTo(16), 3){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_blubber.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_FIREBREATHINGLUNGS = ITEMS.register("wetware_firebreathinglungs", () -> new IgniphorusGlandItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_firebreathinglungs.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_WATERBREATHINGLUNGS = ITEMS.register("wetware_waterbreathinglungs", () -> new GillsItem(new Item.Properties().stacksTo(16), 12){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_waterbreathinglungs.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_GUARDIANEYE = ITEMS.register("wetware_guardianeye", () -> new GuardianEyeItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_guardianeye.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_POLARBEARFUR = ITEMS.register("wetware_polarbearfur", () -> new PolarBearFurItem(new Item.Properties().stacksTo(16), 12){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_polarbearfur.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_RAVAGERTENDONS = ITEMS.register("wetware_ravagertendons", () -> new RavagerTendonsItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_ravagertendons.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_SCULKLUNGS = ITEMS.register("wetware_sculklungs", () -> new SculkLungsItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_sculklungs.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_TACTICALINKSAC = ITEMS.register("wetware_tacticalinksac", () -> new TacticalInkSacItem(new Item.Properties().stacksTo(16), 8){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_tacticalinksac.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_AEROSTASISGYROBLADDER = ITEMS.register("wetware_aerostasisgyrobladder", () -> new AerostasisItem(new Item.Properties().stacksTo(16), 15){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_aerostasisgyrobladder.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_GRASSFEDSTOMACH = ITEMS.register("wetware_grassfedstomach", () -> new GrassfedStomachItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_grassfedstomach.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_WEBSHOOTINGINTESTINES = ITEMS.register("wetware_webshootingintestines", () -> new IntestineSpinneretteItem(new Item.Properties().stacksTo(16), 10){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_webshootingintestines.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_WEBSHOOTING_LEFTARM = ITEMS.register("wetware_webshooting_leftarm", () -> new ArmSpinneretteItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_webshooting_leftarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_WEBSHOOTING_RIGHTARM = ITEMS.register("wetware_webshooting_rightarm", () -> new ArmSpinneretteItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_webshooting_rightarm.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> WETWARE_SPIDEREYES = ITEMS.register("wetware_spidereyes", () -> new SpiderEyesItem(new Item.Properties().stacksTo(16), 7){

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if (Screen.hasShiftDown()) {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetware_spidereyes.tooltip1"));
            } else {
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.wetwareupgrades_tooltip"));
                tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.hold_shift_down"));
            }
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_RIGHTLEG = ITEMS.register("scavenged_rightleg", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_LEFTLEG = ITEMS.register("scavenged_leftleg", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_RIGHTARM = ITEMS.register("scavenged_rightarm", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_LEFTARM = ITEMS.register("scavenged_leftarm", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_LINEARFRAME = ITEMS.register("scavenged_linearframe", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CYBEREYES = ITEMS.register("scavenged_cybereyes", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_HUDLENS = ITEMS.register("scavenged_hudlens", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_NAVIGATIONCHIP = ModItems.registerIfLoaded("xaerominimap", "scavenged_navigationchip", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_HUDJACK = ITEMS.register("scavenged_hudjack", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_NIGHTVISION = ITEMS.register("scavenged_nightvision", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_TARGETING = ITEMS.register("scavenged_targeting", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_UNDERWATERVISION = ITEMS.register("scavenged_underwatervision", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ZOOM = ITEMS.register("scavenged_zoom", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_TRAJECTORYCALCULATOR = ITEMS.register("scavenged_trajectorycalculator", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ARMCANNON = ITEMS.register("scavenged_armcannon", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_FLYWHEEL = ITEMS.register("scavenged_flywheel", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CLAWS = ITEMS.register("scavenged_claws", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CRAFTHANDS = ITEMS.register("scavenged_crafthands", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_DRILLFIST = ITEMS.register("scavenged_drillfist", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_FIRESTARTER = ITEMS.register("scavenged_firestarter", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_PNEUMATICWRIST = ITEMS.register("scavenged_pneumaticwrist", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_REINFORCEDKNUCKLES = ITEMS.register("scavenged_reinforcedknuckles", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_METALDETECTOR = ITEMS.register("scavenged_metaldetector", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ANKLEBRACERS = ITEMS.register("scavenged_anklebracers", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_JUMPBOOST = ITEMS.register("scavenged_jumpboost", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_PROPELLERS = ITEMS.register("scavenged_propellers", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SPURS = ITEMS.register("scavenged_spurs", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_OCELOTPAWS = ITEMS.register("scavenged_ocelotpaws", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_BONEBATTERY = ITEMS.register("scavenged_bonebattery", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_BONEFLEX = ITEMS.register("scavenged_boneflex", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_BONELACING = ITEMS.register("scavenged_bonelacing", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ELYTRA = ModItems.registerIfLoaded("caelus", "scavenged_elytra", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_PIEZO = ITEMS.register("scavenged_piezo", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SPINALINJECTOR = ITEMS.register("scavenged_spinalinjector", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SANDEVISTAN = ITEMS.register("scavenged_sandevistan", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_EYEOFDEFENDER = ITEMS.register("scavenged_eyeofdefender", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CONSCIOUSNESSTRANSMITTER = ModItems.registerIfLoaded("createenchantmentindustry", "scavenged_consciousnesstransmitter", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CORTICALSTACK = ModItems.registerIfLoaded("createenchantmentindustry", "scavenged_corticalstack", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ENDERJAMMER = ITEMS.register("scavenged_enderjammer", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_MATRIX = ITEMS.register("scavenged_matrix", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_NEURALCONTEXTUALIZER = ITEMS.register("scavenged_neuralcontextualizer", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CYBERDECK = ITEMS.register("scavenged_cyberdeck", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_IDEM = ITEMS.register("scavenged_idem", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CHIPWARESLOTS = ITEMS.register("scavenged_chipwareslots", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CYBERHEART = ITEMS.register("scavenged_cyberheart", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_COUPLER = ITEMS.register("scavenged_coupler", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CREEPERHEART = ITEMS.register("scavenged_creeperheart", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_DEFIBRILLATOR = ITEMS.register("scavenged_defibrillator", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_STEMCELL = ITEMS.register("scavenged_stemcell", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_PLATELETS = ITEMS.register("scavenged_platelets", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_HYPEROXYGENATION = ITEMS.register("scavenged_hyperoxygenation", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_OXYGEN = ITEMS.register("scavenged_oxygen", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ADRENALINE = ITEMS.register("scavenged_adrenaline", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_BATTERY = ITEMS.register("scavenged_battery", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_DIAMONDWAFERSTACK = ITEMS.register("scavenged_diamondwaferstack", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_DUALISTICCONVERTER = ITEMS.register("scavenged_dualisticconverter", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_LIVERFILTER = ITEMS.register("scavenged_liverfilter", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_METABOLIC = ITEMS.register("scavenged_metabolic", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_DENSEBATTERY = ITEMS.register("scavenged_densebattery", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_HEATENGINE = ITEMS.register("scavenged_heatengine", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_ARTERIALTURBINE = ITEMS.register("scavenged_arterialturbine", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_CHROMATOPHORES = ITEMS.register("scavenged_chromatophores", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SYNTHSKIN = ITEMS.register("scavenged_synthskin", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_IMMUNO = ITEMS.register("scavenged_immuno", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_FACEPLATE = ITEMS.register("scavenged_faceplate", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_NETHERITEPLATING = ITEMS.register("scavenged_netheriteplating", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SOLARSKIN = ITEMS.register("scavenged_solarskin", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SUBDERMALARMOR = ITEMS.register("scavenged_subdermalarmor", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SUBDERMALSPIKES = ITEMS.register("scavenged_subdermalspikes", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SYNTHETICSETULES = ITEMS.register("scavenged_syntheticsetules", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_METALPLATING = ITEMS.register("scavenged_metalplating", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SWEAT = ModItems.registerIfLoaded("cold_sweat", "scavenged_sweat", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_SYNTHMUSCLE = ITEMS.register("scavenged_synthmuscle", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });
    public static final DeferredItem<Item> SCAVENGED_WIREDREFLEXES = ITEMS.register("scavenged_wiredreflexes", () -> new Item(new Item.Properties().stacksTo(64)){

        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            tooltipComponents.add((Component)Component.translatable((String)"tooltip.createcybernetics.scavenged_tooltip"));
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    });

    public static <T extends Item> DeferredItem<T> registerIfLoaded(String modid, String name, Supplier<T> supplier) {
        if (ModList.get().isLoaded(modid)) {
            return ITEMS.register(name, supplier);
        }
        return null;
    }

    public static <T extends Item> DeferredItem<T> registerIfNotLoaded(String modid, String name, Supplier<T> supplier) {
        if (!ModList.get().isLoaded(modid)) {
            return ITEMS.register(name, supplier);
        }
        return null;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

