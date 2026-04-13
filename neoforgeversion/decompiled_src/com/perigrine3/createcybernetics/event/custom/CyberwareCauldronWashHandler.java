/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.LayeredCauldronBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.api.ICyberwareItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareCauldronWashHandler {
    private CyberwareCauldronWashHandler() {
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level == null) {
            return;
        }
        BlockState state = level.getBlockState(event.getPos());
        if (!state.is(Blocks.WATER_CAULDRON)) {
            return;
        }
        InteractionHand hand = event.getHand();
        ItemStack stack = event.getEntity().getItemInHand(hand);
        if (stack.isEmpty()) {
            return;
        }
        Item item = stack.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return;
        }
        ICyberwareItem cw = (ICyberwareItem)item;
        if (!cw.isDyeable(stack)) {
            return;
        }
        if (!stack.has(DataComponents.DYED_COLOR)) {
            return;
        }
        if (!level.isClientSide) {
            stack.remove(DataComponents.DYED_COLOR);
            if (!event.getEntity().getAbilities().instabuild) {
                LayeredCauldronBlock.lowerFillLevel((BlockState)state, (Level)level, (BlockPos)event.getPos());
            }
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess((boolean)level.isClientSide));
    }
}

