/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
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
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class AnkleBracerItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public AnkleBracerItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTLEG_REPLACEMENTS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTLEG_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RLEG, CyberwareSlot.LLEG);
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
    public void onInstalled(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.hasSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), CyberwareSlot.LLEG)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "fall_bracer_fall_1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "fall_bracer_fall_2");
        } else if (data.hasSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), CyberwareSlot.RLEG) || data.hasSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), CyberwareSlot.LLEG)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "fall_bracer_fall_1");
        }
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "fall_bracer_fall_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "fall_bracer_fall_2");
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }
}

