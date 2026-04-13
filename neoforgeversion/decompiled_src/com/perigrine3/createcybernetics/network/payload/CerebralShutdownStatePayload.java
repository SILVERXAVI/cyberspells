/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.payload;

import com.perigrine3.createcybernetics.item.cyberware.CerebralProcessingUnitItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CerebralShutdownStatePayload(boolean active) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CerebralShutdownStatePayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cerebral_shutdown_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CerebralShutdownStatePayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.BOOL, CerebralShutdownStatePayload::active, CerebralShutdownStatePayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CerebralShutdownStatePayload msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().player == null) {
                return;
            }
            CerebralProcessingUnitItem.setClientShutdownActive(msg.active());
        });
    }
}

