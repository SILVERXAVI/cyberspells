/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent$BossEventProgress
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import org.joml.Vector3f;

@EventBusSubscriber(value={Dist.CLIENT})
public class RecastOverlay
implements LayeredDraw.Layer {
    public static RecastOverlay instance = new RecastOverlay();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    static final int IMAGE_WIDTH = 54;
    static final int COMPLETION_BAR_WIDTH = 44;
    static final int IMAGE_HEIGHT = 21;
    static final int CONNECTOR_WIDTH = 6;
    static final int ORB_WIDTH = 10;
    static final int ORB_TEXTURE_OFFSET_X = 99;
    static final int ORB_TEXTURE_OFFSET_Y = 5;
    static final int CONNECTOR_TEXTURE_OFFSET_X = 109;
    static final int CONNECTOR_TEXTURE_OFFSET_Y = 8;
    int bossbarOffset;

    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        if (!ClientMagicData.getRecasts().hasRecastsActive()) {
            return;
        }
        int totalHeightPerBar = 18;
        int screenTopBuffer = 6;
        List<RecastInstance> activeRecasts = ClientMagicData.getRecasts().getActiveRecasts();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Anchor anchor = (Anchor)((Object)ClientConfigs.RECAST_ANCHOR.get());
        for (int castIndex = 0; castIndex < activeRecasts.size(); ++castIndex) {
            RecastInstance recastInstance = activeRecasts.get(castIndex);
            AbstractSpell spell = SpellRegistry.getSpell(recastInstance.getSpellId());
            int total = recastInstance.getTotalRecasts();
            int remaining = recastInstance.getRemainingRecasts();
            int totalWidth = total * 10 + (total - 1) * 6;
            int barX = (int)((float)screenWidth * anchor.m1);
            int barY = (int)((float)screenHeight * anchor.m2);
            if (anchor == Anchor.Center || anchor == Anchor.TopCenter) {
                barX -= totalWidth / 2;
            }
            if (anchor == Anchor.TopCenter) {
                barY += screenTopBuffer + this.bossbarOffset;
            }
            barY += ((Integer)ClientConfigs.RECAST_Y_OFFSET.get()).intValue();
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate((float)((barX += ((Integer)ClientConfigs.RECAST_X_OFFSET.get()).intValue()) - 18), (float)((barY += totalHeightPerBar * castIndex) - 2), 0.0f);
            poseStack.scale(0.85f, 0.85f, 0.85f);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)spell.getSpellIconResource());
            guiGraphics.blit(spell.getSpellIconResource(), 0, 0, 0.0f, 0.0f, 16, 16, 16, 16);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TEXTURE);
            guiGraphics.blit(TEXTURE, -2, -2, 116.0f, 0.0f, 20, 20, 256, 256);
            poseStack.popPose();
            for (int i = 0; i < total; ++i) {
                int orbX = barX + 16 * i;
                int connectorX = orbX + 10;
                if (i + 1 < total) {
                    guiGraphics.blit(TEXTURE, connectorX, barY + 3, 109.0f, 8.0f, 6, 4, 256, 256);
                }
                boolean charged = i < remaining;
                guiGraphics.blit(TEXTURE, orbX, barY, (float)(99 + (charged ? 0 : 10)), 26.0f, 10, 10, 256, 256);
                if (charged) {
                    Vector3f color = spell.getSchoolType().getTargetingColor();
                    RenderSystem.setShaderColor((float)color.x(), (float)color.y(), (float)color.z(), (float)1.0f);
                    guiGraphics.blit(TEXTURE, orbX, barY, (float)(99 + (charged ? 0 : 10)), 26.0f, 10, 10, 256, 256);
                    RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                guiGraphics.blit(TEXTURE, orbX, barY, 99.0f, 5.0f, 10, 10, 256, 256);
            }
            int textX = barX + 16 * total;
            Font font = Minecraft.getInstance().font;
            String string = RecastOverlay.formatTime(recastInstance.getTicksRemaining(), recastInstance.getTicksToLive());
            Objects.requireNonNull(Minecraft.getInstance().font);
            guiGraphics.drawString(font, string, textX, barY + (10 - 9) / 2, ChatFormatting.WHITE.getColor().intValue());
        }
        this.bossbarOffset = 0;
    }

    private static String formatTime(int ticksRemaining, int totalTicks) {
        int totalSeconds = totalTicks / 20;
        int remainingSeconds = ticksRemaining / 20;
        Object time = "";
        if (totalSeconds > 60) {
            time = (String)time + String.format("%s:", remainingSeconds / 60);
            remainingSeconds %= 60;
        }
        if (totalSeconds >= 10) {
            time = (String)time + remainingSeconds / 10;
        }
        time = (String)time + remainingSeconds % 10;
        return (String)time + "s";
    }

    @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
    public static void countBossBars(CustomizeGuiOverlayEvent.BossEventProgress event) {
        RecastOverlay.instance.bossbarOffset += event.getIncrement();
    }

    public static enum Anchor {
        Center(0.5f, 0.5f),
        TopCenter(0.5f, 0.0f),
        TopLeft(0.0f, 0.0f),
        TopRight(0.0f, 1.0f),
        BottomLeft(0.0f, 1.0f),
        BottomRight(1.0f, 1.0f);

        final float m1;
        final float m2;

        private Anchor(float mx, float my) {
            this.m1 = mx;
            this.m2 = my;
        }
    }
}

