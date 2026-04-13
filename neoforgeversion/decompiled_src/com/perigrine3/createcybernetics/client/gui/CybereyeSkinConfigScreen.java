/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.fml.loading.FMLPaths
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.client.CybereyeIrisConfigClient;
import com.perigrine3.createcybernetics.client.skin.CybereyeOverlayHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

public final class CybereyeSkinConfigScreen
extends Screen {
    private static final int SKIN_TEX_W = 64;
    private static final int SKIN_TEX_H = 64;
    private static final int FACE_U = 8;
    private static final int FACE_V = 8;
    private static final int FACE_PX = 8;
    private static final int GRID_CELLS = 8;
    private static final int PAD = 12;
    private static final int BTN_W = 60;
    private static final int BTN_H = 20;
    private static final int BTN_GAP = 6;
    private static final int GRID_LINE = 0x66FFFFFF;
    private static final int GRID_BORDER = -1426063361;
    private static final int ACTIVE_FILL = 0x66FF0000;
    private static final int ACTIVE_EDGE = -855703552;
    private static final int SAVED_FILL = 0x6600FF00;
    private static final int SAVED_EDGE = -872349952;
    private static final int DIM_OVERLAY = -2013265920;
    private final Screen parent;
    private int gridX;
    private int gridY;
    private int cell;
    private int gridW;
    private int gridH;
    private Eye activeEye = Eye.LEFT;
    private boolean editing = true;
    private final EyeConfig left = new EyeConfig();
    private final EyeConfig right = new EyeConfig();
    private boolean dragging = false;
    private int dragOffX = 0;
    private int dragOffY = 0;
    private Button btnL;
    private Button btnR;
    private Button btnSave;
    private Button btn11;
    private Button btn12;
    private Button btn22;
    private Button btnBack;
    @Nullable
    private UUID playerId = null;

    public CybereyeSkinConfigScreen(Screen parent) {
        super((Component)Component.translatable((String)"screen.createcybernetics.cybereye_skin"));
        this.parent = parent;
    }

    protected void init() {
        StoredConfig loaded;
        LocalPlayer p = Minecraft.getInstance().player;
        UUID uUID = this.playerId = p != null ? p.getUUID() : null;
        if (this.playerId != null && (loaded = ConfigStore.load(this.playerId)) != null) {
            this.applyLoaded(loaded);
        }
        this.pushConfigToPlayerNbtAndInvalidate();
        this.cc$rebuildWidgets();
        this.updateButtonStates();
    }

    public void resize(Minecraft mc, int width, int height) {
        super.resize(mc, width, height);
        this.cc$rebuildWidgets();
        this.updateButtonStates();
    }

    protected void cc$rebuildWidgets() {
        this.clearWidgets();
        this.computeGridLayout();
        int leftColX = this.gridX - 72;
        int rightColX = this.gridX + this.gridW + 12;
        int topY = this.gridY;
        this.btnL = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"L"), b -> this.selectEye(Eye.LEFT)).pos(leftColX, topY).size(60, 20).build());
        this.btnR = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"R"), b -> this.selectEye(Eye.RIGHT)).pos(leftColX, topY + 26).size(60, 20).build());
        this.btnSave = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)"gui.createcybernetics.save"), b -> this.saveActiveEye()).pos(leftColX, topY + 52 + 4).size(60, 20).build());
        this.btnBack = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)"gui.back"), b -> Minecraft.getInstance().setScreen(this.parent)).pos(leftColX, topY + 78 + 10).size(60, 20).build());
        this.btn11 = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"1x1"), b -> this.setActiveSize(IrisSize.ONE_BY_ONE)).pos(rightColX, topY).size(60, 20).build());
        this.btn12 = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"1x2"), b -> this.setActiveSize(IrisSize.ONE_BY_TWO)).pos(rightColX, topY + 26).size(60, 20).build());
        this.btn22 = (Button)this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"2x2"), b -> this.setActiveSize(IrisSize.TWO_BY_TWO)).pos(rightColX, topY + 52).size(60, 20).build());
    }

    private void computeGridLayout() {
        int sideReserve = 168;
        int availW = Math.max(80, this.width - sideReserve);
        int availH = Math.max(80, this.height - 70);
        int maxCellByW = availW / 8;
        int maxCellByH = availH / 8;
        int chosen = Math.min(maxCellByW, maxCellByH);
        this.cell = chosen = Math.max(10, Math.min(64, chosen));
        this.gridW = this.cell * 8;
        this.gridH = this.cell * 8;
        this.gridX = (this.width - this.gridW) / 2;
        this.gridY = 44 + (availH - this.gridH) / 2;
    }

    private void selectEye(Eye eye) {
        this.activeEye = eye;
        this.editing = true;
        this.active().locked = false;
        this.dragging = false;
        this.updateButtonStates();
    }

    private void saveActiveEye() {
        this.active().locked = true;
        this.editing = false;
        this.dragging = false;
        if (this.playerId != null) {
            ConfigStore.save(this.playerId, this.toStored());
        }
        this.pushConfigToPlayerNbtAndInvalidate();
        this.updateButtonStates();
    }

    private void setActiveSize(IrisSize size) {
        if (!this.canEdit()) {
            return;
        }
        EyeConfig cfg = this.active();
        cfg.size = size;
        this.clampSelection(cfg);
        this.updateButtonStates();
    }

    private EyeConfig active() {
        return this.activeEye == Eye.LEFT ? this.left : this.right;
    }

    private EyeConfig other() {
        return this.activeEye == Eye.LEFT ? this.right : this.left;
    }

    private boolean canEdit() {
        return this.editing && !this.active().locked;
    }

    private void updateButtonStates() {
        boolean edit = this.canEdit();
        if (this.btnSave != null) {
            this.btnSave.active = edit;
        }
        if (this.btn11 != null) {
            this.btn11.active = edit;
        }
        if (this.btn12 != null) {
            this.btn12.active = edit;
        }
        if (this.btn22 != null) {
            this.btn22.active = edit;
        }
        if (this.btnBack != null) {
            this.btnBack.active = true;
        }
    }

    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (button != 0) {
            return false;
        }
        if (!this.canEdit()) {
            return false;
        }
        if (!this.isInGrid(mouseX, mouseY)) {
            return false;
        }
        int cx = (int)((mouseX - (double)this.gridX) / (double)this.cell);
        int cy = (int)((mouseY - (double)this.gridY) / (double)this.cell);
        EyeConfig cfg = this.active();
        if (this.isInsideSelectionCell(cfg, cx, cy)) {
            this.dragging = true;
            this.dragOffX = cx - cfg.x;
            this.dragOffY = cy - cfg.y;
            return true;
        }
        cfg.x = cx;
        cfg.y = cy;
        this.clampSelection(cfg);
        this.dragging = true;
        this.dragOffX = 0;
        this.dragOffY = 0;
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        }
        if (button != 0) {
            return false;
        }
        if (!this.dragging) {
            return false;
        }
        if (!this.canEdit()) {
            return false;
        }
        int cx = (int)Math.floor((mouseX - (double)this.gridX) / (double)this.cell);
        int cy = (int)Math.floor((mouseY - (double)this.gridY) / (double)this.cell);
        EyeConfig cfg = this.active();
        cfg.x = cx - this.dragOffX;
        cfg.y = cy - this.dragOffY;
        this.clampSelection(cfg);
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0) {
            this.dragging = false;
        }
        return false;
    }

    private boolean isInsideSelectionCell(EyeConfig cfg, int cx, int cy) {
        int w = cfg.size.w;
        int h = cfg.size.h;
        return cx >= cfg.x && cx < cfg.x + w && cy >= cfg.y && cy < cfg.y + h;
    }

    private boolean isInGrid(double mouseX, double mouseY) {
        return mouseX >= (double)this.gridX && mouseX < (double)(this.gridX + this.gridW) && mouseY >= (double)this.gridY && mouseY < (double)(this.gridY + this.gridH);
    }

    private void clampSelection(EyeConfig cfg) {
        int maxX = 8 - cfg.size.w;
        int maxY = 8 - cfg.size.h;
        if (cfg.x < 0) {
            cfg.x = 0;
        }
        if (cfg.y < 0) {
            cfg.y = 0;
        }
        if (cfg.x > maxX) {
            cfg.x = maxX;
        }
        if (cfg.y > maxY) {
            cfg.y = maxY;
        }
    }

    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.computeGridLayout();
        gg.fill(0, 0, this.width, this.height, -2013265920);
        super.render(gg, mouseX, mouseY, partialTick);
        gg.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        EyeConfig cfg = this.active();
        String eyeName = this.activeEye == Eye.LEFT ? "L" : "R";
        MutableComponent status = Component.literal((String)("Eye: " + eyeName + " | Iris: " + cfg.size.w + "x" + cfg.size.h + " | " + (cfg.locked ? "Saved" : (this.canEdit() ? "Editing" : "Locked"))));
        gg.drawCenteredString(this.font, (Component)status, this.width / 2, 26, 0xAAAAAA);
        this.renderPlayerFaceScaledNearest(gg);
        this.renderGridLines(gg);
        this.renderSavedHighlights(gg);
        this.renderActiveHighlight(gg);
    }

    private void renderPlayerFaceScaledNearest(GuiGraphics gg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            gg.fill(this.gridX, this.gridY, this.gridX + this.gridW, this.gridY + this.gridH, 0x66000000);
            return;
        }
        LocalPlayer player = mc.player;
        ResourceLocation skin = player.getSkin().texture();
        try {
            AbstractTexture tex = mc.getTextureManager().getTexture(skin);
            if (tex != null) {
                tex.setFilter(false, false);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)skin);
        gg.pose().pushPose();
        gg.pose().translate((float)this.gridX, (float)this.gridY, 0.0f);
        gg.pose().scale((float)this.cell, (float)this.cell, 1.0f);
        gg.blit(skin, 0, 0, 8.0f, 8.0f, 8, 8, 64, 64);
        gg.pose().popPose();
    }

    private void renderGridLines(GuiGraphics gg) {
        gg.hLine(this.gridX, this.gridX + this.gridW, this.gridY, -1426063361);
        gg.hLine(this.gridX, this.gridX + this.gridW, this.gridY + this.gridH, -1426063361);
        gg.vLine(this.gridX, this.gridY, this.gridY + this.gridH, -1426063361);
        gg.vLine(this.gridX + this.gridW, this.gridY, this.gridY + this.gridH, -1426063361);
        for (int i = 1; i < 8; ++i) {
            int x = this.gridX + i * this.cell;
            int y = this.gridY + i * this.cell;
            gg.vLine(x, this.gridY, this.gridY + this.gridH, 0x66FFFFFF);
            gg.hLine(this.gridX, this.gridX + this.gridW, y, 0x66FFFFFF);
        }
    }

    private void renderSavedHighlights(GuiGraphics gg) {
        if (this.left.locked) {
            this.renderSelection(gg, this.left, 0x6600FF00, -872349952);
        }
        if (this.right.locked) {
            this.renderSelection(gg, this.right, 0x6600FF00, -872349952);
        }
    }

    private void renderActiveHighlight(GuiGraphics gg) {
        this.renderSelection(gg, this.active(), 0x66FF0000, -855703552);
    }

    private void renderSelection(GuiGraphics gg, EyeConfig cfg, int fill, int edge) {
        int sx = this.gridX + cfg.x * this.cell;
        int sy = this.gridY + cfg.y * this.cell;
        int sw = cfg.size.w * this.cell;
        int sh = cfg.size.h * this.cell;
        gg.fill(sx, sy, sx + sw, sy + sh, fill);
        gg.hLine(sx, sx + sw, sy, edge);
        gg.hLine(sx, sx + sw, sy + sh, edge);
        gg.vLine(sx, sy, sy + sh, edge);
        gg.vLine(sx + sw, sy, sy + sh, edge);
    }

    private StoredConfig toStored() {
        StoredConfig sc = new StoredConfig();
        sc.left.size = CybereyeSkinConfigScreen.encodeSize(this.left.size);
        sc.left.x = this.left.x;
        sc.left.y = this.left.y;
        sc.right.size = CybereyeSkinConfigScreen.encodeSize(this.right.size);
        sc.right.x = this.right.x;
        sc.right.y = this.right.y;
        return sc;
    }

    private void applyLoaded(StoredConfig sc) {
        this.left.size = CybereyeSkinConfigScreen.decodeSize(sc.left.size);
        this.left.x = sc.left.x;
        this.left.y = sc.left.y;
        this.left.locked = true;
        this.clampSelection(this.left);
        this.right.size = CybereyeSkinConfigScreen.decodeSize(sc.right.size);
        this.right.x = sc.right.x;
        this.right.y = sc.right.y;
        this.right.locked = true;
        this.clampSelection(this.right);
        this.editing = false;
        this.activeEye = Eye.LEFT;
        this.dragging = false;
    }

    private static String encodeSize(IrisSize s) {
        return switch (s.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> "1x1";
            case 1 -> "1x2";
            case 2 -> "2x2";
        };
    }

    private static IrisSize decodeSize(String s) {
        if (s == null) {
            return IrisSize.ONE_BY_ONE;
        }
        return switch (s) {
            case "1x2" -> IrisSize.ONE_BY_TWO;
            case "2x2" -> IrisSize.TWO_BY_TWO;
            default -> IrisSize.ONE_BY_ONE;
        };
    }

    private void pushConfigToPlayerNbtAndInvalidate() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return;
        }
        CompoundTag root = p.getPersistentData().getCompound("cc_cybereye_cfg");
        CompoundTag l = new CompoundTag();
        l.putInt("x", 8 + this.left.x);
        l.putInt("y", 8 + this.left.y);
        l.putInt("variant", CybereyeSkinConfigScreen.encodeVariant(this.left.size));
        root.put("left", (Tag)l);
        CompoundTag r = new CompoundTag();
        r.putInt("x", 8 + this.right.x);
        r.putInt("y", 8 + this.right.y);
        r.putInt("variant", CybereyeSkinConfigScreen.encodeVariant(this.right.size));
        root.put("right", (Tag)r);
        p.getPersistentData().put("cc_cybereye_cfg", (Tag)root);
        CybereyeOverlayHandler.invalidate((Player)p);
    }

    private static int encodeVariant(IrisSize size) {
        return switch (size.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
        };
    }

    public static void applyClientConfigToPlayerNbtAndInvalidate(LocalPlayer p) {
        if (p == null) {
            return;
        }
        CybereyeIrisConfigClient.Layout layout = CybereyeIrisConfigClient.get(p.getUUID());
        CompoundTag root = p.getPersistentData().getCompound("cc_cybereye_cfg");
        CompoundTag l = new CompoundTag();
        l.putInt("x", 8 + layout.left.x);
        l.putInt("y", 8 + layout.left.y);
        l.putInt("variant", CybereyeSkinConfigScreen.encodeVariant(CybereyeSkinConfigScreen.convert(layout.left.size)));
        root.put("left", (Tag)l);
        CompoundTag r = new CompoundTag();
        r.putInt("x", 8 + layout.right.x);
        r.putInt("y", 8 + layout.right.y);
        r.putInt("variant", CybereyeSkinConfigScreen.encodeVariant(CybereyeSkinConfigScreen.convert(layout.right.size)));
        root.put("right", (Tag)r);
        p.getPersistentData().put("cc_cybereye_cfg", (Tag)root);
        CybereyeOverlayHandler.invalidate((Player)p);
    }

    private static IrisSize convert(CybereyeIrisConfigClient.IrisSize s) {
        return switch (s) {
            default -> throw new MatchException(null, null);
            case CybereyeIrisConfigClient.IrisSize.ONE_BY_ONE -> IrisSize.ONE_BY_ONE;
            case CybereyeIrisConfigClient.IrisSize.ONE_BY_TWO -> IrisSize.ONE_BY_TWO;
            case CybereyeIrisConfigClient.IrisSize.TWO_BY_TWO -> IrisSize.TWO_BY_TWO;
        };
    }

    private static enum Eye {
        LEFT,
        RIGHT;

    }

    private static final class EyeConfig {
        IrisSize size = IrisSize.ONE_BY_ONE;
        int x = 3;
        int y = 5;
        boolean locked = false;

        private EyeConfig() {
        }
    }

    private static final class ConfigStore {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        private ConfigStore() {
        }

        private static Path fileFor(UUID playerId) {
            Path dir = FMLPaths.CONFIGDIR.get().resolve("createcybernetics");
            return dir.resolve("cybereye_iris_" + String.valueOf(playerId) + ".json");
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Nullable
        static StoredConfig load(UUID playerId) {
            Path file = ConfigStore.fileFor(playerId);
            if (!Files.exists(file, new LinkOption[0])) {
                return null;
            }
            try (BufferedReader r = Files.newBufferedReader(file);){
                JsonObject obj = (JsonObject)GSON.fromJson((Reader)r, JsonObject.class);
                if (obj == null) {
                    StoredConfig storedConfig2 = null;
                    return storedConfig2;
                }
                StoredConfig sc = new StoredConfig();
                if (obj.has("left")) {
                    JsonObject l = obj.getAsJsonObject("left");
                    sc.left.size = l.has("size") ? l.get("size").getAsString() : "1x1";
                    sc.left.x = l.has("x") ? l.get("x").getAsInt() : 3;
                    int n = sc.left.y = l.has("y") ? l.get("y").getAsInt() : 5;
                }
                if (obj.has("right")) {
                    JsonObject rr = obj.getAsJsonObject("right");
                    sc.right.size = rr.has("size") ? rr.get("size").getAsString() : "1x1";
                    sc.right.x = rr.has("x") ? rr.get("x").getAsInt() : 3;
                    sc.right.y = rr.has("y") ? rr.get("y").getAsInt() : 5;
                }
                StoredConfig storedConfig = sc;
                return storedConfig;
            }
            catch (Throwable ignored) {
                return null;
            }
        }

        static void save(UUID playerId, StoredConfig sc) {
            if (sc == null) {
                return;
            }
            Path file = ConfigStore.fileFor(playerId);
            try {
                Files.createDirectories(file.getParent(), new FileAttribute[0]);
                JsonObject root = new JsonObject();
                JsonObject l = new JsonObject();
                l.addProperty("size", sc.left.size);
                l.addProperty("x", (Number)sc.left.x);
                l.addProperty("y", (Number)sc.left.y);
                JsonObject r = new JsonObject();
                r.addProperty("size", sc.right.size);
                r.addProperty("x", (Number)sc.right.x);
                r.addProperty("y", (Number)sc.right.y);
                root.add("left", (JsonElement)l);
                root.add("right", (JsonElement)r);
                try (BufferedWriter w = Files.newBufferedWriter(file, new OpenOption[0]);){
                    GSON.toJson((JsonElement)root, (Appendable)w);
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static final class StoredConfig {
        StoredEye left = new StoredEye();
        StoredEye right = new StoredEye();

        private StoredConfig() {
        }
    }

    private static enum IrisSize {
        ONE_BY_ONE(1, 1),
        ONE_BY_TWO(1, 2),
        TWO_BY_TWO(2, 2);

        final int w;
        final int h;

        private IrisSize(int w, int h) {
            this.w = w;
            this.h = h;
        }
    }

    private static final class StoredEye {
        String size = "1x1";
        int x = 3;
        int y = 5;

        private StoredEye() {
        }
    }
}

