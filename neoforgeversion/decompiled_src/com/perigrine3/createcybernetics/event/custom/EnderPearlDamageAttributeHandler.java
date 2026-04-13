/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ThrownEnderpearl
 *  net.minecraft.world.item.Items
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class EnderPearlDamageAttributeHandler {
    private EnderPearlDamageAttributeHandler() {
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) {
            return;
        }
        DamageSource source = event.getSource();
        if (source == null) {
            return;
        }
        if (!EnderPearlDamageAttributeHandler.isEnderPearlTeleportDamage(target, source)) {
            return;
        }
        double mult = target.getAttributeValue(ModAttributes.ENDER_PEARL_DAMAGE);
        if (!Double.isFinite(mult)) {
            return;
        }
        if (mult < 0.0) {
            mult = 0.0;
        }
        float scaled = (float)((double)event.getNewDamage() * mult);
        event.setNewDamage(scaled);
    }

    private static boolean isEnderPearlTeleportDamage(LivingEntity target, DamageSource source) {
        Player player;
        Entity direct = source.getDirectEntity();
        if (direct instanceof ThrownEnderpearl) {
            return true;
        }
        Entity causing = source.getEntity();
        if (causing instanceof ThrownEnderpearl) {
            return true;
        }
        return target instanceof Player && (player = (Player)target).getCooldowns().isOnCooldown(Items.ENDER_PEARL) && source.is(DamageTypes.FALL);
    }
}

