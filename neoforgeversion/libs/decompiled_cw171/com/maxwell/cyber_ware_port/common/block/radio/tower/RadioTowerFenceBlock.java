/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.FenceBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.maxwell.cyber_ware_port.common.block.radio.tower;

import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadioTowerFenceBlock
extends FenceBlock {
    public static final BooleanProperty FORMED = BooleanProperty.create((String)"formed");
    private static final int MAX_SEARCH_HEIGHT = 10;

    public RadioTowerFenceBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, (Comparable)Boolean.valueOf(false))).setValue((Property)EAST, (Comparable)Boolean.valueOf(false))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(false))).setValue((Property)WEST, (Comparable)Boolean.valueOf(false))).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false))).setValue((Property)FORMED, (Comparable)Boolean.valueOf(false)));
    }

    public boolean connectsTo(BlockState pState, boolean pIsSideSolid, Direction pDirection) {
        return pState.is((Block)this) || pState.getBlock() instanceof RadioTowerCoreBlock || super.connectsTo(pState, pIsSideSolid, pDirection);
    }

    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getShape(pState, pLevel, pPos, pContext);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(new Property[]{FORMED});
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        RadioTowerCoreBlockEntity be;
        if (!level.isClientSide() && (be = this.findTower(level, pos)) != null) {
            be.tryToFormStructure();
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        RadioTowerCoreBlockEntity be;
        if (!pLevel.isClientSide() && ((Boolean)pState.getValue((Property)FORMED)).booleanValue() && !pState.is(pNewState.getBlock()) && (be = this.findTower(pLevel, pPos)) != null) {
            be.deformStructure();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    private RadioTowerCoreBlockEntity findTower(Level level, BlockPos origin) {
        BlockPos.MutableBlockPos searchPos = new BlockPos.MutableBlockPos();
        for (int y = 1; y <= 10; ++y) {
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    searchPos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockEntity be = level.getBlockEntity((BlockPos)searchPos);
                    if (!(be instanceof RadioTowerCoreBlockEntity)) continue;
                    RadioTowerCoreBlockEntity core = (RadioTowerCoreBlockEntity)be;
                    return core;
                }
            }
        }
        return null;
    }
}

