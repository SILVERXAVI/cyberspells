/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.item.generic;

import com.perigrine3.createcybernetics.common.FaceplateAliasHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FaceplateMaskItem
extends Item {
    public FaceplateMaskItem(Item.Properties props) {
        super(props);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)true);
        }
        if (!(player instanceof ServerPlayer)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        ServerPlayer sp = (ServerPlayer)player;
        ItemStack one = stack.copy();
        one.setCount(1);
        if (!FaceplateAliasHandler.apply(sp, one)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        if (!sp.isCreative()) {
            stack.shrink(1);
        }
        sp.getInventory().setChanged();
        sp.inventoryMenu.broadcastChanges();
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)false);
    }
}

