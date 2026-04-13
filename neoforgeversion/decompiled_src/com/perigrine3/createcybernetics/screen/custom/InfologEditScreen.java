/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.components.MultiLineEditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.DyedItemColor
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.network.payload.InfologSaveChipwarePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.network.PacketDistributor;

public final class InfologEditScreen
extends Screen {
    private static final int GUI_W = 256;
    private static final int GUI_H = 256;
    private static final int PAD_L = 14;
    private static final int PAD_T = 14;
    private static final int PAD_R = 14;
    private static final int PAD_B = 42;
    private static final int NEON_GREEN = -16711834;
    private final int chipwareSlot;
    private final String initialText;
    private int leftPos;
    private int topPos;
    private int editX;
    private int editY;
    private int editW;
    private int editH;
    private int accentColor = -1;
    private MultiLineEditBox editor;

    public InfologEditScreen(int chipwareSlot, String initialText) {
        super((Component)Component.translatable((String)"gui.infolog.title"));
        this.chipwareSlot = chipwareSlot;
        this.initialText = initialText == null ? "" : initialText;
    }

    protected void init() {
        this.leftPos = (this.width - 256) / 2;
        this.topPos = (this.height - 256) / 2;
        this.editX = this.leftPos + 14;
        this.editY = this.topPos + 14;
        this.editW = 228;
        this.editH = 200;
        this.accentColor = this.resolveChipDyeOrWhite();
        this.editor = new MultiLineEditBox(this.font, this.editX, this.editY, this.editW, this.editH, (Component)Component.empty(), (Component)Component.empty());
        this.editor.setValue(this.initialText);
        this.addRenderableWidget((GuiEventListener)this.editor);
        this.setInitialFocus((GuiEventListener)this.editor);
        int btnY = this.editY + this.editH + 10;
        int btnW = 90;
        int gap = 10;
        int totalW = btnW + gap + btnW;
        int startX = this.editX + (this.editW - totalW) / 2;
        this.addRenderableWidget((GuiEventListener)new AccentButton(startX, btnY, btnW, 20, (Component)Component.translatable((String)"gui.cancel"), b -> this.onClose(), this.accentColor));
        this.addRenderableWidget((GuiEventListener)new AccentButton(startX + btnW + gap, btnY, btnW, 20, (Component)Component.translatable((String)"gui.done"), b -> {
            this.sendSave();
            this.onClose();
        }, this.accentColor));
    }

    private int resolveChipDyeOrWhite() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return -1;
        }
        if (!mc.player.hasData(ModAttachments.CYBERWARE)) {
            return -1;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return -1;
        }
        if (this.chipwareSlot < 0 || this.chipwareSlot >= 2) {
            return -1;
        }
        ItemStack st = data.getChipwareStack(this.chipwareSlot);
        if (st.isEmpty()) {
            return -1;
        }
        DyedItemColor dyed = (DyedItemColor)st.get(DataComponents.DYED_COLOR);
        if (dyed == null) {
            return -1;
        }
        return 0xFF000000 | dyed.rgb() & 0xFFFFFF;
    }

    private void sendSave() {
        String text = this.editor.getValue();
        if (text == null) {
            text = "";
        }
        if (text.length() > 32000) {
            text = text.substring(0, 32000);
        }
        PacketDistributor.sendToServer((CustomPacketPayload)new InfologSaveChipwarePayload(this.chipwareSlot, text, false), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        super.render(gg, mouseX, mouseY, partialTick);
        InfologEditScreen.drawBorder(gg, this.editX - 1, this.editY - 1, this.editW + 2, this.editH + 2, this.accentColor);
    }

    private static void drawBorder(GuiGraphics gg, int x, int y, int w, int h, int argb) {
        gg.fill(x, y, x + w, y + 1, argb);
        gg.fill(x, y + h - 1, x + w, y + h, argb);
        gg.fill(x, y, x + 1, y + h, argb);
        gg.fill(x + w - 1, y, x + w, y + h, argb);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }

    private static final class AccentButton
    extends Button {
        private final int accent;

        private AccentButton(int x, int y, int w, int h, Component msg, Button.OnPress onPress, int accent) {
            super(x, y, w, h, msg, onPress, DEFAULT_NARRATION);
            this.accent = accent;
        }

        protected void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
            int x = this.getX();
            int y = this.getY();
            int w = this.getWidth();
            int h = this.getHeight();
            gg.fill(x, y, x + w, y + h, -16777216);
            InfologEditScreen.drawBorder(gg, x, y, w, h, this.accent);
            if (this.isHoveredOrFocused()) {
                gg.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0x22000000);
            }
            int textX = x + w / 2;
            int textY = y + (h - 8) / 2;
            gg.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), textX, textY, this.accent);
        }
    }
}

