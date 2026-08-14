/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 */
package com.maxwell.cyber_ware_port.client.creativetab;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CyberwareSideTabButton
extends Button {
    public CyberwareSideTabButton(int x, int y, int width, int height, Button.OnPress onPress) {
        super(x, y, width, height, (Component)Component.empty(), onPress, DEFAULT_NARRATION);
    }

    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        if (this.isHovered()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            g.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x1EFFFFFF);
            RenderSystem.disableBlend();
        }
    }
}

