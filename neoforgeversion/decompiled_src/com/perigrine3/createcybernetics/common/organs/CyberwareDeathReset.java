/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameRules
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$Clone
 */
package com.perigrine3.createcybernetics.common.organs;

import com.perigrine3.createcybernetics.ConfigValues;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import com.perigrine3.createcybernetics.common.surgery.RobosurgeonSlotMap;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.generic.XPCapsuleItem;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareDeathReset {
    private CyberwareDeathReset() {
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData newData = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (newData == null) {
            return;
        }
        if (ConfigValues.KEEP_CYBERWARE) {
            ServerPlayer sp;
            Player original = event.getOriginal();
            PlayerCyberwareData oldData = (PlayerCyberwareData)original.getData(ModAttachments.CYBERWARE);
            if (oldData == null) {
                return;
            }
            RegistryAccess provider = player.registryAccess();
            CompoundTag copied = oldData.serializeNBT((HolderLookup.Provider)provider);
            newData.deserializeNBT(copied, (HolderLookup.Provider)provider);
            CyberwareDeathReset.reapplyInstalledCyberwareHooks(player instanceof ServerPlayer ? (sp = (ServerPlayer)player) : null, newData);
            newData.setDirty();
            player.syncData(ModAttachments.CYBERWARE);
            return;
        }
        newData.resetToDefaultOrgans();
        newData.setDirty();
        player.syncData(ModAttachments.CYBERWARE);
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (!data.hasAnyInSlots(CyberwareSlot.BRAIN)) {
            data.resetToDefaultOrgans();
            data.setDirty();
        }
        player.syncData(ModAttachments.CYBERWARE);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity;
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        if (ConfigValues.KEEP_CYBERWARE) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hadCorticalStack = CyberwareDeathReset.hasCorticalStackInstalled(player);
        int xpPoints = hadCorticalStack ? CyberwareDeathReset.getTotalXpPoints((Player)player) : 0;
        boolean capsuleDropped = false;
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            int mappedSize = RobosurgeonSlotMap.mappedSize(slot);
            for (int i = 0; i < mappedSize; ++i) {
                ItemStack effective;
                InstalledCyberware installed = data.get(slot, i);
                ItemStack installedStack = installed != null && installed.getItem() != null ? installed.getItem() : ItemStack.EMPTY;
                ItemStack def = DefaultOrgans.get(slot, i);
                if (def == null) {
                    def = ItemStack.EMPTY;
                }
                ItemStack itemStack = effective = !installedStack.isEmpty() ? installedStack : def;
                if (effective.isEmpty()) continue;
                if (ModItems.BRAINUPGRADES_CORTICALSTACK != null && effective.is((Item)ModItems.BRAINUPGRADES_CORTICALSTACK.get())) {
                    if (hadCorticalStack && !capsuleDropped) {
                        String ownerName = player.getGameProfile().getName();
                        ItemStack capsule = XPCapsuleItem.makeCapsule(ownerName, xpPoints);
                        player.spawnAtLocation(capsule);
                        capsuleDropped = true;
                    }
                    if (!CyberwareDeathReset.shouldDropInstalledOnDeath(effective, slot)) continue;
                    player.spawnAtLocation(effective.copy());
                    continue;
                }
                if (!CyberwareDeathReset.shouldDropInstalledOnDeath(effective, slot)) continue;
                player.spawnAtLocation(effective.copy());
            }
        }
        CyberwareDeathReset.dropChipwareShards(player, data);
        data.setDirty();
        player.syncData(ModAttachments.CYBERWARE);
    }

    private static void dropChipwareShards(ServerPlayer player, PlayerCyberwareData data) {
        for (int i = 0; i < 2; ++i) {
            ItemStack st = data.getChipwareStack(i);
            if (st == null || st.isEmpty() || !st.is(ModTags.Items.DATA_SHARDS)) continue;
            ItemStack drop = st.copy();
            drop.setCount(1);
            player.spawnAtLocation(drop);
            data.setChipwareStack(i, ItemStack.EMPTY);
        }
    }

    private static boolean shouldDropInstalledOnDeath(ItemStack installedStack, CyberwareSlot slot) {
        if (installedStack.isEmpty()) {
            return false;
        }
        Item item = installedStack.getItem();
        if (item instanceof ICyberwareItem) {
            ICyberwareItem cw = (ICyberwareItem)item;
            return cw.dropsOnDeath(installedStack, slot);
        }
        return true;
    }

    private static boolean hasCorticalStackInstalled(ServerPlayer player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware inst : arr) {
                ItemStack st;
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || ModItems.BRAINUPGRADES_CORTICALSTACK == null || !st.is((Item)ModItems.BRAINUPGRADES_CORTICALSTACK.get())) continue;
                return true;
            }
        }
        return false;
    }

    private static int totalXpForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        }
        if (level <= 31) {
            return (int)(2.5 * (double)level * (double)level - 40.5 * (double)level + 360.0);
        }
        return (int)(4.5 * (double)level * (double)level - 162.5 * (double)level + 2220.0);
    }

    private static int getTotalXpPoints(Player player) {
        int level = player.experienceLevel;
        int base = CyberwareDeathReset.totalXpForLevel(level);
        int toNext = player.getXpNeededForNextLevel();
        int within = Math.round(player.experienceProgress * (float)toNext);
        return Math.max(0, base + within);
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity;
        if (player.level().isClientSide) {
            return;
        }
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        if (ConfigValues.KEEP_CYBERWARE) {
            return;
        }
        if (CyberwareDeathReset.hasCorticalStackInstalled(player)) {
            event.setDroppedExperience(0);
        }
    }

    private static void reapplyInstalledCyberwareHooks(ServerPlayer player, PlayerCyberwareData data) {
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                Item item;
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !data.isEnabled(slot, i) || !((item = st.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem cw = (ICyberwareItem)item;
                cw.onInstalled((Player)player);
            }
        }
    }
}

