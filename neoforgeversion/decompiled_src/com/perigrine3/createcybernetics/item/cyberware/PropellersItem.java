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

public class PropellersItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK_WHEN_SWIMMING = 5;

    public PropellersItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_propellers.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RARM -> Set.of(ModTags.Items.RIGHTLEG_REPLACEMENTS);
            case CyberwareSlot.LARM -> Set.of(ModTags.Items.LEFTLEG_REPLACEMENTS);
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
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return player != null && !player.level().isClientSide && player.isSwimming() ? 5 : 0;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "propeller_swim_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "propeller_swim_2");
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (player.isSwimming()) {
            if (data.hasSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), CyberwareSlot.LLEG)) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, "propeller_swim_1");
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, "propeller_swim_2");
            } else if (data.hasSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), CyberwareSlot.RLEG) || data.hasSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), CyberwareSlot.LLEG)) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, "propeller_swim_1");
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, "propeller_swim_2");
            }
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "propeller_swim_1");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "propeller_swim_2");
        }
    }
}

