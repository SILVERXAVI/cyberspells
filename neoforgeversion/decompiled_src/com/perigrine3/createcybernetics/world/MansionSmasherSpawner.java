/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  net.minecraft.world.level.levelgen.structure.BuiltinStructures
 *  net.minecraft.world.level.levelgen.structure.Structure
 *  net.minecraft.world.level.levelgen.structure.StructureStart
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.world;

import com.perigrine3.createcybernetics.CreateCybernetics;
import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import com.perigrine3.createcybernetics.world.MansionBossSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class MansionSmasherSpawner {
    private static final float CHANCE_PER_MANSION = 0.25f;
    private static final int CHECK_EVERY_TICKS = 100;

    private MansionSmasherSpawner() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        RandomSource r;
        BoundingBox box;
        BlockPos spawnPos;
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        Level level = player2.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        if (player2.isSpectator()) {
            return;
        }
        if (player2.tickCount % 100 != 0) {
            return;
        }
        Structure mansion = (Structure)level2.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolderOrThrow(BuiltinStructures.WOODLAND_MANSION).value();
        BlockPos playerPos = player2.blockPosition();
        StructureStart start = level2.structureManager().getStructureAt(playerPos, mansion);
        if (start == null || !start.isValid()) {
            return;
        }
        long key = start.getChunkPos().toLong();
        MansionBossSavedData data = MansionBossSavedData.get(level2);
        if (data.isSpawned(key) || data.isRolledFail(key)) {
            return;
        }
        if (!data.isPending(key)) {
            RandomSource r2 = level2.getRandom();
            if (r2.nextFloat() >= 0.25f) {
                data.markRolledFail(key);
                return;
            }
            data.markPending(key);
        }
        if ((spawnPos = MansionSmasherSpawner.findInteriorLikeSpot(level2, box = start.getBoundingBox(), playerPos, r = level2.getRandom())) == null) {
            return;
        }
        if (!level2.isPositionEntityTicking(spawnPos)) {
            return;
        }
        SmasherEntity smasher = (SmasherEntity)ModEntities.SMASHER.get().create((Level)level2);
        if (smasher == null) {
            return;
        }
        smasher.moveTo((double)spawnPos.getX() + 0.5, spawnPos.getY(), (double)spawnPos.getZ() + 0.5, r.nextFloat() * 360.0f, 0.0f);
        smasher.finalizeSpawn((ServerLevelAccessor)level2, level2.getCurrentDifficultyAt(spawnPos), MobSpawnType.STRUCTURE, null);
        level2.addFreshEntity((Entity)smasher);
        data.markSpawned(key);
        CreateCybernetics.LOGGER.info("Spawned Smasher in Woodland Mansion at {} (key={})", (Object)spawnPos, (Object)key);
    }

    private static BlockPos findInteriorLikeSpot(ServerLevel level, BoundingBox box, BlockPos around, RandomSource r) {
        for (int i = 0; i < 180; ++i) {
            int dx = r.nextInt(33) - 16;
            int dz = r.nextInt(33) - 16;
            int dy = r.nextInt(9) - 4;
            BlockPos p = around.offset(dx, dy, dz);
            if (!box.isInside((Vec3i)p) || !MansionSmasherSpawner.isTwoTallAir(level, p) || !MansionSmasherSpawner.hasSolidFloor(level, p) || !MansionSmasherSpawner.hasCeilingSoon(level, p) || !MansionSmasherSpawner.isSomewhatEnclosed(level, p)) continue;
            return p;
        }
        int minX = box.minX();
        int maxX = box.maxX();
        int minY = box.minY();
        int maxY = box.maxY();
        int minZ = box.minZ();
        int maxZ = box.maxZ();
        for (int i = 0; i < 260; ++i) {
            BlockPos q;
            int x = r.nextInt(maxX - minX + 1) + minX;
            int z = r.nextInt(maxZ - minZ + 1) + minZ;
            int y = r.nextInt(Math.max(1, maxY - minY + 1)) + minY;
            BlockPos p = new BlockPos(x, y, z);
            if (!box.isInside((Vec3i)p)) continue;
            for (int d = 0; d <= 12 && box.isInside((Vec3i)(q = p.below(d))); ++d) {
                if (!MansionSmasherSpawner.isTwoTallAir(level, q) || !MansionSmasherSpawner.hasSolidFloor(level, q) || !MansionSmasherSpawner.hasCeilingSoon(level, q) || !MansionSmasherSpawner.isSomewhatEnclosed(level, q)) continue;
                return q;
            }
        }
        return null;
    }

    private static boolean isTwoTallAir(ServerLevel level, BlockPos p) {
        return MansionSmasherSpawner.isNonColliding(level.getBlockState(p), level, p) && MansionSmasherSpawner.isNonColliding(level.getBlockState(p.above()), level, p.above());
    }

    private static boolean hasSolidFloor(ServerLevel level, BlockPos p) {
        BlockPos below = p.below();
        return !MansionSmasherSpawner.isNonColliding(level.getBlockState(below), level, below);
    }

    private static boolean hasCeilingSoon(ServerLevel level, BlockPos p) {
        BlockPos head = p.above();
        for (int i = 1; i <= 4; ++i) {
            BlockPos c = head.above(i);
            if (MansionSmasherSpawner.isNonColliding(level.getBlockState(c), level, c)) continue;
            return true;
        }
        return false;
    }

    private static boolean isSomewhatEnclosed(ServerLevel level, BlockPos p) {
        int solid = 0;
        if (!MansionSmasherSpawner.isNonColliding(level.getBlockState(p.north()), level, p.north())) {
            ++solid;
        }
        if (!MansionSmasherSpawner.isNonColliding(level.getBlockState(p.south()), level, p.south())) {
            ++solid;
        }
        if (!MansionSmasherSpawner.isNonColliding(level.getBlockState(p.west()), level, p.west())) {
            ++solid;
        }
        if (!MansionSmasherSpawner.isNonColliding(level.getBlockState(p.east()), level, p.east())) {
            ++solid;
        }
        return solid >= 2;
    }

    private static boolean isNonColliding(BlockState state, ServerLevel level, BlockPos pos) {
        return state.getCollisionShape((BlockGetter)level, pos).isEmpty();
    }
}

