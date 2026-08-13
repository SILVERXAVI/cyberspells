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
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.casting;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncEntityDataPacket
implements CustomPacketPayload {
    SyncedSpellData syncedSpellData;
    int entityId;
    public static final CustomPacketPayload.Type<SyncEntityDataPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_entity_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityDataPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncEntityDataPacket::write, SyncEntityDataPacket::new);

    public SyncEntityDataPacket(SyncedSpellData syncedSpellData, IMagicEntity entity) {
        this.syncedSpellData = syncedSpellData;
        this.entityId = ((Entity)entity).getId();
    }

    public SyncEntityDataPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.syncedSpellData = SyncedSpellData.read(buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        SyncedSpellData.write(buf, this.syncedSpellData);
    }

    public static void handle(SyncEntityDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.handleAbstractCastingMobSyncedData(packet.entityId, packet.syncedSpellData));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

