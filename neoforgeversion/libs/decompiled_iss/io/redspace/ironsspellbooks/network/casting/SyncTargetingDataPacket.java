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
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.casting;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncTargetingDataPacket
implements CustomPacketPayload {
    private final List<UUID> targetUUIDs = new ArrayList<UUID>();
    private final String spellId;
    public static final CustomPacketPayload.Type<SyncTargetingDataPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_targeting_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTargetingDataPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncTargetingDataPacket::write, SyncTargetingDataPacket::new);

    public SyncTargetingDataPacket(LivingEntity entity, AbstractSpell spell) {
        this.targetUUIDs.add(entity.getUUID());
        this.spellId = spell.getSpellId();
    }

    public SyncTargetingDataPacket(AbstractSpell spell, List<UUID> uuids) {
        this.targetUUIDs.addAll(uuids);
        this.spellId = spell.getSpellId();
    }

    public SyncTargetingDataPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        int i = buf.readInt();
        for (int j = 0; j < i; ++j) {
            this.targetUUIDs.add(buf.readUUID());
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.targetUUIDs.size());
        this.targetUUIDs.forEach(arg_0 -> ((FriendlyByteBuf)buf).writeUUID(arg_0));
    }

    public static void handle(SyncTargetingDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.setTargetingData(new ClientSpellTargetingData(packet.spellId, packet.targetUUIDs)));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

