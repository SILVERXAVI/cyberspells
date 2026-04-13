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
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.EntityCollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.perigrine3.createcybernetics.block;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.SurgeryChamberBlockBottom;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SurgeryChamberBlockTop
extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPENED = BooleanProperty.create((String)"opened");
    public static final BooleanProperty SLAVE = BooleanProperty.create((String)"slave");
    public static final BooleanProperty CONNECTED = BooleanProperty.create((String)"connected");
    public static final MapCodec<SurgeryChamberBlockTop> CODEC = SurgeryChamberBlockTop.simpleCodec(SurgeryChamberBlockTop::new);
    private static final VoxelShape BACKWALL = Block.box((double)14.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape WESTWALL = Block.box((double)0.0, (double)0.0, (double)14.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape EASTWALL = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)16.0, (double)2.0);
    private static final VoxelShape TOPWALL = Block.box((double)0.0, (double)14.0, (double)16.0, (double)16.0, (double)16.0, (double)16.0);
    private static final VoxelShape DOOR_CLOSED = Block.box((double)1.0, (double)0.0, (double)2.0, (double)2.0, (double)16.0, (double)14.0);
    private static final VoxelShape SHAPE_OPEN = Shapes.or((VoxelShape)BACKWALL, (VoxelShape[])new VoxelShape[]{WESTWALL, EASTWALL, TOPWALL});
    private static final VoxelShape SHAPE_CLOSED = Shapes.or((VoxelShape)BACKWALL, (VoxelShape[])new VoxelShape[]{WESTWALL, EASTWALL, TOPWALL, DOOR_CLOSED});

    private static VoxelShape rotateShapeFromNorth(Direction facing, VoxelShape shapeNorth) {
        return switch (facing) {
            case Direction.NORTH -> SurgeryChamberBlockTop.rotateYCounterClockwise(shapeNorth);
            case Direction.EAST -> shapeNorth;
            case Direction.SOUTH -> SurgeryChamberBlockTop.rotateYClockwise(shapeNorth);
            case Direction.WEST -> SurgeryChamberBlockTop.rotateYClockwise(SurgeryChamberBlockTop.rotateYClockwise(shapeNorth));
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

    public SurgeryChamberBlockTop(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPENED, (Comparable)Boolean.valueOf(false))).setValue((Property)SLAVE, (Comparable)Boolean.valueOf(true))).setValue((Property)CONNECTED, (Comparable)Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = (Boolean)state.getValue((Property)OPENED) != false ? SHAPE_OPEN : SHAPE_CLOSED;
        return SurgeryChamberBlockTop.rotateShapeFromNorth((Direction)state.getValue((Property)FACING), baseShape);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Player player;
        PlayerCyberwareData data;
        EntityCollisionContext ecc;
        Entity entity;
        VoxelShape baseShape = (Boolean)state.getValue((Property)OPENED) != false ? SHAPE_OPEN : SHAPE_CLOSED;
        VoxelShape normalCollision = SurgeryChamberBlockTop.rotateShapeFromNorth((Direction)state.getValue((Property)FACING), baseShape);
        if (context instanceof EntityCollisionContext && (entity = (ecc = (EntityCollisionContext)context).getEntity()) instanceof Player && (data = (PlayerCyberwareData)(player = (Player)entity).getData(ModAttachments.CYBERWARE)).hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE) && player.isCrouching()) {
            return Shapes.empty();
        }
        return normalCollision;
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, OPENED, SLAVE, CONNECTED});
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean newState = (Boolean)state.getValue((Property)OPENED) == false;
            level.setBlock(pos, (BlockState)state.setValue((Property)OPENED, (Comparable)Boolean.valueOf(newState)), 3);
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is((Block)ModBlocks.SURGERY_CHAMBER_BOTTOM.get())) {
                BlockState newBottomState = (BlockState)((BlockState)bottomState.setValue((Property)SurgeryChamberBlockBottom.OPENED, (Comparable)Boolean.valueOf(newState))).setValue((Property)SurgeryChamberBlockBottom.SURGERY_DONE, (Comparable)Boolean.valueOf(!newState && (Boolean)bottomState.getValue((Property)SurgeryChamberBlockBottom.SURGERY_DONE) != false));
                if (newState) {
                    newBottomState = (BlockState)newBottomState.setValue((Property)SurgeryChamberBlockBottom.SURGERY_DONE, (Comparable)Boolean.valueOf(false));
                }
                level.setBlock(bottomPos, newBottomState, 3);
            }
            level.playSound(null, pos, newState ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of();
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            boolean drop = !player.getAbilities().instabuild;
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is((Block)ModBlocks.SURGERY_CHAMBER_BOTTOM.get())) {
                level.destroyBlock(bottomPos, drop, (Entity)player);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is((Block)ModBlocks.SURGERY_CHAMBER_BOTTOM.get())) {
                level.destroyBlock(bottomPos, false);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public static boolean hasRoboSurgeonAbove(Level level, BlockPos pos) {
        return level.getBlockState(pos.above()).is((Block)ModBlocks.ROBOSURGEON.get());
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        boolean connected;
        if (!level.isClientSide && (connected = SurgeryChamberBlockTop.hasRoboSurgeonAbove(level, pos)) != (Boolean)state.getValue((Property)CONNECTED)) {
            level.setBlock(pos, (BlockState)state.setValue((Property)CONNECTED, (Comparable)Boolean.valueOf(connected)), 3);
        }
    }
}

