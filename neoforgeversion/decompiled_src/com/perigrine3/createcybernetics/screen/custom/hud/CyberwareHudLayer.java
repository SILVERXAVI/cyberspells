/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.math.Axis
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 */
package com.perigrine3.createcybernetics.screen.custom.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.client.HudConfigClient;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.northstar.CopernicusSuitPredicate;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public final class CyberwareHudLayer {
    public static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_hud");
    public static final ResourceLocation CROSSHAIR_LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_hud_crosshair");
    private static final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batteryframe.png");
    private static final ResourceLocation FRAME_EMPTY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batteryframe_empty.png");
    private static final ResourceLocation BARS1 = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batterybars1.png");
    private static final ResourceLocation BARS1_EMPTY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batterybars1_empty.png");
    private static final ResourceLocation BARS2 = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batterybars2.png");
    private static final ResourceLocation BARS2_EMPTY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_batterybars2_empty.png");
    private static final ResourceLocation CENTER_OVERLAY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_overlay.png");
    private static final ResourceLocation CENTER_SPINNER = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud/hud_overlay_circle.png");
    private static final int OVERLAY_W = 2048;
    private static final int OVERLAY_H = 1055;
    private static final int SPINNER_W = 2048;
    private static final int SPINNER_H = 1055;
    private static final float OVERLAY_MAX_SCREEN_FRACTION = 0.95f;
    private static final float OVERLAY_ALPHA = 0.5f;
    private static final boolean OVERLAY_DRAW_BEHIND_BATTERY = true;
    private static final float SPINNER_MAX_SCREEN_FRACTION = 1.25f;
    private static final float SPINNER_ALPHA = 0.1f;
    private static float SPINNER_OFFSET_X_PX = -0.5f;
    private static float SPINNER_OFFSET_Y_PX = -0.5f;
    private static float SPINNER_DEG_PER_SECOND = 10.0f;
    private static final int TEX_W = 13;
    private static final int TEX_H = 25;
    private static final int INNER_X = 1;
    private static final int INNER_Y = 2;
    private static final int INNER_W = 10;
    private static final int INNER_H = 21;
    private static final int HUD_TINT_WHITE_ARGB = -1;
    private static final int HUD_TINT_LOW_RED_ARGB = -43691;
    private static Anchor BATTERY_ANCHOR = Anchor.BOTTOM_RIGHT;
    private static int BATTERY_OFFSET_X_PX = 300;
    private static int BATTERY_OFFSET_Y_PX = 100;
    private static float BATTERY_SCALE_PX = 4.0f;
    private static final float VALUE_SCALE_REL = 0.75f;
    private static final int VALUE_PADDING_PX = 2;
    private static final int VALUE_COLOR = 0xFFFFFF;
    private static final int VALUE_COLOR_LOW = 0xFF5555;
    private static final boolean VALUE_SHADOW = true;
    private static final float LOW_THRESHOLD = 0.25f;
    private static final int ENERGY_STATS_LINE_GAP_PX = 1;
    private static final int ENERGY_STATS_EXTRA_PADDING_PX = 2;
    private static Anchor COORDS_ANCHOR = Anchor.TOP_RIGHT;
    private static int COORDS_OFFSET_X_PX = 160;
    private static int COORDS_OFFSET_Y_PX = 120;
    private static final int COORDS_LINE_GAP_PX = 1;
    private static final boolean COORDS_SHADOW = true;
    private static Anchor TOGGLE_ANCHOR = Anchor.TOP_LEFT;
    private static int TOGGLE_OFFSET_X_PX = 140;
    private static int TOGGLE_OFFSET_Y_PX = 130;
    private static final int TOGGLE_ROW_GAP_PX = 2;
    private static final int TOGGLE_ICON_TEXT_GAP_PX = 4;
    private static final boolean TOGGLE_SHADOW = true;
    private static int TOGGLE_MAX_ROWS = 16;
    private static final Component ENABLED_TXT = Component.literal((String)"ENABLED");
    private static final Component DISABLED_TXT = Component.literal((String)"DISABLED");
    private static final int TOGGLE_ENABLED_COLOR = 0x55FF55;
    private static Anchor SHARDS_ANCHOR = Anchor.BOTTOM_LEFT;
    private static int SHARDS_OFFSET_X_PX = 270;
    private static int SHARDS_OFFSET_Y_PX = 90;
    private static final int SHARDS_ICON_GAP_PX = 0;
    private static float SHARDS_SCALE_REL = 1.75f;
    private static Anchor TARGET_ANCHOR = Anchor.CENTER;
    private static int TARGET_OFFSET_X_PX = 0;
    private static final int TARGET_OFFSET_Y_ABOVE_HOTBAR_PX = 250;
    private static final int TARGET_OFFSET_Y_UNDER_CROSSHAIR_PX = 50;
    private static final boolean TARGET_SHADOW = true;
    public static final int COPERNICUS_OXYGEN_MAX_DISPLAY = 3000;
    private static final int OXYGEN_TEXT_COLOR = 0xFFFFFF;
    private static final int OXYGEN_TEXT_COLOR_LOW = 0xFF5555;
    private static final boolean OXYGEN_TEXT_SHADOW = true;
    private static final float OXYGEN_LOW_THRESHOLD = 0.25f;

    private CyberwareHudLayer() {
    }

    private static int anchoredX(int screenW, int widgetW, Anchor anchor, int offsetX) {
        return switch (anchor.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0, 2 -> offsetX;
            case 1, 3 -> screenW - widgetW - offsetX;
            case 4, 5, 6 -> screenW / 2 - widgetW / 2 + offsetX;
        };
    }

    private static int anchoredY(int screenH, int widgetH, Anchor anchor, int offsetY) {
        return switch (anchor.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0, 1, 4 -> offsetY;
            case 2, 3, 5 -> screenH - widgetH - offsetY;
            case 6 -> screenH / 2 - widgetH / 2 + offsetY;
        };
    }

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, LAYER_ID, CyberwareHudLayer::renderHud);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, CROSSHAIR_LAYER_ID, CyberwareHudLayer::renderCrosshairOverlay);
    }

    public static HudWidgetRects computeRectsForConfig(Minecraft mc, HudConfigClient.HudConfig cfg) {
        float hudTextScale;
        int screenPxW = mc.getWindow().getScreenWidth();
        int screenPxH = mc.getWindow().getScreenHeight();
        float hudIconScale = hudTextScale = BATTERY_SCALE_PX * 0.75f;
        int scaledBatteryW = Math.round(13.0f * BATTERY_SCALE_PX);
        int scaledBatteryH = Math.round(25.0f * BATTERY_SCALE_PX);
        int batteryX = CyberwareHudLayer.anchoredX(screenPxW, scaledBatteryW, BATTERY_ANCHOR, BATTERY_OFFSET_X_PX);
        int batteryY = CyberwareHudLayer.anchoredY(screenPxH, scaledBatteryH, BATTERY_ANCHOR, BATTERY_OFFSET_Y_PX);
        int batteryBlockX = batteryX;
        int batteryBlockY = batteryY;
        int batteryBlockW = scaledBatteryW;
        int batteryBlockH = scaledBatteryH;
        int approxCoordsW = Math.round(180.0f * hudTextScale);
        Objects.requireNonNull(mc.font);
        int approxCoordsH = Math.round(9.0f * hudTextScale * 2.0f) + 1;
        int coordsX = CyberwareHudLayer.anchoredX(screenPxW, approxCoordsW, COORDS_ANCHOR, COORDS_OFFSET_X_PX);
        int coordsY = CyberwareHudLayer.anchoredY(screenPxH, approxCoordsH, COORDS_ANCHOR, COORDS_OFFSET_Y_PX);
        int iconPx = Math.round(16.0f * hudIconScale);
        Objects.requireNonNull(mc.font);
        int lineH = Math.round(9.0f * hudTextScale);
        int rowH = Math.max(iconPx, lineH);
        int rows = Math.max(1, Math.min(TOGGLE_MAX_ROWS, 10));
        int enabledW = Math.round((float)mc.font.width(ENABLED_TXT.getString()) * hudTextScale);
        int disabledW = Math.round((float)mc.font.width(DISABLED_TXT.getString()) * hudTextScale);
        int statusW = Math.max(enabledW, disabledW);
        int toggleW = iconPx + 4 + statusW;
        int toggleH = rows * rowH + Math.max(0, rows - 1) * 2;
        int toggleX = CyberwareHudLayer.anchoredX(screenPxW, toggleW, TOGGLE_ANCHOR, TOGGLE_OFFSET_X_PX);
        int toggleY = CyberwareHudLayer.anchoredY(screenPxH, toggleH, TOGGLE_ANCHOR, TOGGLE_OFFSET_Y_PX);
        float shardScale = hudIconScale * SHARDS_SCALE_REL;
        int shardIconPx = Math.round(16.0f * shardScale);
        int shardsW = 2 * shardIconPx + 0;
        int shardsH = shardIconPx;
        int shardsX = CyberwareHudLayer.anchoredX(screenPxW, shardsW, SHARDS_ANCHOR, SHARDS_OFFSET_X_PX);
        int shardsY = CyberwareHudLayer.anchoredY(screenPxH, shardsH, SHARDS_ANCHOR, SHARDS_OFFSET_Y_PX);
        int targetOffsetY = switch (cfg.targetMode) {
            default -> throw new MatchException(null, null);
            case HudConfigClient.TargetMode.ABOVE_HOTBAR -> 250;
            case HudConfigClient.TargetMode.UNDER_CROSSHAIR -> 50;
            case HudConfigClient.TargetMode.OFF -> 250;
        };
        int approxTargetW = Math.round(140.0f * hudTextScale);
        Objects.requireNonNull(mc.font);
        int approxTargetH = Math.round(9.0f * hudTextScale);
        int targetX = CyberwareHudLayer.anchoredX(screenPxW, approxTargetW, TARGET_ANCHOR, TARGET_OFFSET_X_PX);
        int targetY = CyberwareHudLayer.anchoredY(screenPxH, approxTargetH, TARGET_ANCHOR, targetOffsetY);
        if (cfg.batteryMode == HudConfigClient.BatteryMode.TEXT_ONLY) {
            int textW = Math.round((float)mc.font.width("9999/9999") * hudTextScale);
            Objects.requireNonNull(mc.font);
            int textH = Math.round(9.0f * hudTextScale);
            batteryBlockW = textW;
            batteryBlockH = textH;
            batteryBlockX = batteryX + scaledBatteryW / 2 - textW / 2;
            batteryBlockY = batteryY - 2 - textH;
        }
        return new HudWidgetRects(batteryBlockX, batteryBlockY, batteryBlockW, batteryBlockH, coordsX, coordsY, approxCoordsW, approxCoordsH, toggleX, toggleY, toggleW, toggleH, shardsX, shardsY, shardsW, shardsH, targetX, targetY, approxTargetW, approxTargetH);
    }

    public static void renderHudPreview(GuiGraphics gg, float partialTick, HudConfigClient.HudConfig cfg) {
        float hudTextScale;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        double guiScale = mc.getWindow().getGuiScale();
        int screenPxW = mc.getWindow().getScreenWidth();
        int screenPxH = mc.getWindow().getScreenHeight();
        float hudIconScale = hudTextScale = BATTERY_SCALE_PX * 0.75f;
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        int hudTintArgb = CyberwareHudLayer.resolveHudTintArgb(player);
        int current = data != null ? data.getEnergyStored() : 0;
        int capacity = data != null ? data.getTotalEnergyCapacity((Player)player) : 0;
        TickSnapshot snap = ClientEnergyState.getSnapshot();
        EnergyRates localRates = data != null ? CyberwareHudLayer.computeClientEnergyRates(player, data) : EnergyRates.ZERO;
        int genPerTick = localRates.generatedPerTick;
        int usePerTick = localRates.requiredPerTick;
        if (snap != null) {
            boolean snapMeaningful;
            boolean bl = snapMeaningful = snap.generatedPerTick() != 0 || snap.consumedPerTick() != 0;
            if (snapMeaningful) {
                genPerTick = snap.generatedPerTick();
                usePerTick = snap.consumedPerTick();
            }
        }
        int netPerTick = genPerTick - usePerTick;
        int capForPct = Math.max(1, capacity);
        float pct = Mth.clamp((float)((float)current / (float)capForPct), (float)0.0f, (float)1.0f);
        boolean low = pct <= 0.25f;
        int batteryTintArgb = low ? -43691 : hudTintArgb;
        gg.pose().pushPose();
        gg.pose().scale((float)(1.0 / guiScale), (float)(1.0 / guiScale), 1.0f);
        CyberwareHudLayer.renderCenteredImageAutoFitTintedPixels(gg, CENTER_OVERLAY, 2048, 1055, screenPxW, screenPxH, 0.95f, 0.5f, hudTintArgb);
        CyberwareHudLayer.renderBatteryWithModePixels(gg, mc, screenPxW, screenPxH, current, capacity, capForPct, genPerTick, usePerTick, netPerTick, low, hudTintArgb, batteryTintArgb, hudTextScale, cfg.batteryMode);
        CyberwareHudLayer.renderCopernicusOxygenIndicatorTintedPixels(gg, mc, player, screenPxW, screenPxH, hudTintArgb, hudTextScale);
        if (cfg.coordsEnabled) {
            CyberwareHudLayer.renderCoordsAndBiomePixels(gg, mc, player, screenPxW, screenPxH, hudTintArgb, hudTextScale);
        }
        if (cfg.toggleListEnabled) {
            CyberwareHudLayer.renderToggleListPixels(gg, mc, player, screenPxW, screenPxH, hudTintArgb, hudTextScale, hudIconScale);
        }
        if (cfg.shardsEnabled) {
            CyberwareHudLayer.renderChipwareShardsPixels(gg, player, screenPxW, screenPxH, hudIconScale);
        }
        if (cfg.targetMode != HudConfigClient.TargetMode.OFF) {
            int targetOffsetY = cfg.targetMode == HudConfigClient.TargetMode.UNDER_CROSSHAIR ? 50 : 250;
            CyberwareHudLayer.renderTargetNamePixels(gg, mc, player, screenPxW, screenPxH, hudTintArgb, hudTextScale, targetOffsetY);
        }
        gg.pose().popPose();
    }

    public static void renderCrosshairPreview(GuiGraphics gg, float partialTick, HudConfigClient.HudConfig cfg) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        double guiScale = mc.getWindow().getGuiScale();
        int screenPxW = mc.getWindow().getScreenWidth();
        int screenPxH = mc.getWindow().getScreenHeight();
        int hudTintArgb = CyberwareHudLayer.resolveHudTintArgb(player);
        gg.pose().pushPose();
        gg.pose().scale((float)(1.0 / guiScale), (float)(1.0 / guiScale), 1.0f);
        CyberwareHudLayer.renderSpinningCenteredImageAutoFitTintedPixels(gg, CENTER_SPINNER, 2048, 1055, screenPxW, screenPxH, 1.25f, 0.1f, player.tickCount, partialTick, SPINNER_DEG_PER_SECOND, SPINNER_OFFSET_X_PX, SPINNER_OFFSET_Y_PX, hudTintArgb);
        gg.pose().popPose();
    }

    private static void renderHud(GuiGraphics gg, DeltaTracker delta) {
        float partialTick;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }
        if (!CyberwareInstallQueries.hasHudAccess(player)) {
            return;
        }
        HudConfigClient.HudConfig cfg = HudConfigClient.get(player.getUUID());
        try {
            partialTick = delta.getGameTimeDeltaPartialTick(true);
        }
        catch (Throwable ignored) {
            partialTick = 0.0f;
        }
        CyberwareHudLayer.renderHudPreview(gg, partialTick, cfg);
    }

    private static void renderCrosshairOverlay(GuiGraphics gg, DeltaTracker delta) {
        float partialTick;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.options.hideGui) {
            return;
        }
        if (!mc.options.getCameraType().isFirstPerson()) {
            return;
        }
        if (!CyberwareInstallQueries.hasHudAccess(player)) {
            return;
        }
        HudConfigClient.HudConfig cfg = HudConfigClient.get(player.getUUID());
        try {
            partialTick = delta.getGameTimeDeltaPartialTick(true);
        }
        catch (Throwable ignored) {
            partialTick = 0.0f;
        }
        CyberwareHudLayer.renderCrosshairPreview(gg, partialTick, cfg);
    }

    private static EnergyRates computeClientEnergyRates(LocalPlayer player, PlayerCyberwareData data) {
        int generated = 0;
        int required = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                int act;
                int use;
                Item item;
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item = stack.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem item2 = (ICyberwareItem)item;
                int gen = item2.getEnergyGeneratedPerTick((Player)player, stack, slot);
                if (gen > 0) {
                    generated += gen;
                }
                if ((use = item2.getEnergyUsedPerTick((Player)player, stack, slot)) > 0) {
                    required += use;
                }
                if (!item2.shouldConsumeActivationEnergyThisTick((Player)player, stack, slot) || (act = item2.getEnergyActivationCost((Player)player, stack, slot)) <= 0) continue;
                required += act;
            }
        }
        return new EnergyRates(generated, required);
    }

    private static void renderBatteryWithModePixels(GuiGraphics gg, Minecraft mc, int screenPxW, int screenPxH, int current, int capacity, int capForPct, int genPerTick, int usePerTick, int netPerTick, boolean low, int hudTintArgb, int batteryTintArgb, float hudTextScale, HudConfigClient.BatteryMode mode) {
        int scaledW = Math.round(13.0f * BATTERY_SCALE_PX);
        int scaledH = Math.round(25.0f * BATTERY_SCALE_PX);
        int x = CyberwareHudLayer.anchoredX(screenPxW, scaledW, BATTERY_ANCHOR, BATTERY_OFFSET_X_PX);
        int y = CyberwareHudLayer.anchoredY(screenPxH, scaledH, BATTERY_ANCHOR, BATTERY_OFFSET_Y_PX);
        boolean drawIcon = mode != HudConfigClient.BatteryMode.TEXT_ONLY;
        boolean drawCapacityText = mode == HudConfigClient.BatteryMode.TEXT_ONLY || mode == HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY || mode == HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
        boolean drawStats = mode == HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
        int valueTopY = y;
        if (drawCapacityText) {
            valueTopY = CyberwareHudLayer.renderEnergyValueAboveBatteryPixels(gg, mc, current, capacity, x, y, scaledW, low, hudTextScale);
        }
        if (drawStats && drawCapacityText) {
            CyberwareHudLayer.renderEnergyStatsPixels(gg, mc, genPerTick, usePerTick, netPerTick, x, valueTopY, scaledW, low, hudTintArgb, hudTextScale);
        }
        if (drawIcon) {
            CyberwareHudLayer.renderBatteryScaledPixels(gg, x, y, current, capForPct, 0, low, batteryTintArgb, BATTERY_SCALE_PX);
        }
    }

    private static int renderEnergyValueAboveBatteryPixels(GuiGraphics gg, Minecraft mc, int current, int capacity, int batteryX, int batteryY, int scaledBatteryW, boolean low, float valueScale) {
        String text = current + "/" + capacity;
        Objects.requireNonNull(mc.font);
        int scaledTextH = Math.round(9.0f * valueScale);
        int textY = batteryY - 2 - scaledTextH;
        int scaledTextW = Math.round((float)mc.font.width(text) * valueScale);
        int textX = batteryX + scaledBatteryW / 2 - scaledTextW / 2;
        int color = low ? 0xFF5555 : 0xFFFFFF;
        gg.pose().pushPose();
        gg.pose().translate((float)textX, (float)textY, 0.0f);
        gg.pose().scale(valueScale, valueScale, 1.0f);
        gg.drawString(mc.font, text, 0, 0, color, true);
        gg.pose().popPose();
        return textY;
    }

    private static void renderEnergyStatsPixels(GuiGraphics gg, Minecraft mc, int genPerTick, int usePerTick, int netPerTick, int batteryX, int valueTopY, int scaledBatteryW, boolean low, int hudTintArgb, float statsScale) {
        int rgbTint;
        String genText = "GEN: +" + Math.max(0, genPerTick);
        String useText = "USE: -" + Math.max(0, usePerTick);
        int color = low ? 0xFF5555 : ((rgbTint = hudTintArgb & 0xFFFFFF) != 0 ? rgbTint : 0xFFFFFF);
        int genW = Math.round((float)mc.font.width(genText) * statsScale);
        int useW = Math.round((float)mc.font.width(useText) * statsScale);
        Objects.requireNonNull(mc.font);
        int lineH = Math.round(9.0f * statsScale);
        int gap = 1;
        int blockH = lineH * 2 + gap;
        int baseY = valueTopY - 2 - blockH;
        int genX = batteryX + scaledBatteryW / 2 - genW / 2;
        int useX = batteryX + scaledBatteryW / 2 - useW / 2;
        gg.pose().pushPose();
        gg.pose().translate((float)genX, (float)baseY, 0.0f);
        gg.pose().scale(statsScale, statsScale, 1.0f);
        gg.drawString(mc.font, genText, 0, 0, color, true);
        gg.pose().popPose();
        gg.pose().pushPose();
        gg.pose().translate((float)useX, (float)(baseY + lineH + gap), 0.0f);
        gg.pose().scale(statsScale, statsScale, 1.0f);
        gg.drawString(mc.font, useText, 0, 0, color, true);
        gg.pose().popPose();
    }

    private static void renderBatteryScaledPixels(GuiGraphics gg, int x, int y, int currentPower, int maxPower, int netPowerPerTick, boolean low, int tintArgb, float batteryScalePx) {
        gg.pose().pushPose();
        gg.pose().translate((float)x, (float)y, 0.0f);
        gg.pose().scale(batteryScalePx, batteryScalePx, 1.0f);
        CyberwareHudLayer.renderBatteryTinted(gg, 0, 0, currentPower, maxPower, netPowerPerTick, low, tintArgb);
        gg.pose().popPose();
    }

    private static void renderBatteryTinted(GuiGraphics gg, int x, int y, int currentPower, int maxPower, int netPowerPerTick, boolean low, int tintArgb) {
        ResourceLocation bars2;
        float pct = maxPower <= 0 ? 0.0f : (float)currentPower / (float)maxPower;
        pct = Mth.clamp((float)pct, (float)0.0f, (float)1.0f);
        int fillPx = Math.round(pct * 21.0f);
        fillPx = Mth.clamp((int)fillPx, (int)0, (int)21);
        int usedPx = 21 - fillPx;
        ResourceLocation frame = low ? FRAME_EMPTY : FRAME;
        ResourceLocation bars1 = low ? BARS1_EMPTY : BARS1;
        ResourceLocation resourceLocation = bars2 = low ? BARS2_EMPTY : BARS2;
        if (fillPx > 0) {
            int dstX = x + 1;
            int dstY = y + 2 + usedPx;
            int srcU = 1;
            int srcV = 2 + usedPx;
            CyberwareHudLayer.blitTinted(gg, bars2, dstX, dstY, srcU, srcV, 10, fillPx, 13, 25, tintArgb);
            CyberwareHudLayer.blitTinted(gg, bars1, dstX, dstY, srcU, srcV, 10, fillPx, 13, 25, tintArgb);
        }
        CyberwareHudLayer.blitTinted(gg, frame, x, y, 0, 0, 13, 25, 13, 25, tintArgb);
    }

    private static void renderCoordsAndBiomePixels(GuiGraphics gg, Minecraft mc, LocalPlayer player, int screenPxW, int screenPxH, int hudTintArgb, float hudTextScale) {
        BlockPos pos = player.blockPosition();
        String coords = "X: " + pos.getX() + "  Y: " + pos.getY() + "  Z: " + pos.getZ();
        Component biomeComp = CyberwareHudLayer.biomeDisplayName(player, pos);
        String biomeLine = "Biome: " + biomeComp.getString();
        int rgbTint = hudTintArgb & 0xFFFFFF;
        int color = rgbTint != 0 ? rgbTint : 0xFFFFFF;
        Objects.requireNonNull(mc.font);
        int lineH = Math.round(9.0f * hudTextScale);
        int gap = 1;
        int w1 = Math.round((float)mc.font.width(coords) * hudTextScale);
        int w2 = Math.round((float)mc.font.width(biomeLine) * hudTextScale);
        int blockW = Math.max(w1, w2);
        int blockH = lineH * 2 + gap;
        int x = CyberwareHudLayer.anchoredX(screenPxW, blockW, COORDS_ANCHOR, COORDS_OFFSET_X_PX);
        int y = CyberwareHudLayer.anchoredY(screenPxH, blockH, COORDS_ANCHOR, COORDS_OFFSET_Y_PX);
        gg.pose().pushPose();
        gg.pose().translate((float)x, (float)y, 0.0f);
        gg.pose().scale(hudTextScale, hudTextScale, 1.0f);
        gg.drawString(mc.font, coords, 0, 0, color, true);
        gg.pose().popPose();
        gg.pose().pushPose();
        gg.pose().translate((float)x, (float)(y + lineH + gap), 0.0f);
        gg.pose().scale(hudTextScale, hudTextScale, 1.0f);
        gg.drawString(mc.font, biomeLine, 0, 0, color, true);
        gg.pose().popPose();
    }

    private static Component biomeDisplayName(LocalPlayer player, BlockPos pos) {
        try {
            Holder biomeHolder = player.level().getBiome(pos);
            ResourceKey key = biomeHolder.unwrapKey().orElse(null);
            if (key == null) {
                return Component.literal((String)"Unknown");
            }
            ResourceLocation id = key.location();
            return Component.translatable((String)("biome." + id.getNamespace() + "." + id.getPath()));
        }
        catch (Throwable t) {
            return Component.literal((String)"Unknown");
        }
    }

    private static void renderToggleListPixels(GuiGraphics gg, Minecraft mc, LocalPlayer player, int screenPxW, int screenPxH, int hudTintArgb, float hudTextScale, float hudIconScale) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        List<ToggleEntry> entries = CyberwareHudLayer.collectToggleEntries(data);
        if (entries.isEmpty()) {
            return;
        }
        int rows = Math.min(TOGGLE_MAX_ROWS, entries.size());
        int iconPx = Math.round(16.0f * hudIconScale);
        Objects.requireNonNull(mc.font);
        int lineH = Math.round(9.0f * hudTextScale);
        int rowH = Math.max(iconPx, lineH);
        int enabledW = Math.round((float)mc.font.width(ENABLED_TXT.getString()) * hudTextScale);
        int disabledW = Math.round((float)mc.font.width(DISABLED_TXT.getString()) * hudTextScale);
        int statusW = Math.max(enabledW, disabledW);
        int maxRowW = iconPx + 4 + statusW;
        int blockH = rows * rowH + Math.max(0, rows - 1) * 2;
        int x0 = CyberwareHudLayer.anchoredX(screenPxW, maxRowW, TOGGLE_ANCHOR, TOGGLE_OFFSET_X_PX);
        int y0 = CyberwareHudLayer.anchoredY(screenPxH, blockH, TOGGLE_ANCHOR, TOGGLE_OFFSET_Y_PX);
        int rgbTint = hudTintArgb & 0xFFFFFF;
        int disabledColor = rgbTint != 0 ? rgbTint : 0xFFFFFF;
        for (int i = 0; i < rows; ++i) {
            ToggleEntry e = entries.get(i);
            int rowY = y0 + i * (rowH + 2);
            gg.pose().pushPose();
            gg.pose().translate((float)x0, (float)(rowY + (rowH - iconPx) / 2), 0.0f);
            gg.pose().scale(hudIconScale, hudIconScale, 1.0f);
            gg.renderItem(e.stack, 0, 0);
            gg.pose().popPose();
            boolean enabled = e.enabled;
            String line = enabled ? ENABLED_TXT.getString() : DISABLED_TXT.getString();
            int textX = x0 + iconPx + 4;
            int textY = rowY + (rowH - lineH) / 2;
            int color = enabled ? 0x55FF55 : disabledColor;
            gg.pose().pushPose();
            gg.pose().translate((float)textX, (float)textY, 0.0f);
            gg.pose().scale(hudTextScale, hudTextScale, 1.0f);
            gg.drawString(mc.font, line, 0, 0, color, true);
            gg.pose().popPose();
        }
    }

    private static List<ToggleEntry> collectToggleEntries(PlayerCyberwareData data) {
        ArrayList<ToggleEntry> out = new ArrayList<ToggleEntry>();
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) continue;
                boolean enabled = data.isEnabled(slot, idx);
                out.add(new ToggleEntry(stack.copy(), enabled));
            }
        }
        return out;
    }

    private static void renderChipwareShardsPixels(GuiGraphics gg, LocalPlayer player, int screenPxW, int screenPxH, float hudIconScale) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        ItemStack s0 = data.getChipwareStack(0);
        ItemStack s1 = data.getChipwareStack(1);
        if (s0.isEmpty() && s1.isEmpty()) {
            return;
        }
        float shardScale = hudIconScale * SHARDS_SCALE_REL;
        int iconPx = Math.round(16.0f * shardScale);
        int count = 0;
        if (!s0.isEmpty()) {
            ++count;
        }
        if (!s1.isEmpty()) {
            ++count;
        }
        int blockW = count * iconPx + Math.max(0, count - 1) * 0;
        int blockH = iconPx;
        int x0 = CyberwareHudLayer.anchoredX(screenPxW, blockW, SHARDS_ANCHOR, SHARDS_OFFSET_X_PX);
        int y0 = CyberwareHudLayer.anchoredY(screenPxH, blockH, SHARDS_ANCHOR, SHARDS_OFFSET_Y_PX);
        int x = x0;
        if (!s0.isEmpty()) {
            gg.pose().pushPose();
            gg.pose().translate((float)x, (float)y0, 0.0f);
            gg.pose().scale(shardScale, shardScale, 1.0f);
            gg.renderItem(s0, 0, 0);
            gg.pose().popPose();
            x += iconPx + 0;
        }
        if (!s1.isEmpty()) {
            gg.pose().pushPose();
            gg.pose().translate((float)x, (float)y0, 0.0f);
            gg.pose().scale(shardScale, shardScale, 1.0f);
            gg.renderItem(s1, 0, 0);
            gg.pose().popPose();
        }
    }

    private static void renderTargetNamePixels(GuiGraphics gg, Minecraft mc, LocalPlayer player, int screenPxW, int screenPxH, int hudTintArgb, float hudTextScale, int targetOffsetYPx) {
        HitResult hr = mc.hitResult;
        if (hr == null || hr.getType() == HitResult.Type.MISS) {
            return;
        }
        Component name = null;
        if (hr.getType() == HitResult.Type.ENTITY && hr instanceof EntityHitResult) {
            EntityHitResult ehr = (EntityHitResult)hr;
            Entity e = ehr.getEntity();
            if (e instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity)e;
                name = itemEntity.getItem().getHoverName();
            } else {
                name = e.getDisplayName();
            }
        } else if (hr.getType() == HitResult.Type.BLOCK && hr instanceof BlockHitResult) {
            BlockHitResult bhr = (BlockHitResult)hr;
            BlockPos pos = bhr.getBlockPos();
            try {
                name = player.level().getBlockState(pos).getBlock().getName();
            }
            catch (Throwable ignored) {
                name = null;
            }
        }
        if (name == null) {
            return;
        }
        String text = name.getString();
        if (text.isBlank()) {
            return;
        }
        int rgbTint = hudTintArgb & 0xFFFFFF;
        int color = rgbTint != 0 ? rgbTint : 0xFFFFFF;
        int w = Math.round((float)mc.font.width(text) * hudTextScale);
        Objects.requireNonNull(mc.font);
        int h = Math.round(9.0f * hudTextScale);
        int x = CyberwareHudLayer.anchoredX(screenPxW, w, TARGET_ANCHOR, TARGET_OFFSET_X_PX);
        int y = CyberwareHudLayer.anchoredY(screenPxH, h, TARGET_ANCHOR, targetOffsetYPx);
        gg.pose().pushPose();
        gg.pose().translate((float)x, (float)y, 0.0f);
        gg.pose().scale(hudTextScale, hudTextScale, 1.0f);
        gg.drawString(mc.font, text, 0, 0, color, true);
        gg.pose().popPose();
    }

    private static void renderCopernicusOxygenIndicatorTintedPixels(GuiGraphics gg, Minecraft mc, LocalPlayer player, int screenPxW, int screenPxH, int hudTintArgb, float hudTextScale) {
        if (!CopernicusSuitPredicate.hasCopernicusSetInstalled((Player)player)) {
            return;
        }
        int oxygen = ClientCopernicusOxygenState.get();
        int max = 3000;
        String text = "OXYGEN: " + oxygen + "/" + max;
        float pct = max <= 0 ? 0.0f : (float)oxygen / (float)max;
        boolean low = pct <= 0.25f;
        int rgbTint = hudTintArgb & 0xFFFFFF;
        int color = low ? 0xFF5555 : (rgbTint != 0 ? rgbTint : 0xFFFFFF);
        int airRightX = screenPxW / 2 + 91;
        int airY = screenPxH - 52;
        int scaledTextW = Math.round((float)mc.font.width(text) * hudTextScale);
        Objects.requireNonNull(mc.font);
        int scaledTextH = Math.round(9.0f * hudTextScale);
        int textX = airRightX - scaledTextW;
        int textY = airY - scaledTextH - 1;
        gg.pose().pushPose();
        gg.pose().translate((float)textX, (float)textY, 0.0f);
        gg.pose().scale(hudTextScale, hudTextScale, 1.0f);
        gg.drawString(mc.font, text, 0, 0, color, true);
        gg.pose().popPose();
    }

    private static int resolveHudTintArgb(LocalPlayer player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return -1;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
            int rgb = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
            return rgb & 0xFFFFFF | 0xFF000000;
        }
        return -1;
    }

    private static int argbWithAlphaFromFloat(int argb, float alpha) {
        int a = Mth.clamp((int)Math.round(alpha * 255.0f), (int)0, (int)255);
        return argb & 0xFFFFFF | a << 24;
    }

    private static void setShaderColorFromArgb(int argb) {
        float a = (float)(argb >>> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >>> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >>> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        RenderSystem.setShaderColor((float)r, (float)g, (float)b, (float)a);
    }

    private static void renderCenteredImageAutoFitTintedPixels(GuiGraphics gg, ResourceLocation tex, int texW, int texH, int screenPxW, int screenPxH, float maxScreenFraction, float alpha, int tintArgb) {
        float sx = (float)screenPxW * maxScreenFraction / (float)texW;
        float sy = (float)screenPxH * maxScreenFraction / (float)texH;
        float scale = Math.min(sx, sy);
        scale = Math.min(scale, 1.0f);
        int drawW = Math.round((float)texW * scale);
        int drawH = Math.round((float)texH * scale);
        int x = (screenPxW - drawW) / 2;
        int y = (screenPxH - drawH) / 2;
        gg.pose().pushPose();
        gg.pose().translate((float)x, (float)y, 0.0f);
        gg.pose().scale(scale, scale, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int argb = CyberwareHudLayer.argbWithAlphaFromFloat(tintArgb, alpha);
        CyberwareHudLayer.setShaderColorFromArgb(argb);
        gg.blit(tex, 0, 0, 0.0f, 0.0f, texW, texH, texW, texH);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        gg.pose().popPose();
    }

    private static void renderSpinningCenteredImageAutoFitTintedPixels(GuiGraphics gg, ResourceLocation tex, int texW, int texH, int screenPxW, int screenPxH, float maxScreenFraction, float alpha, int tickCount, float partialTick, float degPerSecond, float offsetXPx, float offsetYPx, int tintArgb) {
        float sx = (float)screenPxW * maxScreenFraction / (float)texW;
        float sy = (float)screenPxH * maxScreenFraction / (float)texH;
        float scale = Math.min(sx, sy);
        scale = Math.min(scale, 1.0f);
        float timeSeconds = ((float)tickCount + partialTick) / 20.0f;
        float angleDeg = timeSeconds * degPerSecond % 360.0f;
        gg.pose().pushPose();
        gg.pose().translate((float)screenPxW / 2.0f + offsetXPx, (float)screenPxH / 2.0f + offsetYPx, 0.0f);
        gg.pose().mulPose(Axis.ZP.rotationDegrees(angleDeg));
        gg.pose().scale(scale, scale, 1.0f);
        gg.pose().translate((float)(-texW) / 2.0f, (float)(-texH) / 2.0f, 0.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int argb = CyberwareHudLayer.argbWithAlphaFromFloat(tintArgb, alpha);
        CyberwareHudLayer.setShaderColorFromArgb(argb);
        gg.blit(tex, 0, 0, 0.0f, 0.0f, texW, texH, texW, texH);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        gg.pose().popPose();
    }

    private static void blitTinted(GuiGraphics gg, ResourceLocation tex, int x, int y, int u, int v, int w, int h, int texW, int texH, int argb) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        CyberwareHudLayer.setShaderColorFromArgb(argb);
        gg.blit(tex, x, y, (float)u, (float)v, w, h, texW, texH);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
    }

    public static enum Anchor {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_CENTER,
        BOTTOM_CENTER,
        CENTER;

    }

    public record HudWidgetRects(int batteryX, int batteryY, int batteryW, int batteryH, int coordsX, int coordsY, int coordsW, int coordsH, int toggleX, int toggleY, int toggleW, int toggleH, int shardsX, int shardsY, int shardsW, int shardsH, int targetX, int targetY, int targetW, int targetH) {
    }

    public static final class ClientEnergyState {
        private static volatile TickSnapshot LAST = null;

        private ClientEnergyState() {
        }

        public static void update(TickSnapshot snapshot) {
            LAST = snapshot;
        }

        public static TickSnapshot getSnapshot() {
            return LAST;
        }
    }

    public record TickSnapshot(int generatedPerTick, int consumedPerTick, int storedBefore, int storedAfter, int capacity, int netDeltaPerTick) {
    }

    private static final class EnergyRates {
        static final EnergyRates ZERO = new EnergyRates(0, 0);
        final int generatedPerTick;
        final int requiredPerTick;

        EnergyRates(int generatedPerTick, int requiredPerTick) {
            this.generatedPerTick = generatedPerTick;
            this.requiredPerTick = requiredPerTick;
        }
    }

    public static final class CyberwareInstallQueries {
        private CyberwareInstallQueries() {
        }

        public static boolean hasHudAccess(LocalPlayer player) {
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return false;
            }
            return data.hasSpecificItem((Item)ModItems.EYEUPGRADES_HUDLENS.get(), CyberwareSlot.EYES) || data.hasSpecificItem((Item)ModItems.EYEUPGRADES_HUDJACK.get(), CyberwareSlot.EYES);
        }
    }

    private record ToggleEntry(ItemStack stack, boolean enabled) {
    }

    public static final class ClientCopernicusOxygenState {
        private static volatile int LAST = 0;

        private ClientCopernicusOxygenState() {
        }

        public static void set(int oxygen) {
            LAST = Math.max(0, oxygen);
        }

        public static int get() {
            return LAST;
        }
    }
}

