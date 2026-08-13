/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$Builder
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.gui.scroll_forge;

import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeMenu;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.network.ScrollForgeSelectSpellPacket;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ScrollForgeScreen
extends AbstractContainerScreen<ScrollForgeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/scroll_forge.png");
    private static final int SPELL_LIST_X = 89;
    private static final int SPELL_LIST_Y = 15;
    private static final int SCROLL_BAR_X = 199;
    private static final int SCROLL_BAR_Y = 15;
    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_HEIGHT = 56;
    public static final ResourceLocation RUNIC_FONT = ResourceLocation.withDefaultNamespace((String)"illageralt");
    public static final ResourceLocation ENCHANT_FONT = ResourceLocation.withDefaultNamespace((String)"alt");
    private List<SpellCardInfo> availableSpells;
    private ItemStack[] oldMenuSlots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    private AbstractSpell selectedSpell = SpellRegistry.none();
    private int scrollOffset;
    private boolean isScrollbarHeld;

    public ScrollForgeScreen(ScrollForgeMenu menu, Inventory inventory, Component title) {
        super((AbstractContainerMenu)menu, inventory, title);
        this.imageWidth = 218;
        this.imageHeight = 166;
    }

    protected void init() {
        this.availableSpells = new ArrayList<SpellCardInfo>();
        this.generateSpellList();
        super.init();
    }

    public void onClose() {
        this.setSelectedSpell(SpellRegistry.none());
        this.resetList();
        super.onClose();
    }

    private void resetList() {
        InkItem inkItem;
        Item item;
        if (((ScrollForgeMenu)this.menu).getInkSlot().getItem().isEmpty() || !((item = ((ScrollForgeMenu)this.menu).getInkSlot().getItem().getItem()) instanceof InkItem) || (inkItem = (InkItem)item).getRarity().compareRarity(SpellRarity.values()[this.selectedSpell.getMinRarity()]) < 0) {
            this.setSelectedSpell(SpellRegistry.none());
        }
        this.scrollOffset = 0;
        for (SpellCardInfo s : this.availableSpells) {
            this.removeWidget((GuiEventListener)s.button);
        }
        this.availableSpells.clear();
    }

    public void render(GuiGraphics guiHelper, int mouseX, int mouseY, float delta) {
        super.render(guiHelper, mouseX, mouseY, delta);
        this.renderTooltip(guiHelper, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        guiHelper.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        float scrollOffset = Mth.clamp((float)((float)this.scrollOffset / (float)(this.totalRowCount() - 3)), (float)0.0f, (float)1.0f);
        guiHelper.blit(TEXTURE, this.leftPos + 199, (int)((float)(this.topPos + 15) + scrollOffset * 41.0f), this.imageWidth + (this.isScrollbarHeld ? 12 : 0), 0, 12, 15);
        if (this.menuSlotsChanged()) {
            this.generateSpellList();
        }
        this.renderSpellList(guiHelper, partialTick, mouseX, mouseY);
    }

    private boolean menuSlotsChanged() {
        if (((ScrollForgeMenu)this.menu).getInkSlot().getItem().getItem() != this.oldMenuSlots[0].getItem() || ((ScrollForgeMenu)this.menu).getFocusSlot().getItem().getItem() != this.oldMenuSlots[2].getItem()) {
            this.oldMenuSlots = new ItemStack[]{((ScrollForgeMenu)this.menu).getInkSlot().getItem(), ((ScrollForgeMenu)this.menu).getBlankScrollSlot().getItem(), ((ScrollForgeMenu)this.menu).getFocusSlot().getItem()};
            return true;
        }
        return false;
    }

    private void renderSpellList(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        ItemStack inkStack = ((ScrollForgeMenu)this.menu).getInkSlot().getItem();
        SpellRarity inkRarity = this.getRarityFromInk(inkStack.getItem());
        this.availableSpells.sort((a, b) -> SpellConfigManager.getSpellConfigValue(a.spell, SpellConfigParameter.MIN_RARITY).compareRarity(SpellConfigManager.getSpellConfigValue(b.spell, SpellConfigParameter.MIN_RARITY)));
        List<FormattedCharSequence> additionalTooltip = null;
        for (int i = 0; i < this.availableSpells.size(); ++i) {
            SpellCardInfo spellCard = this.availableSpells.get(i);
            if (i - this.scrollOffset >= 0 && i - this.scrollOffset < 3) {
                spellCard.activityState = inkRarity == null || spellCard.spell.getMinRarity() > inkRarity.getValue() ? SpellCardInfo.ActivityState.INK_ERROR : (this.minecraft != null && !spellCard.spell.canBeCraftedBy((Player)this.minecraft.player) ? SpellCardInfo.ActivityState.UNLEARNED_ERROR : SpellCardInfo.ActivityState.ENABLED);
                int x = this.leftPos + 89;
                int y = this.topPos + 15 + (i - this.scrollOffset) * 19;
                spellCard.button.setX(x);
                spellCard.button.setY(y);
                spellCard.draw(this, guiHelper, x, y, mouseX, mouseY);
                if (additionalTooltip == null) {
                    additionalTooltip = spellCard.getTooltip(x, y, mouseX, mouseY);
                }
            } else {
                spellCard.activityState = SpellCardInfo.ActivityState.DISABLED;
            }
            spellCard.button.active = spellCard.activityState == SpellCardInfo.ActivityState.ENABLED;
        }
        if (additionalTooltip != null) {
            guiHelper.renderTooltip(this.font, additionalTooltip, mouseX, mouseY);
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        int newScroll = this.scrollOffset - (int)pScrollY;
        int length = this.availableSpells.size();
        if (newScroll <= length - 3 && newScroll >= 0) {
            this.scrollOffset -= (int)pScrollY;
            return true;
        }
        return false;
    }

    public void generateSpellList() {
        this.resetList();
        ItemStack focusStack = ((ScrollForgeMenu)this.menu).getFocusSlot().getItem();
        if (!focusStack.isEmpty() && focusStack.is(ModTags.SCHOOL_FOCUS)) {
            List<AbstractSpell> spells = SchoolRegistry.getSchoolsFromFocus(focusStack).stream().flatMap(school -> SpellRegistry.getSpellsForSchool(school).stream()).filter(AbstractSpell::allowCrafting).toList();
            for (int i = 0; i < spells.size(); ++i) {
                int tempIndex = i;
                if (!spells.get(i).isEnabled() || this.minecraft == null) continue;
                this.availableSpells.add(new SpellCardInfo(spells.get(i), i + 1, i, (Button)this.addWidget((GuiEventListener)new Button.Builder((Component)spells.get(i).getDisplayName((Player)this.minecraft.player), b -> this.setSelectedSpell((AbstractSpell)spells.get(tempIndex))).pos(0, 0).size(108, 19).build())));
            }
        }
    }

    private void setSelectedSpell(AbstractSpell spell) {
        this.selectedSpell = spell;
        PacketDistributor.sendToServer((CustomPacketPayload)new ScrollForgeSelectSpellPacket(((ScrollForgeMenu)this.menu).blockEntity.getBlockPos(), spell.getSpellId()), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    private SpellRarity getRarityFromInk(Item ink) {
        if (ink instanceof InkItem) {
            InkItem inkItem = (InkItem)ink;
            return inkItem.getRarity();
        }
        return null;
    }

    public AbstractSpell getSelectedSpell() {
        return this.selectedSpell;
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.isScrollbarHeld = this.isHovering(199, 15, 12, 56, pMouseX, pMouseY);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.isScrollbarHeld = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        int i = this.totalRowCount() - 3;
        if (this.isScrollbarHeld) {
            int j = this.topPos + 15;
            int k = j + 56;
            float scrollOffs = ((float)pMouseY - (float)j - 7.5f) / ((float)(k - j) - 15.0f);
            scrollOffs = Mth.clamp((float)scrollOffs, (float)0.0f, (float)1.0f);
            this.scrollOffset = Math.max((int)((double)(scrollOffs * (float)i) + 0.5), 0);
            return true;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    private int totalRowCount() {
        return this.availableSpells.size();
    }

    private class SpellCardInfo {
        ActivityState activityState = ActivityState.DISABLED;
        AbstractSpell spell;
        int spellLevel;
        SpellRarity rarity;
        Button button;
        int index;

        SpellCardInfo(AbstractSpell spell, int spellLevel, int index, Button button) {
            this.spell = spell;
            this.spellLevel = spellLevel;
            this.index = index;
            this.button = button;
            this.rarity = spell.getRarity(spellLevel);
        }

        void draw(ScrollForgeScreen screen, GuiGraphics guiHelper, int x, int y, int mouseX, int mouseY) {
            if (this.activityState == ActivityState.ENABLED || this.activityState == ActivityState.UNLEARNED_ERROR) {
                if (this.spell == screen.getSelectedSpell()) {
                    guiHelper.blit(TEXTURE, x, y, 0, 204, 108, 19);
                } else {
                    guiHelper.blit(TEXTURE, x, y, 0, 166, 108, 19);
                }
            } else {
                guiHelper.blit(TEXTURE, x, y, 0, 185, 108, 19);
            }
            ResourceLocation texture = this.activityState == ActivityState.ENABLED ? this.spell.getSpellIconResource() : SpellRegistry.none().getSpellIconResource();
            guiHelper.blit(texture, x + 108 - 18, y + 1, 0.0f, 0.0f, 16, 16, 16, 16);
            int maxWidth = 88;
            FormattedText text = this.trimText(ScrollForgeScreen.this.font, (Component)this.getDisplayName().withStyle(this.activityState == ActivityState.ENABLED ? Style.EMPTY : Style.EMPTY.withFont(RUNIC_FONT)), maxWidth);
            int textX = x + 2;
            int textY = y + 3;
            guiHelper.drawWordWrap(ScrollForgeScreen.this.font, text, textX, textY, maxWidth, 0xFFFFFF);
        }

        @Nullable
        List<FormattedCharSequence> getTooltip(int x, int y, int mouseX, int mouseY) {
            MutableComponent text = this.getDisplayName();
            int textX = x + 2;
            int textY = y + 3;
            if (mouseX >= textX && mouseY >= textY && mouseX < textX + ScrollForgeScreen.this.font.width((FormattedText)text)) {
                Objects.requireNonNull(ScrollForgeScreen.this.font);
                if (mouseY < textY + 9) {
                    return this.getHoverText();
                }
            }
            return null;
        }

        List<FormattedCharSequence> getHoverText() {
            if (this.activityState == ActivityState.INK_ERROR) {
                return List.of(FormattedCharSequence.forward((String)Component.translatable((String)"ui.irons_spellbooks.ink_rarity_error").getString(), (Style)Style.EMPTY));
            }
            if (this.activityState == ActivityState.UNLEARNED_ERROR) {
                return List.of(FormattedCharSequence.forward((String)this.spell.getLockedMessage().getString(), (Style)this.spell.getLockedMessage().getStyle()));
            }
            return TooltipsUtils.createSpellDescriptionTooltip(this.spell, ScrollForgeScreen.this.font);
        }

        private FormattedText trimText(Font font, Component component, int maxWidth) {
            FormattedText text = (FormattedText)font.getSplitter().splitLines((FormattedText)component, maxWidth, component.getStyle()).get(0);
            if (text.getString().length() < component.getString().length()) {
                text = FormattedText.composite((FormattedText[])new FormattedText[]{text, FormattedText.of((String)"...")});
            }
            return text;
        }

        MutableComponent getDisplayName() {
            return this.spell.getDisplayName((Player)((ScrollForgeScreen)ScrollForgeScreen.this).minecraft.player);
        }

        static enum ActivityState {
            DISABLED,
            ENABLED,
            INK_ERROR,
            UNLEARNED_ERROR;

        }
    }
}

