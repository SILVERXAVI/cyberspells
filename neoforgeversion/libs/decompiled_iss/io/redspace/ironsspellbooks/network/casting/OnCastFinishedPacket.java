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

public class OnCastFinishedPacket
implements CustomPacketPayload {
    private final String spellId;
    private final UUID castingEntityId;
    private final boolean cancelled;
    public static final CustomPacketPayload.Type<OnCastFinishedPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"on_cast_finished"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnCastFinishedPacket> STREAM_CODEC = CustomPacketPayload.codec(OnCastFinishedPacket::write, OnCastFinishedPacket::new);

    public OnCastFinishedPacket(UUID castingEntityId, String spellId, boolean cancelled) {
        this.spellId = spellId;
        this.castingEntityId = castingEntityId;
        this.cancelled = cancelled;
    }

    public OnCastFinishedPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.castingEntityId = buf.readUUID();
        this.cancelled = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeUUID(this.castingEntityId);
        buf.writeBoolean(this.cancelled);
    }

    public static void handle(OnCastFinishedPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientBoundOnCastFinished(packet.castingEntityId, packet.spellId, packet.cancelled));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

