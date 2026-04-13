/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
public final class CropMultiplierHandler {
    private CropMultiplierHandler() {
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
        double mult = player.getAttributeValue(ModAttributes.CROP_MULTIPLIER);
        if (!Double.isFinite(mult) || mult <= 1.0) {
            return;
        }
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        BlockEntity be = level.getBlockEntity(pos);
        ItemStack tool = player.getMainHandItem();
        List drops = Block.getDrops((BlockState)state, (ServerLevel)level, (BlockPos)pos, (BlockEntity)be, (Entity)player, (ItemStack)tool);
        for (ItemStack drop : drops) {
            int extra;
            int base;
            if (drop.isEmpty() || !drop.is(Tags.Items.CROPS) || (base = drop.getCount()) <= 0 || (extra = (int)Math.floor((double)base * (mult - 1.0))) <= 0) continue;
            ItemStack extraStack = drop.copy();
            extraStack.setCount(extra);
            Block.popResource((Level)level, (BlockPos)pos, (ItemStack)extraStack);
        }
    }
}

