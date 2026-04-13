/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$HarvestCheck
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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ReinforcedKnucklesItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public ReinforcedKnucklesItem(Item.Properties props, int humanityCost) {
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
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTARM_ITEMS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTARM_ITEMS);
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
    public void onInstalled(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.LARM)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "reinforced_knuckles_damage1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "reinforced_knuckles_damage2");
        } else if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.RARM) || data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.LARM)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "reinforced_knuckles_damage1");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "reinforced_knuckles_damage2");
        }
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "reinforced_knuckles_damage1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "reinforced_knuckles_damage2");
        this.onInstalled(player);
    }

    @Override
    public void onTick(Player player) {
    }

    private static int countInstalledKnuckles(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return 0;
        }
        int count = 0;
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.LARM)) {
            ++count;
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.RARM)) {
            ++count;
        }
        return count;
    }

    private static boolean hasKnuckles(Player player) {
        return ReinforcedKnucklesItem.countInstalledKnuckles(player) > 0;
    }

    private static boolean bareHanded(Player player) {
        return player.getMainHandItem().isEmpty();
    }

    private static boolean isKnucklesEligible(BlockState state) {
        if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return false;
        }
        return !state.is(BlockTags.INCORRECT_FOR_STONE_TOOL);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class MiningHooks {
        @SubscribeEvent
        public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
            Player player = event.getEntity();
            if (!ReinforcedKnucklesItem.bareHanded(player)) {
                return;
            }
            if (!ReinforcedKnucklesItem.hasKnuckles(player)) {
                return;
            }
            BlockState state = event.getTargetBlock();
            if (!ReinforcedKnucklesItem.isKnucklesEligible(state)) {
                return;
            }
            event.setCanHarvest(true);
        }

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            if (!ReinforcedKnucklesItem.bareHanded(player)) {
                return;
            }
            if (!ReinforcedKnucklesItem.hasKnuckles(player)) {
                return;
            }
            BlockState state = event.getState();
            if (!ReinforcedKnucklesItem.isKnucklesEligible(state)) {
                return;
            }
            float original = event.getOriginalSpeed();
            if (original <= 0.0f) {
                return;
            }
            event.setNewSpeed(original * 6.0f);
        }
    }
}

