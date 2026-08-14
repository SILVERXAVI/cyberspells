/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.EntityBlock
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.cwb;

import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlockEntity;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CyberwareWorkbenchBlock
extends HorizontalDirectionalBlock
implements EntityBlock {
    public static final MapCodec<CyberwareWorkbenchBlock> CODEC = CyberwareWorkbenchBlock.simpleCodec(CyberwareWorkbenchBlock::new);
    private static final VoxelShape BASE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape SHAPE_NORTH = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{Block.box((double)4.0, (double)16.0, (double)12.0, (double)12.0, (double)32.0, (double)16.0), Block.box((double)4.0, (double)24.0, (double)3.0, (double)12.0, (double)32.0, (double)12.0)});
    private static final VoxelShape SHAPE_SOUTH = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{Block.box((double)4.0, (double)16.0, (double)0.0, (double)12.0, (double)32.0, (double)4.0), Block.box((double)4.0, (double)24.0, (double)4.0, (double)12.0, (double)32.0, (double)13.0)});
    private static final VoxelShape SHAPE_WEST = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{Block.box((double)12.0, (double)16.0, (double)4.0, (double)16.0, (double)32.0, (double)12.0), Block.box((double)3.0, (double)24.0, (double)4.0, (double)12.0, (double)32.0, (double)12.0)});
    private static final VoxelShape SHAPE_EAST = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{Block.box((double)0.0, (double)16.0, (double)4.0, (double)4.0, (double)32.0, (double)12.0), Block.box((double)4.0, (double)24.0, (double)4.0, (double)13.0, (double)32.0, (double)12.0)});

    public CyberwareWorkbenchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.SOUTH));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FACING});
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = (Direction)state.getValue((Property)FACING);
        return switch (direction) {
            case Direction.SOUTH -> SHAPE_SOUTH;
            case Direction.WEST -> SHAPE_WEST;
            case Direction.EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getOpposite());
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CyberwareWorkbenchBlockEntity(pPos, pState);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof CyberwareWorkbenchBlockEntity) {
                CyberwareWorkbenchBlockEntity workbench = (CyberwareWorkbenchBlockEntity)entity;
                pPlayer.openMenu((MenuProvider)workbench, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess((boolean)pLevel.isClientSide());
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModBlockEntities.CYBERWARE_WORKBENCH.get() ? (lvl, pos, st, be) -> CyberwareWorkbenchBlockEntity.tick(lvl, pos, st, (CyberwareWorkbenchBlockEntity)be) : null;
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockEntity;
        if (!pState.is(pNewState.getBlock()) && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof CyberwareWorkbenchBlockEntity) {
            CyberwareWorkbenchBlockEntity workbench = (CyberwareWorkbenchBlockEntity)blockEntity;
            workbench.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}

