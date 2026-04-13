/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.common.synergy;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CyberwareSynergies {
    public static final List<Synergy> ALL = new ArrayList<Synergy>();

    private CyberwareSynergies() {
    }

    public static void register(Synergy synergy) {
        ALL.add(synergy);
    }

    public static Requirement installed(Supplier<? extends Item> item) {
        return (player, data) -> CyberwareSynergies.countInstalledItem(data, (Item)item.get()) > 0;
    }

    public static Requirement installedCount(Supplier<? extends Item> item, int count) {
        return (player, data) -> CyberwareSynergies.countInstalledItem(data, (Item)item.get()) >= count;
    }

    public static Requirement installedTag(TagKey<Item> tag) {
        return (player, data) -> CyberwareSynergies.countInstalledTag(data, tag) > 0;
    }

    public static Requirement activePowered(Supplier<? extends Item> item) {
        return (player, data) -> CyberwareSynergies.countActivePoweredItem(player, data, (Item)item.get()) > 0;
    }

    private static int countInstalledItem(PlayerCyberwareData data, Item item) {
        int found = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack st;
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
                ++found;
            }
        }
        return found;
    }

    private static int countInstalledTag(PlayerCyberwareData data, TagKey<Item> tag) {
        int found = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack st;
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !st.is(tag)) continue;
                ++found;
            }
        }
        return found;
    }

    private static int countActivePoweredItem(Player player, PlayerCyberwareData data, Item item) {
        int found = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ICyberwareItem cyberItem;
                Item item2;
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !st.is(item) || (item2 = st.getItem()) instanceof ICyberwareItem && (cyberItem = (ICyberwareItem)item2).isToggleableByWheel(st, slot) && !data.isEnabled(slot, i) || !cw.isPowered()) continue;
                ++found;
            }
        }
        return found;
    }

    public static interface Requirement {
        public boolean test(Player var1, PlayerCyberwareData var2);
    }

    public record Synergy(ResourceLocation id, List<Requirement> requirements, List<String> modifierIds) {
        public boolean isActive(Player player, PlayerCyberwareData data) {
            for (Requirement r : this.requirements) {
                if (r.test(player, data)) continue;
                return false;
            }
            return true;
        }

        public void apply(Player player) {
            for (String modId : this.modifierIds) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, modId);
            }
        }

        public void remove(Player player) {
            for (String modId : this.modifierIds) {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, modId);
            }
        }
    }
}

