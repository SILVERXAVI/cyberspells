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

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OnCastStartedPacket
implements CustomPacketPayload {
    private final String spellId;
    private final int spellLevel;
    private final UUID castingEntityId;
    public static final CustomPacketPayload.Type<OnCastStartedPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"on_cast_started"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnCastStartedPacket> STREAM_CODEC = CustomPacketPayload.codec(OnCastStartedPacket::write, OnCastStartedPacket::new);

    public OnCastStartedPacket(UUID castingEntityId, String spellId, int spellLevel) {
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.castingEntityId = castingEntityId;
    }

    public OnCastStartedPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.spellLevel = buf.readInt();
        this.castingEntityId = buf.readUUID();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.spellLevel);
        buf.writeUUID(this.castingEntityId);
    }

    public static void handle(OnCastStartedPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientBoundOnCastStarted(packet.castingEntityId, packet.spellId, packet.spellLevel));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

