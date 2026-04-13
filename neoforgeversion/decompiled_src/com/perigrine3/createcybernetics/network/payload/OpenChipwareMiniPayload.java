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

public record OpenChipwareMiniPayload() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenChipwareMiniPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"open_chipware_mini"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenChipwareMiniPayload> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, OpenChipwareMiniPayload>(){

        public OpenChipwareMiniPayload decode(RegistryFriendlyByteBuf buf) {
            return new OpenChipwareMiniPayload();
        }

        public void encode(RegistryFriendlyByteBuf buf, OpenChipwareMiniPayload value) {
        }
    };

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

