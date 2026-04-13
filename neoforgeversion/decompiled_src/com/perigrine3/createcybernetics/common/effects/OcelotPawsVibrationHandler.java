/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.VanillaGameEvent
 */
package com.perigrine3.createcybernetics.common.effects;

import com.perigrine3.createcybernetics.item.cyberware.OcelotPawsItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.VanillaGameEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class OcelotPawsVibrationHandler {
    private OcelotPawsVibrationHandler() {
    }

    @SubscribeEvent
    public static void onVanillaGameEvent(VanillaGameEvent event) {
        Entity entity = event.getCause();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (OcelotPawsItem.shouldSuppressVibration(player, (Holder<GameEvent>)event.getVanillaEvent(), event.getContext())) {
            event.setCanceled(true);
        }
    }
}

