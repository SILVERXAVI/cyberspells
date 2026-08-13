/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec2
 *  org.joml.Matrix4f
 *  org.joml.Vector4f
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class SpellWheelOverlay
implements LayeredDraw.Layer {
    public static SpellWheelOverlay instance = new SpellWheelOverlay();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    private final Vector4f lineColor = new Vector4f(1.0f, 0.85f, 0.7f, 1.0f);
    private final Vector4f radialButtonColor = new Vector4f(0.04f, 0.03f, 0.01f, 0.6f);
    private final Vector4f highlightColor = new Vector4f(0.8f, 0.7f, 0.55f, 0.7f);
    private final float ringInnerEdge = 20.0f;
    private float ringOuterEdge = 80.0f;
    private final float ringOuterEdgeMax = 80.0f;
    private final float ringOuterEdgeMin = 65.0f;
    public boolean active;
    private int wheelSelection;

    public void open() {
        if (ClientMagicData.getSpellSelectionManager().getAllSpells().isEmpty()) {
            return;
        }
        this.active = true;
        this.wheelSelection = -1;
        Minecraft.getInstance().mouseHandler.releaseMouse();
    }

    public void close() {
        this.active = false;
        if (this.wheelSelection >= 0) {
            ClientMagicData.getSpellSelectionManager().makeSelection(this.wheelSelection);
        }
        Minecraft.getInstance().mouseHandler.grabMouse();
    }

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        int i;
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator() || !this.active) {
            return;
        }
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.screen != null || minecraft.mouseHandler.isMouseGrabbed()) {
            this.close();
            return;
        }
        SpellSelectionManager swsm = ClientMagicData.getSpellSelectionManager();
        int totalSpellsAvailable = swsm.getSpellCount();
        if (totalSpellsAvailable <= 0) {
            this.close();
            return;
        }
        PoseStack poseStack = guiHelper.pose();
        poseStack.pushPose();
        float guiScale = 1.0f;
        if (((Boolean)ClientConfigs.SPELL_WHEEL_CONSISTENT_SIZE.get()).booleanValue()) {
            float invertedGuiScaleFactor = (float)(1.0 / minecraft.getWindow().getGuiScale());
            float physicalScaleFactor = Math.min((float)minecraft.getWindow().getScreenWidth() / 1920.0f, (float)minecraft.getWindow().getScreenHeight() / 1080.0f);
            guiScale = invertedGuiScaleFactor * physicalScaleFactor * 3.0f * ((Double)ClientConfigs.SPELL_WHEEL_SCALE.get()).floatValue();
        }
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        poseStack.translate((float)centerX, (float)centerY, 0.0f);
        poseStack.scale(guiScale, guiScale, 1.0f);
        centerX = 0;
        centerY = 0;
        Vec2 screenCenter = new Vec2((float)minecraft.getWindow().getScreenWidth() * 0.5f, (float)minecraft.getWindow().getScreenHeight() * 0.5f);
        Vec2 mousePos = new Vec2((float)minecraft.mouseHandler.xpos(), (float)minecraft.mouseHandler.ypos());
        double radiansPerSpell = Math.toRadians(360.0f / (float)totalSpellsAvailable);
        float mouseRotation = (Utils.getAngle(mousePos, screenCenter) + 1.57f + (float)radiansPerSpell * 0.5f) % 6.283f;
        this.wheelSelection = (int)Mth.clamp((double)((double)mouseRotation / radiansPerSpell), (double)0.0, (double)(totalSpellsAvailable - 1));
        if (mousePos.distanceToSqr(screenCenter) < 4225.0f) {
            this.wheelSelection = Math.max(0, swsm.getSelectionIndex());
        }
        guiHelper.fill(0, 0, screenWidth, screenHeight, 0);
        this.drawRadialBackgrounds(guiHelper, centerX, centerY, this.wheelSelection);
        this.drawDividingLines(guiHelper, centerX, centerY);
        SpellData selectedSpell = swsm.getSpellData(this.wheelSelection);
        int spellLevel = selectedSpell.getSpell().getLevelFor(selectedSpell.getLevel(), (LivingEntity)player);
        Font font = Minecraft.getInstance().font;
        List<MutableComponent> info = selectedSpell.getSpell().getUniqueInfo(spellLevel, (LivingEntity)minecraft.player);
        int n = Math.max(3, info.size());
        Objects.requireNonNull(font);
        int textHeight = n * 9 + 5;
        int textCenterMargin = 5;
        int textTitleMargin = 5;
        AbstractSpell spell = selectedSpell.getSpell();
        MutableComponent title = spell.getDisplayName((Player)minecraft.player).withStyle(Style.EMPTY.withUnderlined(Boolean.valueOf(true)));
        MutableComponent level = Component.translatable((String)"ui.irons_spellbooks.level", (Object[])new Object[]{TooltipsUtils.getLevelComponenet(selectedSpell, (LivingEntity)player).withStyle(spell.getRarity(spellLevel).getDisplayName().getStyle())});
        MutableComponent mana = Component.translatable((String)"ui.irons_spellbooks.mana_cost", (Object[])new Object[]{selectedSpell.getSpell().getManaCost(spellLevel)}).withStyle(ChatFormatting.AQUA);
        int cooldownTicks = MagicManager.getEffectiveSpellCooldown(spell, (Player)player, swsm.getSpellSlot(this.wheelSelection).getCastSource());
        MutableComponent cooldownTime = Component.translatable((String)"tooltip.irons_spellbooks.cooldown_length_seconds", (Object[])new Object[]{Utils.timeFromTicks(cooldownTicks, 2)}).withStyle(ChatFormatting.YELLOW);
        float f = centerX;
        float f2 = centerY;
        float f3 = this.ringOuterEdge + (float)textHeight - (float)textTitleMargin;
        Objects.requireNonNull(font);
        float f4 = f3 - 9.0f;
        int n2 = Math.max(2, info.size());
        Objects.requireNonNull(font);
        this.drawTextBackground(guiHelper, f, f2, f4, textCenterMargin, n2 * 9);
        guiHelper.drawString(font, (Component)title, centerX - font.width((FormattedText)title) / 2, (int)((float)centerY - (this.ringOuterEdge + (float)textHeight)), 0xFFFFFF, true);
        float f5 = (float)centerY - (this.ringOuterEdge + (float)textHeight);
        Objects.requireNonNull(font);
        int infoHeight = (int)(f5 + 9.0f + (float)textTitleMargin);
        guiHelper.drawString(font, (Component)level, centerX - font.width((FormattedText)level) - textCenterMargin, infoHeight, 0xFFFFFF, true);
        if (spell.getManaCost(spellLevel) > 0) {
            Objects.requireNonNull(font);
            guiHelper.drawString(font, (Component)mana, centerX - font.width((FormattedText)mana) - textCenterMargin, infoHeight += 9, 0xFFFFFF, true);
        }
        if (cooldownTicks > 0) {
            Objects.requireNonNull(font);
            guiHelper.drawString(font, (Component)cooldownTime, centerX - font.width((FormattedText)cooldownTime) - textCenterMargin, infoHeight += 9, 0xFFFFFF, true);
        }
        for (int i2 = 0; i2 < info.size(); ++i2) {
            MutableComponent line = info.get(i2);
            float f6 = (float)centerY - (80.0f + (float)textHeight);
            Objects.requireNonNull(font);
            guiHelper.drawString(font, (Component)line, centerX + textCenterMargin, (int)(f6 + (float)(9 * (i2 + 1)) + (float)textTitleMargin), 0x3BE33B, true);
        }
        float scale = Mth.lerp((float)((float)totalSpellsAvailable / 15.0f), (float)2.0f, (float)1.25f) * 0.65f;
        double radius = (double)(3.0f / scale * 40.0f) * 0.5 * (double)(0.85f + 0.25f * ((float)totalSpellsAvailable / 15.0f));
        Vec2[] locations = new Vec2[totalSpellsAvailable];
        for (i = 0; i < locations.length; ++i) {
            locations[i] = new Vec2((float)(Math.sin(radiansPerSpell * (double)i) * radius), (float)(-Math.cos(radiansPerSpell * (double)i) * radius));
        }
        for (i = 0; i < locations.length; ++i) {
            SpellData currentSpell = swsm.getSpellData(i);
            if (currentSpell == null) continue;
            ResourceLocation texture = currentSpell.getSpell().getSpellIconResource();
            poseStack.pushPose();
            poseStack.translate((float)centerX, (float)centerY, 0.0f);
            poseStack.scale(scale, scale, scale);
            int iconWidth = 8;
            int borderWidth = 16;
            int cdWidth = 8;
            guiHelper.blit(texture, (int)locations[i].x - iconWidth, (int)locations[i].y - iconWidth, 0.0f, 0.0f, 16, 16, 16, 16);
            guiHelper.blit(TEXTURE, (int)locations[i].x - borderWidth, (int)locations[i].y - borderWidth, swsm.getSelectionIndex() == i ? 32 : 0, 106, 32, 32);
            float f7 = ClientMagicData.getCooldownPercent(currentSpell.getSpell());
            if (f7 > 0.0f) {
                RenderSystem.enableBlend();
                int pixels = (int)(16.0f * f7 + 1.0f);
                guiHelper.blit(TEXTURE, (int)locations[i].x - cdWidth, (int)locations[i].y + cdWidth - pixels, 47, 87, 16, pixels);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private void drawTextBackground(GuiGraphics guiHelper, float centerX, float centerY, float textYOffset, int textCenterMargin, int textHeight) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        centerY = centerY - textYOffset - 2.0f;
        int heightMax = textHeight / 2 + 4;
        boolean heightMin = false;
        int widthMax = 70;
        int widthMin = 0;
        widthMin = -1;
        widthMax = 1;
        VertexConsumer vertexConsumer = guiHelper.bufferSource().getBuffer(RenderType.gui());
        Matrix4f m = guiHelper.pose().last().pose();
        vertexConsumer.addVertex(m, centerX + (float)widthMin, centerY + (float)heightMin + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), this.radialButtonColor.w());
        vertexConsumer.addVertex(m, centerX + (float)widthMin, centerY + (float)heightMax + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), 0.0f);
        vertexConsumer.addVertex(m, centerX + (float)widthMax, centerY + (float)heightMax + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), 0.0f);
        vertexConsumer.addVertex(m, centerX + (float)widthMax, centerY + (float)heightMin + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), this.radialButtonColor.w());
        vertexConsumer.addVertex(m, centerX + (float)widthMin, centerY + (float)heightMin, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), 0.0f);
        vertexConsumer.addVertex(m, centerX + (float)widthMin, centerY + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), this.radialButtonColor.w());
        vertexConsumer.addVertex(m, centerX + (float)widthMax, centerY + (float)heightMax, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), this.radialButtonColor.w());
        vertexConsumer.addVertex(m, centerX + (float)widthMax, centerY + (float)heightMin, 0.0f).setColor(this.radialButtonColor.x(), this.radialButtonColor.y(), this.radialButtonColor.z(), 0.0f);
        RenderSystem.disableBlend();
    }

    private void drawRadialBackgrounds(GuiGraphics guiGraphics, float centerX, float centerY, int selectedSpellIndex) {
        float quarterCircle = 1.5707964f;
        int totalSpellsAvailable = ClientMagicData.getSpellSelectionManager().getSpellCount();
        int segments = totalSpellsAvailable < 6 ? (totalSpellsAvailable % 2 == 1 ? 15 : 12) : totalSpellsAvailable * 2;
        float radiansPerObject = (float)Math.PI * 2 / (float)segments;
        float radiansPerSpell = (float)Math.PI * 2 / (float)totalSpellsAvailable;
        this.ringOuterEdge = Math.max(65.0f, 80.0f);
        for (int i = 0; i < segments; ++i) {
            float beginRadians = (float)i * radiansPerObject - (quarterCircle + radiansPerSpell / 2.0f);
            float endRadians = (float)(i + 1) * radiansPerObject - (quarterCircle + radiansPerSpell / 2.0f);
            float x1m1 = Mth.cos((float)beginRadians) * 20.0f;
            float x2m1 = Mth.cos((float)endRadians) * 20.0f;
            float y1m1 = Mth.sin((float)beginRadians) * 20.0f;
            float y2m1 = Mth.sin((float)endRadians) * 20.0f;
            float x1m2 = Mth.cos((float)beginRadians) * this.ringOuterEdge;
            float x2m2 = Mth.cos((float)endRadians) * this.ringOuterEdge;
            float y1m2 = Mth.sin((float)beginRadians) * this.ringOuterEdge;
            float y2m2 = Mth.sin((float)endRadians) * this.ringOuterEdge;
            boolean isHighlighted = i * totalSpellsAvailable / segments == selectedSpellIndex;
            Vector4f color = this.radialButtonColor;
            if (isHighlighted) {
                color = this.highlightColor;
            }
            VertexConsumer vertexConsumer = guiGraphics.bufferSource().getBuffer(RenderType.gui());
            Matrix4f m = guiGraphics.pose().last().pose();
            vertexConsumer.addVertex(m, centerX + x1m1, centerY + y1m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m1, centerY + y2m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m2, centerY + y2m2, 0.0f).setColor(color.x(), color.y(), color.z(), 0.0f);
            vertexConsumer.addVertex(m, centerX + x1m2, centerY + y1m2, 0.0f).setColor(color.x(), color.y(), color.z(), 0.0f);
            color = this.lineColor;
            float categoryLineWidth = 2.0f;
            float categoryLineOuterEdge = 20.0f + categoryLineWidth;
            float x1m3 = Mth.cos((float)beginRadians) * categoryLineOuterEdge;
            float x2m3 = Mth.cos((float)endRadians) * categoryLineOuterEdge;
            float y1m3 = Mth.sin((float)beginRadians) * categoryLineOuterEdge;
            float y2m3 = Mth.sin((float)endRadians) * categoryLineOuterEdge;
            vertexConsumer.addVertex(m, centerX + x1m1, centerY + y1m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m1, centerY + y2m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m3, centerY + y2m3, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x1m3, centerY + y1m3, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
        }
    }

    private void drawDividingLines(GuiGraphics guiHelper, float centerX, float centerY) {
        int totalSpellsAvailable = ClientMagicData.getSpellSelectionManager().getSpellCount();
        if (totalSpellsAvailable <= 1) {
            return;
        }
        float quarterCircle = 1.5707964f;
        float radiansPerSpell = (float)Math.PI * 2 / (float)totalSpellsAvailable;
        this.ringOuterEdge = Math.max(65.0f, 80.0f);
        for (int i = 0; i < totalSpellsAvailable; ++i) {
            float closeWidth = 0.13962634f;
            float farWidth = (float)Math.PI / 90;
            float beginCloseRadians = (float)i * radiansPerSpell - (quarterCircle + radiansPerSpell / 2.0f) - (float)Math.PI / 90;
            float endCloseRadians = beginCloseRadians + 0.13962634f;
            float beginFarRadians = (float)i * radiansPerSpell - (quarterCircle + radiansPerSpell / 2.0f) - (float)Math.PI / 360;
            float endFarRadians = beginCloseRadians + (float)Math.PI / 90;
            float x1m1 = Mth.cos((float)beginCloseRadians) * 20.0f;
            float x2m1 = Mth.cos((float)endCloseRadians) * 20.0f;
            float y1m1 = Mth.sin((float)beginCloseRadians) * 20.0f;
            float y2m1 = Mth.sin((float)endCloseRadians) * 20.0f;
            float x1m2 = Mth.cos((float)beginFarRadians) * this.ringOuterEdge * 1.4f;
            float x2m2 = Mth.cos((float)endFarRadians) * this.ringOuterEdge * 1.4f;
            float y1m2 = Mth.sin((float)beginFarRadians) * this.ringOuterEdge * 1.4f;
            float y2m2 = Mth.sin((float)endFarRadians) * this.ringOuterEdge * 1.4f;
            Vector4f color = this.lineColor;
            VertexConsumer vertexConsumer = guiHelper.bufferSource().getBuffer(RenderType.gui());
            Matrix4f m = guiHelper.pose().last().pose();
            vertexConsumer.addVertex(m, centerX + x1m1, centerY + y1m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m1, centerY + y2m1, 0.0f).setColor(color.x(), color.y(), color.z(), color.w());
            vertexConsumer.addVertex(m, centerX + x2m2, centerY + y2m2, 0.0f).setColor(color.x(), color.y(), color.z(), 0.0f);
            vertexConsumer.addVertex(m, centerX + x1m2, centerY + y1m2, 0.0f).setColor(color.x(), color.y(), color.z(), 0.0f);
        }
    }

    private void setOpaqueTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
    }

    private void setTranslucentTexture(ResourceLocation texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTranslucentShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
    }

    private boolean inTriangle(double x1, double y1, double x2, double y2, double x3, double y3, double x, double y) {
        double ab = (x1 - x) * (y2 - y) - (x2 - x) * (y1 - y);
        double bc = (x2 - x) * (y3 - y) - (x3 - x) * (y2 - y);
        double ca = (x3 - x) * (y1 - y) - (x1 - x) * (y3 - y);
        return this.sign(ab) == this.sign(bc) && this.sign(bc) == this.sign(ca);
    }

    private int sign(double n) {
        return n > 0.0 ? 1 : -1;
    }
}

