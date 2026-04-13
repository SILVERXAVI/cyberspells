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

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DataShardItem
extends Item {
    public DataShardItem(Item.Properties props) {
        super(props);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() || !stack.is(ModTags.Items.DATA_SHARDS)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success((Object)stack);
        }
        if (!(player instanceof ServerPlayer)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!sp.hasData(ModAttachments.CYBERWARE)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return InteractionResultHolder.pass((Object)stack);
        }
        if (!data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get(), CyberwareSlot.BRAIN)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        int target = -1;
        for (int i = 0; i < 2; ++i) {
            if (!data.getChipwareStack(i).isEmpty()) continue;
            target = i;
            break;
        }
        if (target == -1) {
            return InteractionResultHolder.pass((Object)stack);
        }
        ItemStack one = stack.copyWithCount(1);
        data.setChipwareStack(target, one);
        stack.shrink(1);
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
        return InteractionResultHolder.consume((Object)player.getItemInHand(hand));
    }
}

