/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
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
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class LinearFrameItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int PENALTY_REFRESH_TICKS = 40;

    public LinearFrameItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.basecyberware_linearframe.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 10;
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
        return Set.of(CyberwareSlot.BONE);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.BONE);
    }

    @Override
    public void onInstalled(Player player) {
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_health");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_knockback_resist");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_blockbreak");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_speed");
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_health");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_knockback_resist");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_blockbreak");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_speed");
        player.removeEffect(MobEffects.WEAKNESS);
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
    }

    @Override
    public void onTick(Player player, ItemStack installedStack, CyberwareSlot slot, int index) {
        if (player.level().isClientSide) {
            return;
        }
        if (!player.isAlive()) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        InstalledCyberware cw = data.get(slot, index);
        if (cw == null) {
            return;
        }
        boolean powered = cw.isPowered();
        if (!powered) {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_health");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_knockback_resist");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_blockbreak");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "linear_frame_speed");
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false, false));
            return;
        }
        player.removeEffect(MobEffects.WEAKNESS);
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_health");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_knockback_resist");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_blockbreak");
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "linear_frame_speed");
    }

    @Override
    public void onTick(Player player) {
    }
}

