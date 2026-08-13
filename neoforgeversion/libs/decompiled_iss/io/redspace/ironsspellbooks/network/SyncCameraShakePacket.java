/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncCameraShakePacket(CameraShakeData data, boolean remove) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncCameraShakePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_camera_shake"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCameraShakePacket> STREAM_CODEC = CustomPacketPayload.codec(SyncCameraShakePacket::write, SyncCameraShakePacket::new);

    public SyncCameraShakePacket(FriendlyByteBuf buf) {
        this(CameraShakeData.deserializeFromBuffer(buf), buf.readBoolean());
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        this.data.serializeToBuffer(buf);
        buf.writeBoolean(this.remove);
    }

    public static void handle(SyncCameraShakePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (packet.remove) {
                CameraShakeManager.removeClientCameraShake(packet.data);
            } else {
                CameraShakeManager.addClientCameraShake(packet.data);
            }
        });
    }
}

