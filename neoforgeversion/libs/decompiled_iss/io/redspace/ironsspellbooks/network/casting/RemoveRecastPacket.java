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

public class RemoveRecastPacket
implements CustomPacketPayload {
    private final String spellId;
    public static final CustomPacketPayload.Type<RemoveRecastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"remove_recast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveRecastPacket> STREAM_CODEC = CustomPacketPayload.codec(RemoveRecastPacket::write, RemoveRecastPacket::new);

    public RemoveRecastPacket(String spellId) {
        this.spellId = spellId;
    }

    public RemoveRecastPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
    }

    public static void handle(RemoveRecastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientMagicData.getRecasts().removeRecast(packet.spellId);
            ClientMagicData.cacheClientSummons();
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

