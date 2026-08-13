/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.RotatedPillarBlock
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.neoforge.common.ItemAbilities
 *  net.neoforged.neoforge.common.ItemAbility
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class BrazierBlock
extends Block
implements SimpleWaterloggedBlock {
    public static final BooleanProperty HANGING = BooleanProperty.create((String)"hanging");
    private final boolean soul;
    public static final VoxelShape COLLISION_SHAPE = Block.box((double)1.0, (double)0.0, (double)1.0, (double)15.0, (double)10.0, (double)15.0);
    public static final VoxelShape RENDER_SHAPE = Block.box((double)1.0, (double)0.0, (double)1.0, (double)15.0, (double)16.0, (double)15.0);

    public BrazierBlock(boolean soul) {
        super(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.CHAIN).lightLevel(blockState -> (Boolean)blockState.getValue((Property)BlockStateProperties.LIT) != false ? 15 : 0));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(false))).setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf(true))).setValue((Property)HANGING, (Comparable)Boolean.valueOf(false)));
        this.soul = soul;
    }

    public BrazierBlock() {
        this(false);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return RENDER_SHAPE;
    }

    protected VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE;
    }

    protected void entityInside(BlockState pState, Level pLevel, BlockPos blockpos, Entity entity) {
        if (((Boolean)pState.getValue((Property)BlockStateProperties.LIT)).booleanValue() && entity instanceof LivingEntity) {
            float margin = 0.0625f;
            AABB bb = entity.getBoundingBox();
            if (bb.maxX > (double)((float)blockpos.getX() + margin) && bb.maxZ > (double)((float)blockpos.getZ() + margin) && bb.minX < (double)((float)(blockpos.getX() + 1) - margin) && bb.minZ < (double)((float)(blockpos.getZ() + 1) - margin)) {
                entity.hurt(pLevel.damageSources().campfire(), 1.0f);
            }
        }
        super.entityInside(pState, pLevel, blockpos, entity);
    }

    protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        BlockState newState = super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        if (((Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
            pLevel.scheduleTick(pPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)pLevel));
        }
        if (pDirection == Direction.UP) {
            boolean chain;
            boolean bl = chain = pNeighborState.is(Blocks.CHAIN) && ((Direction.Axis)pNeighborState.getValue((Property)RotatedPillarBlock.AXIS)).isVertical();
            if (chain != (Boolean)pState.getValue((Property)HANGING)) {
                newState = (BlockState)newState.setValue((Property)HANGING, (Comparable)Boolean.valueOf(chain));
            }
        }
        return newState;
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (ItemAbilities.SHOVEL_DOUSE == itemAbility) {
            if (state.getBlock() instanceof BrazierBlock && ((Boolean)state.getValue((Property)BlockStateProperties.LIT)).booleanValue()) {
                if (!simulate) {
                    context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return (BlockState)state.setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf(false));
            }
        } else if (ItemAbilities.FIRESTARTER_LIGHT == itemAbility && state.getBlock() instanceof BrazierBlock && !((Boolean)state.getValue((Property)BlockStateProperties.LIT)).booleanValue() && !((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
            return (BlockState)state.setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf(true));
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!((Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && pFluidState.getType() == Fluids.WATER) {
            boolean flag = (Boolean)pState.getValue((Property)BlockStateProperties.LIT);
            if (flag && !pLevel.isClientSide()) {
                pLevel.playSound(null, pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            pLevel.setBlock(pPos, (BlockState)((BlockState)pState.setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(true))).setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf(false)), 3);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay((LevelReader)pLevel));
            return true;
        }
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        boolean water = pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER;
        boolean hanging = pContext.getLevel().getBlockState(pContext.getClickedPos().above()).is(Blocks.CHAIN);
        return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, (Comparable)Boolean.valueOf(water))).setValue((Property)BlockStateProperties.LIT, (Comparable)Boolean.valueOf(!water))).setValue((Property)HANGING, (Comparable)Boolean.valueOf(hanging));
    }

    protected FluidState getFluidState(BlockState pState) {
        return (Boolean)pState.getValue((Property)BlockStateProperties.WATERLOGGED) != false ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{BlockStateProperties.WATERLOGGED, BlockStateProperties.LIT, HANGING});
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (((Boolean)pState.getValue((Property)BlockStateProperties.LIT)).booleanValue() && (!this.soul || pRandom.nextFloat() < 0.085f)) {
            float scale = 0.25f;
            double d0 = (double)pPos.getX() + 0.5;
            double d1 = (double)pPos.getY() + 0.7 + (double)scale;
            double d2 = (double)pPos.getZ() + 0.5;
            double d3 = Utils.getRandomScaled(scale);
            double d4 = Utils.getRandomScaled(scale);
            double d6 = Utils.getRandomScaled(scale);
            double d7 = pRandom.nextDouble() * 0.17;
            pLevel.addParticle((ParticleOptions)(this.soul ? ParticleTypes.SOUL : ParticleHelper.EMBERS), d0 + d3, d1 + d4, d2 + d6, 0.0, d7, 0.0);
        }
    }
}

