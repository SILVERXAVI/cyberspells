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

public class BonelacingItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public BonelacingItem(Item.Properties props, int humanityCost) {
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
        return Set.of(ModTags.Items.BONE_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BONE);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return null;
    }

    @Override
    public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 3;
    }

    @Override
    public void onInstalled(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        int stacks = 0;
        for (int i = 0; i < CyberwareSlot.BONE.size; ++i) {
            if (!data.isInstalled((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, i)) continue;
            ++stacks;
        }
        if (stacks > 3) {
            stacks = 3;
        }
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_3");
        if (stacks >= 1) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "bonelacing_health_1");
        }
        if (stacks >= 2) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "bonelacing_health_2");
        }
        if (stacks >= 3) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "bonelacing_health_3");
        }
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "bonelacing_health_3");
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }
}

