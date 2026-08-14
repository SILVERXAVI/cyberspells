/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.radio;

import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RadioKitBlock
extends HorizontalDirectionalBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final MapCodec<RadioKitBlock> CODEC = RadioKitBlock.simpleCodec(RadioKitBlock::new);
    public static final Map<ResourceKey<Level>, Long> LAST_ACTIVE_TIME = new ConcurrentHashMap<ResourceKey<Level>, Long>();
    private static final VoxelShape SHAPE_NORTH = Shapes.or((VoxelShape)Block.box((double)3.0, (double)0.0, (double)6.0, (double)15.0, (double)4.0, (double)14.0), (VoxelShape)Block.box((double)2.0, (double)0.0, (double)1.0, (double)4.0, (double)15.0, (double)3.0));
    private static final VoxelShape SHAPE_SOUTH = Shapes.or((VoxelShape)Block.box((double)1.0, (double)0.0, (double)2.0, (double)13.0, (double)4.0, (double)10.0), (VoxelShape)Block.box((double)12.0, (double)0.0, (double)13.0, (double)14.0, (double)15.0, (double)15.0));
    private static final VoxelShape SHAPE_WEST = Shapes.or((VoxelShape)Block.box((double)6.0, (double)0.0, (double)1.0, (double)14.0, (double)4.0, (double)13.0), (VoxelShape)Block.box((double)1.0, (double)0.0, (double)12.0, (double)3.0, (double)15.0, (double)14.0));
    private static final VoxelShape SHAPE_EAST = Shapes.or((VoxelShape)Block.box((double)2.0, (double)0.0, (double)3.0, (double)10.0, (double)4.0, (double)15.0), (VoxelShape)Block.box((double)13.0, (double)0.0, (double)2.0, (double)15.0, (double)15.0, (double)4.0));

    public RadioKitBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, (Comparable)Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
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

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = (Direction)pState.getValue((Property)FACING);
        switch (direction) {
            case SOUTH: {
                return SHAPE_SOUTH;
            }
            case EAST: {
                return SHAPE_EAST;
            }
            case WEST: {
                return SHAPE_WEST;
            }
        }
        return SHAPE_NORTH;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{POWERED});
        pBuilder.add(new Property[]{FACING});
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        boolean isPowered;
        if (!pLevel.isClientSide && (isPowered = pLevel.hasNeighborSignal(pPos))) {
            pLevel.scheduleTick(pPos, (Block)this, 20);
        }
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean wasPowered;
        boolean isPoweredNow;
        if (!pLevel.isClientSide && (isPoweredNow = pLevel.hasNeighborSignal(pPos)) != (wasPowered = ((Boolean)pState.getValue((Property)POWERED)).booleanValue())) {
            pLevel.setBlock(pPos, (BlockState)pState.setValue((Property)POWERED, (Comparable)Boolean.valueOf(isPoweredNow)), 3);
            if (isPoweredNow) {
                pLevel.scheduleTick(pPos, (Block)this, 20);
            }
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!((Boolean)pState.getValue((Property)POWERED)).booleanValue() || pLevel.isClientSide()) {
            return;
        }
        LAST_ACTIVE_TIME.put((ResourceKey<Level>)pLevel.dimension(), pLevel.getGameTime());
        pLevel.scheduleTick(pPos, (Block)this, 400);
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (((Boolean)pState.getValue((Property)POWERED)).booleanValue()) {
            if (pRandom.nextInt(5) == 0) {
                double centerX = (double)pPos.getX() + 0.5;
                double centerY = (double)pPos.getY() + 0.7;
                double centerZ = (double)pPos.getZ() + 0.5;
                pLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, centerX, centerY, centerZ, 0.0, 0.05, 0.0);
            }
            if (pRandom.nextInt(100) == 0) {
                pLevel.playSound(null, pPos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.1f, 1.5f);
            }
            if (pRandom.nextInt(200) == 0) {
                pLevel.playSound(null, pPos, SoundEvents.GUARDIAN_ATTACK, SoundSource.BLOCKS, 0.05f, 2.0f);
            }
        }
    }
}

