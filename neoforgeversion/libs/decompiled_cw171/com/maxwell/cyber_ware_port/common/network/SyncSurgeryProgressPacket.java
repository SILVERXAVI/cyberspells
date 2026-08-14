/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.common.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncSurgeryProgressPacket(int progress, int maxProgress) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncSurgeryProgressPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"sync_surgery_progress"));
    public static final StreamCodec<FriendlyByteBuf, SyncSurgeryProgressPacket> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, SyncSurgeryProgressPacket::progress, (StreamCodec)ByteBufCodecs.VAR_INT, SyncSurgeryProgressPacket::maxProgress, SyncSurgeryProgressPacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> ClientPacketHandler.handleProgressPacket(this, ctx));
    }
}

