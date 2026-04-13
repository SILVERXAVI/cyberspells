/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.ServerChatEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$NameFormat
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$TabListNameFormat
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 */
package com.perigrine3.createcybernetics.common;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class FaceplateAliasHandler {
    private static final String NBT_ALIAS_ACTIVE = "cc_faceplate_active";
    private static final String NBT_ALIAS_TEXT = "cc_faceplate_alias";
    private static final String NBT_FACEPLATE_ST = "cc_faceplate_stack";

    private FaceplateAliasHandler() {
    }

    public static boolean hasActive(ServerPlayer player) {
        CompoundTag p = player.getPersistentData();
        return p.getBoolean(NBT_ALIAS_ACTIVE) && p.contains(NBT_ALIAS_TEXT, 8);
    }

    public static String getAlias(ServerPlayer player) {
        return player.getPersistentData().getString(NBT_ALIAS_TEXT);
    }

    public static boolean hasInterchangeableFaceplateInstalled(ServerPlayer player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        Item required = (Item)ModItems.SKINUPGRADES_FACEPLATE.get();
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware inst : arr) {
                ItemStack st;
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !st.is(required)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean apply(ServerPlayer player, ItemStack faceplateOne) {
        String alias;
        if (faceplateOne.isEmpty()) {
            return false;
        }
        Component customName = (Component)faceplateOne.get(DataComponents.CUSTOM_NAME);
        if (customName == null) {
            return false;
        }
        if (!FaceplateAliasHandler.hasInterchangeableFaceplateInstalled(player)) {
            return false;
        }
        if (FaceplateAliasHandler.hasActive(player)) {
            FaceplateAliasHandler.clear(player, true);
        }
        if ((alias = customName.getString().trim()).isEmpty()) {
            return false;
        }
        CompoundTag p = player.getPersistentData();
        p.putBoolean(NBT_ALIAS_ACTIVE, true);
        p.putString(NBT_ALIAS_TEXT, alias);
        Tag stTag = faceplateOne.saveOptional((HolderLookup.Provider)player.level().registryAccess());
        if (stTag instanceof CompoundTag) {
            CompoundTag c = (CompoundTag)stTag;
            p.put(NBT_FACEPLATE_ST, (Tag)c);
        } else {
            p.remove(NBT_FACEPLATE_ST);
        }
        player.setCustomName((Component)Component.literal((String)alias));
        player.setCustomNameVisible(true);
        player.refreshDisplayName();
        player.refreshTabListName();
        return true;
    }

    public static void clear(ServerPlayer player, boolean returnFaceplate) {
        ItemStack stored;
        CompoundTag p = player.getPersistentData();
        if (returnFaceplate && p.contains(NBT_FACEPLATE_ST, 10) && !(stored = ItemStack.parseOptional((HolderLookup.Provider)player.level().registryAccess(), (CompoundTag)p.getCompound(NBT_FACEPLATE_ST))).isEmpty() && !player.getInventory().add(stored.copy())) {
            player.drop(stored.copy(), false);
        }
        p.remove(NBT_FACEPLATE_ST);
        p.remove(NBT_ALIAS_TEXT);
        p.putBoolean(NBT_ALIAS_ACTIVE, false);
        player.setCustomName(null);
        player.setCustomNameVisible(false);
    }

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat event) {
        ServerPlayer sp;
        Player p = event.getEntity();
        Component custom = p.getCustomName();
        if (custom != null) {
            event.setDisplayname(custom);
            return;
        }
        if (p instanceof ServerPlayer && FaceplateAliasHandler.hasActive(sp = (ServerPlayer)p)) {
            event.setDisplayname((Component)Component.literal((String)FaceplateAliasHandler.getAlias(sp)));
        }
    }

    @SubscribeEvent
    public static void onTabListName(PlayerEvent.TabListNameFormat event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!FaceplateAliasHandler.hasActive(sp)) {
            return;
        }
        event.setDisplayName((Component)Component.literal((String)FaceplateAliasHandler.getAlias(sp)));
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer sp = event.getPlayer();
        if (sp == null) {
            return;
        }
        if (!FaceplateAliasHandler.hasActive(sp)) {
            return;
        }
        event.setCanceled(true);
        MutableComponent name = Component.literal((String)FaceplateAliasHandler.getAlias(sp));
        MutableComponent content = Component.literal((String)event.getRawText());
        MutableComponent line = Component.translatable((String)"chat.type.text", (Object[])new Object[]{name, content});
        sp.server.getPlayerList().broadcastSystemMessage((Component)line, false);
    }

    @SubscribeEvent
    public static void onRightClickAnvil(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        Level level = event.getLevel();
        if (level.isClientSide) {
            return;
        }
        if (!sp.isCrouching()) {
            return;
        }
        if (!level.getBlockState(event.getPos()).is(BlockTags.ANVIL)) {
            return;
        }
        if (!FaceplateAliasHandler.hasActive(sp)) {
            return;
        }
        FaceplateAliasHandler.clear(sp, true);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!FaceplateAliasHandler.hasActive(sp)) {
            return;
        }
        if (!FaceplateAliasHandler.hasInterchangeableFaceplateInstalled(sp)) {
            FaceplateAliasHandler.clear(sp, true);
            return;
        }
        String alias = FaceplateAliasHandler.getAlias(sp);
        if (!alias.isBlank()) {
            sp.setCustomName((Component)Component.literal((String)alias));
            sp.setCustomNameVisible(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!FaceplateAliasHandler.hasActive(sp)) {
            return;
        }
        if (!FaceplateAliasHandler.hasInterchangeableFaceplateInstalled(sp)) {
            FaceplateAliasHandler.clear(sp, true);
            return;
        }
        String alias = FaceplateAliasHandler.getAlias(sp);
        if (!alias.isBlank()) {
            sp.setCustomName((Component)Component.literal((String)alias));
            sp.setCustomNameVisible(true);
        }
    }
}

