/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 */
package io.redspace.ironsspellbooks.gui.overlays;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ManaBarOverlay
implements LayeredDraw.Layer {
    public static final ManaBarOverlay instance = new ManaBarOverlay();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    static final int DEFAULT_IMAGE_WIDTH = 98;
    static final int XP_IMAGE_WIDTH = 188;
    static final int IMAGE_HEIGHT = 21;
    static final int HOTBAR_HEIGHT = 25;
    static final int ICON_ROW_HEIGHT = 11;
    static final int CHAR_WIDTH = 6;
    static final int HUNGER_BAR_OFFSET = 50;
    static final int SCREEN_BORDER_MARGIN = 20;
    static final int TEXT_COLOR = ChatFormatting.AQUA.getColor();

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        if (!ManaBarOverlay.shouldShowManaBar((Player)player)) {
            return;
        }
        int maxMana = (int)player.getAttributeValue(AttributeRegistry.MAX_MANA);
        int mana = ClientMagicData.getPlayerMana();
        int configOffsetY = (Integer)ClientConfigs.MANA_BAR_Y_OFFSET.get();
        int configOffsetX = (Integer)ClientConfigs.MANA_BAR_X_OFFSET.get();
        Anchor anchor = (Anchor)((Object)ClientConfigs.MANA_BAR_ANCHOR.get());
        if (anchor == Anchor.XP && player.getJumpRidingScale() > 0.0f) {
            return;
        }
        int barX = ManaBarOverlay.getBarX(anchor, screenWidth) + configOffsetX;
        int barY = ManaBarOverlay.getBarY(anchor, screenHeight, Minecraft.getInstance().gui) - configOffsetY;
        int imageWidth = anchor == Anchor.XP ? 188 : 98;
        int spriteX = anchor == Anchor.XP ? 68 : 0;
        int spriteY = anchor == Anchor.XP ? 40 : 0;
        guiHelper.blit(TEXTURE, barX, barY, (float)spriteX, (float)spriteY, imageWidth, 21, 256, 256);
        guiHelper.blit(TEXTURE, barX, barY, spriteX, spriteY + 21, (int)((double)imageWidth * Math.min((double)mana / (double)maxMana, 1.0)), 21);
        String manaFraction = mana + "/" + maxMana;
        int textX = (Integer)ClientConfigs.MANA_TEXT_X_OFFSET.get() + barX + imageWidth / 2 - (int)(((double)("" + mana).length() + 0.5) * 6.0);
        int textY = (Integer)ClientConfigs.MANA_TEXT_Y_OFFSET.get() + barY + (anchor == Anchor.XP ? 3 : 11);
        if (((Boolean)ClientConfigs.MANA_BAR_TEXT_VISIBLE.get()).booleanValue()) {
            guiHelper.drawString(Minecraft.getInstance().font, manaFraction, textX, textY, TEXT_COLOR);
        }
    }

    public static boolean shouldShowManaBar(Player player) {
        Display display = (Display)((Object)ClientConfigs.MANA_BAR_DISPLAY.get());
        return !player.isSpectator() && display != Display.Never && (display == Display.Always || player.isHolding(itemStack -> itemStack.getItem() instanceof CastingItem || ISpellContainer.isSpellContainer(itemStack) && !ISpellContainer.get(itemStack).mustEquip()) || (double)ClientMagicData.getPlayerMana() < player.getAttributeValue(AttributeRegistry.MAX_MANA));
    }

    private static int getBarX(Anchor anchor, int screenWidth) {
        if (anchor == Anchor.XP) {
            return screenWidth / 2 - 91 - 3;
        }
        if (anchor == Anchor.Hunger || anchor == Anchor.Center) {
            return screenWidth / 2 - 49 + (anchor == Anchor.Center ? 0 : 50);
        }
        if (anchor == Anchor.TopLeft || anchor == Anchor.BottomLeft) {
            return 20;
        }
        return screenWidth - 20 - 98;
    }

    private static int getBarY(Anchor anchor, int screenHeight, Gui gui) {
        if (anchor == Anchor.XP) {
            return screenHeight - 32 + 3 - 7;
        }
        if (anchor == Anchor.Hunger) {
            return screenHeight - (ManaBarOverlay.getAndIncrementRightHeight(gui) - 2) - 10;
        }
        if (anchor == Anchor.Center) {
            return screenHeight - 25 - 27 - 10 - (Math.max(gui.rightHeight, gui.leftHeight) - 49);
        }
        if (anchor == Anchor.TopLeft || anchor == Anchor.TopRight) {
            return 20;
        }
        return screenHeight - 20 - 21;
    }

    private static int getAndIncrementRightHeight(Gui gui) {
        int x = gui.rightHeight;
        gui.rightHeight += 10;
        return x;
    }

    public static enum Anchor {
        Hunger,
        XP,
        Center,
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight;

    }

    public static enum Display {
        Never,
        Always,
        Contextual;

    }
}

