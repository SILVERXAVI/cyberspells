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
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
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
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.network.payload.ArmCannonWheelPayloads;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

public class ArmCannonWheelScreen
extends Screen {
    private static final ResourceLocation LAYER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"radial_wheel_overlay");
    private static boolean OPEN = false;
    private static int SEGMENTS = 4;
    private static int SELECTED_INDEX = 0;
    private static int STICKY_INDEX = 0;
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
    private static final int BASE_ARGB = -2013265920;
    private static final int HOVER_ARGB = -1439794177;
    private static final SimpleContainer TMP_INV = new SimpleContainer(4);

    public ArmCannonWheelScreen() {
        super((Component)Component.empty());
    }

    public static boolean isOpen() {
        return OPEN;
    }

    public static void open(int segments) {
        KeyMapping attack;
        SEGMENTS = Math.max(1, segments);
        OPEN = true;
        STICKY_INDEX = 0;
        SELECTED_INDEX = 0;
        CURSOR_X = 0.0;
        CURSOR_Y = 0.0;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p != null) {
            PlayerCyberwareData data;
            LAST_YAW = p.getYRot();
            LAST_PITCH = p.getXRot();
            HAS_LAST_ROT = true;
            if (p.hasData(ModAttachments.CYBERWARE) && (data = (PlayerCyberwareData)p.getData(ModAttachments.CYBERWARE)) != null) {
                ArmCannonWheelScreen.setPreselectedIndex(data.getArmCannonSelected());
            }
        } else {
            HAS_LAST_ROT = false;
        }
        if ((attack = mc.options.keyAttack) != null && attack.isDown()) {
            attack.setDown(false);
        }
    }

    public static void close() {
        OPEN = false;
        HAS_LAST_ROT = false;
    }

    public static int getSelectedIndex() {
        if (!OPEN) {
            return 0;
        }
        return Mth.clamp((int)SELECTED_INDEX, (int)0, (int)Math.max(0, SEGMENTS - 1));
    }

    public static void setPreselectedIndex(int idx) {
        int clamped;
        int n = Math.max(1, SEGMENTS);
        STICKY_INDEX = clamped = Mth.clamp((int)idx, (int)0, (int)(n - 1));
        SELECTED_INDEX = clamped;
    }

    protected void init() {
        if (this.minecraft != null && this.minecraft.screen == this) {
            this.minecraft.setScreen(null);
        }
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    }

    public boolean shouldBlurBackground() {
        return false;
    }

    public void onClose() {
        ArmCannonWheelScreen.close();
        super.onClose();
    }

    private static void renderHudLayer(GuiGraphics graphics, DeltaTracker delta) {
        int hovered;
        PlayerCyberwareData data;
        if (!OPEN) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            ArmCannonWheelScreen.close();
            return;
        }
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
        int n = Math.max(1, SEGMENTS);
        int loadedIndex = -1;
        ItemStack[] stacks = new ItemStack[n];
        for (int i = 0; i < n; ++i) {
            stacks[i] = ItemStack.EMPTY;
        }
        if (mc.player != null && mc.player.hasData(ModAttachments.CYBERWARE) && (data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE)) != null) {
            loadedIndex = data.getArmCannonSelected();
            ItemStack installedCannon = ArmCannonWheelScreen.findInstalledArmCannonStack(data);
            if (!installedCannon.isEmpty()) {
                ArmCannonItem.loadFromInstalledStack(installedCannon, (HolderLookup.Provider)mc.player.level().registryAccess(), (Container)TMP_INV);
                for (int i = 0; i < n && i < 4; ++i) {
                    ItemStack st = TMP_INV.getItem(i);
                    stacks[i] = st == null ? ItemStack.EMPTY : st;
                }
            }
        }
        SELECTED_INDEX = hovered = ArmCannonWheelScreen.selectedIndexFromCursor(n);
        for (int i = 0; i < n; ++i) {
            int argb = i == hovered ? -1439794177 : -2013265920;
            ArmCannonWheelScreen.drawDonutSegment(graphics, cx, cy, innerR, outerR, n, i, 24, argb);
        }
        RenderSystem.enableDepthTest();
        int nameColor = -1;
        int loadedColor = -1;
        for (int i = 0; i < n; ++i) {
            ItemStack st = stacks[i];
            if (st == null) {
                st = ItemStack.EMPTY;
            }
            double ang = ArmCannonWheelScreen.angleForIndex(n, i) + Math.PI * 2 / (double)n * 0.5;
            int centerX = (int)Math.round((double)cx + Math.cos(ang) * (double)midR);
            int centerY = (int)Math.round((double)cy + Math.sin(ang) * (double)midR);
            if (st.isEmpty()) continue;
            int ix = centerX - 8;
            int iy = centerY - 8;
            graphics.renderItem(st, ix, iy);
            String rawName = st.getHoverName().getString();
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
            if (i != loadedIndex) continue;
            String loaded = "LOADED";
            int lw = mc.font.width(loaded);
            int lx = centerX - lw / 2;
            int ly = iy + 16 + 2;
            graphics.drawString(mc.font, loaded, lx, ly, -1, true);
        }
    }

    private static ItemStack findInstalledArmCannonStack(PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || st.getItem() != ModItems.ARMUPGRADES_ARMCANNON.get()) continue;
                return st;
            }
        }
        return ItemStack.EMPTY;
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

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientGameBus {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            if (!OPEN) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen != null) {
                ArmCannonWheelScreen.close();
                return;
            }
            LocalPlayer p = mc.player;
            if (p == null) {
                ArmCannonWheelScreen.close();
                return;
            }
            KeyMapping attack = mc.options.keyAttack;
            if (attack != null && attack.isDown()) {
                attack.setDown(false);
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
                int idx = ArmCannonWheelScreen.getSelectedIndex();
                PacketDistributor.sendToServer((CustomPacketPayload)new ArmCannonWheelPayloads.SelectArmCannonAmmoSlotPayload(idx), (CustomPacketPayload[])new CustomPacketPayload[0]);
                ArmCannonWheelScreen.close();
                return;
            }
            if (event.getButton() == 1) {
                event.setCanceled(true);
                ArmCannonWheelScreen.close();
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
    public static final class ClientModBus {
        @SubscribeEvent
        public static void registerGuiLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(LAYER_ID, ArmCannonWheelScreen::renderHudLayer);
        }
    }
}

