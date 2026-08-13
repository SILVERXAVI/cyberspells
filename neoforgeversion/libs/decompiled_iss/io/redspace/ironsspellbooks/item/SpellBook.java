/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$Action
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.HoverEvent
 *  net.minecraft.network.chat.HoverEvent$Action
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  org.jetbrains.annotations.NotNull
 *  top.theillusivec4.curios.api.SlotContext
 *  top.theillusivec4.curios.api.type.capability.ICurio$SoundInfo
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.item.ISpellbook;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.render.RenderHelper;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class SpellBook
extends CurioBaseItem
implements ISpellbook,
IPresetSpellContainer,
ILecternPlaceable {
    protected final int maxSpellSlots;

    public SpellBook() {
        this(1);
    }

    public SpellBook(int maxSpellSlots) {
        this(maxSpellSlots, ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public SpellBook(int maxSpellSlots, Item.Properties pProperties) {
        super(pProperties);
        this.maxSpellSlots = maxSpellSlots;
    }

    public SpellBook withAttribute(Holder<Attribute> attribute, double value) {
        return (SpellBook)this.withAttributes(Curios.SPELLBOOK_SLOT, new AttributeContainer(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public int getMaxSpellSlots() {
        return this.maxSpellSlots;
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    public boolean isUnique() {
        return false;
    }

    public void appendHoverText(@NotNull ItemStack itemStack, Item.TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        Player player;
        if (this.isUnique()) {
            lines.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.spellbook_rarity", (Object[])new Object[]{Component.translatable((String)"tooltip.irons_spellbooks.spellbook_unique").withStyle(TooltipsUtils.UNIQUE_STYLE)}).withStyle(ChatFormatting.GRAY));
        }
        if ((player = MinecraftInstanceHelper.getPlayer()) != null && ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer spellList = ISpellContainer.get(itemStack);
            lines.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.spellbook_spell_count", (Object[])new Object[]{spellList.getMaxSpellCount()}).withStyle(ChatFormatting.GRAY));
            List<SpellSlot> activeSpellSlots = spellList.getActiveSpells();
            if (!activeSpellSlots.isEmpty()) {
                lines.add((Component)Component.empty());
                lines.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.press_to_cast", (Object[])new Object[]{Component.keybind((String)"key.irons_spellbooks.spellbook_cast")}).withStyle(ChatFormatting.GOLD));
                lines.add((Component)Component.empty());
                lines.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.spellbook_tooltip").withStyle(ChatFormatting.GRAY));
                SpellSelectionManager spellSelectionManager = ClientMagicData.getSpellSelectionManager();
                for (int i = 0; i < activeSpellSlots.size(); ++i) {
                    MutableComponent spellText = TooltipsUtils.getTitleComponent(activeSpellSlots.get(i).spellData(), (LocalPlayer)player).setStyle(Style.EMPTY);
                    SpellSelectionManager.SelectionOption option = spellSelectionManager.getSpellSlot(spellSelectionManager.getSelectionIndex());
                    if (MinecraftInstanceHelper.getPlayer() != null && Utils.getPlayerSpellbookStack(MinecraftInstanceHelper.getPlayer()) == itemStack && option != null && option.slot.equals(Curios.SPELLBOOK_SLOT) && option.slotIndex == i) {
                        List<MutableComponent> shiftMessage = TooltipsUtils.formatActiveSpellTooltip(itemStack, spellSelectionManager.getSelectedSpellData(), CastSource.SPELLBOOK, (LocalPlayer)player);
                        shiftMessage.remove(0);
                        TooltipsUtils.addShiftTooltip(lines, (Component)Component.literal((String)"> ").append((Component)spellText).withStyle(ChatFormatting.YELLOW), shiftMessage.stream().map(component -> Component.literal((String)" ").append((Component)component)).collect(Collectors.toList()));
                        continue;
                    }
                    lines.add((Component)Component.literal((String)" ").append((Component)spellText.withStyle(Style.EMPTY.withColor(0x8888FE))));
                }
            }
        }
        super.appendHoverText(itemStack, context, lines, flag);
    }

    @Override
    @NotNull
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo((SoundEvent)SoundRegistry.EQUIP_SPELL_BOOK.get(), 1.0f, 1.0f);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer.set(itemStack, ISpellContainer.create(this.getMaxSpellSlots(), true, true));
        }
    }

    @Override
    public List<Component> getPages(ItemStack stack) {
        ISpellContainer spellbookData = ISpellContainer.get(stack);
        if (spellbookData != null && !spellbookData.isEmpty()) {
            Player player = MinecraftInstanceHelper.getPlayer();
            return spellbookData.getActiveSpells().stream().map(slot -> {
                int color = slot.getSpell().getSchoolType().getDisplayName().getStyle().getColor().getValue();
                color = RenderHelper.colorLerp(0.6f, color, 0);
                Style titleStyle = Style.EMPTY.withColor(color).withUnderlined(Boolean.valueOf(true)).withBold(Boolean.valueOf(true)).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.patreon.com/iron431"));
                boolean hideStats = false;
                if (player != null) {
                    List<MutableComponent> scrollTooltip = TooltipsUtils.formatActiveSpellTooltip(null, slot.spellData(), CastSource.SPELLBOOK, (LocalPlayer)player);
                    scrollTooltip.remove(0);
                    titleStyle = titleStyle.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Object)((Component)scrollTooltip.stream().reduce((a, b) -> a.append("\n").append((Component)b)).get())));
                    if (slot.getSpell().obfuscateStats(player)) {
                        hideStats = true;
                    }
                }
                MutableComponent title = Component.translatable((String)slot.getSpell().getComponentId()).withStyle(titleStyle);
                MutableComponent desc = Component.translatable((String)(slot.getSpell().getComponentId() + ".guide")).withStyle(ChatFormatting.BLACK);
                MutableComponent page = Component.literal((String)"").append((Component)title).append("\n\n").append((Component)desc);
                if (hideStats) {
                    page = page.withStyle(page.getStyle().applyTo(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"alt"))));
                }
                return page;
            }).toList();
        }
        return List.of(Component.translatable((String)"ui.irons_spellbooks.empty_spellbook_lectern").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
    }
}

