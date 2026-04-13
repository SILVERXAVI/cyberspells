/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.MenuConstructor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
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
import com.perigrine3.createcybernetics.block.entity.RobosurgeonBlockEntity;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RobosurgeonBlock
extends BaseEntityBlock {
    public static final MapCodec<RobosurgeonBlock> CODEC = RobosurgeonBlock.simpleCodec(RobosurgeonBlock::new);

    public RobosurgeonBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Player player;
        PlayerCyberwareData data;
        EntityCollisionContext ecc;
        Entity entity;
        if (context instanceof EntityCollisionContext && (entity = (ecc = (EntityCollisionContext)context).getEntity()) instanceof Player && (data = (PlayerCyberwareData)(player = (Player)entity).getData(ModAttachments.CYBERWARE)).hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE) && player.isCrouching()) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RobosurgeonBlockEntity(blockPos, blockState);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity blockEntity;
        if (state.getBlock() != newState.getBlock() && (blockEntity = level.getBlockEntity(pos)) instanceof RobosurgeonBlockEntity) {
            RobosurgeonBlockEntity robosurgeonBlockEntity = (RobosurgeonBlockEntity)blockEntity;
            robosurgeonBlockEntity.drops();
            level.updateNeighbourForOutputSignal(pos, (Block)this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RobosurgeonBlockEntity) {
            RobosurgeonBlockEntity robosurgeonBlockEntity = (RobosurgeonBlockEntity)blockEntity;
            if (!level.isClientSide()) {
                ((ServerPlayer)player).openMenu((MenuProvider)new SimpleMenuProvider((MenuConstructor)robosurgeonBlockEntity, (Component)Component.literal((String)("_" + player.getName().getString() + ".exe"))), pos);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}

