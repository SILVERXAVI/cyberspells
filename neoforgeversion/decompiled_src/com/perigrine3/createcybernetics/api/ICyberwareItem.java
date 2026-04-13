/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.DyedItemColor
 */
package com.perigrine3.createcybernetics.api;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public interface ICyberwareItem {
    public Set<CyberwareSlot> getSupportedSlots();

    default public boolean supportsSlot(CyberwareSlot slot) {
        return this.getSupportedSlots().contains((Object)slot);
    }

    default public Set<Item> requiresCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of();
    }

    default public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of();
    }

    default public Set<Item> incompatibleCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of();
    }

    default public Set<TagKey<Item>> incompatibleCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of();
    }

    public boolean replacesOrgan();

    public Set<CyberwareSlot> getReplacedOrgans();

    default public TagKey<Item> getReplacedOrganItemTag(ItemStack installedStack, CyberwareSlot slot) {
        return null;
    }

    default public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 1;
    }

    public int getHumanityCost();

    default public void onInstalled(Player player) {
    }

    default public void onRemoved(Player player) {
    }

    default public void onTick(Player player) {
    }

    default public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        this.onTick(player);
    }

    default public void onInstalled(Player player, ItemStack installedStack) {
        this.onInstalled(player);
    }

    default public boolean dropsOnDeath(ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    default public boolean isToggleableByWheel(ItemStack installedStack, CyberwareSlot slot) {
        return installedStack.is(ModTags.Items.TOGGLEABLE_CYBERWARE);
    }

    default public boolean isEnabledByWheel(Player player) {
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
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware installed = arr[i];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || st.getItem() != (Item)this) continue;
                if (!this.isToggleableByWheel(st, slot)) {
                    return true;
                }
                return data.isEnabled(slot, i);
            }
        }
        return false;
    }

    default public int getEnergyGeneratedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean shouldGenerateEnergyThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.getEnergyGeneratedPerTick(player, installedStack, slot) > 0;
    }

    default public int getEnergyGeneratedThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.shouldGenerateEnergyThisTick(player, installedStack, slot) ? Math.max(0, this.getEnergyGeneratedPerTick(player, installedStack, slot)) : 0;
    }

    default public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean shouldUseEnergyThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.getEnergyUsedPerTick(player, installedStack, slot) > 0;
    }

    default public int getEnergyUsedThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.shouldUseEnergyThisTick(player, installedStack, slot) ? Math.max(0, this.getEnergyUsedPerTick(player, installedStack, slot)) : 0;
    }

    default public int getEnergyCapacity(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean shouldContributeCapacityThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.getEnergyCapacity(player, installedStack, slot) > 0;
    }

    default public boolean storesEnergy(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.shouldContributeCapacityThisTick(player, installedStack, slot) && this.getEnergyCapacity(player, installedStack, slot) > 0;
    }

    default public int getMaxEnergyReceivePerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.storesEnergy(player, installedStack, slot) ? this.getEnergyCapacity(player, installedStack, slot) : 0;
    }

    default public int getMaxEnergyExtractPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.storesEnergy(player, installedStack, slot) ? this.getEnergyCapacity(player, installedStack, slot) : 0;
    }

    default public int getEnergyActivationCost(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean shouldConsumeActivationEnergyThisTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    default public String getActivationPaidNbtKey(Player player, ItemStack installedStack, CyberwareSlot slot) {
        String cls = this.getClass().getName().replace('.', '_');
        return "cc_energy_actpaid_" + cls + "_" + slot.name();
    }

    default public int getEnergyPriority(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.getEnergyUsedPerTick(player, installedStack, slot) > 0 || this.getEnergyActivationCost(player, installedStack, slot) > 0;
    }

    default public void onPowerLost(Player player, ItemStack installedStack, CyberwareSlot slot) {
    }

    default public void onPowerRestored(Player player, ItemStack installedStack, CyberwareSlot slot) {
    }

    default public void onUnpoweredTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
    }

    default public void onPoweredTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
    }

    default public boolean acceptsGeneratedEnergy(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return this.storesEnergy(player, installedStack, slot);
    }

    default public boolean acceptsChargerEnergy(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    default public int getChargerEnergyReceivePerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    default public boolean isDyeable(ItemStack stack, CyberwareSlot slot) {
        return false;
    }

    default public boolean isDyeable(ItemStack stack) {
        return false;
    }

    default public boolean isDyed(ItemStack installedStack, CyberwareSlot slot) {
        if (installedStack == null || installedStack.isEmpty()) {
            return false;
        }
        if (!this.isDyeable(installedStack, slot)) {
            return false;
        }
        return installedStack.has(DataComponents.DYED_COLOR);
    }

    default public int dyeColor(ItemStack installedStack, CyberwareSlot slot) {
        if (!this.isDyed(installedStack, slot)) {
            return -1;
        }
        DyedItemColor dyed = (DyedItemColor)installedStack.get(DataComponents.DYED_COLOR);
        if (dyed == null) {
            return -1;
        }
        return 0xFF000000 | dyed.rgb();
    }
}

