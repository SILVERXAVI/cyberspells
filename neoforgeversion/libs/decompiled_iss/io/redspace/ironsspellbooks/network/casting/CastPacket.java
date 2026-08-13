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
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.casting;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CastPacket
implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CastPacket> STREAM_CODEC = CustomPacketPayload.codec(CastPacket::write, CastPacket::new);

    public CastPacket() {
    }

    public CastPacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(CastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)patt0$temp;
                Utils.serverSideInitiateCast(serverPlayer);
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

