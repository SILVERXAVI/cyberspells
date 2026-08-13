/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.pedestal;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock
extends BaseEntityBlock
implements SimpleWaterloggedBlock {
    public static final VoxelShape SHAPE_COLUMN = Block.box((double)3.0, (double)4.0, (double)3.0, (double)13.0, (double)12.0, (double)13.0);
    public static final VoxelShape SHAPE_BOTTOM = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)4.0, (double)16.0);
    public static final VoxelShape SHAPE_TOP = Block.box((double)0.0, (double)12.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    public static final VoxelShape SHAPE = Shapes.or((VoxelShape)SHAPE_BOTTOM, (VoxelShape[])new VoxelShape[]{SHAPE_TOP, SHAPE_COLUMN});
    public static final MapCodec<PedestalBlock> CODEC = PedestalBlock.simpleCodec(t -> new PedestalBlock());

    public PedestalBlock() {
        super(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.LODESTONE).noOcclusion());
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity entity;
        if (!pLevel.isClientSide() && (entity = pLevel.getBlockEntity(pos)) instanceof PedestalTile) {
            PedestalTile pedestalTile = (PedestalTile)entity;
            ItemStack currentPedestalItem = pedestalTile.getHeldItem();
            ItemStack handItem = player.getItemInHand(hand);
            ItemStack playerItem = currentPedestalItem.copy();
            if (handItem.isEmpty() || handItem.getCount() == 1) {
                player.setItemInHand(hand, playerItem);
            } else {
                this.dropItem(playerItem, player);
            }
            pedestalTile.setHeldItem(ItemStack.EMPTY);
            currentPedestalItem = handItem.copy();
            if (!currentPedestalItem.isEmpty()) {
                currentPedestalItem.setCount(1);
                pedestalTile.setHeldItem(currentPedestalItem);
                handItem.shrink(1);
            }
            pLevel.sendBlockUpdated(pos, state, state, 2);
        }
        return ItemInteractionResult.sidedSuccess((boolean)pLevel.isClientSide());
    }

    private void dropItem(ItemStack itemstack, Player owner) {
        ServerPlayer serverplayer;
        ItemEntity itementity;
        if (owner instanceof ServerPlayer && (itementity = (serverplayer = (ServerPlayer)owner).drop(itemstack, false)) != null) {
            itementity.setNoPickUpDelay();
            itementity.setThrower((Entity)owner);
        }
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockEntity;
        if (pState.getBlock() != pNewState.getBlock() && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof PedestalTile) {
            ((PedestalTile)blockEntity).drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (((Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
            pLevel.scheduleTick(pPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)pLevel));
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER));
    }

    protected FluidState getFluidState(BlockState pState) {
        return (Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED) != false ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{BlockStateProperties.WATERLOGGED});
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalTile(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}

