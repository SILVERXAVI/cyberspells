/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.ChestType
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.material.PushReaction
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package io.redspace.ironsspellbooks.block.inscription_table;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InscriptionTableBlock
extends HorizontalDirectionalBlock
implements SimpleWaterloggedBlock {
    public static final EnumProperty<ChestType> PART = BlockStateProperties.CHEST_TYPE;
    public static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)18.0, (double)16.0);
    public static final VoxelShape SHAPE_TABLETOP = Block.box((double)0.0, (double)10.0, (double)0.0, (double)16.0, (double)14.0, (double)16.0);
    public static final VoxelShape SHAPE_LEG_1 = Block.box((double)1.0, (double)0.0, (double)1.0, (double)4.0, (double)10.0, (double)4.0);
    public static final VoxelShape SHAPE_LEG_2 = Block.box((double)12.0, (double)0.0, (double)1.0, (double)15.0, (double)10.0, (double)4.0);
    public static final VoxelShape SHAPE_LEG_3 = Block.box((double)1.0, (double)0.0, (double)12.0, (double)4.0, (double)10.0, (double)15.0);
    public static final VoxelShape SHAPE_LEG_4 = Block.box((double)12.0, (double)0.0, (double)12.0, (double)15.0, (double)10.0, (double)15.0);
    public static final VoxelShape SHAPE_LEGS_EAST = Shapes.or((VoxelShape)SHAPE_LEG_2, (VoxelShape[])new VoxelShape[]{SHAPE_LEG_4, SHAPE_TABLETOP});
    public static final VoxelShape SHAPE_LEGS_WEST = Shapes.or((VoxelShape)SHAPE_LEG_1, (VoxelShape[])new VoxelShape[]{SHAPE_LEG_3, SHAPE_TABLETOP});
    public static final VoxelShape SHAPE_LEGS_NORTH = Shapes.or((VoxelShape)SHAPE_LEG_3, (VoxelShape[])new VoxelShape[]{SHAPE_LEG_4, SHAPE_TABLETOP});
    public static final VoxelShape SHAPE_LEGS_SOUTH = Shapes.or((VoxelShape)SHAPE_LEG_1, (VoxelShape[])new VoxelShape[]{SHAPE_LEG_2, SHAPE_TABLETOP});
    public static final MapCodec<InscriptionTableBlock> CODEC = InscriptionTableBlock.simpleCodec(t -> new InscriptionTableBlock());

    public InscriptionTableBlock() {
        super(BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.WOOD).noOcclusion());
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    public BlockState updateShape(BlockState myState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos myPos, BlockPos pFacingPos) {
        BlockState neighborState;
        ChestType half = (ChestType)myState.getValue(PART);
        BlockPos requiredNeighborPos = myPos.relative(InscriptionTableBlock.getNeighbourDirection(half, (Direction)myState.getValue((Property)FACING)));
        boolean waterlogged = (Boolean)myState.getValue((Property)BlockStateProperties.WATERLOGGED);
        if (waterlogged) {
            pLevel.scheduleTick(myPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)pLevel));
            pLevel.scheduleTick(requiredNeighborPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)pLevel));
        }
        if (!(neighborState = pLevel.getBlockState(requiredNeighborPos)).is((Block)this)) {
            BlockState air = (waterlogged ? Blocks.WATER : Blocks.AIR).defaultBlockState();
            pLevel.setBlock(myPos, air, 35);
            pLevel.levelEvent(null, 2001, myPos, Block.getId((BlockState)air));
            return air;
        }
        return super.updateShape(myState, pFacing, pFacingState, pLevel, myPos, pFacingPos);
    }

    private static Direction getNeighbourDirection(ChestType pPart, Direction pDirection) {
        return pPart == ChestType.LEFT ? pDirection.getCounterClockWise() : pDirection.getClockWise();
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = ((ChestType)pState.getValue(PART)).equals((Object)ChestType.RIGHT) ? (Direction)pState.getValue((Property)FACING) : ((Direction)pState.getValue((Property)FACING)).getOpposite();
        return switch (direction) {
            case Direction.NORTH -> SHAPE_LEGS_WEST;
            case Direction.SOUTH -> SHAPE_LEGS_EAST;
            case Direction.WEST -> SHAPE_LEGS_NORTH;
            default -> SHAPE_LEGS_SOUTH;
        };
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction.getCounterClockWise());
        Level level = pContext.getLevel();
        if (level.getBlockState(blockpos1).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(blockpos1)) {
            return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)direction.getOpposite())).setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(level.getFluidState(blockpos).getType() == Fluids.WATER));
        }
        return null;
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pPos.relative(((Direction)pState.getValue((Property)FACING)).getClockWise());
            pLevel.setBlock(blockpos, (BlockState)((BlockState)pState.setValue(PART, (Comparable)ChestType.LEFT)).setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(pLevel.getFluidState(blockpos).getType() == Fluids.WATER)), 3);
            pLevel.setBlock(pPos, (BlockState)pState.setValue(PART, (Comparable)ChestType.RIGHT), 3);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes((LevelAccessor)pLevel, pPos, 3);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, PART, BlockStateProperties.WATERLOGGED});
    }

    public RenderShape getRenderShape(BlockState blockState) {
        if (((ChestType)blockState.getValue(PART)).equals((Object)ChestType.RIGHT)) {
            return RenderShape.MODEL;
        }
        return RenderShape.INVISIBLE;
    }

    protected FluidState getFluidState(BlockState pState) {
        return (Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED) != false ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
        return InteractionResult.CONSUME;
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((i, inventory, player) -> new InscriptionTableMenu(i, inventory, ContainerLevelAccess.create((Level)pLevel, (BlockPos)pPos)), (Component)Component.translatable((String)"block.irons_spellbooks.inscription_table"));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}

