/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.network.OpenEldritchScreenPacket;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class EldritchManuscript
extends Item {
    private static final Component description = Component.translatable((String)"item.irons_spellbooks.eldritch_manuscript_desc").withStyle(ChatFormatting.GRAY);

    public EldritchManuscript(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new OpenEldritchScreenPacket(pUsedHand), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        return super.use(level, player, pUsedHand);
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> lines, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, lines, pIsAdvanced);
        lines.add(description);
    }
}

