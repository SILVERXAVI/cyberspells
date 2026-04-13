/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.Tags$Blocks
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class OreMultiplierHandler {
    private OreMultiplierHandler() {
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.isCreative()) {
            return;
        }
        LevelAccessor levelAccessor = event.getLevel();
        if (!(levelAccessor instanceof ServerLevel)) {
            return;
        }
        ServerLevel level = (ServerLevel)levelAccessor;
        double mult = player.getAttributeValue(ModAttributes.ORE_DROP_MULTIPLIER);
        if (!Double.isFinite(mult) || mult <= 1.0) {
            return;
        }
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        if (!state.is(Tags.Blocks.ORES)) {
            return;
        }
        ItemStack tool = player.getMainHandItem();
        if (OreMultiplierHandler.hasSilkTouch((Level)level, tool)) {
            return;
        }
        BlockEntity be = level.getBlockEntity(pos);
        List drops = Block.getDrops((BlockState)state, (ServerLevel)level, (BlockPos)pos, (BlockEntity)be, (Entity)player, (ItemStack)tool);
        for (ItemStack drop : drops) {
            int extra;
            int base;
            if (drop.isEmpty() || (base = drop.getCount()) <= 0 || (extra = (int)Math.floor((double)base * (mult - 1.0))) <= 0) continue;
            ItemStack extraStack = drop.copy();
            extraStack.setCount(extra);
            Block.popResource((Level)level, (BlockPos)pos, (ItemStack)extraStack);
        }
    }

    private static boolean hasSilkTouch(Level level, ItemStack tool) {
        if (tool.isEmpty()) {
            return false;
        }
        Holder.Reference silk = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        return EnchantmentHelper.getItemEnchantmentLevel((Holder)silk, (ItemStack)tool) > 0;
    }
}

