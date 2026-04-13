/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.common.events;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.network.payload.TargetingHighlightPayload;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Map;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class TargetingModuleServerEvents {
    private static final int DURATION_TICKS = 900;

    private TargetingModuleServerEvents() {
    }

    @SubscribeEvent
    public static void onDamageDealt(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof LivingEntity)) {
            return;
        }
        LivingEntity target = livingEntity;
        DamageSource src = event.getSource();
        if (src == null) {
            return;
        }
        Entity entity = src.getEntity();
        if (!(entity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)entity;
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        InstalledLoc targeting = TargetingModuleServerEvents.findInstalled(data, (Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES);
        if (targeting == null) {
            return;
        }
        InstalledLoc hudjack = TargetingModuleServerEvents.findInstalledAny(data, (Item)ModItems.EYEUPGRADES_HUDJACK.get());
        if (hudjack == null) {
            return;
        }
        if (!TargetingModuleServerEvents.isEnabledIfToggleable(data, targeting)) {
            return;
        }
        if (!TargetingModuleServerEvents.isEnabledIfToggleable(data, hudjack)) {
            return;
        }
        PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new TargetingHighlightPayload(target.getId(), 900), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    private static boolean isEnabledIfToggleable(PlayerCyberwareData data, InstalledLoc loc) {
        if (loc.stack == null || loc.stack.isEmpty()) {
            return false;
        }
        if (!loc.stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) {
            return true;
        }
        return data.isEnabled(loc.slot, loc.index);
    }

    private static InstalledLoc findInstalled(PlayerCyberwareData data, Item item, CyberwareSlot slot) {
        InstalledCyberware[] arr = data.getAll().get((Object)slot);
        if (arr == null) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != item) continue;
            return new InstalledLoc(slot, i, st);
        }
        return null;
    }

    private static InstalledLoc findInstalledAny(PlayerCyberwareData data, Item item) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != item) continue;
                return new InstalledLoc(slot, i, st);
            }
        }
        return null;
    }

    private record InstalledLoc(CyberwareSlot slot, int index, ItemStack stack) {
    }
}

