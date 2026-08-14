/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.joml.Matrix4f
 */
package com.maxwell.cyber_ware_port.client.upgrades.cybereye;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.client.ClientCyberwareSettings;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.network.ToggleCyberwarePacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

public class CyberwareMenuScreen
extends Screen {
    private static final ResourceLocation HUD_COLOR_ICON = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/hud_color.png");
    private static final ResourceLocation HUD_POS_ICON = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/hud_pos.png");
    private static final ResourceLocation HUD_RESET_ICON = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/hud_reset.png");
    private static final float INNER_RADIUS = 40.0f;
    private static final float OUTER_RADIUS = 100.0f;
    private static final float ITEM_RADIUS = 70.0f;
    private static final int HUD_WIDTH = 80;
    private static final int HUD_HEIGHT = 25;
    private static final int BAR_WIDTH = 26;
    private static final int BAR_HEIGHT = 210;
    private static final int BTN_SIZE = 16;
    private int btnX;
    private int btnY;
    private static final int[] PRESET_COLORS = new int[]{-16711681, -16711936, -65536, -256, -1, -65281, -16776961, Short.MIN_VALUE};
    private final List<ToggleablePart> parts = new ArrayList<ToggleablePart>();
    public boolean isColorSettingsOpen = false;
    public boolean isHudMoveMode = false;
    private boolean isDraggingHud = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private boolean isDraggingBar = false;
    private int dragBarOffsetX = 0;
    private int dragBarOffsetY = 0;
    private EditBox hexInput;

    public CyberwareMenuScreen() {
        super((Component)Component.translatable((String)"gui.cyber_ware_port.menu"));
    }

    protected void init() {
        super.init();
        this.parts.clear();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.btnX = centerX + 120;
        this.btnY = centerY - 60;
        if (this.minecraft != null && this.minecraft.player != null) {
            CyberwareUserData data = (CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            ItemStackHandler handler = data.getInstalledCyberware();
            for (int i = 0; i < handler.getSlots(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                ICyberware cw = CyberwareAPI.getCyberware(stack);
                if (cw == null || !cw.canToggle(stack)) continue;
                this.parts.add(new ToggleablePart(i, stack, cw));
            }
        }
        int boxWidth = 80;
        int boxHeight = 20;
        this.hexInput = new EditBox(this.font, centerX - boxWidth / 2, centerY + 30, boxWidth, boxHeight, (Component)Component.literal((String)"Color Hex"));
        this.hexInput.setMaxLength(8);
        this.hexInput.setValue(ClientCyberwareSettings.getHudColorAsHex());
        this.hexInput.setBordered(true);
        this.hexInput.setVisible(false);
        this.hexInput.setResponder(ClientCyberwareSettings::setHudColorFromHex);
        this.addRenderableWidget((GuiEventListener)this.hexInput);
    }

    public void tick() {
        super.tick();
        this.handleMovementInput();
    }

    private void handleMovementInput() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        if (this.hexInput.isFocused()) {
            return;
        }
        this.updateKey(this.minecraft.options.keyUp);
        this.updateKey(this.minecraft.options.keyDown);
        this.updateKey(this.minecraft.options.keyLeft);
        this.updateKey(this.minecraft.options.keyRight);
        this.updateKey(this.minecraft.options.keyJump);
        this.updateKey(this.minecraft.options.keySprint);
        this.updateKey(this.minecraft.options.keyShift);
    }

    private void updateKey(KeyMapping keyMapping) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        InputConstants.Key key = keyMapping.getKey();
        boolean isDown = InputConstants.isKeyDown((long)window, (int)key.getValue());
        keyMapping.setDown(isDown);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isColorSettingsOpen && this.hexInput.isFocused()) {
            if (this.hexInput.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
            if (keyCode == 256) {
                this.toggleColorSettings();
                return true;
            }
        }
        if (this.isHudMoveMode && (keyCode == 256 || keyCode == this.minecraft.options.keyInventory.getKey().getValue())) {
            this.isHudMoveMode = false;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        if (this.isColorSettingsOpen || this.isHudMoveMode) {
            g.fillGradient(0, 0, this.width, this.height, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        if (!this.isColorSettingsOpen && !this.isHudMoveMode) {
            this.renderRadialMenu(g, centerX, centerY, mouseX, mouseY);
        }
        this.renderHudPreview(g, mouseX, mouseY);
        if (!this.isHudMoveMode) {
            this.renderIconButton(g, HUD_COLOR_ICON, this.btnX, this.btnY + 20, mouseX, mouseY, "HUD Color Settings");
            if (!this.isColorSettingsOpen) {
                this.renderIconButton(g, HUD_POS_ICON, this.btnX, this.btnY, mouseX, mouseY, "Move HUD Position");
            }
        }
        if (this.isColorSettingsOpen) {
            this.renderColorSettings(g, centerX, centerY, mouseX, mouseY);
        }
        if (this.isHudMoveMode) {
            g.drawCenteredString(this.font, "HUD MOVE MODE", centerX, 40, -16711936);
            g.drawCenteredString(this.font, "Drag HUD to move / Press ESC to finish", centerX, 55, -1);
        }
    }

    private void renderIconButton(GuiGraphics g, ResourceLocation texture, int x, int y, int mouseX, int mouseY, String tooltip) {
        float[] rgba = ClientCyberwareSettings.getColorFloats();
        g.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        g.blit(texture, x, y, 0.0f, 0.0f, 16, 16, 16, 16);
        g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            g.renderOutline(x - 1, y - 1, 18, 18, -1);
            if (!this.isColorSettingsOpen && !this.isHudMoveMode) {
                g.renderTooltip(this.font, (Component)Component.literal((String)tooltip), mouseX, mouseY);
            }
        }
    }

    private void renderRadialMenu(GuiGraphics g, int centerX, int centerY, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = g.pose().last().pose();
        int segments = 60;
        for (int i = 0; i <= segments; ++i) {
            double angle = Math.PI * 2 * (double)i / (double)segments;
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, (float)centerX + cos * 100.0f, (float)centerY + sin * 100.0f, 0.0f).setColor(1.0f, 0.0f, 0.0f, 0.4f);
            buffer.addVertex(matrix, (float)centerX + cos * 40.0f, (float)centerY + sin * 40.0f, 0.0f).setColor(1.0f, 0.0f, 0.0f, 0.4f);
        }
        MeshData meshData = buffer.build();
        if (meshData != null) {
            BufferUploader.drawWithShader((MeshData)meshData);
        }
        RenderSystem.disableBlend();
        if (!this.parts.isEmpty()) {
            double angleStep = Math.PI * 2 / (double)this.parts.size();
            for (int i = 0; i < this.parts.size(); ++i) {
                boolean isHovered;
                ToggleablePart part = this.parts.get(i);
                double itemAngle = (double)i * angleStep - 1.5707963267948966;
                int x = centerX + (int)(70.0 * Math.cos(itemAngle));
                int y = centerY + (int)(70.0 * Math.sin(itemAngle));
                boolean isActive = part.item.isActive(part.stack);
                boolean bl = isHovered = mouseX >= x - 12 && mouseX <= x + 12 && mouseY >= y - 12 && mouseY <= y + 12;
                if (isHovered) {
                    MutableComponent statusText = isActive ? Component.translatable((String)"cyberware.gui.active") : Component.translatable((String)"cyberware.gui.inactive");
                    g.drawCenteredString(this.font, (Component)statusText, x, y - 20, -256);
                }
                g.renderItem(part.stack, x - 8, y - 8);
                int outlineColor = isActive ? -16711936 : -65536;
                g.renderOutline(x - 10, y - 10, 20, 20, outlineColor);
                if (!isHovered) continue;
                g.renderTooltip(this.font, part.stack, mouseX, mouseY);
            }
        }
    }

    private void renderColorSettings(GuiGraphics g, int centerX, int centerY, int mouseX, int mouseY) {
        int boxWidth = 80;
        this.hexInput.setX(centerX - boxWidth / 2);
        this.hexInput.setY(centerY + 15);
        this.hexInput.setVisible(true);
        int swatchSize = 20;
        int gap = 4;
        int totalWidth = swatchSize * PRESET_COLORS.length + gap * (PRESET_COLORS.length - 1);
        int startX = centerX - totalWidth / 2;
        int startY = centerY - 15;
        for (int i = 0; i < PRESET_COLORS.length; ++i) {
            int color = PRESET_COLORS[i];
            int x = startX + (swatchSize + gap) * i;
            int y = startY;
            g.fill(x, y, x + swatchSize, y + swatchSize, color);
            g.renderOutline(x, y, swatchSize, swatchSize, -7829368);
            if (mouseX >= x && mouseX <= x + swatchSize && mouseY >= y && mouseY <= y + swatchSize) {
                g.renderOutline(x - 1, y - 1, swatchSize + 2, swatchSize + 2, -1);
            }
            if (color != ClientCyberwareSettings.hudColor) continue;
            g.renderOutline(x - 2, y - 2, swatchSize + 4, swatchSize + 4, -256);
        }
        g.drawCenteredString(this.font, "HUD Color Palette", centerX, startY - 15, -1);
        g.drawCenteredString(this.font, "(Hex)", centerX, this.hexInput.getY() + 22, -5592406);
    }

    private void renderHudPreview(GuiGraphics g, int mouseX, int mouseY) {
        if (this.minecraft.player == null) {
            return;
        }
        int hudX = ClientCyberwareSettings.hudX;
        int hudY = ClientCyberwareSettings.hudY;
        int barX = ClientCyberwareSettings.barX;
        int barY = ClientCyberwareSettings.barY;
        if (this.isHudMoveMode) {
            float[] rgba = ClientCyberwareSettings.getColorFloats();
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)HUD_RESET_ICON);
            g.renderOutline(hudX - 1, hudY - 1, 82, 27, -16711936);
            g.drawCenteredString(this.font, "BATTERY", hudX + 40, hudY - 10, -16711936);
            if (this.isDraggingHud || mouseX >= hudX && mouseX <= hudX + 80 && mouseY >= hudY && mouseY <= hudY + 25) {
                g.renderOutline(hudX - 1, hudY - 1, 82, 27, -1);
            }
            int batResetBtnX = hudX + 32;
            int batResetBtnY = hudY + 25 + 5;
            g.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
            g.blit(HUD_RESET_ICON, batResetBtnX, batResetBtnY, 0.0f, 0.0f, 16, 16, 16, 16);
            g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            if (mouseX >= batResetBtnX && mouseX <= batResetBtnX + 16 && mouseY >= batResetBtnY && mouseY <= batResetBtnY + 16) {
                g.renderOutline(batResetBtnX - 1, batResetBtnY - 1, 18, 18, -1);
                g.renderTooltip(this.font, (Component)Component.literal((String)"Reset Battery HUD"), mouseX, mouseY);
            }
            g.renderOutline(barX - 1, barY - 1, 28, 212, -16711936);
            g.drawCenteredString(this.font, "STATUS BAR", barX + 13, barY - 10, -16711936);
            if (this.isDraggingBar || mouseX >= barX && mouseX <= barX + 26 && mouseY >= barY && mouseY <= barY + 210) {
                g.renderOutline(barX - 1, barY - 1, 28, 212, -1);
            }
            int barResetBtnX = barX + 5;
            int barResetBtnY = barY + 210 + 5;
            g.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
            g.blit(HUD_RESET_ICON, barResetBtnX, barResetBtnY, 0.0f, 0.0f, 16, 16, 16, 16);
            g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            if (mouseX >= barResetBtnX && mouseX <= barResetBtnX + 16 && mouseY >= barResetBtnY && mouseY <= barResetBtnY + 16) {
                g.renderOutline(barResetBtnX - 1, barResetBtnY - 1, 18, 18, -1);
                g.renderTooltip(this.font, (Component)Component.literal((String)"Reset Status Bar"), mouseX, mouseY);
            }
            int ltrBtnX = barResetBtnX - 20;
            int ltrBtnY = barResetBtnY;
            int rtlBtnX = barResetBtnX + 20;
            int rtlBtnY = barResetBtnY;
            int hudColorInt = ClientCyberwareSettings.hudColor;
            boolean ltrSelected = ClientCyberwareSettings.slideDirection == 0;
            boolean rtlSelected = ClientCyberwareSettings.slideDirection == 1;
            g.fill(ltrBtnX, ltrBtnY, ltrBtnX + 16, ltrBtnY + 16, 0x40000000 | hudColorInt & 0xFFFFFF);
            g.renderOutline(ltrBtnX, ltrBtnY, 16, 16, ltrSelected ? -1 : 0x60000000 | hudColorInt & 0xFFFFFF);
            g.drawString(this.font, "\u2192", ltrBtnX + 4, ltrBtnY + 4, ltrSelected ? -1 : -5592406, false);
            if (mouseX >= ltrBtnX && mouseX <= ltrBtnX + 16 && mouseY >= ltrBtnY && mouseY <= ltrBtnY + 16) {
                g.renderOutline(ltrBtnX - 1, ltrBtnY - 1, 18, 18, -1);
                g.renderTooltip(this.font, (Component)Component.literal((String)"Slide: Left to Right"), mouseX, mouseY);
            }
            g.fill(rtlBtnX, rtlBtnY, rtlBtnX + 16, rtlBtnY + 16, 0x40000000 | hudColorInt & 0xFFFFFF);
            g.renderOutline(rtlBtnX, rtlBtnY, 16, 16, rtlSelected ? -1 : 0x60000000 | hudColorInt & 0xFFFFFF);
            g.drawString(this.font, "\u2190", rtlBtnX + 4, rtlBtnY + 4, rtlSelected ? -1 : -5592406, false);
            if (mouseX >= rtlBtnX && mouseX <= rtlBtnX + 16 && mouseY >= rtlBtnY && mouseY <= rtlBtnY + 16) {
                g.renderOutline(rtlBtnX - 1, rtlBtnY - 1, 18, 18, -1);
                g.renderTooltip(this.font, (Component)Component.literal((String)"Slide: Right to Left"), mouseX, mouseY);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            if (!this.isHudMoveMode) {
                int colorBtnX = this.btnX;
                int colorBtnY = this.btnY + 20;
                if (mouseX >= (double)colorBtnX && mouseX <= (double)(colorBtnX + 16) && mouseY >= (double)colorBtnY && mouseY <= (double)(colorBtnY + 16)) {
                    this.toggleColorSettings();
                    this.playClickSound();
                    return true;
                }
            }
            if (!this.isColorSettingsOpen && !this.isHudMoveMode && mouseX >= (double)this.btnX && mouseX <= (double)(this.btnX + 16) && mouseY >= (double)this.btnY && mouseY <= (double)(this.btnY + 16)) {
                this.isHudMoveMode = true;
                this.playClickSound();
                return true;
            }
            if (this.isColorSettingsOpen) {
                if (this.hexInput.mouseClicked(mouseX, mouseY, button)) {
                    this.setFocused((GuiEventListener)this.hexInput);
                    return true;
                }
                int swatchSize = 20;
                int gap = 4;
                int totalWidth = swatchSize * PRESET_COLORS.length + gap * (PRESET_COLORS.length - 1);
                int startX = centerX - totalWidth / 2;
                for (int i = 0; i < PRESET_COLORS.length; ++i) {
                    int x = startX + (swatchSize + gap) * i;
                    if (!(mouseX >= (double)x) || !(mouseX <= (double)(x + swatchSize)) || !(mouseY >= (double)(centerY - 15)) || !(mouseY <= (double)(centerY - 15 + swatchSize))) continue;
                    ClientCyberwareSettings.hudColor = PRESET_COLORS[i];
                    this.hexInput.setValue(ClientCyberwareSettings.getHudColorAsHex());
                    this.playClickSound();
                    return true;
                }
            }
            if (this.isHudMoveMode) {
                int hudX = ClientCyberwareSettings.hudX;
                int hudY = ClientCyberwareSettings.hudY;
                int barX = ClientCyberwareSettings.barX;
                int barY = ClientCyberwareSettings.barY;
                int batResetBtnX = hudX + 32;
                int batResetBtnY = hudY + 25 + 5;
                int barResetBtnX = barX + 5;
                int barResetBtnY = barY + 210 + 5;
                int ltrBtnX = barResetBtnX - 20;
                int ltrBtnY = barResetBtnY;
                int rtlBtnX = barResetBtnX + 20;
                int rtlBtnY = barResetBtnY;
                if (mouseX >= (double)batResetBtnX && mouseX <= (double)(batResetBtnX + 16) && mouseY >= (double)batResetBtnY && mouseY <= (double)(batResetBtnY + 16)) {
                    ClientCyberwareSettings.hudX = 10;
                    ClientCyberwareSettings.hudY = 10;
                    this.playClickSound();
                    return true;
                }
                if (mouseX >= (double)barResetBtnX && mouseX <= (double)(barResetBtnX + 16) && mouseY >= (double)barResetBtnY && mouseY <= (double)(barResetBtnY + 16)) {
                    ClientCyberwareSettings.barX = 10;
                    ClientCyberwareSettings.barY = 40;
                    this.playClickSound();
                    return true;
                }
                if (mouseX >= (double)ltrBtnX && mouseX <= (double)(ltrBtnX + 16) && mouseY >= (double)ltrBtnY && mouseY <= (double)(ltrBtnY + 16)) {
                    ClientCyberwareSettings.slideDirection = 0;
                    this.playClickSound();
                    return true;
                }
                if (mouseX >= (double)rtlBtnX && mouseX <= (double)(rtlBtnX + 16) && mouseY >= (double)rtlBtnY && mouseY <= (double)(rtlBtnY + 16)) {
                    ClientCyberwareSettings.slideDirection = 1;
                    this.playClickSound();
                    return true;
                }
                if (mouseX >= (double)hudX && mouseX <= (double)(hudX + 80) && mouseY >= (double)hudY && mouseY <= (double)(hudY + 25)) {
                    this.isDraggingHud = true;
                    this.dragOffsetX = (int)mouseX - hudX;
                    this.dragOffsetY = (int)mouseY - hudY;
                    this.playClickSound();
                    return true;
                }
                if (mouseX >= (double)barX && mouseX <= (double)(barX + 26) && mouseY >= (double)barY && mouseY <= (double)(barY + 210)) {
                    this.isDraggingBar = true;
                    this.dragBarOffsetX = (int)mouseX - barX;
                    this.dragBarOffsetY = (int)mouseY - barY;
                    this.playClickSound();
                    return true;
                }
                return true;
            }
            if (!this.isColorSettingsOpen && !this.isHudMoveMode) {
                double angleStep = Math.PI * 2 / (double)this.parts.size();
                for (int i = 0; i < this.parts.size(); ++i) {
                    ToggleablePart part = this.parts.get(i);
                    double itemAngle = (double)i * angleStep - 1.5707963267948966;
                    int x = centerX + (int)(70.0 * Math.cos(itemAngle));
                    int y = centerY + (int)(70.0 * Math.sin(itemAngle));
                    if (!(mouseX >= (double)(x - 12)) || !(mouseX <= (double)(x + 12)) || !(mouseY >= (double)(y - 12)) || !(mouseY <= (double)(y + 12))) continue;
                    PacketDistributor.sendToServer((CustomPacketPayload)new ToggleCyberwarePacket(part.slotId), (CustomPacketPayload[])new CustomPacketPayload[0]);
                    this.playClickSound();
                    part.item.toggle(part.stack);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void toggleColorSettings() {
        this.isColorSettingsOpen = !this.isColorSettingsOpen;
        this.hexInput.setVisible(this.isColorSettingsOpen);
        if (!this.isColorSettingsOpen) {
            this.hexInput.setFocused(false);
            this.setFocused(null);
        }
    }

    private void playClickSound() {
        Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isHudMoveMode && button == 0) {
            if (this.isDraggingHud) {
                ClientCyberwareSettings.hudX = (int)mouseX - this.dragOffsetX;
                ClientCyberwareSettings.hudY = (int)mouseY - this.dragOffsetY;
                return true;
            }
            if (this.isDraggingBar) {
                ClientCyberwareSettings.barX = (int)mouseX - this.dragBarOffsetX;
                ClientCyberwareSettings.barY = (int)mouseY - this.dragBarOffsetY;
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.isDraggingHud) {
                this.isDraggingHud = false;
                return true;
            }
            if (this.isDraggingBar) {
                this.isDraggingBar = false;
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean isPauseScreen() {
        return false;
    }

    private record ToggleablePart(int slotId, ItemStack stack, ICyberware item) {
    }
}

