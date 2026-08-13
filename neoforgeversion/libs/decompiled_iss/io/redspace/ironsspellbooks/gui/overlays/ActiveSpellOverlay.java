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
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ActiveSpellOverlay
implements LayeredDraw.Layer {
    public static ActiveSpellOverlay instance = new ActiveSpellOverlay();
    protected static final ResourceLocation WIDGETS_LOCATION = ResourceLocation.withDefaultNamespace((String)"textures/gui/widgets.png");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/icons.png");
    protected static final ResourceLocation FRAME_LOCATION = ResourceLocation.withDefaultNamespace((String)"textures/gui/sprites/hud/hotbar_offhand_left.png");

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        AbstractSpell spell;
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator()) {
            return;
        }
        int screenWidth = guiHelper.guiWidth();
        int screenHeight = guiHelper.guiHeight();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (ActiveSpellOverlay.hasRightClickCasting(stack.getItem())) {
            spell = ISpellContainer.isSpellContainer(stack) ? ISpellContainer.get(stack).getSpellAtIndex(0).getSpell() : ClientMagicData.getSpellSelectionManager().getSelectedSpellData().getSpell();
        } else {
            stack = player.getOffhandItem();
            if (ActiveSpellOverlay.hasRightClickCasting(stack.getItem())) {
                spell = ISpellContainer.isSpellContainer(stack) ? ISpellContainer.get(stack).getSpellAtIndex(0).getSpell() : ClientMagicData.getSpellSelectionManager().getSelectedSpellData().getSpell();
            } else {
                return;
            }
        }
        if (stack.isEmpty() || spell == SpellRegistry.none()) {
            return;
        }
        int centerX = screenWidth / 2 + 91 + 9;
        int centerY = screenHeight - 23;
        guiHelper.blit(FRAME_LOCATION, centerX, centerY, 0.0f, 0.0f, 29, 24, 29, 24);
        guiHelper.blit(spell.getSpellIconResource(), centerX + 3, centerY + 4, 0.0f, 0.0f, 16, 16, 16, 16);
        float f = ClientMagicData.getCooldownPercent(spell);
        if (f > 0.0f && !stack.getItem().equals(ItemRegistry.SCROLL.get())) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int pixels = (int)(16.0f * f + 1.0f);
            guiHelper.blit(TEXTURE, centerX + 3, centerY + 20 - pixels, 47, 87, 16, pixels);
        }
    }

    private static boolean hasRightClickCasting(Item item) {
        return item instanceof Scroll || item instanceof CastingItem;
    }

    private static void setOpaqueTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
    }

    private static void setTranslucentTexture(ResourceLocation texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTranslucentShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)texture);
    }
}

