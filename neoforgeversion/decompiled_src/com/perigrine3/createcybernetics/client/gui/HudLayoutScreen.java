/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.client.gui;

import com.perigrine3.createcybernetics.client.HudConfigClient;
import com.perigrine3.createcybernetics.screen.custom.hud.CyberwareHudLayer;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class HudLayoutScreen
extends Screen {
    private static final int DIM_OVERLAY = -2013265920;
    private static final int BTN_W = 130;
    private static final int BTN_H = 20;
    private static final int STACK_GAP = 6;
    private static final int TITLE_Y = 12;
    private static final int SUBTITLE_Y = 28;
    private static final int BUTTON_BLOCK_TOP_Y = 44;
    private static final int TOP_GAP_AFTER_SAVE = 10;
    private final Screen parent;
    @Nullable
    private UUID playerId;
    private HudConfigClient.HudConfig working;
    private Button btnBack;
    private Button btnSave;
    private Button btnCoords;
    private Button btnToggleables;
    private Button btnShards;
    private Button btnTarget;
    private Button btnBattery;
    private boolean savedPulse = false;
    private int savedPulseTicks = 0;

    public HudLayoutScreen(Screen parent) {
        super((Component)Component.translatable((String)"screen.createcybernetics.hud_layout"));
        this.parent = parent;
        this.working = new HudConfigClient.HudConfig(true, true, true, HudConfigClient.TargetMode.ABOVE_HOTBAR, HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS);
    }

    protected void init() {
        LocalPlayer p = Minecraft.getInstance().player;
        UUID uUID = this.playerId = p != null ? p.getUUID() : null;
        if (this.playerId != null) {
            this.working = HudConfigClient.get(this.playerId).copy();
        }
        this.rebuildWidgets();
    }

    public void resize(Minecraft mc, int width, int height) {
        super.resize(mc, width, height);
        this.rebuildWidgets();
    }

    protected void rebuildWidgets() {
        this.clearWidgets();
        int cx = this.width / 2;
        int x = cx - 65;
        int y = 44;
        this.btnBack = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)"gui.back"), b -> this.onClose()).pos(x, y).size(130, 20).build());
        this.btnSave = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)"gui.createcybernetics.save"), b -> this.save()).pos(x, y += 26).size(130, 20).build());
        this.btnCoords = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)this.coordsLabel(), b -> this.cycleCoords()).pos(x, y += 30).size(130, 20).build());
        this.btnToggleables = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)this.toggleablesLabel(), b -> this.cycleToggleables()).pos(x, y += 26).size(130, 20).build());
        this.btnShards = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)this.shardsLabel(), b -> this.cycleShards()).pos(x, y += 26).size(130, 20).build());
        this.btnTarget = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)this.targetLabel(), b -> this.cycleTarget()).pos(x, y += 26).size(130, 20).build());
        this.btnBattery = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)this.batteryLabel(), b -> this.cycleBattery()).pos(x, y += 26).size(130, 20).build());
    }

    private void save() {
        if (this.playerId == null) {
            return;
        }
        HudConfigClient.save(this.playerId, this.working.copy());
        this.savedPulse = true;
        this.savedPulseTicks = 20;
    }

    private void cycleCoords() {
        this.working.coordsEnabled = !this.working.coordsEnabled;
        this.refreshButtonLabels();
    }

    private void cycleToggleables() {
        this.working.toggleListEnabled = !this.working.toggleListEnabled;
        this.refreshButtonLabels();
    }

    private void cycleShards() {
        this.working.shardsEnabled = !this.working.shardsEnabled;
        this.refreshButtonLabels();
    }

    private void cycleTarget() {
        this.working.targetMode = switch (this.working.targetMode) {
            default -> throw new MatchException(null, null);
            case HudConfigClient.TargetMode.ABOVE_HOTBAR -> HudConfigClient.TargetMode.UNDER_CROSSHAIR;
            case HudConfigClient.TargetMode.UNDER_CROSSHAIR -> HudConfigClient.TargetMode.OFF;
            case HudConfigClient.TargetMode.OFF -> HudConfigClient.TargetMode.ABOVE_HOTBAR;
        };
        this.refreshButtonLabels();
    }

    private void cycleBattery() {
        this.working.batteryMode = switch (this.working.batteryMode) {
            default -> throw new MatchException(null, null);
            case HudConfigClient.BatteryMode.TEXT_ONLY -> HudConfigClient.BatteryMode.ICON_ONLY;
            case HudConfigClient.BatteryMode.ICON_ONLY -> HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY;
            case HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY -> HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS;
            case HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS -> HudConfigClient.BatteryMode.TEXT_ONLY;
        };
        this.refreshButtonLabels();
    }

    private void refreshButtonLabels() {
        if (this.btnCoords != null) {
            this.btnCoords.setMessage(this.coordsLabel());
        }
        if (this.btnToggleables != null) {
            this.btnToggleables.setMessage(this.toggleablesLabel());
        }
        if (this.btnShards != null) {
            this.btnShards.setMessage(this.shardsLabel());
        }
        if (this.btnTarget != null) {
            this.btnTarget.setMessage(this.targetLabel());
        }
        if (this.btnBattery != null) {
            this.btnBattery.setMessage(this.batteryLabel());
        }
    }

    private Component coordsLabel() {
        return Component.literal((String)("COORDINATES: " + (this.working.coordsEnabled ? "ON" : "OFF")));
    }

    private Component toggleablesLabel() {
        return Component.literal((String)("TOGGLEABLES: " + (this.working.toggleListEnabled ? "ON" : "OFF")));
    }

    private Component shardsLabel() {
        return Component.literal((String)("CHIPWARE: " + (this.working.shardsEnabled ? "ON" : "OFF")));
    }

    private Component targetLabel() {
        return Component.literal((String)("TARGET: " + (switch (this.working.targetMode) {
            default -> throw new MatchException(null, null);
            case HudConfigClient.TargetMode.ABOVE_HOTBAR -> "ABOVE HOTBAR";
            case HudConfigClient.TargetMode.UNDER_CROSSHAIR -> "UNDER CROSSHAIR";
            case HudConfigClient.TargetMode.OFF -> "OFF";
        })));
    }

    private Component batteryLabel() {
        return Component.literal((String)("BATTERY: " + (switch (this.working.batteryMode) {
            default -> throw new MatchException(null, null);
            case HudConfigClient.BatteryMode.TEXT_ONLY -> "CAPACITY ONLY";
            case HudConfigClient.BatteryMode.ICON_ONLY -> "ICON ONLY";
            case HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY -> "ICON+CAPACITY";
            case HudConfigClient.BatteryMode.ICON_PLUS_CAPACITY_PLUS_STATS -> "FULL INFO";
        })));
    }

    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.savedPulse) {
            --this.savedPulseTicks;
            if (this.savedPulseTicks <= 0) {
                this.savedPulse = false;
            }
        }
    }

    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        gg.fill(0, 0, this.width, this.height, -2013265920);
        super.render(gg, mouseX, mouseY, partialTick);
        gg.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        if (this.savedPulse) {
            gg.drawCenteredString(this.font, (Component)Component.literal((String)"Saved"), this.width / 2, 28, 0x55FF55);
        } else {
            gg.drawCenteredString(this.font, (Component)Component.literal((String)"Preview + cycle buttons"), this.width / 2, 28, 0xAAAAAA);
        }
        CyberwareHudLayer.renderHudPreview(gg, partialTick, this.working);
        CyberwareHudLayer.renderCrosshairPreview(gg, partialTick, this.working);
    }
}

