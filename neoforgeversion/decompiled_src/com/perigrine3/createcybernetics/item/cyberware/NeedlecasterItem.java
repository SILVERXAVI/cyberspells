/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.GameRules
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$Clone
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class NeedlecasterItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int MAX_LEVELS_TRANSFER = 14;
    private static final int XP_PER_LEVEL_DEATH_DROP = 7;
    private static final String NBT_STORED_LEVELS = "cc_needlecaster_storedLevels";

    public NeedlecasterItem(Item.Properties props, int humanityCost) {
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
    public Set<Item> incompatibleCyberware(ItemStack installedStack, CyberwareSlot slot) {
        if (ModItems.BRAINUPGRADES_CORTICALSTACK != null) {
            return Set.of((Item)ModItems.BRAINUPGRADES_CORTICALSTACK.get());
        }
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
    }

    @EventBusSubscriber(modid="createcybernetics")
    public static final class Events {
        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            if (!Events.hasNeedlecasterInstalled(player)) {
                return;
            }
            int toStore = Math.min(14, player.experienceLevel);
            if (toStore <= 0) {
                return;
            }
            player.getPersistentData().putInt(NeedlecasterItem.NBT_STORED_LEVELS, toStore);
        }

        @SubscribeEvent
        public static void onExperienceDrop(LivingExperienceDropEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            if (!Events.hasNeedlecasterInstalled(player)) {
                return;
            }
            int levelAtDeath = player.experienceLevel;
            if (levelAtDeath <= 14) {
                event.setDroppedExperience(0);
                return;
            }
            int reduceBy = 98;
            int newDrop = Math.max(0, event.getDroppedExperience() - reduceBy);
            event.setDroppedExperience(newDrop);
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (!event.isWasDeath()) {
                return;
            }
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer newPlayer = (ServerPlayer)player;
            Player player2 = event.getOriginal();
            if (!(player2 instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer oldPlayer = (ServerPlayer)player2;
            if (newPlayer.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            if (!Events.hasNeedlecasterInstalled(oldPlayer)) {
                return;
            }
            int stored = oldPlayer.getPersistentData().getInt(NeedlecasterItem.NBT_STORED_LEVELS);
            if (stored <= 0) {
                return;
            }
            oldPlayer.getPersistentData().remove(NeedlecasterItem.NBT_STORED_LEVELS);
            newPlayer.giveExperienceLevels(stored);
        }

        private static boolean hasNeedlecasterInstalled(ServerPlayer player) {
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER != null) {
                return data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER.get(), CyberwareSlot.BRAIN);
            }
            return false;
        }
    }
}

