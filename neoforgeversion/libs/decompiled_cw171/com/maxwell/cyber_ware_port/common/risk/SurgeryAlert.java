/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 */
package com.maxwell.cyber_ware_port.common.risk;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record SurgeryAlert(Component message, int color) {
    public static SurgeryAlert create(String key, ChatFormatting colorFormat) {
        int colorHex = colorFormat.getColor() != null ? colorFormat.getColor() : 0xFFFFFF;
        MutableComponent text = Component.translatable((String)key).withStyle(colorFormat);
        return new SurgeryAlert((Component)text, colorHex);
    }
}

