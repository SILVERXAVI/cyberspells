/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.common.network.ClientPacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncCyberwareDataPacket(CompoundTag data, int entityId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncCyberwareDataPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"sync_cyberware"));
    public static final StreamCodec<ByteBuf, SyncCyberwareDataPacket> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.COMPOUND_TAG, SyncCyberwareDataPacket::data, (StreamCodec)ByteBufCodecs.VAR_INT, SyncCyberwareDataPacket::entityId, SyncCyberwareDataPacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> ClientPacketHandler.handleSyncPacket(this, ctx));
    }
}

