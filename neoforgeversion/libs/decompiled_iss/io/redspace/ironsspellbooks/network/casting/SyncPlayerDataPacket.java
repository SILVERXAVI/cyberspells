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

import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncPlayerDataPacket
implements CustomPacketPayload {
    SyncedSpellData syncedSpellData;
    public static final CustomPacketPayload.Type<SyncPlayerDataPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_player_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerDataPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncPlayerDataPacket::write, SyncPlayerDataPacket::new);

    public SyncPlayerDataPacket(SyncedSpellData playerSyncedData) {
        this.syncedSpellData = playerSyncedData;
    }

    public SyncPlayerDataPacket(FriendlyByteBuf buf) {
        this.syncedSpellData = SyncedSpellData.read(buf);
    }

    public void write(FriendlyByteBuf buf) {
        SyncedSpellData.write(buf, this.syncedSpellData);
    }

    public static void handle(SyncPlayerDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.handlePlayerSyncedData(packet.syncedSpellData));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

