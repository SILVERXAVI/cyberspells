/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.cranium;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class NeuralContextualizerItem
extends CyberwareItem {
    public NeuralContextualizerItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_BRAIN).maxInstall(1).energy(1, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }

    @Override
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ItemStack stack, LivingEntity wearer) {
        Player player;
        if (!(wearer instanceof Player) || (player = (Player)wearer).isCreative()) {
            return;
        }
        if (!player.level().isClientSide) {
            BlockState state = player.level().getBlockState(event.getPos());
            ItemStack currentStack = player.getMainHandItem();
            if (currentStack.getDestroySpeed(state) > 1.0f) {
                return;
            }
            int bestSlot = -1;
            float bestSpeed = 1.0f;
            for (int i = 0; i < 9; ++i) {
                ItemStack invStack = player.getInventory().getItem(i);
                float speed = invStack.getDestroySpeed(state);
                if (!(speed > bestSpeed)) continue;
                bestSpeed = speed;
                bestSlot = i;
            }
            if (bestSlot != -1 && bestSlot != player.getInventory().selected) {
                player.getInventory().selected = bestSlot;
                ((ServerPlayer)player).connection.send((Packet)new ClientboundSetCarriedItemPacket(bestSlot));
            }
        }
    }
}

