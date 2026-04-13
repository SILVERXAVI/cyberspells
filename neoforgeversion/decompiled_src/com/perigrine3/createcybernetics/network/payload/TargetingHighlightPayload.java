/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.fml.loading.FMLEnvironment
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.payload;

import com.perigrine3.createcybernetics.client.TargetingModuleClientOutline;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TargetingHighlightPayload(int entityId, int durationTicks) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<TargetingHighlightPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"targeting_highlight"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TargetingHighlightPayload> STREAM_CODEC = StreamCodec.of((buf, msg) -> {
        buf.writeVarInt(msg.entityId);
        buf.writeVarInt(msg.durationTicks);
    }, buf -> new TargetingHighlightPayload(buf.readVarInt(), buf.readVarInt()));

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TargetingHighlightPayload msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist != Dist.CLIENT) {
                return;
            }
            TargetingModuleClientOutline.setTarget(msg.entityId, msg.durationTicks);
        });
    }
}

