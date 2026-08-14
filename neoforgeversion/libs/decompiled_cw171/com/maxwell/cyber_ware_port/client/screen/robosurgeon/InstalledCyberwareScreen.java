/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.client.screen.robosurgeon;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;

public class InstalledCyberwareScreen
extends Screen {
    private static final int ITEM_HEIGHT = 20;
    private static final int SCROLL_BAR_WIDTH = 6;
    private final Screen previousScreen;
    private final List<ItemStack> installedCyberware = new ArrayList<ItemStack>();
    private double scrollOffset = 0.0;

    public InstalledCyberwareScreen(Screen previousScreen) {
        super((Component)Component.translatable((String)"gui.cyber_ware_port.installed_cyberware.title"));
        this.previousScreen = previousScreen;
    }

    protected void init() {
        super.init();
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)"gui.back"), button -> this.onClose()).bounds(this.width / 2 - 100, this.height - 28, 200, 20).build());
        if (this.minecraft != null && this.minecraft.player != null) {
            CyberwareUserData cyberware = (CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            ItemStackHandler installed = cyberware.getInstalledCyberware();
            for (int i = 0; i < installed.getSlots(); ++i) {
                ItemStack stack = installed.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                this.installedCyberware.add(stack);
            }
        }
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int listTop = 32;
        int listBottom = this.height - 36;
        int listLeft = this.width / 2 - 120;
        int listRight = this.width / 2 + 120;
        guiGraphics.fill(listLeft, listTop, listRight, listBottom, Integer.MIN_VALUE);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        guiGraphics.enableScissor(listLeft, listTop, listRight, listBottom);
        int currentY = listTop + 4 - (int)this.scrollOffset;
        for (ItemStack stack : this.installedCyberware) {
            guiGraphics.renderItem(stack, listLeft + 5, currentY);
            guiGraphics.drawString(this.font, stack.getHoverName(), listLeft + 28, currentY + 5, 0xFFFFFF);
            if (mouseY >= listTop && mouseY < listBottom && this.isMouseOver(mouseX, mouseY, listLeft + 5, currentY, 16, 16)) {
                guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
            }
            currentY += 20;
        }
        guiGraphics.disableScissor();
        int listHeight = listBottom - listTop;
        int contentHeight = this.installedCyberware.size() * 20;
        if (contentHeight > listHeight) {
            int scrollBarHeight = (int)((float)listHeight / (float)contentHeight * (float)listHeight);
            int scrollBarY = listTop + (int)((float)this.scrollOffset / (float)(contentHeight - listHeight) * (float)(listHeight - scrollBarHeight));
            guiGraphics.fill(listRight - 6 - 1, scrollBarY, listRight - 1, scrollBarY + scrollBarHeight, -1);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int listHeight = this.height - 36 - 32;
        int contentHeight = this.installedCyberware.size() * 20;
        int maxScroll = Math.max(0, contentHeight - listHeight);
        this.scrollOffset -= scrollY * 10.0;
        this.scrollOffset = Math.max(0.0, Math.min(this.scrollOffset, (double)maxScroll));
        return true;
    }

    private boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= (double)x && mouseX < (double)(x + width) && mouseY >= (double)y && mouseY < (double)(y + height);
    }

    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.previousScreen);
        }
    }

    public boolean isPauseScreen() {
        return false;
    }
}

