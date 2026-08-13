/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.gui.overlays;

import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Objects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public class CastBarOverlay
implements LayeredDraw.Layer {
    public static CastBarOverlay instance = new CastBarOverlay();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    static final int IMAGE_WIDTH = 54;
    static final int COMPLETION_BAR_WIDTH = 44;
    static final int IMAGE_HEIGHT = 21;

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        if (!ClientMagicData.isCasting() || ClientMagicData.isCasting() && ClientMagicData.getCastType() == CastType.INSTANT) {
            return;
        }
        float castCompletionPercent = ClientMagicData.getCastCompletionPercent();
        String castTimeString = Utils.timeFromTicks((1.0f - castCompletionPercent) * (float)ClientMagicData.getCastDuration(), 1);
        if (ClientMagicData.getCastType() == CastType.CONTINUOUS) {
            castCompletionPercent = 1.0f - castCompletionPercent;
        }
        int barX = screenWidth / 2 - 27;
        int barY = screenHeight / 2 + screenHeight / 8;
        guiHelper.blit(TEXTURE, barX, barY, 0.0f, 42.0f, 54, 21, 256, 256);
        guiHelper.blit(TEXTURE, barX, barY, 0, 63, (int)(44.0f * castCompletionPercent + 5.0f), 21);
        Font font = Minecraft.getInstance().font;
        int textX = barX + (54 - font.width(castTimeString)) / 2;
        Objects.requireNonNull(font);
        int textY = barY + 10 - 9 / 2 + 1;
        guiHelper.drawString(font, castTimeString, textX, textY, 0xFFFFFF);
    }
}

