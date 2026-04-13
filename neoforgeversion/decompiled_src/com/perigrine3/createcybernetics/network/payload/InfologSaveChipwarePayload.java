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

public record InfologSaveChipwarePayload(int chipwareSlot, String text, boolean locked) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<InfologSaveChipwarePayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"infolog_save_chipware"));
    public static final StreamCodec<RegistryFriendlyByteBuf, InfologSaveChipwarePayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, InfologSaveChipwarePayload::chipwareSlot, (StreamCodec)ByteBufCodecs.STRING_UTF8, InfologSaveChipwarePayload::text, (StreamCodec)ByteBufCodecs.BOOL, InfologSaveChipwarePayload::locked, InfologSaveChipwarePayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

