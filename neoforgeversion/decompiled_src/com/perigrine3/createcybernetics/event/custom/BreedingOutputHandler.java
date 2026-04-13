/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class BreedingOutputHandler {
    private static final List<PendingBreed> PENDING = new ArrayList<PendingBreed>();

    private BreedingOutputHandler() {
    }

    @SubscribeEvent
    public static void onBabySpawn(BabyEntitySpawnEvent event) {
        Animal parentA;
        Mob mob;
        ServerLevel level;
        Player player;
        block10: {
            block9: {
                player = event.getCausedByPlayer();
                if (player == null) {
                    return;
                }
                Level level2 = player.level();
                if (!(level2 instanceof ServerLevel)) {
                    return;
                }
                level = (ServerLevel)level2;
                mob = event.getParentA();
                if (!(mob instanceof Animal)) break block9;
                parentA = (Animal)mob;
                mob = event.getParentB();
                if (mob instanceof Animal) break block10;
            }
            return;
        }
        Animal parentB = (Animal)mob;
        AgeableMob ageableMob = event.getChild();
        if (!(ageableMob instanceof AgeableMob)) {
            return;
        }
        AgeableMob child = ageableMob;
        double mult = player.getAttributeValue(ModAttributes.BREEDING_MULTIPLIER);
        if (!Double.isFinite(mult) || mult <= 1.0) {
            return;
        }
        int guaranteed = (int)Math.floor(mult) - 1;
        double remainder = mult - Math.floor(mult);
        if (remainder > 0.0 && level.random.nextDouble() < remainder) {
            ++guaranteed;
        }
        if (guaranteed <= 0) {
            return;
        }
        PENDING.add(new PendingBreed(level, parentA, parentB, child, guaranteed));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (PENDING.isEmpty()) {
            return;
        }
        ArrayList<PendingBreed> copy = new ArrayList<PendingBreed>(PENDING);
        PENDING.clear();
        for (PendingBreed p : copy) {
            if (p.level.isClientSide()) continue;
            for (int i = 0; i < p.extras; ++i) {
                AgeableMob extra = p.parentA.getBreedOffspring(p.level, (AgeableMob)p.parentB);
                if (extra == null) continue;
                extra.setAge(-24000);
                extra.moveTo(p.child.getX() + (p.level.random.nextDouble() - 0.5) * 0.6, p.child.getY(), p.child.getZ() + (p.level.random.nextDouble() - 0.5) * 0.6, p.level.random.nextFloat() * 360.0f, 0.0f);
                p.level.addFreshEntity((Entity)extra);
            }
        }
    }

    private record PendingBreed(ServerLevel level, Animal parentA, Animal parentB, AgeableMob child, int extras) {
    }
}

