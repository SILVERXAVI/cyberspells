/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class ArmCannonWheelPayloads {
    private ArmCannonWheelPayloads() {
    }

    public record SelectArmCannonAmmoSlotPayload(int slotIndex) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<SelectArmCannonAmmoSlotPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"arm_cannon_wheel_select"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SelectArmCannonAmmoSlotPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, SelectArmCannonAmmoSlotPayload::slotIndex, SelectArmCannonAmmoSlotPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record OpenArmCannonWheelClientPayload(int segments, int selectedIndex) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<OpenArmCannonWheelClientPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"arm_cannon_wheel_open"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenArmCannonWheelClientPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, OpenArmCannonWheelClientPayload::segments, (StreamCodec)ByteBufCodecs.VAR_INT, OpenArmCannonWheelClientPayload::selectedIndex, OpenArmCannonWheelClientPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record RequestOpenArmCannonWheelPayload() implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<RequestOpenArmCannonWheelPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"arm_cannon_wheel_open_req"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestOpenArmCannonWheelPayload> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, RequestOpenArmCannonWheelPayload>(){

            public RequestOpenArmCannonWheelPayload decode(RegistryFriendlyByteBuf buf) {
                return new RequestOpenArmCannonWheelPayload();
            }

            public void encode(RegistryFriendlyByteBuf buf, RequestOpenArmCannonWheelPayload value) {
            }
        };

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

