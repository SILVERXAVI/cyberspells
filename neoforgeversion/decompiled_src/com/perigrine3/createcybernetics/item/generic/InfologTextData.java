/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 */
package com.perigrine3.createcybernetics.item.generic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class InfologTextData {
    public static final String KEY_TEXT = "cc_infolog_text";
    public static final String KEY_LOCKED = "cc_infolog_locked";

    private InfologTextData() {
    }

    public static String getText(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (cd == null) {
            return "";
        }
        CompoundTag tag = cd.copyTag();
        return tag.contains(KEY_TEXT, 8) ? tag.getString(KEY_TEXT) : "";
    }

    public static boolean isLocked(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (cd == null) {
            return false;
        }
        CompoundTag tag = cd.copyTag();
        return tag.contains(KEY_LOCKED, 1) && tag.getBoolean(KEY_LOCKED);
    }

    public static void setText(ItemStack stack, String text) {
        CompoundTag tag = InfologTextData.getOrCreateTag(stack);
        tag.putString(KEY_TEXT, text == null ? "" : text);
        stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
    }

    public static void setLocked(ItemStack stack, boolean locked) {
        CompoundTag tag = InfologTextData.getOrCreateTag(stack);
        tag.putBoolean(KEY_LOCKED, locked);
        stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
    }

    private static CompoundTag getOrCreateTag(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return cd != null ? cd.copyTag() : new CompoundTag();
    }
}

