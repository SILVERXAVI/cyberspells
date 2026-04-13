/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.tterrag.registrate.util.RegistrateDistExecutor
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.payload;

import com.perigrine3.createcybernetics.compat.northstar.CopernicusClientHooks;
import com.tterrag.registrate.util.RegistrateDistExecutor;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CopernicusOxygenSyncPayload(int oxygen) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CopernicusOxygenSyncPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"copernicus_oxygen_sync"));
    public static final StreamCodec<ByteBuf, CopernicusOxygenSyncPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, CopernicusOxygenSyncPayload::oxygen, CopernicusOxygenSyncPayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CopernicusOxygenSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> RegistrateDistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> CopernicusClientHooks.setOxygenHud(payload.oxygen())));
    }
}

