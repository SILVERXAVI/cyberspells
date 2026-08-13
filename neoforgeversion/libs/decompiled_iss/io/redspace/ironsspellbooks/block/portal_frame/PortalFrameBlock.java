/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.PushReaction
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.portal_frame;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PortalFrameBlock
extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape SOUTH_AABB = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)3.0);
    protected static final VoxelShape NORTH_AABB = Block.box((double)0.0, (double)0.0, (double)13.0, (double)16.0, (double)16.0, (double)16.0);
    protected static final VoxelShape WEST_AABB = Block.box((double)13.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    protected static final VoxelShape EAST_AABB = Block.box((double)0.0, (double)0.0, (double)0.0, (double)3.0, (double)16.0, (double)16.0);
    protected static final VoxelShape LOWER_SOUTH_COLLIDER_AABB = Shapes.join((VoxelShape)SOUTH_AABB, (VoxelShape)Block.box((double)1.0, (double)0.0, (double)0.0, (double)15.0, (double)16.0, (double)3.0), (BooleanOp)BooleanOp.ONLY_FIRST);
    protected static final VoxelShape LOWER_NORTH_COLLIDER_AABB = Shapes.join((VoxelShape)NORTH_AABB, (VoxelShape)Block.box((double)1.0, (double)0.0, (double)13.0, (double)15.0, (double)16.0, (double)16.0), (BooleanOp)BooleanOp.ONLY_FIRST);
    protected static final VoxelShape LOWER_WEST_COLLIDER_AABB = Shapes.join((VoxelShape)WEST_AABB, (VoxelShape)Block.box((double)13.0, (double)0.0, (double)1.0, (double)16.0, (double)16.0, (double)15.0), (BooleanOp)BooleanOp.ONLY_FIRST);
    protected static final VoxelShape LOWER_EAST_COLLIDER_AABB = Shapes.join((VoxelShape)EAST_AABB, (VoxelShape)Block.box((double)0.0, (double)0.0, (double)1.0, (double)3.0, (double)16.0, (double)15.0), (BooleanOp)BooleanOp.ONLY_FIRST);
    protected static final VoxelShape UPPER_SOUTH_COLLIDER_AABB = Shapes.join((VoxelShape)LOWER_SOUTH_COLLIDER_AABB, (VoxelShape)Block.box((double)0.0, (double)15.0, (double)0.0, (double)16.0, (double)16.0, (double)3.0), (BooleanOp)BooleanOp.OR);
    protected static final VoxelShape UPPER_NORTH_COLLIDER_AABB = Shapes.join((VoxelShape)LOWER_NORTH_COLLIDER_AABB, (VoxelShape)Block.box((double)0.0, (double)15.0, (double)13.0, (double)16.0, (double)16.0, (double)16.0), (BooleanOp)BooleanOp.OR);
    protected static final VoxelShape UPPER_WEST_COLLIDER_AABB = Shapes.join((VoxelShape)LOWER_WEST_COLLIDER_AABB, (VoxelShape)Block.box((double)13.0, (double)15.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0), (BooleanOp)BooleanOp.OR);
    protected static final VoxelShape UPPER_EAST_COLLIDER_AABB = Shapes.join((VoxelShape)LOWER_EAST_COLLIDER_AABB, (VoxelShape)Block.box((double)0.0, (double)15.0, (double)0.0, (double)3.0, (double)16.0, (double)16.0), (BooleanOp)BooleanOp.OR);
    public static final MapCodec<PortalFrameBlock> CODEC = PortalFrameBlock.simpleCodec(t -> new PortalFrameBlock());

    public PortalFrameBlock() {
        this(BlockBehaviour.Properties.of().noOcclusion().isSuffocating((x, y, z) -> false).sound(SoundType.COPPER_GRATE).isViewBlocking((x, y, z) -> false).strength(10.0f, 6.0f));
    }

    public PortalFrameBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return PortalFrameBlock.createTicker(pLevel, pBlockEntityType, (BlockEntityType<? extends PortalFrameBlockEntity>)((BlockEntityType)BlockRegistry.PORTAL_FRAME_BLOCK_ENTITY.get()));
    }

    @javax.annotation.Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends PortalFrameBlockEntity> pClientType) {
        return pLevel.isClientSide ? null : PortalFrameBlock.createTickerHelper(pServerType, pClientType, PortalFrameBlockEntity::serverTick);
    }

    public BlockState updateShape(BlockState myState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos myPos, BlockPos pFacingPos) {
        DoubleBlockHalf half = (DoubleBlockHalf)myState.getValue(HALF);
        BlockPos requiredNeighborPos = myPos.relative(half.getDirectionToOther());
        BlockState neighborState = pLevel.getBlockState(requiredNeighborPos);
        if (!neighborState.is((Block)this)) {
            BlockState air = Blocks.AIR.defaultBlockState();
            pLevel.setBlock(myPos, air, 35);
            pLevel.levelEvent(null, 2001, myPos, Block.getId((BlockState)air));
            return air;
        }
        return super.updateShape(myState, pFacing, pFacingState, pLevel, myPos, pFacingPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction horizontalDir = context.getHorizontalDirection();
        Direction facing = horizontalDir.getOpposite();
        BlockPos blockPos = context.getClickedPos();
        boolean bottom = context.getClickedFace() != Direction.DOWN;
        BlockPos blockPos2 = bottom ? blockPos.above() : blockPos.below();
        Level level = context.getLevel();
        if (level.getBlockState(blockPos2).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockPos2)) {
            return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue(HALF, (Comparable)(bottom ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER));
        }
        return null;
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @javax.annotation.Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            BlockEntity blockEntity;
            DoubleBlockHalf half = (DoubleBlockHalf)pState.getValue(HALF);
            Direction facing = (Direction)pState.getValue((Property)FACING);
            BlockPos blockpos = pPos.relative(half.getDirectionToOther());
            pLevel.setBlock(blockpos, (BlockState)((BlockState)pState.setValue(HALF, (Comparable)half.getOtherHalf())).setValue((Property)FACING, (Comparable)facing), 3);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes((LevelAccessor)pLevel, pPos, 3);
            if (pPlacer != null && (blockEntity = pLevel.getBlockEntity(pPos)) instanceof PortalFrameBlockEntity) {
                PortalFrameBlockEntity portalFrameBlockEntity = (PortalFrameBlockEntity)blockEntity;
                portalFrameBlockEntity.setOwnerUUID(pPlacer.getUUID());
            }
        }
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = (Direction)pState.getValue((Property)FACING);
        return switch (direction) {
            case Direction.NORTH -> NORTH_AABB;
            case Direction.SOUTH -> SOUTH_AABB;
            case Direction.WEST -> WEST_AABB;
            default -> EAST_AABB;
        };
    }

    protected VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = (Direction)pState.getValue((Property)FACING);
        boolean lower = ((DoubleBlockHalf)pState.getValue(HALF)).equals((Object)DoubleBlockHalf.LOWER);
        return switch (direction) {
            case Direction.NORTH -> {
                if (lower) {
                    yield LOWER_NORTH_COLLIDER_AABB;
                }
                yield UPPER_NORTH_COLLIDER_AABB;
            }
            case Direction.SOUTH -> {
                if (lower) {
                    yield LOWER_SOUTH_COLLIDER_AABB;
                }
                yield UPPER_SOUTH_COLLIDER_AABB;
            }
            case Direction.WEST -> {
                if (lower) {
                    yield LOWER_WEST_COLLIDER_AABB;
                }
                yield UPPER_WEST_COLLIDER_AABB;
            }
            default -> lower ? LOWER_EAST_COLLIDER_AABB : UPPER_EAST_COLLIDER_AABB;
        };
    }

    protected void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pEntity.level.isClientSide) {
            VoxelShape voxelshape = pState.getShape((BlockGetter)pLevel, pPos, CollisionContext.of((Entity)pEntity));
            VoxelShape voxelshape1 = voxelshape.move((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ());
            if (pEntity.getBoundingBox().intersects(voxelshape1.bounds())) {
                pLevel.getBlockEntity(pPos, (BlockEntityType)BlockRegistry.PORTAL_FRAME_BLOCK_ENTITY.get()).ifPresent(tile -> tile.setActive());
            }
        }
    }

    public boolean canTeleport(Entity entity) {
        return true;
    }

    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = pStack.getItem();
        if (item instanceof DyeItem) {
            DyeItem dyeItem = (DyeItem)item;
            Optional portal = pLevel.getBlockEntity(pos, (BlockEntityType)BlockRegistry.PORTAL_FRAME_BLOCK_ENTITY.get());
            if (portal.isPresent()) {
                PortalFrameBlockEntity tile = (PortalFrameBlockEntity)((Object)portal.get());
                int color = dyeItem.getDyeColor().getTextureDiffuseColor();
                if (tile.isPortalConnected() && tile.getColor() != color) {
                    if (!((Boolean)ServerConfigs.PORTAL_FRAME_RESTRICT_DYE.get()).booleanValue() || tile.getOwnerUUID() == null || player.getUUID().equals(tile.getOwnerUUID())) {
                        if (!player.getAbilities().instabuild) {
                            pStack.shrink(1);
                        }
                        tile.setColor(color);
                        return ItemInteractionResult.SUCCESS;
                    }
                    if (player instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)player;
                        serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.portal_dye_failure").withStyle(ChatFormatting.RED)));
                    }
                }
            }
        }
        return super.useItemOn(pStack, state, pLevel, pos, player, hand, hit);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PortalFrameBlockEntity(pPos, pState);
    }

    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof PortalFrameBlockEntity) {
            PortalFrameBlockEntity portalFrame = (PortalFrameBlockEntity)blockEntity;
            portalFrame.breakPortalConnection();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        if (((DoubleBlockHalf)blockState.getValue(HALF)).equals((Object)DoubleBlockHalf.LOWER)) {
            return RenderShape.MODEL;
        }
        return RenderShape.INVISIBLE;
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState)pState.setValue((Property)FACING, (Comparable)pRotation.rotate((Direction)pState.getValue((Property)FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue((Property)FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, HALF});
    }
}

