/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenHeatEnginePayload() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenHeatEnginePayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"open_heat_engine"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenHeatEnginePayload> STREAM_CODEC = StreamCodec.unit((Object)new OpenHeatEnginePayload());

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

