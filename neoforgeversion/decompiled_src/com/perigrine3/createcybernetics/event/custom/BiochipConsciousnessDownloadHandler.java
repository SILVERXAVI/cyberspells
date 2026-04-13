/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.generic.BiochipDataShardItem;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class BiochipConsciousnessDownloadHandler {
    private BiochipConsciousnessDownloadHandler() {
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer sp : event.getServer().getPlayerList().getPlayers()) {
            if (!sp.hasData(ModAttachments.CYBERWARE)) continue;
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            boolean changed = false;
            HashSet<String> slottedIds = new HashSet<String>();
            for (int i = 0; i < 2; ++i) {
                boolean done;
                ItemStack stack = data.getChipwareStack(i);
                if (stack.isEmpty() || !stack.is((Item)ModItems.DATA_SHARD_BIOCHIP.get())) continue;
                CompoundTag tag = BiochipDataShardItem.getOrCreateTag(stack);
                String id = tag.getString("cc_biochip_id");
                if (id == null || id.isBlank()) {
                    id = UUID.randomUUID().toString();
                    tag.putString("cc_biochip_id", id);
                    changed = true;
                }
                slottedIds.add(id);
                if (!tag.contains("cc_biochip_progress")) {
                    tag.putLong("cc_biochip_progress", 0L);
                    changed = true;
                }
                if (!(done = tag.getBoolean("cc_biochip_done"))) {
                    long ticks = tag.getLong("cc_biochip_progress");
                    if (ticks < 0L) {
                        ticks = 0L;
                    }
                    if (++ticks >= 672000L) {
                        ticks = 672000L;
                        tag.putBoolean("cc_biochip_done", true);
                        BiochipConsciousnessDownloadHandler.writeOwner(tag, sp);
                    }
                    tag.putLong("cc_biochip_progress", ticks);
                    changed = true;
                }
                BiochipDataShardItem.setTag(stack, tag);
                data.setChipwareStack(i, stack);
            }
            if (!(changed |= BiochipConsciousnessDownloadHandler.resetUnslottedBiochips(sp, slottedIds))) continue;
            data.setDirty();
            if (sp.serverLevel().getGameTime() % 20L != 0L) continue;
            sp.syncData(ModAttachments.CYBERWARE);
        }
    }

    private static void writeOwner(CompoundTag tag, ServerPlayer sp) {
        tag.putString("cc_biochip_owner_uuid", sp.getUUID().toString());
        tag.putString("cc_biochip_owner_name", sp.getGameProfile().getName());
    }

    private static boolean resetUnslottedBiochips(ServerPlayer sp, Set<String> slottedIds) {
        boolean changed = false;
        Inventory inv = sp.getInventory();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            String id;
            CompoundTag tag;
            ItemStack s = inv.getItem(i);
            if (s.isEmpty() || !s.is((Item)ModItems.DATA_SHARD_BIOCHIP.get()) || (tag = BiochipDataShardItem.getTagOrNull(s)) == null || tag.getBoolean("cc_biochip_done") || (id = tag.getString("cc_biochip_id")) == null || id.isBlank() || slottedIds.contains(id)) continue;
            BiochipConsciousnessDownloadHandler.resetDownload(tag);
            BiochipDataShardItem.setTag(s, tag);
            changed = true;
        }
        return changed;
    }

    private static void resetDownload(CompoundTag tag) {
        tag.putLong("cc_biochip_progress", 0L);
        tag.putBoolean("cc_biochip_done", false);
        tag.remove("cc_biochip_owner_uuid");
        tag.remove("cc_biochip_owner_name");
    }
}

