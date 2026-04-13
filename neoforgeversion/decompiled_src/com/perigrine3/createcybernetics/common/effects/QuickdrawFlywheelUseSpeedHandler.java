/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Tick
 */
package com.perigrine3.createcybernetics.common.effects;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class QuickdrawFlywheelUseSpeedHandler {
    private static final float CHARGE_SPEED_MULTIPLIER = 2.0f;

    private QuickdrawFlywheelUseSpeedHandler() {
    }

    @SubscribeEvent
    public static void onUseItemTick(LivingEntityUseItemEvent.Tick event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (!QuickdrawFlywheelUseSpeedHandler.isQuickdrawFlywheelPowered(player)) {
            return;
        }
        ItemStack using = event.getItem();
        if (!(using.getItem() instanceof BowItem) && !(using.getItem() instanceof CrossbowItem)) {
            return;
        }
        if (using.getItem() instanceof CrossbowItem && CrossbowItem.isCharged((ItemStack)using)) {
            return;
        }
        int total = using.getUseDuration((LivingEntity)player);
        if (total <= 0) {
            return;
        }
        int remaining = event.getDuration();
        int used = Math.max(0, total - (remaining = Math.min(remaining, total)));
        int boostedUsed = Math.min(total, Math.round((float)used * 2.0f));
        int boostedRemaining = Math.max(0, total - boostedUsed);
        if (boostedRemaining < remaining) {
            event.setDuration(boostedRemaining);
        }
    }

    private static boolean isQuickdrawFlywheelPowered(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        Item target = (Item)ModItems.ARMUPGRADES_FLYWHEEL.get();
        boolean hasRight = false;
        for (int i = 0; i < CyberwareSlot.RARM.size; ++i) {
            ItemStack st;
            InstalledCyberware cw = data.get(CyberwareSlot.RARM, i);
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != target) continue;
            hasRight = true;
            break;
        }
        CyberwareSlot primary = hasRight ? CyberwareSlot.RARM : CyberwareSlot.LARM;
        for (int i = 0; i < primary.size; ++i) {
            ItemStack st;
            InstalledCyberware cw = data.get(primary, i);
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != target) continue;
            return cw.isPowered();
        }
        return false;
    }
}

