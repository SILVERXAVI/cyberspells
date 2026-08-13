/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Plane
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.FenceBlock
 *  net.minecraft.world.level.block.PipeBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.MapColor
 *  net.minecraft.world.level.material.PushReaction
 */
package io.redspace.ironsspellbooks.block;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class VoidstoneBlock
extends Block {
    public static final MapCodec<FenceBlock> CODEC = VoidstoneBlock.simpleCodec(FenceBlock::new);
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;

    public MapCodec<FenceBlock> codec() {
        return CODEC;
    }

    public VoidstoneBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0f, 3600000.8f).mapColor(MapColor.NONE).noLootTable().isValidSpawn(Blocks::never).pushReaction(PushReaction.BLOCK).sound(SoundType.COPPER).lightLevel(state -> 9));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, (Comparable)Boolean.valueOf(false))).setValue((Property)EAST, (Comparable)Boolean.valueOf(false))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(false))).setValue((Property)WEST, (Comparable)Boolean.valueOf(false)));
    }

    public boolean connectsTo(BlockState state, Direction direction) {
        return state.is(BlockRegistry.VOIDSTONE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = blockgetter.getBlockState(blockpos1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos3);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos4);
        return (BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(context).setValue((Property)NORTH, (Comparable)Boolean.valueOf(this.connectsTo(blockstate, Direction.SOUTH)))).setValue((Property)EAST, (Comparable)Boolean.valueOf(this.connectsTo(blockstate1, Direction.WEST)))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(this.connectsTo(blockstate2, Direction.NORTH)))).setValue((Property)WEST, (Comparable)Boolean.valueOf(this.connectsTo(blockstate3, Direction.EAST)));
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? (BlockState)state.setValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(facing), (Comparable)Boolean.valueOf(this.connectsTo(facingState, facing.getOpposite()))) : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{NORTH, EAST, WEST, SOUTH});
    }
}

