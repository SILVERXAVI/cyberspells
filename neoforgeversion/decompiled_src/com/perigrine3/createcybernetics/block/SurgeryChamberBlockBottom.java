/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.EntityCollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.block;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.SurgeryChamberBlockTop;
import com.perigrine3.createcybernetics.block.entity.RobosurgeonBlockEntity;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.surgery.SurgeryController;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SurgeryChamberBlockBottom
extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPENED = BooleanProperty.create((String)"opened");
    public static final BooleanProperty SLAVE = BooleanProperty.create((String)"slave");
    public static final BooleanProperty SURGERY_DONE = BooleanProperty.create((String)"surgery_done");
    public static final MapCodec<SurgeryChamberBlockBottom> CODEC = SurgeryChamberBlockBottom.simpleCodec(SurgeryChamberBlockBottom::new);
    private static final VoxelShape BACKWALL = Block.box((double)14.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape WESTWALL = Block.box((double)0.0, (double)0.0, (double)14.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape EASTWALL = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)2.0);
    private static final VoxelShape BOTTOMWALL = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)2.0, (double)16.0);
    private static final VoxelShape DOOR_CLOSED = Block.box((double)1.0, (double)2.0, (double)2.0, (double)2.0, (double)16.0, (double)14.0);
    private static final VoxelShape SHAPE_OPEN = Shapes.or((VoxelShape)BACKWALL, (VoxelShape[])new VoxelShape[]{WESTWALL, EASTWALL, BOTTOMWALL});
    private static final VoxelShape SHAPE_CLOSED = Shapes.or((VoxelShape)BACKWALL, (VoxelShape[])new VoxelShape[]{WESTWALL, EASTWALL, BOTTOMWALL, DOOR_CLOSED});

    public SurgeryChamberBlockBottom(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPENED, (Comparable)Boolean.valueOf(false))).setValue((Property)SLAVE, (Comparable)Boolean.valueOf(false))).setValue((Property)SURGERY_DONE, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, OPENED, SLAVE, SURGERY_DONE});
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = (Boolean)state.getValue((Property)OPENED) != false ? SHAPE_OPEN : SHAPE_CLOSED;
        return SurgeryChamberBlockBottom.rotateShapeFromNorth((Direction)state.getValue((Property)FACING), baseShape);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Player player;
        PlayerCyberwareData data;
        EntityCollisionContext ecc;
        Entity entity;
        VoxelShape baseShape = (Boolean)state.getValue((Property)OPENED) != false ? SHAPE_OPEN : SHAPE_CLOSED;
        VoxelShape normalCollision = SurgeryChamberBlockBottom.rotateShapeFromNorth((Direction)state.getValue((Property)FACING), baseShape);
        if (context instanceof EntityCollisionContext && (entity = (ecc = (EntityCollisionContext)context).getEntity()) instanceof Player && (data = (PlayerCyberwareData)(player = (Player)entity).getData(ModAttachments.CYBERWARE)).hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE) && player.isCrouching()) {
            return Shapes.empty();
        }
        return normalCollision;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        CollisionContext cc;
        BlockPos bottomPos;
        BlockPos topPos;
        Level level = context.getLevel();
        if (!level.getBlockState(topPos = (bottomPos = context.getClickedPos()).above()).canBeReplaced(context)) {
            return null;
        }
        Direction facing = context.getHorizontalDirection();
        BlockState bottomState = (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue((Property)OPENED, (Comparable)Boolean.valueOf(false))).setValue((Property)SLAVE, (Comparable)Boolean.valueOf(false))).setValue((Property)SURGERY_DONE, (Comparable)Boolean.valueOf(false));
        BlockState topState = (BlockState)((BlockState)((BlockState)((BlockState)((Block)ModBlocks.SURGERY_CHAMBER_TOP.get()).defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue((Property)SurgeryChamberBlockTop.OPENED, (Comparable)Boolean.valueOf(false))).setValue((Property)SurgeryChamberBlockTop.SLAVE, (Comparable)Boolean.valueOf(true))).setValue((Property)SurgeryChamberBlockTop.CONNECTED, (Comparable)Boolean.valueOf(false));
        Player placer = context.getPlayer();
        CollisionContext collisionContext = cc = placer != null ? CollisionContext.of((Entity)placer) : CollisionContext.empty();
        if (!level.isUnobstructed(bottomState, bottomPos, cc)) {
            return null;
        }
        if (!level.isUnobstructed(topState, topPos, cc)) {
            return null;
        }
        return bottomState;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide) {
            return;
        }
        BlockPos topPos = pos.above();
        if (!level.getBlockState(topPos).canBeReplaced()) {
            level.destroyBlock(pos, false);
            return;
        }
        Direction facing = (Direction)state.getValue((Property)FACING);
        BlockState topState = (BlockState)((BlockState)((BlockState)((BlockState)((Block)ModBlocks.SURGERY_CHAMBER_TOP.get()).defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue((Property)SurgeryChamberBlockTop.OPENED, (Comparable)((Boolean)state.getValue((Property)OPENED)))).setValue((Property)SurgeryChamberBlockTop.SLAVE, (Comparable)Boolean.valueOf(true))).setValue((Property)SurgeryChamberBlockTop.CONNECTED, (Comparable)Boolean.valueOf(false));
        level.setBlock(topPos, topState, 3);
    }

    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (dir == Direction.UP && !neighborState.is((Block)ModBlocks.SURGERY_CHAMBER_TOP.get())) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean newState = (Boolean)state.getValue((Property)OPENED) == false;
            level.setBlock(pos, (BlockState)state.setValue((Property)OPENED, (Comparable)Boolean.valueOf(newState)), 3);
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (topState.is((Block)ModBlocks.SURGERY_CHAMBER_TOP.get())) {
                level.setBlock(topPos, (BlockState)topState.setValue((Property)SurgeryChamberBlockTop.OPENED, (Comparable)Boolean.valueOf(newState)), 3);
            }
            level.playSound(null, pos, newState ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (SurgeryChamberBlockBottom.brokenByCreativePlayer(builder)) {
            return List.of();
        }
        if (((Boolean)state.getValue((Property)SLAVE)).booleanValue()) {
            return List.of();
        }
        return List.of(new ItemStack((ItemLike)ModBlocks.SURGERY_CHAMBER_BOTTOM.get()));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean brokenByCreativePlayer(LootParams.Builder builder) {
        Entity e = (Entity)builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (!(e instanceof Player)) return false;
        Player p = (Player)e;
        if (!p.getAbilities().instabuild) return false;
        return true;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (topState.is((Block)ModBlocks.SURGERY_CHAMBER_TOP.get())) {
                level.destroyBlock(topPos, false);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        boolean closed;
        if (level.isClientSide) {
            return;
        }
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        BlockPos topPos = pos.above();
        BlockState topState = level.getBlockState(topPos);
        if (!topState.is((Block)ModBlocks.SURGERY_CHAMBER_TOP.get())) {
            return;
        }
        boolean connected = (Boolean)topState.getValue((Property)SurgeryChamberBlockTop.CONNECTED);
        boolean bl = closed = (Boolean)topState.getValue((Property)SurgeryChamberBlockTop.OPENED) == false;
        if (!connected || !closed || ((Boolean)state.getValue((Property)SURGERY_DONE)).booleanValue()) {
            return;
        }
        BlockPos surgeonPos = topPos.above();
        if (!level.getBlockState(surgeonPos).is((Block)ModBlocks.ROBOSURGEON.get())) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(surgeonPos);
        if (!(blockEntity instanceof RobosurgeonBlockEntity)) {
            return;
        }
        RobosurgeonBlockEntity surgeon = (RobosurgeonBlockEntity)blockEntity;
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        SurgeryController.performSurgery(player, surgeon);
        level.setBlock(pos, (BlockState)state.setValue((Property)SURGERY_DONE, (Comparable)Boolean.valueOf(true)), 3);
    }

    private static VoxelShape rotateShapeFromNorth(Direction facing, VoxelShape shapeNorth) {
        return switch (facing) {
            case Direction.NORTH -> SurgeryChamberBlockBottom.rotateYCounterClockwise(shapeNorth);
            case Direction.EAST -> shapeNorth;
            case Direction.SOUTH -> SurgeryChamberBlockBottom.rotateYClockwise(shapeNorth);
            case Direction.WEST -> SurgeryChamberBlockBottom.rotateYClockwise(SurgeryChamberBlockBottom.rotateYClockwise(shapeNorth));
            default -> shapeNorth;
        };
    }

    private static VoxelShape rotateYClockwise(VoxelShape shape) {
        VoxelShape[] out = new VoxelShape[]{Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            out[0] = Shapes.or((VoxelShape)out[0], (VoxelShape)Shapes.box((double)(1.0 - maxZ), (double)minY, (double)minX, (double)(1.0 - minZ), (double)maxY, (double)maxX));
        });
        return out[0];
    }

    private static VoxelShape rotateYCounterClockwise(VoxelShape shape) {
        VoxelShape[] out = new VoxelShape[]{Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            out[0] = Shapes.or((VoxelShape)out[0], (VoxelShape)Shapes.box((double)minZ, (double)minY, (double)(1.0 - maxX), (double)maxZ, (double)maxY, (double)(1.0 - minX)));
        });
        return out[0];
    }
}

