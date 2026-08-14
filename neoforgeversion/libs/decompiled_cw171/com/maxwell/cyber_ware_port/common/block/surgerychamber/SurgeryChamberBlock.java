/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.EntityBlock
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.surgerychamber;

import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlockEntity;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SurgeryChamberBlock
extends HorizontalDirectionalBlock
implements EntityBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final MapCodec<SurgeryChamberBlock> CODEC = SurgeryChamberBlock.simpleCodec(SurgeryChamberBlock::new);
    private static final Map<Direction, VoxelShape> LOWER_SHAPES_OPEN = new EnumMap<Direction, VoxelShape>(Direction.class);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES_OPEN = new EnumMap<Direction, VoxelShape>(Direction.class);
    private static final Map<Direction, VoxelShape> LOWER_SHAPES_CLOSED = new EnumMap<Direction, VoxelShape>(Direction.class);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES_CLOSED = new EnumMap<Direction, VoxelShape>(Direction.class);

    public SurgeryChamberBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue(HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)OPEN, (Comparable)Boolean.valueOf(true)));
    }

    private static VoxelShape rotateShape(VoxelShape shape, Direction toDir) {
        if (toDir == Direction.NORTH) {
            return shape;
        }
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = switch (toDir) {
            case Direction.EAST -> 1;
            case Direction.SOUTH -> 2;
            case Direction.WEST -> 3;
            default -> 0;
        };
        for (int i = 0; i < times; ++i) {
            VoxelShape current = buffer[0];
            VoxelShape rotated = Shapes.empty();
            current.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buffer[1] = Shapes.or((VoxelShape)buffer[1], (VoxelShape)Shapes.box((double)(1.0 - maxZ), (double)minY, (double)minX, (double)(1.0 - minZ), (double)maxY, (double)maxX));
            });
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos targetPos = pState.getValue(HALF) == DoubleBlockHalf.UPPER ? pPos.below() : pPos;
        BlockEntity blockEntity = pLevel.getBlockEntity(targetPos);
        if (blockEntity instanceof SurgeryChamberBlockEntity) {
            SurgeryChamberBlockEntity chamberEntity = (SurgeryChamberBlockEntity)blockEntity;
            chamberEntity.toggleDoor();
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockPos blockpos;
        BlockState blockstate;
        DoubleBlockHalf half;
        if (!pLevel.isClientSide && pPlayer.isCreative() && (half = (DoubleBlockHalf)pState.getValue(HALF)) == DoubleBlockHalf.UPPER && (blockstate = pLevel.getBlockState(blockpos = pPos.below())).is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
            pLevel.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
            pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId((BlockState)blockstate));
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return pState;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        DoubleBlockHalf half = (DoubleBlockHalf)pState.getValue(HALF);
        if (pFacing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
            return pFacingState.is((Block)this) ? pState : Blocks.AIR.defaultBlockState();
        }
        if (half == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN && !pState.canSurvive((LevelReader)pLevel, pCurrentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = (Direction)pState.getValue((Property)FACING);
        DoubleBlockHalf half = (DoubleBlockHalf)pState.getValue(HALF);
        return ((Boolean)pState.getValue((Property)OPEN)).booleanValue() ? (half == DoubleBlockHalf.LOWER ? LOWER_SHAPES_OPEN.get(facing) : UPPER_SHAPES_OPEN.get(facing)) : (half == DoubleBlockHalf.LOWER ? LOWER_SHAPES_CLOSED.get(facing) : UPPER_SHAPES_CLOSED.get(facing));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FACING, HALF, OPEN});
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? new SurgeryChamberBlockEntity(pPos, pState) : null;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(pContext)) {
            return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getOpposite());
        }
        return null;
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), (BlockState)pState.setValue(HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModBlockEntities.SURGERY_CHAMBER.get() ? (lvl, pos, st, be) -> SurgeryChamberBlockEntity.tick(lvl, pos, st, (SurgeryChamberBlockEntity)be) : null;
    }

    static {
        VoxelShape floor = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)1.0, (double)16.0);
        VoxelShape ceiling = Block.box((double)0.0, (double)15.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
        VoxelShape backWall = Block.box((double)0.0, (double)0.0, (double)14.0, (double)16.0, (double)16.0, (double)16.0);
        VoxelShape rightWall = Block.box((double)14.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)14.0);
        VoxelShape leftWall = Block.box((double)0.0, (double)0.0, (double)0.0, (double)2.0, (double)16.0, (double)14.0);
        VoxelShape frontWall = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)2.0);
        VoxelShape baseLowerOpen = Shapes.or((VoxelShape)floor, (VoxelShape[])new VoxelShape[]{backWall, rightWall, leftWall});
        VoxelShape baseUpperOpen = Shapes.or((VoxelShape)ceiling, (VoxelShape[])new VoxelShape[]{backWall, rightWall, leftWall});
        VoxelShape baseLowerClosed = Shapes.or((VoxelShape)baseLowerOpen, (VoxelShape)frontWall);
        VoxelShape baseUpperClosed = Shapes.or((VoxelShape)baseUpperOpen, (VoxelShape)frontWall);
        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isHorizontal()) continue;
            LOWER_SHAPES_OPEN.put(direction, SurgeryChamberBlock.rotateShape(baseLowerOpen, direction));
            UPPER_SHAPES_OPEN.put(direction, SurgeryChamberBlock.rotateShape(baseUpperOpen, direction));
            LOWER_SHAPES_CLOSED.put(direction, SurgeryChamberBlock.rotateShape(baseLowerClosed, direction));
            UPPER_SHAPES_CLOSED.put(direction, SurgeryChamberBlock.rotateShape(baseUpperClosed, direction));
        }
    }
}

