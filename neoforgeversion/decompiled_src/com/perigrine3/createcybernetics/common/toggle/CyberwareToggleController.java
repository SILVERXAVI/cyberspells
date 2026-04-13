/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.common.toggle;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CyberwareToggleController {
    private CyberwareToggleController() {
    }

    private static String key(ResourceLocation itemId) {
        return "cc_toggle_" + itemId.getNamespace() + "_" + itemId.getPath();
    }

    public static boolean isActive(ServerPlayer player, ResourceLocation itemId) {
        String k;
        CompoundTag p = player.getPersistentData();
        return !p.contains(k = CyberwareToggleController.key(itemId)) || p.getBoolean(k);
    }

    public static boolean setActive(ServerPlayer player, ResourceLocation itemId, boolean active) {
        String k;
        boolean before;
        CompoundTag p = player.getPersistentData();
        boolean bl = before = !p.contains(k = CyberwareToggleController.key(itemId)) || p.getBoolean(k);
        if (before == active) {
            return before;
        }
        p.putBoolean(k, active);
        return before;
    }

    public static boolean toggle(ServerPlayer player, ResourceLocation itemId) {
        boolean now = !CyberwareToggleController.isActive(player, itemId);
        CyberwareToggleController.setActive(player, itemId, now);
        return now;
    }

    public static boolean hasToggleableInstalled(ServerPlayer player, ResourceLocation itemId) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(itemId);
        if (item == null) {
            return false;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || stack.getItem() != item || !stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) continue;
                return true;
            }
        }
        return false;
    }

    public static Map<ResourceLocation, ItemStack> collectToggleables(ServerPlayer player) {
        LinkedHashMap<ResourceLocation, ItemStack> out = new LinkedHashMap<ResourceLocation, ItemStack>();
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return out;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return out;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ResourceLocation id;
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE) || (id = BuiltInRegistries.ITEM.getKey((Object)stack.getItem())) == null) continue;
                out.putIfAbsent(id, stack.copy());
            }
        }
        return out;
    }
}

