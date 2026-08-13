/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec2
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

public class SpellBarOverlay
implements LayeredDraw.Layer {
    public static final SpellBarOverlay instance = new SpellBarOverlay();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    static final int IMAGE_HEIGHT = 21;
    static final int IMAGE_WIDTH = 21;
    static final int HOTBAR_HALFWIDTH = 91;
    static final int boxSize = 20;
    static int screenHeight;
    static int screenWidth;
    static final int CONTEXTUAL_FADE_WAIT = 80;
    public static int fadeoutDelay;
    static int lastTick;
    static float alpha;
    static int lastSpellCount;

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        int i;
        int centerY;
        int centerX;
        SpellSelectionManager ssm;
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        LocalPlayer player = Minecraft.getInstance().player;
        ManaBarOverlay.Display displayMode = (ManaBarOverlay.Display)((Object)ClientConfigs.SPELL_BAR_DISPLAY.get());
        if (displayMode == ManaBarOverlay.Display.Never || player == null) {
            return;
        }
        if (displayMode == ManaBarOverlay.Display.Contextual) {
            SpellBarOverlay.handleFading((Player)player);
            if (fadeoutDelay <= 0) {
                return;
            }
        } else {
            alpha = 1.0f;
        }
        if ((ssm = ClientMagicData.getSpellSelectionManager()).getSpellCount() != lastSpellCount) {
            lastSpellCount = ssm.getSpellCount();
            ClientRenderCache.generateRelativeLocations(ssm, 20, 22);
            if (displayMode == ManaBarOverlay.Display.Contextual) {
                fadeoutDelay = 80;
            }
        }
        if (ssm.getSpellCount() <= 0) {
            return;
        }
        int configOffsetY = (Integer)ClientConfigs.SPELL_BAR_Y_OFFSET.get();
        int configOffsetX = (Integer)ClientConfigs.SPELL_BAR_X_OFFSET.get();
        Anchor anchor = (Anchor)((Object)ClientConfigs.SPELL_BAR_ANCHOR.get());
        if (anchor == Anchor.Hotbar) {
            centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
            centerY = screenHeight - Math.max(55, screenHeight / 8);
        } else {
            centerX = screenWidth * anchor.m1;
            centerY = screenHeight * anchor.m2;
        }
        centerX += configOffsetX;
        centerY += configOffsetY;
        List<SpellData> spells = ssm.getAllSpells().stream().map(slot -> slot.spellData).toList();
        int spellbookCount = ssm.getSpellsForSlot(Curios.SPELLBOOK_SLOT).size();
        List<Vec2> locations = ClientRenderCache.relativeSpellBarSlotLocations;
        int approximateWidth = locations.size() / 3;
        centerX -= approximateWidth * 5;
        int selectedSpellIndex = ssm.getGlobalSelectionIndex();
        SpellBarOverlay.prepTranslucency();
        for (Vec2 location : locations) {
            guiHelper.blit(TEXTURE, centerX + (int)location.x, centerY + (int)location.y, 66, 84, 22, 22);
        }
        for (i = 0; i < locations.size(); ++i) {
            guiHelper.blit(spells.get(i).getSpell().getSpellIconResource(), centerX + (int)locations.get((int)i).x + 3, centerY + (int)locations.get((int)i).y + 3, 0.0f, 0.0f, 16, 16, 16, 16);
        }
        for (i = 0; i < locations.size(); ++i) {
            float f;
            if (i != selectedSpellIndex) {
                guiHelper.blit(TEXTURE, centerX + (int)locations.get((int)i).x, centerY + (int)locations.get((int)i).y, 22 + (!ssm.getAllSpells().get((int)i).slot.equals(Curios.SPELLBOOK_SLOT) ? 110 : 0), 84, 22, 22);
            }
            if (!((f = ClientMagicData.getCooldownPercent(spells.get(i).getSpell())) > 0.0f)) continue;
            int pixels = (int)(16.0f * f + 1.0f);
            guiHelper.blit(TEXTURE, centerX + (int)locations.get((int)i).x + 3, centerY + (int)locations.get((int)i).y + 19 - pixels, 47, 87, 16, pixels);
        }
        for (i = 0; i < locations.size(); ++i) {
            if (i != selectedSpellIndex) continue;
            guiHelper.blit(TEXTURE, centerX + (int)locations.get((int)i).x, centerY + (int)locations.get((int)i).y, 0, 84, 22, 22);
        }
        SpellBarOverlay.flushTranslucency();
    }

    private static void handleFading(Player player) {
        if (lastTick != player.tickCount) {
            lastTick = player.tickCount;
            if (ClientMagicData.isCasting() || ClientMagicData.getCooldowns().hasCooldownsActive() || ClientMagicData.getRecasts().hasRecastsActive()) {
                fadeoutDelay = 80;
            }
            if (fadeoutDelay > 0) {
                --fadeoutDelay;
            }
        }
        alpha = Mth.clamp((float)((float)fadeoutDelay / 20.0f), (float)0.0f, (float)1.0f);
        if (fadeoutDelay <= 0) {
            return;
        }
    }

    private static void prepTranslucency() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTranslucentShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
    }

    private static void flushTranslucency() {
        RenderSystem.disableBlend();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static enum Anchor {
        Hotbar(0, 0),
        TopLeft(0, 0),
        TopRight(0, 1),
        BottomLeft(0, 1),
        BottomRight(1, 1);

        final int m1;
        final int m2;

        private Anchor(int mx, int my) {
            this.m1 = mx;
            this.m2 = my;
        }
    }
}

