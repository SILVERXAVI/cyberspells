/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.phys.Vec2
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.joml.Vector4f
 */
package io.redspace.ironsspellbooks.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.network.spells.LearnSpellPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector4f;

@OnlyIn(value=Dist.CLIENT)
public class EldritchResearchScreen
extends Screen {
    private static final ResourceLocation WINDOW_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/eldritch_research_screen/window.png");
    private static final ResourceLocation FRAME_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/eldritch_research_screen/spell_frame.png");
    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 256;
    private static final int WINDOW_INSIDE_X = 9;
    private static final int WINDOW_INSIDE_Y = 18;
    public static final int WINDOW_INSIDE_WIDTH = 234;
    public static final int WINDOW_INSIDE_HEIGHT = 229;
    private static final int WINDOW_TITLE_X = 8;
    private static final int WINDOW_TITLE_Y = 6;
    public static final int BACKGROUND_TILE_WIDTH = 16;
    public static final int BACKGROUND_TILE_HEIGHT = 16;
    public static final int BACKGROUND_TILE_COUNT_X = 14;
    public static final int BACKGROUND_TILE_COUNT_Y = 7;
    int leftPos;
    int topPos;
    InteractionHand activeHand;
    List<AbstractSpell> learnableSpells;
    List<SpellNode> nodes;
    SyncedSpellData playerData;
    Vec2 maxViewportOffset;
    Vec2 viewportOffset;
    boolean isMouseHoldingSpell;
    boolean isMouseDragging;
    int heldSpellIndex = -1;
    int heldSpellTime = -1;
    int lastPlayerTick;
    static final int TIME_TO_HOLD = 15;
    private static final Component ALREADY_LEARNED = Component.translatable((String)"ui.irons_spellbooks.research_already_learned").withStyle(ChatFormatting.DARK_AQUA);
    private static final Component UNLEARNED = Component.translatable((String)"ui.irons_spellbooks.research_warning").withStyle(ChatFormatting.RED);

    public EldritchResearchScreen(Component pTitle, InteractionHand activeHand) {
        super(pTitle);
        this.activeHand = activeHand;
    }

    protected void init() {
        float offset;
        this.learnableSpells = SpellRegistry.getEnabledSpells().stream().filter(spell -> spell.getSchoolType().equals(SchoolRegistry.ELDRITCH.get())).toList();
        if (this.minecraft != null) {
            this.playerData = ClientMagicData.getSyncedSpellData((LivingEntity)this.minecraft.player);
        }
        this.viewportOffset = Vec2.ZERO;
        this.leftPos = (this.width - 252) / 2;
        this.topPos = (this.height - 256) / 2;
        this.nodes = new ArrayList<SpellNode>();
        RandomSource randomSource = RandomSource.create((long)431L);
        float f = 1.0471976f;
        float r = 35.0f;
        float circumference = 0.0f;
        float a = offset = 0.5f;
        for (int i = 0; i < this.learnableSpells.size(); ++i) {
            if (circumference > r * ((float)Math.PI * 2)) {
                f = 35.0f / (r += 40.0f);
                a -= f;
                circumference = 0.0f;
            }
            int x = this.leftPos + 126 - 8 + (int)(r * Mth.cos((float)(a += f)));
            int y = this.topPos + 128 - 8 + (int)(r * Mth.sin((float)a));
            this.nodes.add(new SpellNode(this.learnableSpells.get(i), x, y));
            circumference += r * f * 1.1f;
        }
        float maxDistX = 0.0f;
        float maxDistY = 0.0f;
        for (int i = 0; i < this.nodes.size(); ++i) {
            for (int j = 1; j < this.nodes.size(); ++j) {
                int y;
                int x = Math.abs(this.nodes.get((int)i).x - this.nodes.get((int)j).x);
                if ((float)x > maxDistX) {
                    maxDistX = x;
                }
                if (!((float)(y = Math.abs(this.nodes.get((int)i).y - this.nodes.get((int)j).y)) > maxDistY)) continue;
                maxDistY = y;
            }
        }
        this.maxViewportOffset = new Vec2((float)((int)maxDistX), (float)((int)maxDistY));
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        this.drawBackdrop(guiGraphics, this.leftPos + 9, this.topPos + 18);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.tickCount != this.lastPlayerTick) {
            this.lastPlayerTick = player.tickCount;
            if (this.isMouseHoldingSpell && this.heldSpellIndex >= 0 && this.heldSpellIndex < this.nodes.size() && !this.nodes.get((int)this.heldSpellIndex).spell.isLearned((Player)player)) {
                if (this.heldSpellTime > 15) {
                    this.heldSpellTime = -1;
                    PacketDistributor.sendToServer((CustomPacketPayload)new LearnSpellPacket(this.activeHand, this.nodes.get((int)this.heldSpellIndex).spell.getSpellId()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                    player.playNotifySound((SoundEvent)SoundRegistry.LEARN_ELDRITCH_SPELL.get(), SoundSource.MASTER, 1.0f, (float)Utils.random.nextIntBetweenInclusive(9, 11) * 0.1f);
                }
                ++this.heldSpellTime;
                if (this.lastPlayerTick % 2 == 0) {
                    player.playNotifySound((SoundEvent)SoundEvents.SOUL_ESCAPE.value(), SoundSource.MASTER, 1.0f, Mth.lerp((float)((float)this.heldSpellTime / 15.0f), (float)0.5f, (float)1.5f));
                    player.playNotifySound((SoundEvent)SoundRegistry.UI_TICK.get(), SoundSource.MASTER, 1.0f, Mth.lerp((float)((float)this.heldSpellTime / 15.0f), (float)0.5f, (float)1.5f));
                }
            } else if (this.heldSpellTime >= 0) {
                this.heldSpellTime = Math.max(this.heldSpellTime - 3, -1);
            }
        }
        this.handleConnections(guiGraphics, partialTick);
        List<FormattedCharSequence> tooltip = null;
        for (int i = 0; i < this.nodes.size(); ++i) {
            SpellNode node = this.nodes.get(i);
            this.drawNode(guiGraphics, node, player, i == this.heldSpellIndex && this.heldSpellTime > 0);
            if (!this.isHoveringNode(node, mouseX, mouseY)) continue;
            tooltip = EldritchResearchScreen.buildTooltip(node.spell, this.font);
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(WINDOW_LOCATION, this.leftPos, this.topPos, 0, 0, 252, 256);
        if (tooltip != null) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
        }
    }

    private void renderProgressOverlay(GuiGraphics gui, int x, int y, float progress) {
        gui.fill(x += (int)this.viewportOffset.x, y += (int)this.viewportOffset.y, x + Mth.ceil((float)(16.0f * progress)), y + 16, FastColor.ARGB32.color((int)127, (int)244, (int)65, (int)255));
    }

    private void drawNode(GuiGraphics guiGraphics, SpellNode node, LocalPlayer player, boolean drawProgress) {
        this.drawWithClipping(node.spell.getSpellIconResource(), guiGraphics, node.x, node.y, 0, 0, 16, 16, 16, 16, this.leftPos + 9, this.topPos + 18, 234, 229);
        if (drawProgress) {
            this.renderProgressOverlay(guiGraphics, node.x, node.y, (float)this.heldSpellTime / 15.0f);
        }
        this.drawWithClipping(FRAME_LOCATION, guiGraphics, node.x - 8, node.y - 8, node.spell.isLearned((Player)player) ? 32 : 0, 0, 32, 32, 64, 32, this.leftPos + 9, this.topPos + 18, 234, 229);
    }

    private void drawWithClipping(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int uvx, int uvy, int width, int height, int imageWidth, int imageHeight, int bbx, int bby, int bbw, int bbh) {
        if ((x += (int)this.viewportOffset.x) < bbx) {
            xDiff = bbx - x;
            width -= xDiff;
            uvx += xDiff;
            x += xDiff;
        } else if (x > bbx + bbw - width) {
            xDiff = x - (bbx + bbw - width);
            width -= xDiff;
        }
        if ((y += (int)this.viewportOffset.y) < bby) {
            yDiff = bby - y;
            height -= yDiff;
            uvy += yDiff;
            y += yDiff;
        } else if (y > bby + bbh - height) {
            yDiff = y - (bby + bbh - height);
            height -= yDiff;
        }
        if (width > 0 && height > 0) {
            guiGraphics.blit(texture, x, y, width, height, (float)uvx, (float)uvy, width, height, imageWidth, imageHeight);
        }
    }

    public static List<FormattedCharSequence> buildTooltip(AbstractSpell spell, Font font) {
        boolean learned = spell.isLearned((Player)Minecraft.getInstance().player);
        MutableComponent name = spell.getDisplayName(null).withStyle(learned ? ChatFormatting.DARK_AQUA : ChatFormatting.RED);
        List description = font.split((FormattedText)Component.translatable((String)String.format("%s.guide", spell.getComponentId())).withStyle(ChatFormatting.GRAY), 180);
        ArrayList<FormattedCharSequence> hoverText = new ArrayList<FormattedCharSequence>();
        hoverText.add(FormattedCharSequence.forward((String)name.getString(), (Style)name.getStyle().withUnderlined(Boolean.valueOf(true))));
        hoverText.addAll(description);
        hoverText.add(FormattedCharSequence.EMPTY);
        hoverText.add((learned ? ALREADY_LEARNED : UNLEARNED).getVisualOrderText());
        return hoverText;
    }

    private void handleConnections(GuiGraphics guiGraphics, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0);
        RenderSystem.enableDepthTest();
        float f = Mth.sin((float)(((float)Minecraft.getInstance().player.tickCount + partialTick) * 0.1f));
        float glowIntensity = f * f * 0.8f + 0.2f;
        Vector4f color = new Vector4f(0.5294118f, 0.6039216f, 0.68235296f, 0.5f);
        Vector4f glowcolor = new Vector4f(0.95686275f, 0.25490198f, 1.0f, 0.5f);
        for (int i = 0; i < this.nodes.size() - 1; ++i) {
            Vec2 a = new Vec2((float)this.nodes.get((int)i).x, (float)this.nodes.get((int)i).y);
            Vec2 b = new Vec2((float)this.nodes.get((int)(i + 1)).x, (float)this.nodes.get((int)(i + 1)).y);
            Vec2 orth = new Vec2(-(b.y - a.y), b.x - a.x).normalized().scale(1.5f);
            float x1m1 = a.x + orth.x + 8.0f + (float)((int)this.viewportOffset.x);
            float x2m1 = b.x + orth.x + 8.0f + (float)((int)this.viewportOffset.x);
            float y1m1 = a.y + orth.y + 8.0f + (float)((int)this.viewportOffset.y);
            float y2m1 = b.y + orth.y + 8.0f + (float)((int)this.viewportOffset.y);
            float x1m2 = a.x - orth.x + 8.0f + (float)((int)this.viewportOffset.x);
            float x2m2 = b.x - orth.x + 8.0f + (float)((int)this.viewportOffset.x);
            float y1m2 = a.y - orth.y + 8.0f + (float)((int)this.viewportOffset.y);
            float y2m2 = b.y - orth.y + 8.0f + (float)((int)this.viewportOffset.y);
            Vector4f color1 = EldritchResearchScreen.lerpColor(color, glowcolor, glowIntensity * (float)(this.nodes.get((int)i).spell.isLearned((Player)Minecraft.getInstance().player) ? 1 : 0));
            Vector4f color2 = EldritchResearchScreen.lerpColor(color, glowcolor, glowIntensity * (float)(this.nodes.get((int)(i + 1)).spell.isLearned((Player)Minecraft.getInstance().player) ? 1 : 0));
            RenderHelper.quadBuilder().vertex(x1m1, y1m1).color(this.fadeOutTowardEdges(guiGraphics, x1m1, y1m1, color1)).vertex(x2m1, y2m1).color(this.fadeOutTowardEdges(guiGraphics, x2m1, y2m1, color2)).vertex(x2m2, y2m2).color(this.fadeOutTowardEdges(guiGraphics, x2m2, y2m2, color2)).vertex(x1m2, y1m2).color(this.fadeOutTowardEdges(guiGraphics, x1m2, y1m2, color1)).build(guiGraphics, RenderType.gui());
        }
    }

    private Vector4f fadeOutTowardEdges(GuiGraphics guiGraphics, double x, double y, Vector4f color) {
        float margin = 40.0f;
        int maxWidth = 252;
        int maxHeight = 256;
        int boundXMin = (int)Mth.clamp((double)(x + (double)this.viewportOffset.x - (double)this.leftPos), (double)0.0, (double)maxWidth);
        int boundXMax = maxWidth - (int)Mth.clamp((double)(x + (double)this.viewportOffset.x - (double)this.leftPos), (double)0.0, (double)maxWidth);
        int boundYMin = (int)Mth.clamp((double)(y + (double)this.viewportOffset.y - (double)this.topPos), (double)0.0, (double)maxHeight);
        int boundYMax = maxHeight - (int)Mth.clamp((double)(y + (double)this.viewportOffset.y - (double)this.topPos), (double)0.0, (double)maxHeight);
        float px = Mth.clamp((float)((float)Math.min(boundXMin, boundXMax) / margin), (float)0.0f, (float)1.0f);
        float py = Mth.clamp((float)((float)Math.min(boundYMin, boundYMax) / margin), (float)0.0f, (float)1.0f);
        float alpha = Mth.sqrt((float)(px * py));
        return new Vector4f(color.x, color.y, color.z, color.w * alpha);
    }

    private int colorFromRGBA(Vector4f rgba) {
        int r = (int)(rgba.x() * 255.0f) & 0xFF;
        int g = (int)(rgba.y() * 255.0f) & 0xFF;
        int b = (int)(rgba.z() * 255.0f) & 0xFF;
        int a = (int)(rgba.w() * 255.0f) & 0xFF;
        return (r << 24) + (g << 16) + (b << 8) + a;
    }

    private void drawBackdrop(GuiGraphics guiGraphics, int left, int top) {
        float f = Minecraft.getInstance().player != null ? (float)Minecraft.getInstance().player.tickCount * 0.02f : 0.0f;
        float color = (Mth.sin((float)f) + 1.0f) * 0.25f + 0.15f;
        RenderHelper.QuadBuilder definitelynothowabuilderworks = RenderHelper.quadBuilder().vertex(left, top + 229).vertex(left + 234, top + 229).vertex(left + 234, top).vertex(left, top).color(0.0f, 0.0f, 0.0f, color);
        definitelynothowabuilderworks.build(guiGraphics, RenderType.endPortal());
        definitelynothowabuilderworks.build(guiGraphics, RenderType.guiOverlay());
    }

    private static Vector4f lerpColor(Vector4f a, Vector4f b, float pDelta) {
        float f = 1.0f - pDelta;
        float x = a.x() * f + b.x() * pDelta;
        float y = a.y() * f + b.y() * pDelta;
        float z = a.z() * f + b.z() * pDelta;
        float w = a.w() * f + b.w() * pDelta;
        return new Vector4f(x, y, z, w);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int mouseX = (int)pMouseX;
        int mouseY = (int)pMouseY;
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getItemInHand(this.activeHand).is((Item)ItemRegistry.ELDRITCH_PAGE.get())) {
            for (int i = 0; i < this.nodes.size(); ++i) {
                if (!this.isHoveringNode(this.nodes.get(i), mouseX, mouseY)) continue;
                this.heldSpellIndex = i;
                this.isMouseHoldingSpell = true;
                break;
            }
        }
        if (!this.isMouseHoldingSpell && this.isHovering(this.leftPos + 9, this.topPos + 18, 234, 229, mouseX, mouseY)) {
            this.isMouseDragging = true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean isHoveringNode(SpellNode node, int mouseX, int mouseY) {
        return this.isHovering(node.x - 2 + (int)this.viewportOffset.x, node.y - 2 + (int)this.viewportOffset.y, 20, 20, mouseX, mouseY);
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.isMouseHoldingSpell = false;
        this.isMouseDragging = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.isMouseDragging) {
            // empty if block
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey((int)pKeyCode, (int)pScanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public boolean isPauseScreen() {
        return false;
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    record SpellNode(AbstractSpell spell, int x, int y) {
    }

    record NodeConnection(SpellNode node1, SpellNode node2) {
    }
}

