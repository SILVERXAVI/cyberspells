/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.InputEvent$MouseButton$Pre
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.joml.Matrix4f
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.cyberware.PneumaticCalvesItem;
import com.perigrine3.createcybernetics.network.payload.CyberwareTogglePayloads;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

public class CyberwareToggleWheelScreen
extends Screen {
    private static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_toggle_wheel");
    private static boolean OPEN = false;
    private static int SELECTED_INDEX = 0;
    private static int STICKY_INDEX = 0;
    private static final List<Entry> ENTRIES = new ArrayList<Entry>();
    private static PlayerCyberwareData LAST_DATA = null;
    private static double CURSOR_X = 0.0;
    private static double CURSOR_Y = 0.0;
    private static boolean HAS_LAST_ROT = false;
    private static float LAST_YAW = 0.0f;
    private static float LAST_PITCH = 0.0f;
    private static final double YAW_TO_CURSOR = 0.02;
    private static final double PITCH_TO_CURSOR = 0.02;
    private static final double DAMPING = 0.85;
    private static final double CURSOR_MAX = 1.25;
    private static final double SELECT_DEADZONE = 0.08;

    public CyberwareToggleWheelScreen() {
        super((Component)Component.empty());
    }

    protected void init() {
        OPEN = true;
        STICKY_INDEX = 0;
        SELECTED_INDEX = 0;
        CURSOR_X = 0.0;
        CURSOR_Y = 0.0;
        PacketDistributor.sendToServer((CustomPacketPayload)new CyberwareTogglePayloads.RequestToggleStatesPayload(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p != null) {
            LAST_YAW = p.getYRot();
            LAST_PITCH = p.getXRot();
            HAS_LAST_ROT = true;
        } else {
            HAS_LAST_ROT = false;
        }
        if (this.minecraft != null && this.minecraft.screen == this) {
            this.minecraft.setScreen(null);
        }
    }

    public boolean isPauseScreen() {
        return false;
    }

    public static boolean isWheelOpen() {
        return OPEN;
    }

    public static void closeWheel() {
        OPEN = false;
        HAS_LAST_ROT = false;
    }

    public static void toggleSelected() {
        if (!OPEN) {
            return;
        }
        if (ENTRIES.isEmpty()) {
            return;
        }
        int idx = Mth.clamp((int)SELECTED_INDEX, (int)0, (int)(ENTRIES.size() - 1));
        Entry e = ENTRIES.get(idx);
        if (e.targets().isEmpty()) {
            return;
        }
        PlayerCyberwareData data = LAST_DATA;
        if (data == null) {
            SlotIndex t = e.targets().get(0);
            PacketDistributor.sendToServer((CustomPacketPayload)new CyberwareTogglePayloads.ToggleCyberwarePayload(t.slot().name(), t.index()), (CustomPacketPayload[])new CustomPacketPayload[0]);
            return;
        }
        boolean currentlyEnabled = CyberwareToggleWheelScreen.isEntryEnabled(data, e);
        boolean desiredEnabled = !currentlyEnabled;
        for (SlotIndex t : e.targets()) {
            boolean nowEnabled = data.isEnabled(t.slot(), t.index());
            if (nowEnabled == desiredEnabled) continue;
            PacketDistributor.sendToServer((CustomPacketPayload)new CyberwareTogglePayloads.ToggleCyberwarePayload(t.slot().name(), t.index()), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public boolean shouldBlurBackground() {
        return false;
    }

    public void onClose() {
        OPEN = false;
        HAS_LAST_ROT = false;
        super.onClose();
    }

    private static void renderHudLayer(GuiGraphics graphics, DeltaTracker delta) {
        int selected;
        PlayerCyberwareData data;
        if (!OPEN) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            CyberwareToggleWheelScreen.closeWheel();
            return;
        }
        LAST_DATA = data = CyberwareToggleWheelScreen.rebuildEntries(mc);
        Window window = mc.getWindow();
        int w = window.getGuiScaledWidth();
        int h = window.getGuiScaledHeight();
        int cx = w / 2;
        int cy = h / 2;
        int sw = window.getScreenWidth();
        int sh = window.getScreenHeight();
        double guiScale = window.getGuiScale();
        float outerR_px = (float)Math.min(sw, sh) * 0.37f;
        float outerR = (float)((double)outerR_px / guiScale);
        float innerR = outerR * 0.4f;
        float midR = (innerR + outerR) * 0.5f;
        int n = Math.max(1, ENTRIES.size());
        SELECTED_INDEX = selected = CyberwareToggleWheelScreen.selectedIndexFromCursor(n);
        int baseArgb = -2013265920;
        int hoverArgb = -1439794177;
        for (int i = 0; i < n; ++i) {
            int argb = i == selected ? -1439794177 : -2013265920;
            CyberwareToggleWheelScreen.drawDonutSegment(graphics, cx, cy, innerR, outerR, n, i, 24, argb);
        }
        int nameColor = -1;
        int enabledColor = -11141291;
        int disabledColor = -43691;
        RenderSystem.enableDepthTest();
        for (int i = 0; i < ENTRIES.size(); ++i) {
            Entry e = ENTRIES.get(i);
            double ang = CyberwareToggleWheelScreen.angleForIndex(n, i) + Math.PI * 2 / (double)n * 0.5;
            int centerX = (int)Math.round((double)cx + Math.cos(ang) * (double)midR);
            int centerY = (int)Math.round((double)cy + Math.sin(ang) * (double)midR);
            int ix = centerX - 8;
            int iy = centerY - 8;
            graphics.renderItem(e.icon(), ix, iy);
            String rawName = e.icon().getHoverName().getString();
            String name = rawName.length() > 22 ? rawName.substring(0, 21) + "\u2026" : rawName;
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            float nameScale = 0.55f;
            poseStack.scale(0.55f, 0.55f, 1.0f);
            int nameW = mc.font.width(name);
            int scaledCenterX = (int)((float)centerX / 0.55f);
            int scaledNameX = scaledCenterX - nameW / 2;
            Objects.requireNonNull(mc.font);
            int scaledNameY = (int)((float)(iy - (9 + 2)) / 0.55f);
            graphics.drawString(mc.font, name, scaledNameX, scaledNameY, -1, true);
            poseStack.popPose();
            boolean enabled = data != null && CyberwareToggleWheelScreen.isEntryEnabled(data, e);
            String stateText = enabled ? "ENABLED" : "DISABLED";
            int stateW = mc.font.width(stateText);
            int stateX = centerX - stateW / 2;
            int stateY = iy + 16 + 2;
            graphics.drawString(mc.font, stateText, stateX, stateY, enabled ? -11141291 : -43691, true);
        }
    }

    private static boolean isEntryEnabled(PlayerCyberwareData data, Entry e) {
        if (data == null) {
            return false;
        }
        for (SlotIndex t : e.targets()) {
            if (!data.isEnabled(t.slot(), t.index())) continue;
            return true;
        }
        return false;
    }

    private static int selectedIndexFromCursor(int n) {
        if (n <= 0) {
            return 0;
        }
        double mag = Math.sqrt(CURSOR_X * CURSOR_X + CURSOR_Y * CURSOR_Y);
        if (mag < 0.08) {
            return Mth.clamp((int)STICKY_INDEX, (int)0, (int)(n - 1));
        }
        double ang = Math.atan2(CURSOR_Y, CURSOR_X);
        int idx = (int)Math.floor((ang = (ang + 1.5707963267948966 + Math.PI * 2) % (Math.PI * 2)) / (Math.PI * 2) * (double)n);
        if (idx < 0) {
            idx += n;
        }
        if (idx >= n) {
            idx -= n;
        }
        STICKY_INDEX = idx;
        return idx;
    }

    private static PlayerCyberwareData rebuildEntries(Minecraft mc) {
        ENTRIES.clear();
        if (mc.player == null) {
            return null;
        }
        if (!mc.player.hasData(ModAttachments.CYBERWARE)) {
            return null;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return null;
        }
        ArrayList<SlotIndex> pneumaticCalvesTargets = new ArrayList<SlotIndex>();
        ItemStack pneumaticCalvesIcon = ItemStack.EMPTY;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack stack;
                InstalledCyberware cw = arr[i];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) continue;
                if (stack.getItem() instanceof PneumaticCalvesItem) {
                    pneumaticCalvesTargets.add(new SlotIndex(slot, i));
                    if (!pneumaticCalvesIcon.isEmpty()) continue;
                    pneumaticCalvesIcon = stack.copy();
                    continue;
                }
                ENTRIES.add(new Entry(stack.copy(), List.of(new SlotIndex(slot, i))));
            }
        }
        if (pneumaticCalvesTargets.size() >= 2) {
            ENTRIES.add(new Entry(pneumaticCalvesIcon.isEmpty() ? ItemStack.EMPTY : pneumaticCalvesIcon, pneumaticCalvesTargets));
        }
        if (SELECTED_INDEX >= ENTRIES.size()) {
            SELECTED_INDEX = Math.max(0, ENTRIES.size() - 1);
        }
        if (STICKY_INDEX >= ENTRIES.size()) {
            STICKY_INDEX = Math.max(0, ENTRIES.size() - 1);
        }
        return data;
    }

    private static double angleForIndex(int n, int i) {
        double step = Math.PI * 2 / (double)n;
        return -1.5707963267948966 + step * (double)i;
    }

    private static void drawDonutSegment(GuiGraphics graphics, int cx, int cy, float innerR, float outerR, int n, int idx, int arcSteps, int argb) {
        float a = (float)(argb >>> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >>> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >>> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        double step = Math.PI * 2 / (double)n;
        double a0 = -1.5707963267948966 + step * (double)idx;
        double a1 = a0 + step;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f pose = graphics.pose().last().pose();
        BufferBuilder bb = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= arcSteps; ++i) {
            double t = (double)i / (double)arcSteps;
            double ang = a0 + (a1 - a0) * t;
            float cos = (float)Math.cos(ang);
            float sin = (float)Math.sin(ang);
            float xo = (float)cx + cos * outerR;
            float yo = (float)cy + sin * outerR;
            float xi = (float)cx + cos * innerR;
            float yi = (float)cy + sin * innerR;
            bb.addVertex(pose, xo, yo, 0.0f).setColor(r, g, b, a);
            bb.addVertex(pose, xi, yi, 0.0f).setColor(r, g, b, a);
        }
        BufferUploader.drawWithShader((MeshData)bb.buildOrThrow());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private record Entry(ItemStack icon, List<SlotIndex> targets) {
    }

    private record SlotIndex(CyberwareSlot slot, int index) {
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientGameBus {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            LocalPlayer p;
            if (!OPEN) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen != null) {
                CyberwareToggleWheelScreen.closeWheel();
                return;
            }
            KeyMapping attack = mc.options.keyAttack;
            if (attack != null && attack.isDown()) {
                attack.setDown(false);
            }
            if ((p = mc.player) == null) {
                return;
            }
            float yaw = p.getYRot();
            float pitch = p.getXRot();
            if (!HAS_LAST_ROT) {
                LAST_YAW = yaw;
                LAST_PITCH = pitch;
                HAS_LAST_ROT = true;
                return;
            }
            float dyaw = Mth.wrapDegrees((float)(yaw - LAST_YAW));
            float dpitch = pitch - LAST_PITCH;
            LAST_YAW = yaw;
            LAST_PITCH = pitch;
            CURSOR_X += (double)dyaw * 0.02;
            CURSOR_Y += (double)dpitch * 0.02;
            CURSOR_X = Mth.clamp((double)CURSOR_X, (double)-1.25, (double)1.25);
            CURSOR_Y = Mth.clamp((double)CURSOR_Y, (double)-1.25, (double)1.25);
            CURSOR_X *= 0.85;
            CURSOR_Y *= 0.85;
        }

        @SubscribeEvent
        public static void onMouseButton(InputEvent.MouseButton.Pre event) {
            if (!OPEN) {
                return;
            }
            if (event.getAction() != 1) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (event.getButton() == 0) {
                event.setCanceled(true);
                KeyMapping attack = mc.options.keyAttack;
                if (attack != null) {
                    attack.setDown(false);
                }
                CyberwareToggleWheelScreen.toggleSelected();
                return;
            }
            if (event.getButton() == 1) {
                event.setCanceled(true);
                CyberwareToggleWheelScreen.closeWheel();
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
    public static final class ClientModBus {
        @SubscribeEvent
        public static void registerGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(LAYER_ID, CyberwareToggleWheelScreen::renderHudLayer);
        }
    }
}

