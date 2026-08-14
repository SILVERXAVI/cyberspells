/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.api.json;

import com.maxwell.cyber_ware_port.api.json.CyberwareDataManager;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CyberwareAPI {
    @Nullable
    public static ICyberware getCyberware(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Item item = stack.getItem();
        if (item instanceof ICyberware) {
            ICyberware cyber = (ICyberware)item;
            return cyber;
        }
        return CyberwareDataManager.DYNAMIC_CYBERWARE.get(stack.getItem());
    }

    public static boolean isCyberware(ItemStack stack) {
        return CyberwareAPI.getCyberware(stack) != null;
    }
}

