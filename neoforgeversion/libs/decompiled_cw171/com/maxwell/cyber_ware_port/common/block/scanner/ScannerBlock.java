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
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.scanner;

import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlockEntity;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.jetbrains.annotations.Nullable;

public class ScannerBlock
extends HorizontalDirectionalBlock
implements EntityBlock {
    public static final MapCodec<ScannerBlock> CODEC = ScannerBlock.simpleCodec(ScannerBlock::new);

    public ScannerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FACING});
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getOpposite());
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        BlockEntity entity;
        if (!pLevel.isClientSide() && (entity = pLevel.getBlockEntity(pPos)) instanceof ScannerBlockEntity) {
            ScannerBlockEntity scanner = (ScannerBlockEntity)entity;
            pPlayer.openMenu((MenuProvider)scanner, pPos);
        }
        return InteractionResult.sidedSuccess((boolean)pLevel.isClientSide());
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockEntity;
        if (!pState.is(pNewState.getBlock()) && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof ScannerBlockEntity) {
            ScannerBlockEntity scanner = (ScannerBlockEntity)blockEntity;
            scanner.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ScannerBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null;
        }
        return pBlockEntityType == ModBlockEntities.SCANNER.get() ? (lvl, pos, st, be) -> ScannerBlockEntity.tick(lvl, pos, st, (ScannerBlockEntity)be) : null;
    }
}

