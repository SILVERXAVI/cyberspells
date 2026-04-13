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

public class ChipwareSlotsItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public ChipwareSlotsItem(Item.Properties props, int humanityCost) {
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
        return Set.of(ModTags.Items.BRAIN_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BRAIN);
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
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_RED.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "redshard_strength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "redshard_speed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "redshard_knockback");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "redshard_strength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "redshard_speed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "redshard_knockback");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_ORANGE.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "orangeshard_ore");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "orangeshard_mining");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "orangeshard_ore");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "orangeshard_mining");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_YELLOW.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "yellowshard_haggling");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "yellowshard_haggling");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_GREEN.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "greenshard_xp");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "greenshard_xp");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_CYAN.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyanshard_aim");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyanshard_aim");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_BLUE.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blueshard_swim");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blueshard_mining");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blueshard_movement");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blueshard_oxygen");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blueshard_swim");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blueshard_mining");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blueshard_movement");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blueshard_oxygen");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_PURPLE.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "purpleshard_pearl");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "purpleshard_pearl");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_PINK.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "pinkshard_breeding");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "pinkshard_breeding");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_BROWN.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "brownshard_crops");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "brownshard_crops");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_GRAY.get())) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "grayshard_speed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "grayshard_handling");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "grayshard_speed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "grayshard_handling");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_BLACK.get())) {
            if (player.isSprinting()) {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blackshard_crouch");
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blackshard_sprint");
            } else {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blackshard_sprint");
                CyberwareAttributeHelper.applyModifier((LivingEntity)player, "blackshard_crouch");
            }
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blackshard_sprint");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "blackshard_crouch");
        }
        if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_BIOCHIP.get())) {
            // empty if block
        }
    }
}

