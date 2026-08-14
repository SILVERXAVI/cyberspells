/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.item.componentbox;

import com.maxwell.cyber_ware_port.common.container.ComponentBoxMenu;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ComponentBoxItem
extends BlockItem {
    public ComponentBoxItem() {
        super((Block)ModBlocks.COMPONENT_BOX.get(), new Item.Properties().stacksTo(1));
    }

    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && !player.isShiftKeyDown()) {
            return this.use(context.getLevel(), player, context.getHand()).getResult();
        }
        return super.useOn(context);
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)pPlayer;
            if (!pPlayer.isShiftKeyDown()) {
                serverPlayer.openMenu((MenuProvider)new SimpleMenuProvider((id, inv, p) -> new ComponentBoxMenu(id, inv, p.getItemInHand(pHand)), (Component)Component.translatable((String)"item.cyber_ware_port.component_box")), buf -> buf.writeBoolean(pHand == InteractionHand.MAIN_HAND));
                return InteractionResultHolder.success((Object)stack);
            }
        }
        return InteractionResultHolder.pass((Object)stack);
    }
}

