/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ExperienceOrb
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.FishingHook
 *  net.minecraft.world.inventory.MerchantMenu
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 *  net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent
 *  net.neoforged.neoforge.event.level.BlockDropsEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class XpMultiplierHandler {
    private static final boolean DEBUG_XP_MESSAGES = false;

    private XpMultiplierHandler() {
    }

    private static void debugXp(ServerPlayer player, String source, int baseXp, int finalXp, double mult) {
    }

    @SubscribeEvent
    public static void onMobXp(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        int baseXp = event.getDroppedExperience();
        if (baseXp <= 0) {
            return;
        }
        double mult = player2.getAttributeValue(ModAttributes.XP_GAIN_MULTIPLIER);
        if (mult <= 1.0) {
            return;
        }
        int finalXp = (int)Math.floor((double)baseXp * mult);
        if (finalXp == baseXp) {
            return;
        }
        event.setDroppedExperience(finalXp);
        XpMultiplierHandler.debugXp(player2, "mob", baseXp, finalXp, mult);
    }

    @SubscribeEvent
    public static void onBlockDropsXp(BlockDropsEvent event) {
        Entity entity = event.getBreaker();
        if (!(entity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)entity;
        int baseXp = event.getDroppedExperience();
        if (baseXp <= 0) {
            return;
        }
        double mult = player.getAttributeValue(ModAttributes.XP_GAIN_MULTIPLIER);
        if (mult <= 1.0) {
            return;
        }
        int finalXp = (int)Math.floor((double)baseXp * mult);
        if (finalXp == baseXp) {
            return;
        }
        event.setDroppedExperience(finalXp);
        XpMultiplierHandler.debugXp(player, "block", baseXp, finalXp, mult);
    }

    @SubscribeEvent
    public static void onFishingXp(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ExperienceOrb)) {
            return;
        }
        ExperienceOrb orb = (ExperienceOrb)entity;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        int baseXp = orb.value;
        if (baseXp <= 0) {
            return;
        }
        for (Player p : level2.getEntitiesOfClass(Player.class, orb.getBoundingBox().inflate(4.0))) {
            FishingHook hook;
            FishingHook fishingHook = p.fishing;
            if (!(fishingHook instanceof FishingHook) || (hook = fishingHook).isRemoved() || !(p instanceof ServerPlayer)) continue;
            ServerPlayer sp = (ServerPlayer)p;
            double mult = sp.getAttributeValue(ModAttributes.XP_GAIN_MULTIPLIER);
            if (mult <= 1.0) {
                return;
            }
            int finalXp = (int)Math.floor((double)baseXp * mult);
            if (finalXp == baseXp) {
                return;
            }
            orb.value = finalXp;
            XpMultiplierHandler.debugXp(sp, "fishing", baseXp, finalXp, mult);
            return;
        }
    }

    @SubscribeEvent
    public static void onBreedingXp(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ExperienceOrb)) {
            return;
        }
        ExperienceOrb orb = (ExperienceOrb)entity;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        int baseXp = orb.value;
        if (baseXp <= 0) {
            return;
        }
        level2.getEntitiesOfClass(Animal.class, orb.getBoundingBox().inflate(3.0)).stream().map(Animal::getLoveCause).filter(p -> p instanceof ServerPlayer).map(p -> p).findFirst().ifPresent(sp -> {
            double mult = sp.getAttributeValue(ModAttributes.XP_GAIN_MULTIPLIER);
            if (mult <= 1.0) {
                return;
            }
            int finalXp = (int)Math.floor((double)baseXp * mult);
            if (finalXp == baseXp) {
                return;
            }
            orb.value = finalXp;
            XpMultiplierHandler.debugXp(sp, "breeding", baseXp, finalXp, mult);
        });
    }

    @SubscribeEvent
    public static void onTradingXp(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ExperienceOrb)) {
            return;
        }
        ExperienceOrb orb = (ExperienceOrb)entity;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        int baseXp = orb.value;
        if (baseXp <= 0) {
            return;
        }
        for (Player p : level2.getEntitiesOfClass(Player.class, orb.getBoundingBox().inflate(4.0))) {
            if (!(p instanceof ServerPlayer)) continue;
            ServerPlayer sp = (ServerPlayer)p;
            if (!(sp.containerMenu instanceof MerchantMenu)) continue;
            double mult = sp.getAttributeValue(ModAttributes.XP_GAIN_MULTIPLIER);
            if (mult <= 1.0) {
                return;
            }
            int finalXp = (int)Math.floor((double)baseXp * mult);
            if (finalXp == baseXp) {
                return;
            }
            orb.value = finalXp;
            XpMultiplierHandler.debugXp(sp, "trading", baseXp, finalXp, mult);
            return;
        }
    }
}

