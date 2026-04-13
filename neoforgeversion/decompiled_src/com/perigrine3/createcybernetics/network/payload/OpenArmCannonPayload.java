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

public record OpenArmCannonPayload() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenArmCannonPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"open_arm_cannon"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenArmCannonPayload> STREAM_CODEC = StreamCodec.unit((Object)new OpenArmCannonPayload());

    public CustomPacketPayload.Type<OpenArmCannonPayload> type() {
        return TYPE;
    }
}

