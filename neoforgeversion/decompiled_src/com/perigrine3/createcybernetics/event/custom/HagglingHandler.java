/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MerchantMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerContainerEvent$Close
 *  net.neoforged.neoforge.event.entity.player.PlayerContainerEvent$Open
 *  net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class HagglingHandler {
    private static final Map<UUID, Map<MerchantOffer, OfferSnapshot>> SNAPSHOTS = new HashMap<UUID, Map<MerchantOffer, OfferSnapshot>>();

    private HagglingHandler() {
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        double haggle = player.getAttributeValue(ModAttributes.HAGGLING);
        if (haggle <= 1.0) {
            return;
        }
        AbstractContainerMenu menu = event.getContainer();
        if (!(menu instanceof MerchantMenu)) {
            return;
        }
        MerchantMenu merchantMenu = (MerchantMenu)menu;
        MerchantOffers offers = merchantMenu.getOffers();
        if (offers == null || offers.isEmpty()) {
            return;
        }
        HashMap<MerchantOffer, OfferSnapshot> perPlayer = new HashMap<MerchantOffer, OfferSnapshot>();
        for (MerchantOffer offer : offers) {
            perPlayer.put(offer, new OfferSnapshot(offer.getSpecialPriceDiff(), offer.getCostA().getCount(), !offer.getCostB().isEmpty(), offer.getResult().getCount()));
            ItemStack currentA = offer.getCostA();
            int currentCount = currentA.getCount();
            if (currentCount <= 1) continue;
            int discounted = Math.max(1, (int)Math.ceil((double)currentCount / haggle));
            int delta = discounted - currentCount;
            offer.addToSpecialPriceDiff(delta);
            int after = offer.getCostA().getCount();
            if (after >= 1) continue;
            offer.addToSpecialPriceDiff(1 - after);
        }
        SNAPSHOTS.put(player.getUUID(), perPlayer);
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        AbstractContainerMenu menu = event.getContainer();
        if (!(menu instanceof MerchantMenu)) {
            return;
        }
        Map<MerchantOffer, OfferSnapshot> perPlayer = SNAPSHOTS.remove(player.getUUID());
        if (perPlayer == null) {
            return;
        }
        for (Map.Entry<MerchantOffer, OfferSnapshot> e : perPlayer.entrySet()) {
            MerchantOffer offer = e.getKey();
            OfferSnapshot snap = e.getValue();
            offer.setSpecialPriceDiff(snap.originalSpecialPriceDiff);
        }
    }

    @SubscribeEvent
    public static void onTradeCompleted(TradeWithVillagerEvent event) {
        boolean originallyHadCostB;
        int originalCostA;
        ItemStack costB;
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        double haggle = player.getAttributeValue(ModAttributes.HAGGLING);
        if (haggle <= 1.0) {
            return;
        }
        MerchantOffer offer = event.getMerchantOffer();
        OfferSnapshot snap = null;
        Map<MerchantOffer, OfferSnapshot> perPlayer = SNAPSHOTS.get(player.getUUID());
        if (perPlayer != null) {
            snap = perPlayer.get(offer);
        }
        if (!(costB = offer.getCostB()).isEmpty()) {
            HagglingHandler.refundDiscount(player, costB, haggle);
        }
        int n = originalCostA = snap != null ? snap.originalCostACount : offer.getCostA().getCount();
        boolean bl = snap != null ? snap.hadCostB : (originallyHadCostB = !offer.getCostB().isEmpty());
        if (!originallyHadCostB && originalCostA == 1) {
            ItemStack bonus = offer.getResult().copy();
            HagglingHandler.giveToPlayer(player, bonus);
        }
    }

    private static void refundDiscount(Player player, ItemStack paidCost, double haggle) {
        if (paidCost.isEmpty()) {
            return;
        }
        int original = paidCost.getCount();
        if (original <= 1) {
            return;
        }
        int discounted = Math.max(1, (int)Math.ceil((double)original / haggle));
        int refund = original - discounted;
        if (refund <= 0) {
            return;
        }
        ItemStack refundStack = paidCost.copy();
        refundStack.setCount(refund);
        HagglingHandler.giveToPlayer(player, refundStack);
    }

    private static void giveToPlayer(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        player.addItem(stack);
        if (!stack.isEmpty()) {
            player.drop(stack, false);
        }
    }

    private record OfferSnapshot(int originalSpecialPriceDiff, int originalCostACount, boolean hadCostB, int originalResultCount) {
    }
}

