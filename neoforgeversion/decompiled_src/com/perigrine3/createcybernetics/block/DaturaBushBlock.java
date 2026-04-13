/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.SweetBerryBushBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.gameevent.GameEvent$Context
 *  net.minecraft.world.phys.BlockHitResult
 */
package com.perigrine3.createcybernetics.block;

import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class DaturaBushBlock
extends SweetBerryBushBlock {
    public DaturaBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike)ModItems.DATURA_FLOWER.get());
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        boolean flag;
        int i = (Integer)state.getValue((Property)AGE);
        boolean bl = flag = i == 3;
        if (i > 1) {
            int j = 1 + level.random.nextInt(2);
            DaturaBushBlock.popResource((Level)level, (BlockPos)pos, (ItemStack)new ItemStack((ItemLike)ModItems.DATURA_FLOWER.get(), j + (flag ? 1 : 0)));
            level.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
            BlockState blockstate = (BlockState)state.setValue((Property)AGE, (Comparable)Integer.valueOf(1));
            level.setBlock(pos, blockstate, 2);
            level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, (BlockState)blockstate));
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}

