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

public class QuickCastPacket
implements CustomPacketPayload {
    private int slot;
    public static final CustomPacketPayload.Type<QuickCastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"quick_cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuickCastPacket> STREAM_CODEC = CustomPacketPayload.codec(QuickCastPacket::write, QuickCastPacket::new);

    public QuickCastPacket(int slot) {
        this.slot = slot;
    }

    public QuickCastPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public static void handle(QuickCastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)patt0$temp;
                Utils.serverSideInitiateQuickCast(serverPlayer, packet.slot);
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

