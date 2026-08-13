/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SimpleDescriptiveCurio
extends CurioBaseItem {
    @Nullable
    final String slotIdentifier;
    Style descriptionStyle;
    boolean showHeader;

    public SimpleDescriptiveCurio(Item.Properties properties, String slotIdentifier) {
        super(properties);
        this.slotIdentifier = slotIdentifier;
        this.showHeader = true;
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.YELLOW);
    }

    public SimpleDescriptiveCurio(Item.Properties properties) {
        this(properties, null);
    }

    public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext tooltipContext, ItemStack stack) {
        List attrTooltip = super.getAttributesTooltip(tooltips, tooltipContext, stack);
        boolean needHeader = attrTooltip.isEmpty();
        List<Component> descriptionLines = this.getDescriptionLines(stack);
        if (needHeader && !descriptionLines.isEmpty()) {
            attrTooltip.add(Component.empty());
            attrTooltip.add(Component.translatable((String)("curios.modifiers." + this.slotIdentifier)).withStyle(ChatFormatting.GOLD));
        }
        attrTooltip.addAll(descriptionLines);
        return attrTooltip;
    }

    public List<Component> getDescriptionLines(ItemStack stack) {
        return List.of(this.getDescription(stack));
    }

    public Component getDescription(ItemStack stack) {
        return Component.literal((String)" ").append((Component)Component.translatable((String)(this.getDescriptionId() + ".desc"))).withStyle(this.descriptionStyle);
    }
}

