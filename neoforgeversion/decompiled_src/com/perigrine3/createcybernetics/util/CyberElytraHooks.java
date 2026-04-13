/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.util;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CyberElytraHooks {
    private CyberElytraHooks() {
    }

    public static boolean isDeployableElytraEnabled(Player player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BONE);
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || ModItems.BONEUPGRADES_ELYTRA == null || !st.is((Item)ModItems.BONEUPGRADES_ELYTRA.get())) continue;
            boolean wheelToggleable = st.is(ModTags.Items.TOGGLEABLE_CYBERWARE);
            return !wheelToggleable || data.isEnabled(CyberwareSlot.BONE, i);
        }
        return false;
    }
}

