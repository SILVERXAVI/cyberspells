/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class HyperoxygenationBoostItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK_PER_STACK = 3;
    private static final String NBT_LAST_APPLIED_TICK = "cc_hyperox_last_tick";

    public HyperoxygenationBoostItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.lungsupgrades_hyperoxygenation.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.LUNGS_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.LUNGS);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 3;
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_3");
        player.getPersistentData().remove(NBT_LAST_APPLIED_TICK);
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        int cost;
        if (player.level().isClientSide) {
            return;
        }
        long now = player.level().getGameTime();
        CompoundTag ptag = player.getPersistentData();
        if (ptag.getLong(NBT_LAST_APPLIED_TICK) == now) {
            return;
        }
        ptag.putLong(NBT_LAST_APPLIED_TICK, now);
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "hyperoxygenation_speed_3");
        if (!player.isSprinting()) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        int stacks = 0;
        for (int i = 0; i < CyberwareSlot.LUNGS.size; ++i) {
            if (!data.isInstalled((Item)ModItems.LUNGSUPGRADES_HYPEROXYGENATION.get(), CyberwareSlot.LUNGS, i)) continue;
            ++stacks;
        }
        if (stacks <= 0) {
            return;
        }
        if (stacks > 3) {
            stacks = 3;
        }
        if (!data.tryConsumeEnergy(cost = 3 * stacks)) {
            return;
        }
        if (stacks >= 1) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "hyperoxygenation_speed_1");
        }
        if (stacks >= 2) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "hyperoxygenation_speed_2");
        }
        if (stacks >= 3) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "hyperoxygenation_speed_3");
        }
    }

    @Override
    public void onTick(Player player) {
    }
}

