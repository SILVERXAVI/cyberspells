/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner
 *  net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil
 *  net.minecraft.locale.Language
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  org.joml.Vector2ic
 *  org.joml.Vector4i
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.joml.Vector2ic;
import org.joml.Vector4i;

public class ScreenTooltipOverlay
implements LayeredDraw.Layer {
    public static final ScreenTooltipOverlay instance = new ScreenTooltipOverlay();
    RenderInfo toRender = null;

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui || this.toRender == null) {
            return;
        }
        PoseStack pose = guiHelper.pose();
        Font font = Minecraft.getInstance().font;
        List<ClientTextTooltip> components = Language.getInstance().getVisualOrder(this.toRender.tooltip.stream().map(ScreenTooltipOverlay::cast).toList()).stream().map(ClientTextTooltip::new).toList();
        int maxTextWidth = 0;
        int totalHeight = components.size() == 1 ? -2 : 0;
        for (ClientTooltipComponent clientTooltipComponent : components) {
            int k = clientTooltipComponent.getWidth(font);
            if (k > maxTextWidth) {
                maxTextWidth = k;
            }
            totalHeight += clientTooltipComponent.getHeight();
        }
        Vector2ic vector2ic = this.toRender.positioner.positionTooltip(guiHelper.guiWidth(), guiHelper.guiHeight(), 0, 0, maxTextWidth, totalHeight);
        int n = vector2ic.x();
        int y = vector2ic.y();
        pose.pushPose();
        Integer bgColor1 = this.toRender.colors.map(Vector4i::x).orElse(-1877999600);
        Integer bgColor2 = this.toRender.colors.map(Vector4i::y).orElse(-1877999600);
        Integer edgeColor1 = this.toRender.colors.map(Vector4i::z).orElse(1884291327);
        Integer edgeColor2 = this.toRender.colors.map(Vector4i::w).orElse(1881669759);
        TooltipRenderUtil.renderTooltipBackground((GuiGraphics)guiHelper, (int)n, (int)y, (int)maxTextWidth, (int)totalHeight, (int)400, (int)bgColor1, (int)bgColor2, (int)edgeColor1, (int)edgeColor2);
        guiHelper.flush();
        pose.translate(0.0f, 0.0f, 400.0f);
        int lineY = y;
        for (int l1 = 0; l1 < components.size(); ++l1) {
            ClientTooltipComponent clienttooltipcomponent1 = (ClientTooltipComponent)components.get(l1);
            clienttooltipcomponent1.renderText(font, n, lineY, pose.last().pose(), guiHelper.bufferSource());
            lineY += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
        }
        pose.popPose();
        this.toRender = null;
    }

    private static FormattedText cast(Component component) {
        return component;
    }

    public static void renderTooltip(List<Component> tooltip, ClientTooltipPositioner positioner) {
        ScreenTooltipOverlay.instance.toRender = new RenderInfo(tooltip, positioner, Optional.empty());
    }

    public static void renderTooltip(List<Component> tooltip, ClientTooltipPositioner positioner, Vector4i colors) {
        ScreenTooltipOverlay.instance.toRender = new RenderInfo(tooltip, positioner, Optional.of(colors));
    }

    private record RenderInfo(List<Component> tooltip, ClientTooltipPositioner positioner, Optional<Vector4i> colors) {
    }
}

