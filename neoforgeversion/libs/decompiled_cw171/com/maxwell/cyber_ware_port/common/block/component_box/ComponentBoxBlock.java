/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.EntityBlock
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.component_box;

import com.maxwell.cyber_ware_port.common.block.component_box.ComponentBoxBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentBoxBlock
extends HorizontalDirectionalBlock
implements EntityBlock {
    public static final MapCodec<ComponentBoxBlock> CODEC = ComponentBoxBlock.simpleCodec(ComponentBoxBlock::new);
    private static final VoxelShape SHAPE_NS = Block.box((double)1.0, (double)0.0, (double)4.0, (double)15.0, (double)11.0, (double)12.0);
    private static final VoxelShape SHAPE_EW = Block.box((double)4.0, (double)0.0, (double)1.0, (double)12.0, (double)11.0, (double)15.0);

    public ComponentBoxBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
    }

    @NotNull
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @NotNull
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return ((Direction)state.getValue((Property)FACING)).getAxis() == Direction.Axis.X ? SHAPE_EW : SHAPE_NS;
    }

    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ComponentBoxBlockEntity(pos, state);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)pContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FACING});
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHit) {
        BlockEntity blockEntity;
        if (!pLevel.isClientSide && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof ComponentBoxBlockEntity) {
            ComponentBoxBlockEntity boxEntity = (ComponentBoxBlockEntity)blockEntity;
            pPlayer.openMenu((MenuProvider)boxEntity, pPos);
        }
        return InteractionResult.sidedSuccess((boolean)pLevel.isClientSide);
    }

    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ComponentBoxBlockEntity) {
            CustomData customData;
            ComponentBoxBlockEntity box = (ComponentBoxBlockEntity)blockEntity;
            if (pStack.has(DataComponents.CUSTOM_NAME)) {
                box.setCustomName(pStack.getHoverName());
            }
            if ((customData = (CustomData)pStack.get(DataComponents.CUSTOM_DATA)) != null && customData.contains("Inventory")) {
                box.itemHandler.deserializeNBT((HolderLookup.Provider)pLevel.registryAccess(), customData.copyTag().getCompound("Inventory"));
                box.setChanged();
            }
        }
    }

    @NotNull
    public BlockState playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        BlockEntity blockEntity;
        if (!pLevel.isClientSide && !pPlayer.isCreative() && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof ComponentBoxBlockEntity) {
            ComponentBoxBlockEntity box = (ComponentBoxBlockEntity)blockEntity;
            ItemStack itemStack = new ItemStack((ItemLike)this);
            CompoundTag inventoryTag = box.itemHandler.serializeNBT((HolderLookup.Provider)pLevel.registryAccess());
            itemStack.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, data -> data.update(tag -> tag.put("Inventory", (Tag)inventoryTag)));
            if (box.hasCustomName()) {
                itemStack.set(DataComponents.CUSTOM_NAME, (Object)box.getDisplayName());
            }
            ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, itemStack);
            pLevel.addFreshEntity((Entity)itementity);
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }
}

