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
package io.redspace.ironsspellbooks.network.casting;

import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncRecastPacket
implements CustomPacketPayload {
    private final RecastInstance recastInstance;
    public static final CustomPacketPayload.Type<SyncRecastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_recast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecastPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncRecastPacket::write, SyncRecastPacket::new);

    public SyncRecastPacket(RecastInstance recastInstance) {
        this.recastInstance = recastInstance;
    }

    public SyncRecastPacket(FriendlyByteBuf buf) {
        this.recastInstance = new RecastInstance();
        this.recastInstance.readFromBuffer(buf);
    }

    public void write(FriendlyByteBuf buf) {
        if (this.recastInstance != null) {
            this.recastInstance.writeToBuffer(buf);
        }
    }

    public static void handle(SyncRecastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientMagicData.getRecasts().forceAddRecast(packet.recastInstance);
            ClientMagicData.cacheClientSummons();
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

