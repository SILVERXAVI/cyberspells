/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.Skeleton
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class SpawnHijackHandler {
    private static final String NBT_NO_HIJACK = "cc_no_hijack";
    private static final float ZOMBIE_REPLACE_CHANCE = 0.15f;
    private static final float SKELETON_REPLACE_CHANCE = 0.1f;

    private SpawnHijackHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Mob replacement;
        float chance;
        boolean isSkeleton;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        Entity entity = event.getEntity();
        if (entity.getPersistentData().getBoolean(NBT_NO_HIJACK)) {
            return;
        }
        if (entity.getType() == ModEntities.CYBERZOMBIE.get()) {
            return;
        }
        if (entity.getType() == ModEntities.CYBERSKELETON.get()) {
            return;
        }
        boolean isZombie = entity.getType() == EntityType.ZOMBIE && entity instanceof Zombie;
        boolean bl = isSkeleton = entity.getType() == EntityType.SKELETON && entity instanceof Skeleton;
        if (!isZombie && !isSkeleton) {
            return;
        }
        float f = chance = isZombie ? 0.15f : 0.1f;
        if (level2.getRandom().nextFloat() >= chance) {
            return;
        }
        MobSpawnType spawnType = null;
        try {
            spawnType = ((Monster)entity).getSpawnType();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (spawnType == MobSpawnType.CONVERSION) {
            return;
        }
        if (spawnType == MobSpawnType.COMMAND) {
            return;
        }
        if (spawnType == MobSpawnType.SPAWN_EGG) {
            return;
        }
        Mob mob = replacement = isZombie ? (Mob)ModEntities.CYBERZOMBIE.get().create((Level)level2) : (Mob)ModEntities.CYBERSKELETON.get().create((Level)level2);
        if (replacement == null) {
            return;
        }
        replacement.getPersistentData().putBoolean(NBT_NO_HIJACK, true);
        replacement.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
        replacement.setDeltaMovement(entity.getDeltaMovement());
        if (entity.hasCustomName()) {
            replacement.setCustomName(entity.getCustomName());
            replacement.setCustomNameVisible(entity.isCustomNameVisible());
        }
        if (entity.isSilent()) {
            replacement.setSilent(true);
        }
        BlockPos at = BlockPos.containing((Position)replacement.position());
        SpawnGroupData groupData = null;
        MobSpawnType finalizeType = spawnType != null ? spawnType : MobSpawnType.NATURAL;
        replacement.finalizeSpawn((ServerLevelAccessor)level2, level2.getCurrentDifficultyAt(at), finalizeType, groupData);
        event.setCanceled(true);
        entity.discard();
        level2.addFreshEntity((Entity)replacement);
    }
}

