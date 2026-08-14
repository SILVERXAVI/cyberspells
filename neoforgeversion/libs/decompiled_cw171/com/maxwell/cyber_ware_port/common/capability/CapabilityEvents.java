/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.common.util.FakePlayer
 *  net.neoforged.neoforge.event.entity.living.LivingDropsEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$Clone
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.capability;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.util.CyberwareBodyStatus;
import com.maxwell.cyber_ware_port.config.CyberwareConfig;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

@EventBusSubscriber(modid="cyber_ware_port")
public class CapabilityEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            CyberwareUserData cap = (CyberwareUserData)serverPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            if (!cap.isInitialized()) {
                cap.fillWithHumanParts();
            }
            cap.syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            ((CyberwareUserData)serverPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            CyberwareUserData data = (CyberwareUserData)serverPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            data.recalculateCapacity(serverPlayer);
            data.syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        CyberwareUserData oldData = (CyberwareUserData)original.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        CyberwareUserData newData = (CyberwareUserData)newPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (!event.isWasDeath()) {
            newData.copyFrom(oldData);
        } else if (((Boolean)CyberwareConfig.KEEP_CYBERWARE_ON_DEATH.get()).booleanValue()) {
            newData.copyFrom(oldData);
            newData.setRespawnGracePeriod(12000);
            newData.ensureEssentialPartsAfterDeath();
        } else {
            newData.resetToHuman();
            ItemStackHandler handler = newData.getInstalledCyberware();
            for (int i = 0; i < handler.getSlots(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.isEmpty() || !((Boolean)stack.getOrDefault(CyberWare.GHOST_COMPONENT, (Object)false)).booleanValue()) continue;
                handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        if (newPlayer instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)newPlayer;
            newData.recalculateCapacity(serverPlayer);
            newData.syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) {
            Collection drops = event.getDrops();
            Iterator iterator = drops.iterator();
            while (iterator.hasNext()) {
                ItemStack stack = ((ItemEntity)iterator.next()).getItem();
                if (!((Boolean)stack.getOrDefault(CyberWare.GHOST_COMPONENT, (Object)false)).booleanValue()) continue;
                iterator.remove();
            }
        }
    }

    private static boolean isHandFunctional(Player player, InteractionHand hand) {
        if (player instanceof FakePlayer) {
            return true;
        }
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        return new CyberwareBodyStatus(data.getInstalledCyberware()).isHandFunctional();
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!CapabilityEvents.isHandFunctional(event.getEntity(), InteractionHand.MAIN_HAND)) {
            event.setNewSpeed(event.getOriginalSpeed() * 0.05f);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.is((Block)ModBlocks.ROBO_SURGEON.get()) || state.is((Block)ModBlocks.SURGERY_CHAMBER.get())) {
            return;
        }
        if (!CapabilityEvents.isHandFunctional(event.getEntity(), InteractionHand.MAIN_HAND)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!CapabilityEvents.isHandFunctional(event.getEntity(), event.getHand())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.is((Block)ModBlocks.ROBO_SURGEON.get()) || state.is((Block)ModBlocks.SURGERY_CHAMBER.get()) || state.is(BlockTags.DOORS) || state.is(BlockTags.TRAPDOORS) || state.is(BlockTags.BUTTONS) || state.is(BlockTags.BEDS)) {
            return;
        }
        if (!CapabilityEvents.isHandFunctional(event.getEntity(), event.getHand())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!CapabilityEvents.isHandFunctional(event.getEntity(), event.getHand())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        Player player;
        CyberwareUserData data;
        CyberwareBodyStatus status;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player && !(status = new CyberwareBodyStatus((data = (CyberwareUserData)(player = (Player)livingEntity).getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).getInstalledCyberware())).hasPart(BodyPartType.SKIN)) {
            event.setAmount(event.getAmount() * 1.5f);
        }
    }
}

