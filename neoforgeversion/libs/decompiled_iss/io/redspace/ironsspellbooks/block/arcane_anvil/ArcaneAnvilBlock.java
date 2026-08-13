/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.item.FallingBlockEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.FallingBlock
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package io.redspace.ironsspellbooks.block.arcane_anvil;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilMenu;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArcaneAnvilBlock
extends FallingBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<ArcaneAnvilBlock> CODEC = ArcaneAnvilBlock.simpleCodec(properties -> new ArcaneAnvilBlock());
    private static final VoxelShape BASE = Block.box((double)2.0, (double)0.0, (double)2.0, (double)14.0, (double)4.0, (double)14.0);
    private static final VoxelShape X_LEG1 = Block.box((double)3.0, (double)4.0, (double)4.0, (double)13.0, (double)5.0, (double)12.0);
    private static final VoxelShape X_LEG2 = Block.box((double)4.0, (double)5.0, (double)6.0, (double)12.0, (double)10.0, (double)10.0);
    private static final VoxelShape X_TOP = Block.box((double)0.0, (double)10.0, (double)3.0, (double)16.0, (double)16.0, (double)13.0);
    private static final VoxelShape Z_LEG1 = Block.box((double)4.0, (double)4.0, (double)3.0, (double)12.0, (double)5.0, (double)13.0);
    private static final VoxelShape Z_LEG2 = Block.box((double)6.0, (double)5.0, (double)4.0, (double)10.0, (double)10.0, (double)12.0);
    private static final VoxelShape Z_TOP = Block.box((double)3.0, (double)10.0, (double)0.0, (double)13.0, (double)16.0, (double)16.0);
    private static final VoxelShape X_AXIS_AABB = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{X_LEG1, X_LEG2, X_TOP});
    private static final VoxelShape Z_AXIS_AABB = Shapes.or((VoxelShape)BASE, (VoxelShape[])new VoxelShape[]{Z_LEG1, Z_LEG2, Z_TOP});
    private static final Component CONTAINER_TITLE = Component.translatable((String)"ui.irons_spellbooks.arcane_anvil_title");

    public ArcaneAnvilBlock() {
        super(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.ENCHANTING_TABLE).noOcclusion().sound(SoundType.AMETHYST));
    }

    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    protected void falling(FallingBlockEntity pFallingEntity) {
        pFallingEntity.setHurtsEntities(2.0f, 40);
    }

    public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
        if (!pFallingBlock.isSilent()) {
            pLevel.playSound(null, pPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3f, Utils.random.nextFloat() * 0.1f + 0.9f);
        }
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = (Direction)pState.getValue((Property)FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getClockWise());
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState)pState.setValue((Property)FACING, (Comparable)pRotation.rotate((Direction)pState.getValue((Property)FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue((Property)FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
        pPlayer.awardStat(Stats.INTERACT_WITH_ANVIL);
        return InteractionResult.CONSUME;
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((i, inventory, player) -> new ArcaneAnvilMenu(i, inventory, ContainerLevelAccess.create((Level)pLevel, (BlockPos)pPos)), CONTAINER_TITLE);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}

