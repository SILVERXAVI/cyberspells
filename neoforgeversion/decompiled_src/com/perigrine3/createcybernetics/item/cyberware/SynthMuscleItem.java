/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
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
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class SynthMuscleItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public SynthMuscleItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_synthmuscle.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 3;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.MUSCLE);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.MUSCLE);
    }

    @Override
    public Set<Item> incompatibleCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.WETWARE_RAVAGERTENDONS.get());
    }

    @Override
    public void onInstalled(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        SynthMuscleItem.setBaseModifiers(player, false);
        SynthMuscleItem.setSprintModifier(player, false);
    }

    @Override
    public void onRemoved(Player player) {
        SynthMuscleItem.setBaseModifiers(player, false);
        SynthMuscleItem.setSprintModifier(player, false);
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
        if (data == null) {
            return;
        }
        InstalledCyberware cw = data.get(slot, index);
        boolean powered = cw != null && cw.isPowered();
        SynthMuscleItem.setBaseModifiers(player, powered);
        SynthMuscleItem.setSprintModifier(player, powered && player.isSprinting());
    }

    private static void setBaseModifiers(Player player, boolean on) {
        if (on) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_strength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_size");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_knockback_resist");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_knockback");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_jump");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_strength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_size");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_knockback_resist");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_knockback");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_jump");
        }
    }

    private static void setSprintModifier(Player player, boolean on) {
        if (on) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "synthmuscle_speed");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "synthmuscle_speed");
        }
    }
}

