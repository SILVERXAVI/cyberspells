/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Rarity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.render.CinderousRarity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class BetrayerSignetRingItem
extends PassiveAbilityCurio {
    public static final int COOLDOWN_IN_TICKS = 100;

    public BetrayerSignetRingItem() {
        super(new Item.Properties().stacksTo(1).rarity((Rarity)CinderousRarity.CINDEROUS_RARITY_PROXY.getValue()).fireResistant(), Curios.RING_SLOT);
        this.showHeader = false;
    }

    @Override
    protected int getCooldownTicks() {
        return 100;
    }

    @SubscribeEvent
    public static void handleAbility(LivingIncomingDamageEvent event) {
        double victimBaseMana;
        LivingEntity victim;
        double victimMaxMana;
        ServerPlayer attackingPlayer;
        BetrayerSignetRingItem RING = (BetrayerSignetRingItem)ItemRegistry.SIGNET_OF_THE_BETRAYER.get();
        Entity entity = event.getSource().getEntity();
        if (entity instanceof ServerPlayer && RING.isEquippedBy((LivingEntity)(attackingPlayer = (ServerPlayer)entity)) && (victimMaxMana = (victim = event.getEntity()).getAttributeValue(AttributeRegistry.MAX_MANA)) > (victimBaseMana = victim.getAttributeBaseValue(AttributeRegistry.MAX_MANA)) && RING.tryProcCooldown((Player)attackingPlayer)) {
            double manaAboveBase = victimMaxMana - victimBaseMana;
            double totalExtraDamagePercent = 0.0;
            for (double conversionRatioPer100 = 0.1; manaAboveBase > 0.0 && conversionRatioPer100 > 0.0; manaAboveBase -= 100.0, conversionRatioPer100 -= 0.01) {
                double step = Math.clamp((double)manaAboveBase, (double)0.0, (double)100.0) * 0.01;
                totalExtraDamagePercent += step * conversionRatioPer100;
            }
            event.setAmount((float)((double)event.getAmount() * Math.max(1.0, 1.0 + totalExtraDamagePercent)));
        }
    }
}

