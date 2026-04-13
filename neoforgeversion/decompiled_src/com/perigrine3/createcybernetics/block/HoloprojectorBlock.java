/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.block;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.block.entity.HoloprojectorBlockEntity;
import com.perigrine3.createcybernetics.item.generic.HoloProjectionChipItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HoloprojectorBlock
extends BaseEntityBlock {
    public static final MapCodec<HoloprojectorBlock> CODEC = HoloprojectorBlock.simpleCodec(HoloprojectorBlock::new);
    private static final VoxelShape SHAPE = Block.box((double)4.0, (double)0.0, (double)4.0, (double)12.0, (double)1.5, (double)12.0);

    public HoloprojectorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof HoloprojectorBlockEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        HoloprojectorBlockEntity holo = (HoloprojectorBlockEntity)be;
        boolean shift = player.isShiftKeyDown();
        if (shift) {
            holo.clearProjection();
            return ItemInteractionResult.CONSUME;
        }
        if (stack.isEmpty()) {
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                holo.setProjectedPlayerFrom(sp);
                return ItemInteractionResult.CONSUME;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (HoloProjectionChipItem.hasEntityData(stack)) {
            CompoundTag chip = HoloProjectionChipItem.getChipTag(stack);
            String typeId = chip.getString("entity_type");
            String name = chip.getString("entity_name");
            CompoundTag entityNbt = chip.contains("entity_nbt", 10) ? chip.getCompound("entity_nbt") : new CompoundTag();
            holo.setProjectedEntity(typeId, entityNbt, name);
            return ItemInteractionResult.CONSUME;
        }
        ItemStack one = stack.copy();
        one.setCount(1);
        holo.setProjectedItem(one);
        return ItemInteractionResult.CONSUME;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HoloprojectorBlockEntity(pos, state);
    }

    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}

