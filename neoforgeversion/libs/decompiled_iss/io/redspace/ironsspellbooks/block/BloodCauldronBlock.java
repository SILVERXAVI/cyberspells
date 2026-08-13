/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.cauldron.CauldronInteraction
 *  net.minecraft.core.cauldron.CauldronInteraction$InteractionMap
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemUtils
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.biome.Biome$Precipitation
 *  net.minecraft.world.level.block.AbstractCauldronBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.CampfireBlock
 *  net.minecraft.world.level.block.LayeredCauldronBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 */
package io.redspace.ironsspellbooks.block;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;

public class BloodCauldronBlock
extends LayeredCauldronBlock {
    public BloodCauldronBlock() {
        super(Biome.Precipitation.NONE, BloodCauldronBlock.getInteractionMap(), BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.CAULDRON));
    }

    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity.tickCount % 20 == 0) {
            BloodCauldronBlock.attemptCookEntity(blockState, level, pos, entity, () -> {
                level.setBlockAndUpdate(pos, (BlockState)blockState.cycle((Property)LayeredCauldronBlock.LEVEL));
                level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
            });
        }
        super.entityInside(blockState, level, pos, entity);
    }

    public static void attemptCookEntity(BlockState blockState, Level level, BlockPos pos, Entity entity, CookExecution execution) {
        LivingEntity livingEntity;
        if (level.isClientSide) {
            return;
        }
        if (!CampfireBlock.isLitCampfire((BlockState)level.getBlockState(pos.below()))) {
            return;
        }
        Block block = level.getBlockState(pos).getBlock();
        if (!(block instanceof AbstractCauldronBlock)) {
            return;
        }
        AbstractCauldronBlock cauldron = (AbstractCauldronBlock)block;
        if (entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).getBoundingBox().intersects(cauldron.defaultBlockState().getInteractionShape((BlockGetter)level, pos).bounds().move(pos)) && livingEntity.hurt(DamageSources.get(level, ISSDamageTypes.CAULDRON), 2.0f) && !livingEntity.getType().is(ModTags.CANT_PRODUCE_BLOOD)) {
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, entity.getX(), entity.getY() + (double)(entity.getBbHeight() / 2.0f), entity.getZ(), 20, 0.05, 0.05, 0.05, 0.1, false);
            if (Utils.random.nextDouble() <= 0.5 && !BloodCauldronBlock.isCauldronFull(blockState)) {
                execution.execute();
            }
        }
    }

    private static boolean isCauldronFull(BlockState blockState) {
        if (!blockState.hasProperty((Property)LEVEL)) {
            return false;
        }
        return (Integer)blockState.getValue((Property)LayeredCauldronBlock.LEVEL) == 3;
    }

    public static CauldronInteraction.InteractionMap getInteractionMap() {
        CauldronInteraction.InteractionMap map = CauldronInteraction.newInteractionMap((String)"blood_cauldron");
        map.map().put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, hand, itemStack) -> {
            if (!level.isClientSide) {
                Item item = itemStack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult((ItemStack)itemStack, (Player)player, (ItemStack)new ItemStack((ItemLike)ItemRegistry.BLOOD_VIAL.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get((Object)item));
                LayeredCauldronBlock.lowerFillLevel((BlockState)blockState, (Level)level, (BlockPos)blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, blockPos);
            }
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        });
        return map;
    }

    @Deprecated(forRemoval=true)
    public static interface CookExecution {
        public void execute();
    }
}

