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

import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncRecastsPacket
implements CustomPacketPayload {
    private final Map<String, RecastInstance> recastLookup;
    public static final CustomPacketPayload.Type<SyncRecastsPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_recasts"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecastsPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncRecastsPacket::write, SyncRecastsPacket::new);

    public SyncRecastsPacket(Map<String, RecastInstance> recastLookup) {
        this.recastLookup = recastLookup;
    }

    public SyncRecastsPacket(FriendlyByteBuf buf) {
        this.recastLookup = buf.readMap(SyncRecastsPacket::readSpellID, SyncRecastsPacket::readRecastInstance);
    }

    public static String readSpellID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static RecastInstance readRecastInstance(FriendlyByteBuf buffer) {
        RecastInstance tmp = new RecastInstance();
        tmp.readFromBuffer(buffer);
        return tmp;
    }

    public static void writeSpellId(FriendlyByteBuf buf, String spellId) {
        buf.writeUtf(spellId);
    }

    public static void writeRecastInstance(FriendlyByteBuf buf, RecastInstance recastInstance) {
        recastInstance.writeToBuffer(buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.recastLookup, SyncRecastsPacket::writeSpellId, SyncRecastsPacket::writeRecastInstance);
    }

    public static void handle(SyncRecastsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.setRecasts(new PlayerRecasts(packet.recastLookup)));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

