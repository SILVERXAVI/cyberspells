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

public record ArmCannonFirePayload() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ArmCannonFirePayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"arm_cannon_fire"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmCannonFirePayload> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, ArmCannonFirePayload>(){

        public ArmCannonFirePayload decode(RegistryFriendlyByteBuf buf) {
            return new ArmCannonFirePayload();
        }

        public void encode(RegistryFriendlyByteBuf buf, ArmCannonFirePayload value) {
        }
    };

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

