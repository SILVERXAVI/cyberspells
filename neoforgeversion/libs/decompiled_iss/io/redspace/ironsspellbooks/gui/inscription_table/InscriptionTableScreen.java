/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.CommonComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 */
package io.redspace.ironsspellbooks.gui.inscription_table;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class InscriptionTableScreen
extends AbstractContainerScreen<InscriptionTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/gui/inscription_table.png");
    private static final int INSCRIBE_BUTTON_X = 43;
    private static final int INSCRIBE_BUTTON_Y = 35;
    private static final int EXTRACT_BUTTON_X = 188;
    private static final int EXTRACT_BUTTON_Y = 137;
    private static final int SPELLBOOK_SLOT = 36;
    private static final int SCROLL_SLOT = 37;
    private static final int EXTRACTION_SLOT = 38;
    private static final int SPELL_BG_X = 67;
    private static final int SPELL_BG_Y = 15;
    private static final int SPELL_BG_WIDTH = 95;
    private static final int SPELL_BG_HEIGHT = 57;
    private static final int LORE_PAGE_X = 176;
    private static final int LORE_PAGE_WIDTH = 80;
    private boolean isDirty;
    protected Button inscribeButton;
    private ItemStack lastSpellBookItem = ItemStack.EMPTY;
    protected ArrayList<SpellSlotInfo> spellSlots;
    private int selectedSpellIndex = -1;
    private int inscriptionErrorCode = 0;
    private final int[][] LAYOUT = ClientRenderCache.SPELL_LAYOUT;

    public InscriptionTableScreen(InscriptionTableMenu menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 166;
    }

    protected void init() {
        super.init();
        this.inscribeButton = (Button)this.addWidget((GuiEventListener)Button.builder((Component)CommonComponents.GUI_DONE, p_169820_ -> this.onInscription()).bounds(0, 0, 14, 14).build());
        this.spellSlots = new ArrayList();
        this.generateSpellSlots();
    }

    public void onClose() {
        super.onClose();
        this.resetSelectedSpell();
    }

    public void render(GuiGraphics guiHelper, int mouseX, int mouseY, float delta) {
        try {
            super.render(guiHelper, mouseX, mouseY, delta);
            this.renderTooltip(guiHelper, mouseX, mouseY);
        }
        catch (Exception ignore) {
            this.onClose();
        }
    }

    protected void renderBg(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        guiHelper.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.inscribeButton.active = this.isValidInscription() && this.inscriptionErrorCode == 0;
        this.renderButtons(guiHelper, mouseX, mouseY);
        if (((Slot)((InscriptionTableMenu)this.menu).slots.get(36)).getItem() != this.lastSpellBookItem) {
            this.onSpellBookSlotChanged();
            this.lastSpellBookItem = ((Slot)((InscriptionTableMenu)this.menu).slots.get(36)).getItem();
        }
        this.renderSpells(guiHelper, mouseX, mouseY);
        this.renderLorePage(guiHelper, partialTick, mouseX, mouseY);
        this.inscriptionErrorCode = ((Slot)((InscriptionTableMenu)this.menu).slots.get(36)).hasItem() ? this.getErrorCode() : 0;
        if (this.inscriptionErrorCode > 0) {
            guiHelper.blit(TEXTURE, this.leftPos + 35, this.topPos + 51, 0, 213, 28, 22);
            if (this.isHovering(this.leftPos + 35, this.topPos + 51, 28, 22, mouseX, mouseY)) {
                guiHelper.renderTooltip(this.font, this.getErrorMessage(this.inscriptionErrorCode), mouseX, mouseY);
            }
        }
    }

    private int getErrorCode() {
        return 0;
    }

    private Component getErrorMessage(int code) {
        if (code == 1) {
            return Component.translatable((String)"ui.irons_spellbooks.inscription_table_rarity_error");
        }
        return Component.empty();
    }

    private void renderSpells(GuiGraphics guiHelper, int mouseX, int mouseY) {
        if (this.isDirty) {
            this.generateSpellSlots();
        }
        Vec2 center = new Vec2((float)(67 + this.leftPos + 47), (float)(15 + this.topPos + 28));
        for (int i = 0; i < this.spellSlots.size(); ++i) {
            Button spellSlot = this.spellSlots.get((int)i).button;
            Vec2 pos = this.spellSlots.get((int)i).relativePosition.add(center);
            spellSlot.setX((int)pos.x);
            spellSlot.setY((int)pos.y);
            this.renderSpellSlot(guiHelper, pos, mouseX, mouseY, i, this.spellSlots.get(i));
        }
    }

    private void renderButtons(GuiGraphics guiHelper, int mouseX, int mouseY) {
        this.inscribeButton.setX(this.leftPos + 43);
        this.inscribeButton.setY(this.topPos + 35);
        if (this.inscribeButton.active) {
            if (this.isHovering(this.inscribeButton.getX(), this.inscribeButton.getY(), 14, 14, mouseX, mouseY)) {
                guiHelper.blit(TEXTURE, this.inscribeButton.getX(), this.inscribeButton.getY(), 28, 185, 14, 14);
            } else {
                guiHelper.blit(TEXTURE, this.inscribeButton.getX(), this.inscribeButton.getY(), 14, 185, 14, 14);
            }
        } else {
            guiHelper.blit(TEXTURE, this.inscribeButton.getX(), this.inscribeButton.getY(), 0, 185, 14, 14);
        }
    }

    private void renderSpellSlot(GuiGraphics guiHelper, Vec2 pos, int mouseX, int mouseY, int index, SpellSlotInfo slot) {
        boolean hovering = this.isHovering((int)pos.x, (int)pos.y, 19, 19, mouseX, mouseY);
        int iconToDraw = hovering ? 38 : (slot.hasSpell() ? 19 : 0);
        guiHelper.blit(TEXTURE, (int)pos.x, (int)pos.y, iconToDraw, 166, 19, 19);
        if (slot.hasSpell()) {
            this.drawSpellIcon(guiHelper, pos, slot);
            if (hovering && !slot.spellSlot.spellData().canRemove()) {
                guiHelper.blit(TEXTURE, (int)pos.x, (int)pos.y, 76, 166, 19, 19);
            }
        }
        if (index == this.selectedSpellIndex) {
            guiHelper.blit(TEXTURE, (int)pos.x, (int)pos.y, 57, 166, 19, 19);
        }
    }

    private void drawSpellIcon(GuiGraphics guiHelper, Vec2 pos, SpellSlotInfo slot) {
        guiHelper.blit(slot.spellSlot.getSpell().getSpellIconResource(), (int)pos.x + 2, (int)pos.y + 2, 0.0f, 0.0f, 15, 15, 16, 16);
    }

    private void renderLorePage(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY) {
        boolean spellSelected;
        int x = this.leftPos + 176;
        int y = this.topPos;
        int margin = 2;
        Style textColor = Style.EMPTY.withColor(3288106);
        PoseStack poseStack = guiHelper.pose();
        boolean bl = spellSelected = this.selectedSpellIndex >= 0 && this.selectedSpellIndex < this.spellSlots.size() && this.spellSlots.get(this.selectedSpellIndex).hasSpell();
        MutableComponent title = this.selectedSpellIndex < 0 ? Component.translatable((String)"ui.irons_spellbooks.no_selection") : (spellSelected ? this.spellSlots.get((int)this.selectedSpellIndex).spellSlot.getSpell().getDisplayName((Player)Minecraft.getInstance().player) : Component.translatable((String)"ui.irons_spellbooks.empty_slot"));
        List titleLines = this.font.split((FormattedText)title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int titleY = this.topPos + 10;
        for (FormattedCharSequence line : titleLines) {
            int titleWidth = this.font.width(line);
            int titleX = x + (80 - titleWidth) / 2;
            guiHelper.drawString(this.font, line, titleX, titleY, 0xFFFFFF, false);
            if (spellSelected) {
                Objects.requireNonNull(this.font);
                if (this.isHovering(titleX, titleY, titleWidth, 9, mouseX, mouseY)) {
                    guiHelper.renderTooltip(this.font, TooltipsUtils.createSpellDescriptionTooltip(this.spellSlots.get((int)this.selectedSpellIndex).spellSlot.getSpell(), this.font), mouseX, mouseY);
                }
            }
            Objects.requireNonNull(this.font);
            titleY += 9;
        }
        int titleHeight = this.font.wordWrapHeight((FormattedText)title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int descLine = titleY + 4;
        if (this.selectedSpellIndex < 0 || this.selectedSpellIndex >= this.spellSlots.size() || !this.spellSlots.get(this.selectedSpellIndex).hasSpell()) {
            return;
        }
        Style colorMana = Style.EMPTY.withColor(17577);
        Style colorCast = Style.EMPTY.withColor(0x115511);
        Style colorCooldown = Style.EMPTY.withColor(0x115511);
        AbstractSpell spell = this.spellSlots.get((int)this.selectedSpellIndex).spellSlot.getSpell();
        int spellLevel = this.spellSlots.get((int)this.selectedSpellIndex).spellSlot.getLevel();
        float textScale = 1.0f;
        float reverseScale = 1.0f / textScale;
        Component school = spell.getSchoolType().getDisplayName();
        poseStack.scale(textScale, textScale, textScale);
        this.drawTextWithShadow(this.font, guiHelper, school, x + (80 - this.font.width(school.getString())) / 2, descLine, 0xFFFFFF, 1.0f);
        float f = descLine;
        Objects.requireNonNull(this.font);
        descLine = (int)(f + 9.0f * textScale);
        MutableComponent levelText = Component.translatable((String)"ui.irons_spellbooks.level", (Object[])new Object[]{spellLevel}).withStyle(textColor);
        guiHelper.drawString(this.font, (Component)levelText, x + (80 - this.font.width(levelText.getString())) / 2, descLine, 0xFFFFFF, false);
        float f2 = descLine;
        Objects.requireNonNull(this.font);
        descLine = (int)(f2 + 9.0f * textScale * 2.0f);
        descLine += this.drawStatText(this.font, guiHelper, x + margin, descLine, "ui.irons_spellbooks.mana_cost", textColor, Component.translatable((String)("" + spell.getManaCost(spellLevel))), colorMana, textScale);
        descLine += this.drawText(this.font, guiHelper, (Component)TooltipsUtils.getCastTimeComponent(spell.getCastType(), Utils.timeFromTicks(spell.getEffectiveCastTime(spellLevel, null), 1)), x + margin, descLine, textColor.getColor().getValue(), textScale);
        descLine += this.drawStatText(this.font, guiHelper, x + margin, descLine, "ui.irons_spellbooks.cooldown", textColor, Component.translatable((String)Utils.timeFromTicks(spell.getSpellCooldown(), 1)), colorCooldown, textScale);
        for (MutableComponent component : spell.getUniqueInfo(spellLevel, null)) {
            descLine += this.drawText(this.font, guiHelper, (Component)component, x + margin, descLine, textColor.getColor().getValue(), 1.0f);
        }
        poseStack.scale(reverseScale, reverseScale, reverseScale);
    }

    private void drawTextWithShadow(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale) {
        x = (int)((float)x / scale);
        y = (int)((float)y / scale);
        guiHelper.drawString(font, text, x, y, color);
    }

    private int drawText(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale) {
        x = (int)((float)x / scale);
        y = (int)((float)y / scale);
        guiHelper.drawWordWrap(font, (FormattedText)text, x, y, 80, color);
        return font.wordWrapHeight((FormattedText)text, 80);
    }

    private int drawStatText(Font font, GuiGraphics guiHelper, int x, int y, String translationKey, Style textStyle, MutableComponent stat, Style statStyle, float scale) {
        return this.drawText(font, guiHelper, (Component)Component.translatable((String)translationKey, (Object[])new Object[]{stat.withStyle(statStyle)}).withStyle(textStyle), x, y, 0xFFFFFF, scale);
    }

    private void generateSpellSlots() {
        for (SpellSlotInfo s : this.spellSlots) {
            this.removeWidget((GuiEventListener)s.button);
        }
        this.spellSlots.clear();
        if (!this.isSpellBookSlotted()) {
            return;
        }
        Slot spellBookSlot = (Slot)((InscriptionTableMenu)this.menu).slots.get(36);
        ItemStack spellBookItemStack = spellBookSlot.getItem();
        ISpellContainer spellBookContainer = ISpellContainer.get(spellBookItemStack);
        if (spellBookContainer == null) {
            return;
        }
        SpellSlot[] storedSpells = spellBookContainer.getAllSpells();
        int spellCount = spellBookContainer.getMaxSpellCount();
        if (spellCount > 15) {
            spellCount = 15;
        }
        if (spellCount <= 0) {
            return;
        }
        int boxSize = 19;
        int[] rowCounts = ClientRenderCache.getRowCounts(spellCount);
        int[] row1 = new int[rowCounts[0]];
        int[] row2 = new int[rowCounts[1]];
        int[] row3 = new int[rowCounts[2]];
        int[] rowWidth = new int[]{boxSize * row1.length, boxSize * row2.length, boxSize * row3.length};
        int[] rowHeight = new int[]{row1.length > 0 ? boxSize : 0, row2.length > 0 ? boxSize : 0, row3.length > 0 ? boxSize : 0};
        int overallHeight = rowHeight[0] + rowHeight[1] + rowHeight[2];
        int[][] display = new int[][]{row1, row2, row3};
        int index = 0;
        for (int row = 0; row < display.length; ++row) {
            for (int column = 0; column < display[row].length; ++column) {
                int offset = -rowWidth[row] / 2;
                Vec2 location = new Vec2((float)(offset + column * boxSize), (float)(row * boxSize - overallHeight / 2));
                location.add(-9.0f);
                int temp_index = index;
                this.spellSlots.add(new SpellSlotInfo(storedSpells[index], location, (Button)this.addWidget((GuiEventListener)Button.builder((Component)Component.translatable((String)("" + temp_index)), p_169820_ -> this.setSelectedIndex(temp_index)).pos((int)location.x, (int)location.y).size(boxSize, boxSize).build())));
                ++index;
            }
        }
        this.isDirty = false;
    }

    private void onSpellBookSlotChanged() {
        this.isDirty = true;
        ItemStack spellBookStack = ((Slot)((InscriptionTableMenu)this.menu).slots.get(36)).getItem();
        if (spellBookStack.getItem() instanceof SpellBook) {
            ISpellContainer spellBookContainer = ISpellContainer.get(spellBookStack);
            if (spellBookContainer.getMaxSpellCount() <= this.selectedSpellIndex) {
                this.resetSelectedSpell();
            }
        } else {
            this.resetSelectedSpell();
        }
    }

    private void onInscription() {
        Item item = ((InscriptionTableMenu)this.menu).getSpellBookSlot().getItem().getItem();
        if (item instanceof SpellBook) {
            SpellBook spellBook = (SpellBook)item;
            item = ((InscriptionTableMenu)this.menu).getScrollSlot().getItem().getItem();
            if (item instanceof Scroll) {
                Scroll scroll = (Scroll)item;
                if (this.spellSlots.isEmpty()) {
                    return;
                }
                ISpellContainer scrollContainer = ISpellContainer.get(((InscriptionTableMenu)this.menu).getScrollSlot().getItem());
                SpellData scrollSlot = scrollContainer.getSpellAtIndex(0);
                if (this.selectedSpellIndex < 0 || this.spellSlots.get(this.selectedSpellIndex).hasSpell()) {
                    for (int i = this.selectedSpellIndex + 1; i < this.spellSlots.size(); ++i) {
                        if (this.spellSlots.get(i).hasSpell()) continue;
                        this.setSelectedIndex(i);
                        break;
                    }
                }
                this.setSelectedIndex(Mth.clamp((int)this.selectedSpellIndex, (int)0, (int)(this.spellSlots.size() - 1)));
                if (this.spellSlots.get(this.selectedSpellIndex).hasSpell()) {
                    return;
                }
                this.isDirty = true;
                Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float)1.0f));
                this.minecraft.gameMode.handleInventoryButtonClick(((InscriptionTableMenu)this.menu).containerId, -1);
            }
        }
    }

    private void setSelectedIndex(int index) {
        this.selectedSpellIndex = index;
        this.minecraft.gameMode.handleInventoryButtonClick(((InscriptionTableMenu)this.menu).containerId, index);
    }

    private void resetSelectedSpell() {
        this.setSelectedIndex(-1);
    }

    private boolean isValidInscription() {
        return this.isSpellBookSlotted() && this.isScrollSlotted();
    }

    private boolean isValidExtraction() {
        return this.selectedSpellIndex >= 0 && this.spellSlots.get(this.selectedSpellIndex).hasSpell() && !((Slot)((InscriptionTableMenu)this.menu).slots.get(38)).hasItem();
    }

    private boolean isSpellBookSlotted() {
        return ((Slot)((InscriptionTableMenu)this.menu).slots.get(36)).getItem().getItem() instanceof SpellBook;
    }

    private boolean isScrollSlotted() {
        return ((Slot)((InscriptionTableMenu)this.menu).slots.get(37)).hasItem() && ((Slot)((InscriptionTableMenu)this.menu).slots.get(37)).getItem().getItem() instanceof Scroll;
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    private static class SpellSlotInfo {
        public SpellSlot spellSlot;
        public Vec2 relativePosition;
        public Button button;

        SpellSlotInfo(SpellSlot spellSlot, Vec2 relativePosition, Button button) {
            this.spellSlot = spellSlot;
            this.relativePosition = relativePosition;
            this.button = button;
        }

        public boolean hasSpell() {
            return this.spellSlot != null && !this.spellSlot.spellData().equals(SpellData.EMPTY);
        }
    }
}

