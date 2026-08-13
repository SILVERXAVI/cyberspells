/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentContents
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.contents.PlainTextContents$LiteralContents
 *  net.minecraft.network.chat.contents.TranslatableContents
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TooltipsUtils {
    public static final Style UNIQUE_STYLE = Style.EMPTY.withColor(14697252);
    private static final Style INFO_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
    private static final Style OBFUSCATED_STYLE = AbstractSpell.ELDRITCH_OBFUSCATED_STYLE.applyTo(INFO_STYLE);

    public static int indexOfComponent(List<Component> lines, String key) {
        return TooltipsUtils.indexOfInternal(lines, key::equals);
    }

    public static int indexOfComponentRegex(List<Component> lines, String regex) {
        return TooltipsUtils.indexOfInternal(lines, string -> string.matches(regex));
    }

    public static int indexOfAdvancedText(List<Component> lines, ItemStack itemStack) {
        return TooltipsUtils.indexOfComponentRegex(lines, "item.durability|item.components|" + String.valueOf(BuiltInRegistries.ITEM.getKey((Object)itemStack.getItem())));
    }

    private static int indexOfInternal(List<Component> lines, Predicate<String> comparator) {
        int size = lines.size();
        for (int i = 0; i < size; ++i) {
            PlainTextContents.LiteralContents literalContents;
            TranslatableContents translatableContents;
            Component component = lines.get(i);
            ComponentContents componentContents = component.getContents();
            if (!(componentContents instanceof TranslatableContents ? comparator.test((translatableContents = (TranslatableContents)componentContents).getKey()) : (componentContents = component.getContents()) instanceof PlainTextContents.LiteralContents && comparator.test((literalContents = (PlainTextContents.LiteralContents)componentContents).text()))) continue;
            return i;
        }
        return -1;
    }

    public static List<MutableComponent> formatActiveSpellTooltip(ItemStack stack, SpellData spellData, CastSource castSource, @Nonnull LocalPlayer player) {
        AbstractSpell spell = spellData.getSpell();
        int spellLevel = spell.getLevelFor(spellData.getLevel(), (LivingEntity)player);
        MutableComponent title = TooltipsUtils.getTitleComponent(spellData, player);
        List<MutableComponent> uniqueInfo = spell.getUniqueInfo(spellLevel, (LivingEntity)player);
        MutableComponent manaCost = TooltipsUtils.getManaCostComponent(spell.getCastType(), spell.getManaCost(spellLevel)).withStyle(ChatFormatting.BLUE);
        MutableComponent cooldownTime = Component.translatable((String)"tooltip.irons_spellbooks.cooldown_length_seconds", (Object[])new Object[]{Utils.timeFromTicks(MagicManager.getEffectiveSpellCooldown(spell, (Player)player, castSource), 2)}).withStyle(ChatFormatting.BLUE);
        ArrayList<MutableComponent> lines = new ArrayList<MutableComponent>();
        lines.add(Component.empty());
        lines.add(title);
        uniqueInfo.forEach(line -> lines.add(Component.literal((String)" ").append((Component)line.withStyle(TooltipsUtils.getStyleFor((Player)player, spell)))));
        if (spell.getCastType() != CastType.INSTANT) {
            lines.add(Component.literal((String)" ").append((Component)TooltipsUtils.getCastTimeComponent(spell.getCastType(), Utils.timeFromTicks(spell.getEffectiveCastTime(spellLevel, (LivingEntity)player), 2)).withStyle(ChatFormatting.BLUE)));
        }
        if ((castSource != CastSource.SWORD || ((Boolean)ServerConfigs.SWORDS_CONSUME_MANA.get()).booleanValue()) && spell.getManaCost(spellLevel) > 0) {
            lines.add(manaCost);
        }
        if ((castSource != CastSource.SWORD || ((Double)ServerConfigs.SWORDS_CD_MULTIPLIER.get()).floatValue() > 0.0f) && spell.getSpellCooldown() > 0) {
            lines.add(cooldownTime);
        }
        return lines;
    }

    public static List<Component> formatScrollTooltip(ItemStack stack, Player player) {
        if (stack.getItem() instanceof Scroll && ISpellContainer.isSpellContainer(stack)) {
            ISpellContainer spellList = ISpellContainer.get(stack);
            if (spellList.isEmpty()) {
                return List.of();
            }
            SpellData spellData = spellList.getSpellAtIndex(0);
            AbstractSpell spell = spellData.getSpell();
            int spellLevel = spell.getLevelFor(spellData.getLevel(), (LivingEntity)player);
            MutableComponent levelText = TooltipsUtils.getLevelComponenet(spellData, (LivingEntity)player);
            MutableComponent title = Component.translatable((String)"tooltip.irons_spellbooks.level", (Object[])new Object[]{levelText}).append(" ").append((Component)Component.translatable((String)"tooltip.irons_spellbooks.rarity", (Object[])new Object[]{spell.getRarity(spellData.getLevel()).getDisplayName()}).withStyle(spell.getRarity(spellData.getLevel()).getDisplayName().getStyle())).withStyle(ChatFormatting.GRAY);
            List<MutableComponent> uniqueInfo = spell.getUniqueInfo(spellLevel, (LivingEntity)player);
            MutableComponent whenInSpellBook = Component.translatable((String)"tooltip.irons_spellbooks.scroll_tooltip").withStyle(ChatFormatting.GRAY);
            MutableComponent manaCost = TooltipsUtils.getManaCostComponent(spell.getCastType(), spell.getManaCost(spellLevel)).withStyle(ChatFormatting.BLUE);
            MutableComponent cooldownTime = Component.translatable((String)"tooltip.irons_spellbooks.cooldown_length_seconds", (Object[])new Object[]{Utils.timeFromTicks(MagicManager.getEffectiveSpellCooldown(spell, player, CastSource.SCROLL), 2)}).withStyle(ChatFormatting.BLUE);
            MutableComponent castType = null;
            if (spell.getCastType() != CastType.INSTANT) {
                castType = Component.literal((String)" ").append((Component)TooltipsUtils.getCastTimeComponent(spell.getCastType(), Utils.timeFromTicks(spell.getEffectiveCastTime(spellLevel, (LivingEntity)player), 2)).withStyle(ChatFormatting.BLUE));
            }
            ArrayList<Component> lines = new ArrayList<Component>();
            lines.add((Component)Component.literal((String)" ").append((Component)title));
            uniqueInfo.forEach(line -> lines.add((Component)Component.literal((String)" ").append((Component)line.withStyle(line.getStyle().applyTo(TooltipsUtils.getStyleFor(player, spell))))));
            if (castType != null) {
                lines.add((Component)castType);
            }
            lines.add((Component)Component.empty());
            lines.add((Component)whenInSpellBook);
            if (spell.getManaCost(spellLevel) > 0) {
                lines.add((Component)manaCost);
            }
            if (spell.getSpellCooldown() > 0) {
                lines.add((Component)cooldownTime);
            }
            lines.add((Component)spell.getSchoolType().getDisplayName().copy());
            return lines;
        }
        return List.of();
    }

    public static void addShiftTooltip(List<Component> currentTooltip, List<Component> tooltipToAdd) {
        TooltipsUtils.addShiftTooltip(currentTooltip, (Component)Component.translatable((String)"tooltip.irons_spellbooks.shift_tooltip").withStyle(ChatFormatting.GRAY), tooltipToAdd);
    }

    public static void addShiftTooltip(List<Component> currentTooltip, Component shiftHeader, List<Component> tooltipToAdd) {
        if (ClientInputEvents.isShiftKeyDown) {
            currentTooltip.addAll(tooltipToAdd);
        } else {
            currentTooltip.add(shiftHeader);
        }
    }

    public static MutableComponent getLevelComponenet(SpellData spellData, LivingEntity caster) {
        int levelTotal = spellData.getSpell().getLevelFor(spellData.getLevel(), caster);
        int diff = levelTotal - spellData.getLevel();
        if (diff > 0) {
            return Component.translatable((String)"tooltip.irons_spellbooks.level_plus", (Object[])new Object[]{levelTotal, diff});
        }
        if (diff < 0) {
            return Component.translatable((String)"tooltip.irons_spellbooks.level_minus", (Object[])new Object[]{levelTotal, diff});
        }
        return Component.literal((String)String.valueOf(levelTotal));
    }

    public static MutableComponent getCastTimeComponent(CastType type, String castTime) {
        return switch (type) {
            case CastType.CONTINUOUS -> Component.translatable((String)"tooltip.irons_spellbooks.cast_continuous", (Object[])new Object[]{castTime});
            case CastType.LONG -> Component.translatable((String)"tooltip.irons_spellbooks.cast_long", (Object[])new Object[]{castTime});
            default -> Component.translatable((String)"ui.irons_spellbooks.cast_instant");
        };
    }

    public static MutableComponent getManaCostComponent(CastType castType, int manaCost) {
        if (castType == CastType.CONTINUOUS) {
            return Component.translatable((String)"tooltip.irons_spellbooks.mana_cost_per_second", (Object[])new Object[]{manaCost * 2});
        }
        return Component.translatable((String)"tooltip.irons_spellbooks.mana_cost", (Object[])new Object[]{manaCost});
    }

    public static MutableComponent getTitleComponent(SpellData spellData, @NotNull LocalPlayer player) {
        MutableComponent levelText = TooltipsUtils.getLevelComponenet(spellData, (LivingEntity)player);
        AbstractSpell spell = spellData.getSpell();
        return Component.translatable((String)"tooltip.irons_spellbooks.selected_spell", (Object[])new Object[]{spell.getDisplayName((Player)player), levelText}).withStyle(spell.getSchoolType().getDisplayName().getStyle());
    }

    public static List<FormattedCharSequence> createSpellDescriptionTooltip(AbstractSpell spell, Font font) {
        Player player = MinecraftInstanceHelper.instance.player();
        MutableComponent name = spell.getDisplayName(player);
        List description = font.split((FormattedText)Component.translatable((String)String.format("%s.guide", spell.getComponentId())).withStyle(ChatFormatting.GRAY), 180);
        ArrayList<FormattedCharSequence> hoverText = new ArrayList<FormattedCharSequence>();
        hoverText.add(FormattedCharSequence.forward((String)name.getString(), (Style)name.getStyle().withUnderlined(Boolean.valueOf(true))));
        if (!spell.obfuscateStats(player)) {
            hoverText.addAll(description);
        }
        return hoverText;
    }

    public static Style getStyleFor(Player player, AbstractSpell spell) {
        return spell.obfuscateStats(player) ? OBFUSCATED_STYLE : INFO_STYLE;
    }
}

