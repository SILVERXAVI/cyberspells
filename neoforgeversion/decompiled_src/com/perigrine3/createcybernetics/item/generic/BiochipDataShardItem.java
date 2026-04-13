/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 */
package com.perigrine3.createcybernetics.item.generic;

import com.perigrine3.createcybernetics.item.generic.DataShardItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

public class BiochipDataShardItem
extends DataShardItem {
    public static final long TOTAL_TICKS = 672000L;
    public static final String TAG_ID = "cc_biochip_id";
    public static final String TAG_PROGRESS = "cc_biochip_progress";
    public static final String TAG_DONE = "cc_biochip_done";
    public static final String TAG_OWNER_UUID = "cc_biochip_owner_uuid";
    public static final String TAG_OWNER_NAME = "cc_biochip_owner_name";

    public BiochipDataShardItem(Item.Properties props) {
        super(props);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean done;
        CompoundTag tag = BiochipDataShardItem.getTagOrNull(stack);
        boolean bl = done = tag != null && tag.getBoolean(TAG_DONE);
        if (done) {
            String name = tag.getString(TAG_OWNER_NAME);
            if (name != null && !name.isBlank()) {
                tooltip.add((Component)Component.literal((String)(name + ".dhf")).withStyle(new ChatFormatting[]{ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD}));
            } else {
                tooltip.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_biochip.downloaded").withStyle(new ChatFormatting[]{ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD}));
            }
            return;
        }
        long ticks = tag == null ? 0L : Math.max(0L, tag.getLong(TAG_PROGRESS));
        double pct = 100.0 * (double)ticks / 672000.0;
        double clamped = Mth.clamp((double)pct, (double)0.0, (double)100.0);
        tooltip.add((Component)Component.translatable((String)"item.createcybernetics.data_shard_biochip.downloading", (Object[])new Object[]{String.format("%.1f", clamped)}).withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
    }

    public static CompoundTag getOrCreateTag(ItemStack stack) {
        CustomData cd = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag tag = cd.copyTag();
        return tag == null ? new CompoundTag() : tag;
    }

    public static CompoundTag getTagOrNull(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (cd == null) {
            return null;
        }
        CompoundTag tag = cd.copyTag();
        return tag == null || tag.isEmpty() ? null : tag;
    }

    public static void setTag(ItemStack stack, CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, null);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
        }
    }
}

