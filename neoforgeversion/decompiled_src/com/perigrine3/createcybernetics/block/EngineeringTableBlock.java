/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.perigrine3.createcybernetics.block;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.block.entity.EngineeringTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EngineeringTableBlock
extends BaseEntityBlock {
    public static final MapCodec<EngineeringTableBlock> CODEC = EngineeringTableBlock.simpleCodec(EngineeringTableBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape BASE = EngineeringTableBlock.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape ARM_TALL = EngineeringTableBlock.box((double)4.0, (double)16.0, (double)0.0, (double)12.0, (double)32.0, (double)4.0);
    private static final VoxelShape TOP_TALL = EngineeringTableBlock.box((double)4.0, (double)24.0, (double)4.0, (double)12.0, (double)32.0, (double)12.0);
    private static final VoxelShape SHAPE_NORTH = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{ARM_TALL, TOP_TALL});
    private static final VoxelShape SHAPE_EAST = EngineeringTableBlock.rotateYClockwise(SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = EngineeringTableBlock.rotateYClockwise(SHAPE_EAST);
    private static final VoxelShape SHAPE_WEST = EngineeringTableBlock.rotateYClockwise(SHAPE_SOUTH);

    public EngineeringTableBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    private static VoxelShape shapeForFacing(Direction facing) {
        return switch (facing) {
            case Direction.NORTH -> SHAPE_SOUTH;
            case Direction.EAST -> SHAPE_WEST;
            case Direction.SOUTH -> SHAPE_NORTH;
            case Direction.WEST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    private static VoxelShape rotateYClockwise(VoxelShape shape) {
        VoxelShape[] out = new VoxelShape[]{Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            out[0] = Shapes.or((VoxelShape)out[0], (VoxelShape)Shapes.box((double)(1.0 - maxZ), (double)minY, (double)minX, (double)(1.0 - minZ), (double)maxY, (double)maxX));
        });
        return out[0];
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EngineeringTableBlock.shapeForFacing((Direction)state.getValue((Property)FACING));
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return EngineeringTableBlock.shapeForFacing((Direction)state.getValue((Property)FACING));
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    private static boolean upperSpaceIsClear(Level level, BlockPos pos) {
        return level.getBlockState(pos.above()).canBeReplaced();
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, level, pos, block, fromPos, moving);
        if (level.isClientSide) {
            return;
        }
        if (!fromPos.equals((Object)pos.above())) {
            return;
        }
        if (!EngineeringTableBlock.upperSpaceIsClear(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EngineeringTableBlockEntity(pos, state);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos;
        Level level = ctx.getLevel();
        if (!level.getBlockState((pos = ctx.getClickedPos()).above()).canBeReplaced(ctx)) {
            return null;
        }
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)ctx.getHorizontalDirection().getOpposite());
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation((Direction)state.getValue((Property)FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity blockEntity;
        if (state.getBlock() != newState.getBlock() && (blockEntity = level.getBlockEntity(pos)) instanceof EngineeringTableBlockEntity) {
            EngineeringTableBlockEntity blockEntity2 = (EngineeringTableBlockEntity)blockEntity;
            blockEntity2.drops();
            level.updateNeighbourForOutputSignal(pos, (Block)this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EngineeringTableBlockEntity) {
                EngineeringTableBlockEntity be = (EngineeringTableBlockEntity)blockEntity;
                sp.openMenu((MenuProvider)be, buf -> buf.writeBlockPos(pos));
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }
}

