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

import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncCooldownPacket
implements CustomPacketPayload {
    private final String spellId;
    private final int duration;
    public static final CustomPacketPayload.Type<SyncCooldownPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_cooldown"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCooldownPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncCooldownPacket::write, SyncCooldownPacket::new);

    public SyncCooldownPacket(String spellId, int duration) {
        this.spellId = spellId;
        this.duration = duration;
    }

    public SyncCooldownPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.duration = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.duration);
    }

    public static void handle(SyncCooldownPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.getCooldowns().addCooldown(packet.spellId, packet.duration));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

