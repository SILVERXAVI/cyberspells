/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
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
 *  net.neoforged.neoforge.client.event.InputEvent$MouseButton$Pre
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFov
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.custom.CyberwareToggleWheelScreen;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class OpticZoomModuleItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK = 2;
    private static final int[] LEVELS = new int[]{5, 15, 25};
    private static final Map<UUID, Integer> CLIENT_LEVEL = new ConcurrentHashMap<UUID, Integer>();

    public OpticZoomModuleItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.eyeupgrades_zoom.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<Item> requiresCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.BASECYBERWARE_CYBEREYES.get());
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.EYES);
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
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.isEnabledByWheel(player) ? 2 : 0;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public void onTick(Player player) {
    }

    private static int getLevelIndex(Player player) {
        if (player == null) {
            return 0;
        }
        return Mth.clamp((int)CLIENT_LEVEL.getOrDefault(player.getUUID(), 0), (int)0, (int)(LEVELS.length - 1));
    }

    private static void setLevelIndex(Player player, int idx) {
        if (player == null) {
            return;
        }
        CLIENT_LEVEL.put(player.getUUID(), Mth.clamp((int)idx, (int)0, (int)(LEVELS.length - 1)));
    }

    private static int getZoomFactor(Player player) {
        return LEVELS[OpticZoomModuleItem.getLevelIndex(player)];
    }

    private static double getFovMultiplier(Player player) {
        return 1.0 / (double)OpticZoomModuleItem.getZoomFactor(player);
    }

    private static boolean isZoomEnabledForPlayer(Player player) {
        if (player == null) {
            return false;
        }
        Item it = (Item)ModItems.EYEUPGRADES_ZOOM.get();
        if (!(it instanceof ICyberwareItem)) {
            return false;
        }
        ICyberwareItem cw = (ICyberwareItem)it;
        return cw.isEnabledByWheel(player);
    }

    private static boolean isZoomPoweredForPlayer(Player player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        Item target = (Item)ModItems.EYEUPGRADES_ZOOM.get();
        for (int i = 0; i < CyberwareSlot.EYES.size; ++i) {
            ItemStack st;
            InstalledCyberware cw = data.get(CyberwareSlot.EYES, i);
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != target) continue;
            return cw.isPowered();
        }
        return false;
    }

    private static boolean isZoomActiveForPlayer(Player player) {
        return OpticZoomModuleItem.isZoomEnabledForPlayer(player) && OpticZoomModuleItem.isZoomPoweredForPlayer(player);
    }

    private static void cycleZoomLevel(Player player) {
        int cur = OpticZoomModuleItem.getLevelIndex(player);
        int next = (cur + 1) % LEVELS.length;
        OpticZoomModuleItem.setLevelIndex(player, next);
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == player) {
            player.displayClientMessage((Component)Component.translatable((String)"message.createcybernetics.zoom_level", (Object[])new Object[]{LEVELS[next]}).withStyle(ChatFormatting.AQUA), true);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void onMouseButton(InputEvent.MouseButton.Pre event) {
            if (event.getAction() != 1) {
                return;
            }
            if (event.getButton() != 1) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (CyberwareToggleWheelScreen.isWheelOpen()) {
                return;
            }
            if (mc.screen != null) {
                return;
            }
            if (!OpticZoomModuleItem.isZoomActiveForPlayer((Player)mc.player)) {
                return;
            }
            OpticZoomModuleItem.cycleZoomLevel((Player)mc.player);
            event.setCanceled(true);
        }

        @SubscribeEvent
        public static void onComputeFov(ViewportEvent.ComputeFov event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (!OpticZoomModuleItem.isZoomActiveForPlayer((Player)mc.player)) {
                return;
            }
            event.setFOV(event.getFOV() * OpticZoomModuleItem.getFovMultiplier((Player)mc.player));
        }
    }
}

