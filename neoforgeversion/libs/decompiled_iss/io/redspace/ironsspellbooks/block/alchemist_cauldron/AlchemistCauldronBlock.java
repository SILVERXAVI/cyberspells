/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler$FluidAction
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlchemistCauldronBlock
extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.or((VoxelShape)Shapes.or((VoxelShape)AlchemistCauldronBlock.box((double)0.0, (double)0.0, (double)4.0, (double)16.0, (double)2.0, (double)6.0), (VoxelShape[])new VoxelShape[]{AlchemistCauldronBlock.box((double)0.0, (double)0.0, (double)10.0, (double)16.0, (double)2.0, (double)12.0), AlchemistCauldronBlock.box((double)4.0, (double)0.0, (double)0.0, (double)6.0, (double)2.0, (double)16.0), AlchemistCauldronBlock.box((double)10.0, (double)0.0, (double)0.0, (double)12.0, (double)2.0, (double)16.0)}), (VoxelShape)Shapes.join((VoxelShape)Shapes.or((VoxelShape)Shapes.join((VoxelShape)AlchemistCauldronBlock.box((double)0.0, (double)2.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0), (VoxelShape)AlchemistCauldronBlock.box((double)0.0, (double)12.0, (double)0.0, (double)16.0, (double)14.0, (double)16.0), (BooleanOp)BooleanOp.ONLY_FIRST), (VoxelShape)AlchemistCauldronBlock.box((double)1.0, (double)12.0, (double)1.0, (double)15.0, (double)14.0, (double)15.0)), (VoxelShape)AlchemistCauldronBlock.box((double)2.0, (double)4.0, (double)2.0, (double)14.0, (double)16.0, (double)14.0), (BooleanOp)BooleanOp.ONLY_FIRST));
    public static final MapCodec<AlchemistCauldronBlock> CODEC = AlchemistCauldronBlock.simpleCodec(t -> new AlchemistCauldronBlock());

    public AlchemistCauldronBlock() {
        super(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.CAULDRON).lightLevel(blockState -> 3));
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return AlchemistCauldronBlock.createTicker(pLevel, pBlockEntityType, (BlockEntityType<? extends AlchemistCauldronTile>)((BlockEntityType)BlockRegistry.ALCHEMIST_CAULDRON_TILE.get()));
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends AlchemistCauldronTile> pClientType) {
        return pLevel.isClientSide ? null : AlchemistCauldronBlock.createTickerHelper(pServerType, pClientType, AlchemistCauldronTile::serverTick);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AlchemistCauldronTile) {
            AlchemistCauldronTile tile = (AlchemistCauldronTile)blockEntity;
            return tile.handleUse(level.getBlockState(pos), level, pos, player, hand);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, blockHitResult);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        BlockEntity blockEntity;
        if (entity.tickCount % 20 == 0 && (blockEntity = level.getBlockEntity(pos)) instanceof AlchemistCauldronTile) {
            LivingEntity livingEntity;
            AlchemistCauldronTile cauldronTile = (AlchemistCauldronTile)blockEntity;
            if (entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).hurt(DamageSources.get(level, ISSDamageTypes.CAULDRON), 2.0f)) {
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, entity.getX(), entity.getY() + (double)(entity.getBbHeight() / 2.0f), entity.getZ(), 20, 0.05, 0.05, 0.05, 0.1, false);
                cauldronTile.fluidInventory.fill(new FluidStack(FluidRegistry.BLOOD, 250), IFluidHandler.FluidAction.EXECUTE);
            }
        }
        super.entityInside(blockState, level, pos, entity);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemistCauldronTile(pos, state);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity blockEntity;
        if (pState.getBlock() != pNewState.getBlock() && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof AlchemistCauldronTile) {
            AlchemistCauldronTile cauldronTile = (AlchemistCauldronTile)blockEntity;
            cauldronTile.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}

