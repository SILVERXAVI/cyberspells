/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package com.maxwell.cyber_ware_port.client.screen;

import com.maxwell.cyber_ware_port.common.container.ScannerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ScannerScreen
extends AbstractContainerScreen<ScannerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/scanner_gui.png");
    private static final int SAYING_COUNT = 74;
    private final List<Component> logLines = new ArrayList<Component>();
    private final RandomSource random = RandomSource.create();
    private int tickCounter = 0;

    public ScannerScreen(ScannerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super((AbstractContainerMenu)pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    protected void containerTick() {
        super.containerTick();
        if (((ScannerMenu)this.menu).isCrafting()) {
            ++this.tickCounter;
            if (this.tickCounter % 5 == 0) {
                this.addRandomLog();
            }
        } else if (!this.logLines.isEmpty()) {
            this.logLines.clear();
        }
    }

    private void addRandomLog() {
        int index = this.random.nextInt(74);
        String key = "cyberware.gui.scanner_saying." + index;
        this.logLines.add((Component)Component.translatable((String)key));
        if (this.logLines.size() > 1) {
            this.logLines.remove(0);
        }
    }

    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        int color = 0x55FFFF;
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, color, false);
        MutableComponent warnText = Component.literal((String)"Destroys Cyberware").withStyle(ChatFormatting.RED);
        int warnWidth = this.font.width((FormattedText)warnText);
        guiGraphics.drawString(this.font, (Component)warnText, this.imageWidth - warnWidth - 9, this.titleLabelY, 0xFFFFFF, false);
        MutableComponent chanceText = Component.literal((String)"50% Chance").withStyle(ChatFormatting.YELLOW);
        int chanceWidth = this.font.width((FormattedText)chanceText);
        guiGraphics.drawString(this.font, (Component)chanceText, this.imageWidth - chanceWidth - 8, this.titleLabelY + 10, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, color, false);
    }

    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        if (!this.logLines.isEmpty()) {
            Component line = this.logLines.get(this.logLines.size() - 1);
            int logStartX = x + 8;
            int logStartY = y + 20;
            int logColor = 0x55FFFF;
            guiGraphics.drawString(this.font, line, logStartX, logStartY, logColor, false);
        }
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)TEXTURE);
        guiGraphics.blit(TEXTURE, x + 4, y + 30, 0, 166, 161, 8);
        int maxBarWidth = 161;
        int progressWidth = ((ScannerMenu)this.menu).getScaledProgress(maxBarWidth);
        if (progressWidth > 0) {
            guiGraphics.blit(TEXTURE, x + 4, y + 30, 0, 175, progressWidth, 8);
        }
        RenderSystem.disableBlend();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

