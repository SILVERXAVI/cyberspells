/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MarkerManager {
    private final List<Marker> markers = new ArrayList<Marker>();
    private Marker hovered = null;
    private final ResourceLocation icon;

    public MarkerManager(ResourceLocation iconTexture) {
        this.icon = iconTexture;
    }

    public void add(Marker marker) {
        this.markers.add(marker);
    }

    public void clear() {
        this.markers.clear();
    }

    public Marker getHovered() {
        return this.hovered;
    }

    public void render(GuiGraphics gui, int modelX, int modelY, double mouseX, double mouseY, RobosurgeonScreen.ViewMode viewMode, float markerPhase, Font font) {
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)this.icon);
        this.hovered = null;
        for (Marker marker : this.markers) {
            float amp;
            boolean visible;
            boolean bl = visible = marker.parent == viewMode;
            if (!visible) continue;
            int px = modelX + marker.offX;
            int py = modelY + marker.offY;
            float f = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LARM || marker.target == RobosurgeonScreen.ViewMode.RARM) ? 30.0f : (amp = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LLEG || marker.target == RobosurgeonScreen.ViewMode.RLEG) ? 14.0f : 4.0f);
            if (marker.animated && viewMode.allowMarkerAnimation) {
                float offset = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LLEG || marker.target == RobosurgeonScreen.ViewMode.RLEG) && marker.offX == -2 ? -amp * markerPhase : amp * (marker.offX > 0 ? -markerPhase : markerPhase);
                px += (int)offset;
            }
            if (mouseX >= (double)px && mouseX <= (double)(px + 16) && mouseY >= (double)py && mouseY <= (double)(py + 16)) {
                this.hovered = marker;
            }
            gui.pose().pushPose();
            gui.pose().translate(0.0f, 0.0f, 200.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float alpha = this.hovered == marker ? 1.0f : 0.2f;
            gui.setColor(1.0f, 1.0f, 1.0f, alpha);
            gui.blit(this.icon, px, py, 0.0f, 0.0f, 16, 16, 16, 16);
            gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
            gui.pose().popPose();
        }
        if (this.hovered != null) {
            gui.renderTooltip(font, this.hovered.tooltip, (int)mouseX, (int)mouseY);
        }
    }

    public RobosurgeonScreen.ViewMode tryClick(double mouseX, double mouseY, int modelX, int modelY, float markerPhase, RobosurgeonScreen.ViewMode viewMode) {
        for (Marker marker : this.markers) {
            float amp;
            boolean visible;
            boolean bl = visible = marker.parent == viewMode;
            if (!visible) continue;
            int px = modelX + marker.offX;
            int py = modelY + marker.offY;
            float f = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LARM || marker.target == RobosurgeonScreen.ViewMode.RARM) ? 30.0f : (amp = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LLEG || marker.target == RobosurgeonScreen.ViewMode.RLEG) ? 14.0f : 4.0f);
            if (marker.animated && viewMode.allowMarkerAnimation) {
                float offset = marker.parent == RobosurgeonScreen.ViewMode.FULL_BODY && (marker.target == RobosurgeonScreen.ViewMode.LLEG || marker.target == RobosurgeonScreen.ViewMode.RLEG) && marker.offX == -2 ? -amp * markerPhase : amp * (marker.offX > 0 ? -markerPhase : markerPhase);
                px += (int)offset;
            }
            if (!(mouseX >= (double)px) || !(mouseX <= (double)(px + 16)) || !(mouseY >= (double)py) || !(mouseY <= (double)(py + 16))) continue;
            return marker.target;
        }
        return null;
    }

    public static class Marker {
        public final int offX;
        public final int offY;
        public final RobosurgeonScreen.ViewMode parent;
        public final RobosurgeonScreen.ViewMode target;
        public final Component tooltip;
        public final boolean animated;

        public Marker(int x, int y, RobosurgeonScreen.ViewMode parent, RobosurgeonScreen.ViewMode target, Component tip, boolean animated) {
            this.offX = x;
            this.offY = y;
            this.parent = parent;
            this.target = target;
            this.tooltip = tip;
            this.animated = animated;
        }

        public Marker(int x, int y, RobosurgeonScreen.ViewMode parent, RobosurgeonScreen.ViewMode target, Component tip) {
            this(x, y, parent, target, tip, true);
        }
    }
}

