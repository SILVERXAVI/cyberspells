/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Vec3i
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.CustomSpawner
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LightLayer
 *  net.minecraft.world.level.NaturalSpawner
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.level.material.FluidState
 *  net.neoforged.neoforge.event.EventHooks
 */
package io.redspace.ironsspellbooks.worldgen;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.EventHooks;

public class IceSpiderPatrolSpawner
implements CustomSpawner {
    private static final int DELAY_FIXED = 9600;
    private static final int DELAY_VARIABLE = 3600;
    private int tickDelay;

    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!spawnEnemies || !((Boolean)ServerConfigs.ICE_SPIDER_PATROLS.get()).booleanValue()) {
            return 0;
        }
        int playercount = level.players().size();
        if (playercount < 1) {
            return 0;
        }
        RandomSource randomsource = level.random;
        --this.tickDelay;
        if (this.tickDelay > 0) {
            return 0;
        }
        this.tickDelay = 9600 / IceSpiderPatrolSpawner.getGroupedPlayerCount(level) + randomsource.nextInt(3600);
        if (!level.isRaining() || randomsource.nextBoolean()) {
            return 0;
        }
        Player player = null;
        for (int i = 0; i < playercount && ((player = (Player)level.players().get(randomsource.nextInt(playercount))).isSpectator() || player.isCreative()); ++i) {
            player = null;
        }
        if (player == null) {
            return 0;
        }
        if (IceSpiderPatrolSpawner.performIceSpiderHuntSpawn(level, player, 4)) {
            return 1;
        }
        return 0;
    }

    public static boolean performIceSpiderHuntSpawn(ServerLevel level, LivingEntity targetEntity, int maxAttempts) {
        for (int i = 0; i < maxAttempts; ++i) {
            Holder holder;
            Holder holder2;
            if (i > 0 && !(holder2 = level.getBiome(targetEntity.blockPosition())).is(ModTags.ICE_SPIDER_PATROLS)) {
                return false;
            }
            RandomSource randomsource = level.random;
            int k = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
            int l = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = targetEntity.blockPosition().mutable().move(k, 0, l);
            if (!level.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10) || !(holder = level.getBiome((BlockPos)blockpos$mutableblockpos)).is(ModTags.ICE_SPIDER_PATROLS)) break;
            blockpos$mutableblockpos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)blockpos$mutableblockpos).getY());
            if (!IceSpiderPatrolSpawner.createSpider(level, blockpos$mutableblockpos, targetEntity)) continue;
            return true;
        }
        return false;
    }

    private static int getGroupedPlayerCount(ServerLevel serverLevel) {
        ArrayList<BlockPos> groupPositions = new ArrayList<BlockPos>();
        int count = 0;
        int groupRange = 48;
        for (Player player : serverLevel.players()) {
            if (!groupPositions.stream().noneMatch(pos -> pos.distSqr((Vec3i)player.blockPosition()) < (double)(groupRange * groupRange))) continue;
            ++count;
            groupPositions.add(player.blockPosition());
        }
        return count;
    }

    private static boolean createSpider(ServerLevel level, BlockPos.MutableBlockPos pos, LivingEntity targetEntity) {
        BlockState blockstate = level.getBlockState((BlockPos)pos);
        if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)level, (BlockPos)pos, (BlockState)blockstate, (FluidState)blockstate.getFluidState(), (EntityType)((EntityType)EntityRegistry.ICE_SPIDER.get()))) {
            return false;
        }
        if (!IceSpiderPatrolSpawner.checkPatrollingMonsterSpawnRules((EntityType<? extends Mob>)((EntityType)EntityRegistry.ICE_SPIDER.get()), (LevelAccessor)level, MobSpawnType.PATROL, (BlockPos)pos, level.random)) {
            return false;
        }
        IceSpiderEntity iceSpider = new IceSpiderEntity((Level)level);
        iceSpider.moveTo(pos.immutable(), 0.0f, 0.0f);
        iceSpider.setTarget(targetEntity);
        level.playSound(null, iceSpider.blockPosition(), (SoundEvent)SoundRegistry.ICE_SPIDER_HOWL.get(), SoundSource.HOSTILE, 4.0f, 1.0f);
        iceSpider.setEmergeFromGround();
        if (!EventHooks.checkSpawnPosition((Mob)iceSpider, (ServerLevelAccessor)level, (MobSpawnType)MobSpawnType.PATROL)) {
            return false;
        }
        level.addFreshEntity((Entity)iceSpider);
        iceSpider.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt((BlockPos)pos), MobSpawnType.PATROL, null);
        return true;
    }

    public static boolean checkPatrollingMonsterSpawnRules(EntityType<? extends Mob> mob, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBrightness(LightLayer.BLOCK, pos) <= 8 && level.getDifficulty() != Difficulty.PEACEFUL && Monster.checkMobSpawnRules(mob, (LevelAccessor)level, (MobSpawnType)spawnType, (BlockPos)pos, (RandomSource)random);
    }
}

