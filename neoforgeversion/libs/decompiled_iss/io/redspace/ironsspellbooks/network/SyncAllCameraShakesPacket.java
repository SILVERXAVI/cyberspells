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
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncAllCameraShakesPacket
implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncAllCameraShakesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_all_camera_shake"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAllCameraShakesPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncAllCameraShakesPacket::write, SyncAllCameraShakesPacket::new);
    ArrayList<CameraShakeData> cameraShakeData;

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public SyncAllCameraShakesPacket(ArrayList<CameraShakeData> cameraShakeData) {
        this.cameraShakeData = cameraShakeData;
    }

    public SyncAllCameraShakesPacket(FriendlyByteBuf buf) {
        this.cameraShakeData = new ArrayList();
        int i = buf.readInt();
        for (int j = 0; j < i; ++j) {
            this.cameraShakeData.add(CameraShakeData.deserializeFromBuffer(buf));
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.cameraShakeData.size());
        for (CameraShakeData data : this.cameraShakeData) {
            data.serializeToBuffer(buf);
        }
    }

    public static void handle(SyncAllCameraShakesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            CameraShakeManager.cameraShakeData.clear();
            CameraShakeManager.cameraShakeData.addAll(packet.cameraShakeData);
        });
    }
}

