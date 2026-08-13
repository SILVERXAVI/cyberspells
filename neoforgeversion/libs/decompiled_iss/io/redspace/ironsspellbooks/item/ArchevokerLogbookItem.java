/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.network.Filterable
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.WrittenBookContent
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.ReadableLoreItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.WrittenBookContent;

public class ArchevokerLogbookItem
extends ReadableLoreItem {
    public static WrittenBookContent TRANSLATED_CONTENTS = new WrittenBookContent(Filterable.passThrough((Object)""), "Archevoker", 0, List.of(Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").append("2:\n\n").append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_1.1"))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_1.2")), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").append("14:\n\n").append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_2.1"))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_2.2")), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").append("31:\n\n").append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_3.1"))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_3.2")), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").append("73:\n\n").append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_4.1"))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_4.2"))), true);
    public static WrittenBookContent UNTRANSLATED_CONTENTS = new WrittenBookContent(Filterable.passThrough((Object)""), "Archevoker", 0, List.of(Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))).append((Component)Component.literal((String)"2:\n\n").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"default")))).append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_1.1").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_1.2").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt")))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))).append((Component)Component.literal((String)"14:\n\n").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"default")))).append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_2.1").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_2.2").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt")))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))).append((Component)Component.literal((String)"31:\n\n").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"default")))).append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_3.1").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_3.2").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt")))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.header").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))).append((Component)Component.literal((String)"73:\n\n").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"default")))).append((Component)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_4.1").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))))), Filterable.passThrough((Object)Component.translatable((String)"item.irons_spellbooks.archevoker_log.entry_4.2").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"illageralt"))))), true);
    private final boolean translated;

    public ArchevokerLogbookItem(boolean translated, Item.Properties pProperties) {
        super(IronsSpellbooks.id("textures/entity/lectern/archevoker_logbook.png"), pProperties);
        this.translated = translated;
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        if (this.translated) {
            pTooltipComponents.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.translated").withStyle(ChatFormatting.YELLOW));
        } else {
            pTooltipComponents.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.untranslated").withStyle(ChatFormatting.RED));
        }
    }
}

