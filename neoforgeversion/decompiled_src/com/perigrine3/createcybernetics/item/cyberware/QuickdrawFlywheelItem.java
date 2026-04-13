/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class QuickdrawFlywheelItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK_WHILE_USING = 2;

    public QuickdrawFlywheelItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_flywheel.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RARM -> Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
            case CyberwareSlot.LARM -> Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RARM, CyberwareSlot.LARM);
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
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        if (player == null) {
            return 0;
        }
        if (player.level().isClientSide) {
            return 0;
        }
        if (!player.isAlive()) {
            return 0;
        }
        ItemStack using = player.getUseItem();
        if (using == null || using.isEmpty()) {
            return 0;
        }
        Item u = using.getItem();
        if (!(u instanceof BowItem) && !(u instanceof CrossbowItem)) {
            return 0;
        }
        if (u instanceof CrossbowItem && CrossbowItem.isCharged((ItemStack)using)) {
            return 0;
        }
        if (!this.shouldChargeOnThisSlot(player, slot)) {
            return 0;
        }
        return 2;
    }

    private boolean shouldChargeOnThisSlot(Player player, CyberwareSlot slot) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        boolean hasRight = false;
        for (int i = 0; i < CyberwareSlot.RARM.size; ++i) {
            ItemStack st;
            InstalledCyberware cw = data.get(CyberwareSlot.RARM, i);
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != this) continue;
            hasRight = true;
            break;
        }
        if (hasRight) {
            return slot == CyberwareSlot.RARM;
        }
        return slot == CyberwareSlot.LARM;
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }
}

