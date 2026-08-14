/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.neoforged.neoforge.client.event.InputEvent$Key
 *  net.neoforged.neoforge.client.event.RenderPlayerEvent$Pre
 *  net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.maxwell.cyber_ware_port.client;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.client.KeyInit;
import com.maxwell.cyber_ware_port.client.upgrades.cybereye.CyberwareMenuScreen;
import com.maxwell.cyber_ware_port.common.block.blueprintchest.BlueprintChestBlock;
import com.maxwell.cyber_ware_port.common.block.charger.ChargerBlock;
import com.maxwell.cyber_ware_port.common.block.component_box.ComponentBoxBlock;
import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlock;
import com.maxwell.cyber_ware_port.common.block.radio.RadioKitBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerFenceBlock;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlock;
import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlock;
import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlock;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareSlotType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.network.ClientPacketHandler;
import com.maxwell.cyber_ware_port.common.network.DoubleJumpPacket;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class ForgeClientEvents {
    private static final String NBT_DOUBLE_JUMPED = "cyberware_double_jumped";

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List tooltip = event.getToolTip();
        if (item == ((RadioKitBlock)((Object)ModBlocks.RADIO_KIT_BLOCK.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.radio_kit").withStyle(ChatFormatting.GRAY));
        } else if (item == ((ComponentBoxBlock)((Object)ModBlocks.COMPONENT_BOX.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.component_box").withStyle(ChatFormatting.GRAY));
        } else if (item == ((RadioTowerCoreBlock)((Object)ModBlocks.RADIO_TOWER_CORE.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.radio_tower_core").withStyle(ChatFormatting.GRAY));
        } else if (item == ((ScannerBlock)((Object)ModBlocks.SCANNER.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.scanner").withStyle(ChatFormatting.GRAY));
        } else if (item == ((RadioTowerFenceBlock)((Object)ModBlocks.RADIO_TOWER_COMPONENT.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.radio_component").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.radio_component2").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.radio_component3").withStyle(ChatFormatting.GRAY));
        } else if (item == ((ChargerBlock)((Object)ModBlocks.CHARGER.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.charger").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.charger2").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.charger3").withStyle(ChatFormatting.GRAY));
        } else if (item == ((SurgeryChamberBlock)((Object)ModBlocks.SURGERY_CHAMBER.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.surgery_chamber").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.surgery_chamber2").withStyle(ChatFormatting.GRAY));
        } else if (item == ((BlueprintChestBlock)((Object)ModBlocks.BLUEPRINT_CHEST.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.blueprint_chest").withStyle(ChatFormatting.GRAY));
        } else if (item == ((CyberwareWorkbenchBlock)((Object)ModBlocks.CYBERWARE_WORKBENCH.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.cyberware_workbench").withStyle(ChatFormatting.GRAY));
        } else if (item == ((RobosurgeonBlock)((Object)ModBlocks.ROBO_SURGEON.get())).asItem()) {
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.robo_surgeon").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable((String)"tooltip.cyber_ware_port.robo_surgeon2").withStyle(ChatFormatting.GRAY));
        }
        if (((Boolean)stack.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) {
            Component name = tooltip.isEmpty() ? stack.getHoverName() : (Component)tooltip.get(0);
            tooltip.clear();
            tooltip.add(name);
            tooltip.add(Component.translatable((String)"cyberware.tooltip.ghost.remove").withStyle(ChatFormatting.RED));
            return;
        }
        ICyberware cyberware = CyberwareAPI.getCyberware(stack);
        if (cyberware != null) {
            CyberwareSlotType slotType;
            Set<Item> incompatibles;
            ResourceLocation registryName = BuiltInRegistries.ITEM.getKey((Object)item);
            if (registryName.getPath().contains("body_part")) {
                return;
            }
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable((String)"cyberware.tooltip.shiftPrompt", (Object[])new Object[]{Component.translatable((String)"key.keyboard.shift")}).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
                return;
            }
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable((String)("cyberware.tooltip." + registryName.getPath())).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.empty());
            if (cyberware.canToggle(stack)) {
                boolean isActive = cyberware.isActive(stack);
                tooltip.add(Component.translatable((String)"cyberware.tooltip.status", (Object[])new Object[]{Component.translatable((String)(isActive ? "cyberware.gui.active.enable" : "cyberware.gui.active.disable")).withStyle(isActive ? ChatFormatting.GREEN : ChatFormatting.RED)}).withStyle(ChatFormatting.WHITE));
            }
            if (cyberware.hasEnergyProperties(stack)) {
                int eventCost;
                int storage;
                int generation;
                int consumption = cyberware.getEnergyConsumption(stack);
                if (consumption > 0) {
                    tooltip.add(Component.translatable((String)"cyberware.tooltip.powerConsumption", (Object[])new Object[]{consumption}).withStyle(ChatFormatting.RED));
                }
                if ((generation = cyberware.getEnergyGeneration(stack)) > 0) {
                    tooltip.add(Component.translatable((String)"cyberware.tooltip.powerProduction", (Object[])new Object[]{generation}).withStyle(ChatFormatting.GREEN));
                }
                if ((storage = cyberware.getEnergyStorage(stack)) > 0) {
                    tooltip.add(Component.translatable((String)"cyberware.tooltip.capacity", (Object[])new Object[]{storage}).withStyle(ChatFormatting.AQUA));
                }
                if ((eventCost = cyberware.getEventConsumption(stack)) > 0) {
                    tooltip.add(Component.translatable((String)"cyberware.tooltip.eventCost", (Object[])new Object[]{eventCost}).withStyle(ChatFormatting.RED));
                }
            }
            if (cyberware.getMaxInstallAmount(stack) > 1) {
                tooltip.add(Component.translatable((String)"cyberware.tooltip.maxInstall", (Object[])new Object[]{cyberware.getMaxInstallAmount(stack)}).withStyle(ChatFormatting.BLUE));
            }
            tooltip.add(Component.translatable((String)"cyberware.tooltip.essence", (Object[])new Object[]{cyberware.getEssenceCost(stack)}).withStyle(ChatFormatting.DARK_PURPLE));
            Set<Item> reqs = cyberware.getPrerequisites(stack);
            if (!reqs.isEmpty()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable((String)"cyberware.tooltip.requires").withStyle(ChatFormatting.AQUA));
                for (Item req : reqs) {
                    tooltip.add(Component.literal((String)" - ").append(req.getName(new ItemStack((ItemLike)req))).withStyle(ChatFormatting.GRAY));
                }
            }
            if (!(incompatibles = cyberware.getIncompatibleItems(stack)).isEmpty()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable((String)"cyberware.tooltip.incompatible").withStyle(ChatFormatting.RED));
                for (Item incompatible : incompatibles) {
                    tooltip.add(Component.literal((String)" - ").append(incompatible.getName(new ItemStack((ItemLike)incompatible))).withStyle(ChatFormatting.GRAY));
                }
            }
            if ((slotType = CyberwareSlotType.fromId(cyberware.getSlot(stack))) != null) {
                tooltip.add(Component.translatable((String)"cyberware.tooltip.slot", (Object[])new Object[]{slotType.getDisplayName()}).withStyle(ChatFormatting.GRAY));
            }
            tooltip.add(Component.translatable((String)(cyberware.isPristine(stack) ? "cyberware.quality.manufactured" : "cyberware.quality.scavenged")).withStyle(cyberware.isPristine(stack) ? ChatFormatting.AQUA : ChatFormatting.RED));
        }
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPacketHandler.reset();
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        ICyberware cw;
        ItemStack actuatorStack;
        CyberwareUserData data;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (KeyInit.MENU_KEY.consumeClick()) {
            data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            if (data.isCyberwareInstalled((Item)ModItems.CYBER_EYE.get())) {
                if (mc.screen == null) {
                    mc.setScreen((Screen)new CyberwareMenuScreen());
                }
            } else {
                player.displayClientMessage((Component)Component.translatable((String)"message.cyber_ware_port.no_hud_installed"), true);
            }
        }
        if (!(event.getKey() != mc.options.keyJump.getKey().getValue() || event.getAction() != 1 || player.onGround() || player.isCreative() || player.isSpectator() || (actuatorStack = ForgeClientEvents.getInstalledStack(data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get()), (Item)ModItems.LINEAR_ACTUATORS.get())).isEmpty() || (cw = CyberwareAPI.getCyberware(actuatorStack)) == null || !cw.isActive(actuatorStack) || player.getPersistentData().getBoolean(NBT_DOUBLE_JUMPED))) {
            PacketDistributor.sendToServer((CustomPacketPayload)new DoubleJumpPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    private static ItemStack getInstalledStack(CyberwareUserData data, Item item) {
        ItemStackHandler handler = data.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.is(item)) continue;
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PlayerModel model = (PlayerModel)event.getRenderer().getModel();
        model.leftSleeve.visible = true;
        model.leftArm.visible = true;
        model.rightSleeve.visible = true;
        model.rightArm.visible = true;
        model.leftPants.visible = true;
        model.leftLeg.visible = true;
        model.rightPants.visible = true;
        model.rightLeg.visible = true;
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (ForgeClientEvents.hasSkinUpgrade(data)) {
            return;
        }
        if (!data.isCyberwareInstalled((Item)ModItems.HUMAN_LEFT_ARM.get())) {
            model.leftArm.visible = false;
            model.leftSleeve.visible = false;
        }
        if (!data.isCyberwareInstalled((Item)ModItems.HUMAN_RIGHT_ARM.get())) {
            model.rightArm.visible = false;
            model.rightSleeve.visible = false;
        }
        if (!data.isCyberwareInstalled((Item)ModItems.HUMAN_LEFT_LEG.get())) {
            model.leftLeg.visible = false;
            model.leftPants.visible = false;
        }
        if (!data.isCyberwareInstalled((Item)ModItems.HUMAN_RIGHT_LEG.get())) {
            model.rightLeg.visible = false;
            model.rightPants.visible = false;
        }
    }

    private static boolean hasSkinUpgrade(CyberwareUserData data) {
        if (data.isCyberwareInstalled((Item)ModItems.SYNTHETIC_SKIN.get())) {
            return true;
        }
        ItemStackHandler handler = data.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            ItemStack stack = handler.getStackInSlot(i);
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (cw == null || cw.getBodyPartType(stack) != BodyPartType.SKIN) continue;
            return true;
        }
        return false;
    }
}

