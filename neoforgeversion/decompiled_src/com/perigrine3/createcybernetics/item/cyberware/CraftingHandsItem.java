/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ScreenEvent$Opening
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.network.payload.OpenExpandedInventoryPayload;
import com.perigrine3.createcybernetics.screen.custom.ExpandedInventoryScreen;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class CraftingHandsItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public CraftingHandsItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_crafthands.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 2;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTARM_ITEMS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTARM_ITEMS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.LARM, CyberwareSlot.RARM);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public void onTick(Player player) {
    }

    private static Found findFirst(PlayerCyberwareData data, CyberwareSlot slot) {
        InstalledCyberware[] arr = data.getAll().get((Object)slot);
        if (arr == null) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof CraftingHandsItem)) continue;
            return new Found(slot, i);
        }
        return null;
    }

    public static boolean hasInstalledEitherArm(PlayerCyberwareData data) {
        return CraftingHandsItem.findFirst(data, CyberwareSlot.LARM) != null || CraftingHandsItem.findFirst(data, CyberwareSlot.RARM) != null;
    }

    private record Found(CyberwareSlot slot, int index) {
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientHandler {
        @SubscribeEvent
        public static void onScreenOpening(ScreenEvent.Opening event) {
            if (!(event.getNewScreen() instanceof InventoryScreen)) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (mc.player.isCreative() || mc.player.isSpectator()) {
                return;
            }
            if (event.getCurrentScreen() instanceof ExpandedInventoryScreen) {
                return;
            }
            if (!mc.player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (!CraftingHandsItem.hasInstalledEitherArm(data)) {
                return;
            }
            event.setNewScreen(null);
            PacketDistributor.sendToServer((CustomPacketPayload)new OpenExpandedInventoryPayload(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ServerHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (player.level().isClientSide) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            Found left = CraftingHandsItem.findFirst(data, CyberwareSlot.LARM);
            Found right = CraftingHandsItem.findFirst(data, CyberwareSlot.RARM);
            if (left != null && right != null) {
                ItemStack st;
                InstalledCyberware removed = data.remove(right.slot(), right.index());
                data.setDirty();
                if (removed != null && (st = removed.getItem()) != null && !st.isEmpty()) {
                    ItemStack copy = st.copy();
                    if (!player.getInventory().add(copy)) {
                        player.drop(copy, false);
                    }
                }
            }
        }
    }
}

