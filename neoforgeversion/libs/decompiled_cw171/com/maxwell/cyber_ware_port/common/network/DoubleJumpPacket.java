/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.cyberware.leg.LinearActuatorsItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DoubleJumpPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<DoubleJumpPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"double_jump"));
    public static final StreamCodec<FriendlyByteBuf, DoubleJumpPacket> STREAM_CODEC = StreamCodec.unit((Object)new DoubleJumpPacket());

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player;
            CyberwareUserData data;
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer && (data = (CyberwareUserData)(player = (ServerPlayer)patt0$temp).getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).isCyberwareInstalled((Item)ModItems.LINEAR_ACTUATORS.get()) && !player.getPersistentData().getBoolean("cyberware_double_jumped")) {
                LinearActuatorsItem.performDoubleJump((Player)player);
                player.hurtMarked = true;
            }
        });
    }
}

