/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Style
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

public class InvisibiltyRing
extends SimpleDescriptiveCurio {
    public InvisibiltyRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), Curios.RING_SLOT);
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(Boolean.valueOf(true));
    }
}

