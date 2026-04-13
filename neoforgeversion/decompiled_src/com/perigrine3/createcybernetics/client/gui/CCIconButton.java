/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractButton
 *  net.minecraft.client.gui.components.Tooltip
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class CCIconButton
extends AbstractButton {
    private final Icon icon;
    private final Runnable onPress;
    private final String markerId;
    private static final int DRAW_ICON_W = 16;
    private static final int DRAW_ICON_H = 16;
    private static final int ICON_TEX_W = 16;
    private static final int ICON_TEX_H = 16;

    public CCIconButton(int x, int y, int w, int h, Icon icon, Component tooltip, String markerId, Runnable onPress) {
        super(x, y, w, h, (Component)Component.empty());
        this.icon = icon;
        this.onPress = onPress;
        this.markerId = markerId;
        this.setTooltip(Tooltip.create((Component)tooltip));
    }

    public String cc$getMarkerId() {
        return this.markerId;
    }

    public void onPress() {
        if (this.onPress != null) {
            this.onPress.run();
        }
    }

    protected void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(gg, mouseX, mouseY, partialTick);
        int ix = this.getX() + (this.width - 16) / 2;
        int iy = this.getY() + (this.height - 16) / 2;
        if (!this.active) {
            gg.setColor(1.0f, 1.0f, 1.0f, 0.35f);
        }
        gg.blit(this.icon.texture, ix, iy, 0.0f, 0.0f, 16, 16, 16, 16);
        gg.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected void updateWidgetNarration(NarrationElementOutput narration) {
        this.defaultButtonNarrationText(narration);
    }

    public static enum Icon {
        CYBEREYE_SKIN(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/cybereye_skin.png")),
        HUD_LAYOUT(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/hud_layout.png"));

        final ResourceLocation texture;

        private Icon(ResourceLocation texture) {
            this.texture = texture;
        }
    }
}

