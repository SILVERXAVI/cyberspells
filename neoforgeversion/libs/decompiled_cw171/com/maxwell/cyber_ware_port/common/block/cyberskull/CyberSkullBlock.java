/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SkullBlock
 *  net.minecraft.world.level.block.SkullBlock$Type
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.SkullBlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.pattern.BlockInWorld
 *  net.minecraft.world.level.block.state.pattern.BlockPattern
 *  net.minecraft.world.level.block.state.pattern.BlockPattern$BlockPatternMatch
 *  net.minecraft.world.level.block.state.pattern.BlockPatternBuilder
 */
package com.maxwell.cyber_ware_port.common.block.cyberskull;

import com.maxwell.cyber_ware_port.common.block.cyberskull.CyberSkullBlockEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModEntities;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

public class CyberSkullBlock
extends SkullBlock {
    @Nullable
    private static BlockPattern witherPatternFull;
    @Nullable
    private static BlockPattern witherPatternBase;

    public CyberSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
        super(type, properties);
    }

    public static void checkSpawn(Level level, BlockPos pos, SkullBlockEntity skull) {
        if (!level.isClientSide) {
            BlockPattern pattern;
            BlockPattern.BlockPatternMatch match;
            boolean isBase;
            BlockState blockstate = skull.getBlockState();
            boolean bl = isBase = blockstate.is((Block)ModBlocks.CYBER_WITHER_SKELETON_SKULL.get()) || blockstate.is((Block)ModBlocks.CYBER_WITHER_SKELETON_WALL_SKULL.get());
            if (isBase && pos.getY() >= level.getMinBuildHeight() && level.getDifficulty() != Difficulty.PEACEFUL && (match = (pattern = CyberSkullBlock.getOrCreateWitherFull()).find((LevelReader)level, pos)) != null) {
                for (int i = 0; i < pattern.getWidth(); ++i) {
                    for (int j = 0; j < pattern.getHeight(); ++j) {
                        BlockInWorld blockinworld = match.getBlock(i, j, 0);
                        level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                        level.levelEvent(2001, blockinworld.getPos(), CyberSkullBlock.getId((BlockState)blockinworld.getState()));
                    }
                }
                CyberWitherBoss boss = (CyberWitherBoss)((EntityType)ModEntities.CYBER_WITHER.get()).create(level);
                if (boss != null) {
                    BlockPos blockpos = match.getBlock(1, 2, 0).getPos();
                    boss.moveTo((double)blockpos.getX() + 0.5, (double)blockpos.getY() + 0.55, (double)blockpos.getZ() + 0.5, match.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f, 0.0f);
                    boss.yBodyRot = match.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f;
                    for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, boss.getBoundingBox().inflate(50.0))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, (Entity)boss);
                    }
                    level.addFreshEntity((Entity)boss);
                    for (int k = 0; k < pattern.getWidth(); ++k) {
                        for (int l = 0; l < pattern.getHeight(); ++l) {
                            level.blockUpdated(match.getBlock(k, l, 0).getPos(), Blocks.AIR);
                        }
                    }
                }
            }
        }
    }

    private static BlockPattern getOrCreateWitherFull() {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle(new String[]{"^^^", "###", "~#~"}).where('#', block -> block.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', block -> block.getState().is((Block)ModBlocks.CYBER_WITHER_SKELETON_SKULL.get()) || block.getState().is((Block)ModBlocks.CYBER_WITHER_SKELETON_WALL_SKULL.get())).where('~', block -> block.getState().isAir()).build();
        }
        return witherPatternFull;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof SkullBlockEntity) {
            CyberSkullBlock.checkSpawn(level, pos, (SkullBlockEntity)blockentity);
        }
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CyberSkullBlockEntity(pos, state);
    }
}

