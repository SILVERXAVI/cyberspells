/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.EntityBlock
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.radio.tower;

import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlockEntity;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RadioTowerCoreBlock
extends HorizontalDirectionalBlock
implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FORMED = BooleanProperty.create((String)"formed");
    public static final MapCodec<RadioTowerCoreBlock> CODEC = RadioTowerCoreBlock.simpleCodec(RadioTowerCoreBlock::new);
    public static final Map<ResourceKey<Level>, Long> LAST_TOWER_ACTIVE_TIME = new ConcurrentHashMap<ResourceKey<Level>, Long>();
    private static final VoxelShape SHAPE_NORTH = Shapes.or((VoxelShape)Block.box((double)6.5, (double)0.0, (double)6.5, (double)9.5, (double)16.0, (double)9.5), (VoxelShape[])new VoxelShape[]{Block.box((double)3.5, (double)8.0, (double)7.2, (double)12.5, (double)14.0, (double)8.7), Block.box((double)3.1, (double)7.0, (double)8.7, (double)5.1, (double)15.0, (double)10.7), Block.box((double)11.1, (double)7.0, (double)8.7, (double)13.1, (double)15.0, (double)10.7), Block.box((double)3.1, (double)7.0, (double)5.2, (double)5.1, (double)15.0, (double)7.2), Block.box((double)11.1, (double)7.0, (double)5.2, (double)13.1, (double)15.0, (double)7.2)});
    private static final VoxelShape SHAPE_EAST = Shapes.or((VoxelShape)Block.box((double)6.5, (double)0.0, (double)6.5, (double)9.5, (double)16.0, (double)9.5), (VoxelShape[])new VoxelShape[]{Block.box((double)7.3, (double)8.0, (double)3.5, (double)8.8, (double)14.0, (double)12.5), Block.box((double)5.3, (double)7.0, (double)3.1, (double)7.3, (double)15.0, (double)5.1), Block.box((double)5.3, (double)7.0, (double)11.1, (double)7.3, (double)15.0, (double)13.1), Block.box((double)8.8, (double)7.0, (double)3.1, (double)10.8, (double)15.0, (double)5.1), Block.box((double)8.8, (double)7.0, (double)11.1, (double)10.8, (double)15.0, (double)13.1)});
    private static final VoxelShape SHAPE_SOUTH = Shapes.or((VoxelShape)Block.box((double)6.5, (double)0.0, (double)6.5, (double)9.5, (double)16.0, (double)9.5), (VoxelShape[])new VoxelShape[]{Block.box((double)3.5, (double)8.0, (double)7.3, (double)12.5, (double)14.0, (double)8.8), Block.box((double)10.9, (double)7.0, (double)5.3, (double)12.9, (double)15.0, (double)7.3), Block.box((double)2.9, (double)7.0, (double)5.3, (double)4.9, (double)15.0, (double)7.3), Block.box((double)10.9, (double)7.0, (double)8.8, (double)12.9, (double)15.0, (double)10.8), Block.box((double)2.9, (double)7.0, (double)8.8, (double)4.9, (double)15.0, (double)10.8)});
    private static final VoxelShape SHAPE_WEST = Shapes.or((VoxelShape)Block.box((double)6.5, (double)0.0, (double)6.5, (double)9.5, (double)16.0, (double)9.5), (VoxelShape[])new VoxelShape[]{Block.box((double)7.2, (double)8.0, (double)3.5, (double)8.7, (double)14.0, (double)12.5), Block.box((double)8.7, (double)7.0, (double)10.9, (double)10.7, (double)15.0, (double)12.9), Block.box((double)8.7, (double)7.0, (double)2.9, (double)10.7, (double)15.0, (double)4.9), Block.box((double)5.2, (double)7.0, (double)10.9, (double)7.2, (double)15.0, (double)12.9), Block.box((double)5.2, (double)7.0, (double)2.9, (double)7.2, (double)15.0, (double)4.9)});

    public RadioTowerCoreBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FORMED, (Comparable)Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch ((Direction)pState.getValue((Property)FACING)) {
            case Direction.EAST -> SHAPE_EAST;
            case Direction.SOUTH -> SHAPE_SOUTH;
            case Direction.WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState)pState.setValue((Property)FACING, (Comparable)pRotation.rotate((Direction)pState.getValue((Property)FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue((Property)FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FORMED});
        pBuilder.add(new Property[]{FACING});
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        BlockEntity be;
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if (!pLevel.isClientSide && (be = pLevel.getBlockEntity(pPos)) instanceof RadioTowerCoreBlockEntity) {
            RadioTowerCoreBlockEntity core = (RadioTowerCoreBlockEntity)be;
            core.tryToFormStructure();
        }
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity be;
        if (!pState.is(pNewState.getBlock()) && !pLevel.isClientSide && ((Boolean)pState.getValue((Property)FORMED)).booleanValue() && (be = pLevel.getBlockEntity(pPos)) instanceof RadioTowerCoreBlockEntity) {
            RadioTowerCoreBlockEntity core = (RadioTowerCoreBlockEntity)be;
            core.deformFencesOnly();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RadioTowerCoreBlockEntity(pPos, pState);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null;
        }
        if (pBlockEntityType == ModBlockEntities.RADIO_TOWER_CORE.get()) {
            return (lvl, pos, st, be) -> {
                if (((Boolean)st.getValue((Property)FORMED)).booleanValue() && lvl.getGameTime() % 20L == 0L) {
                    LAST_TOWER_ACTIVE_TIME.put((ResourceKey<Level>)lvl.dimension(), lvl.getGameTime());
                }
            };
        }
        return null;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (((Boolean)pState.getValue((Property)FORMED)).booleanValue()) {
            if (pRandom.nextInt(40) == 0) {
                pLevel.playSound(null, pPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.1f, 0.5f);
            }
            if (pRandom.nextInt(20) == 0) {
                pLevel.playSound(null, pPos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 0.1f, 1.0f);
            }
        }
    }
}

