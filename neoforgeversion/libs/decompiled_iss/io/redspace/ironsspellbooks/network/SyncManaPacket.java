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
package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncManaPacket
implements CustomPacketPayload {
    private int playerMana = 0;
    private MagicData playerMagicData = null;
    public static final CustomPacketPayload.Type<SyncManaPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_mana"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncManaPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncManaPacket::write, SyncManaPacket::new);

    public SyncManaPacket(MagicData playerMagicData) {
        this.playerMagicData = playerMagicData;
    }

    public SyncManaPacket(FriendlyByteBuf buf) {
        this.playerMana = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt((int)this.playerMagicData.getMana());
    }

    public static void handle(SyncManaPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.setMana(packet.playerMana));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

