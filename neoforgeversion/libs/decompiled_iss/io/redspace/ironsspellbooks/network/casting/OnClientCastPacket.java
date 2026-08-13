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

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OnClientCastPacket
implements CustomPacketPayload {
    String spellId;
    int level;
    CastSource castSource;
    ICastData castData;
    public static final CustomPacketPayload.Type<OnClientCastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"on_client_cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnClientCastPacket> STREAM_CODEC = CustomPacketPayload.codec(OnClientCastPacket::write, OnClientCastPacket::new);

    public OnClientCastPacket(String spellId, int level, CastSource castSource, ICastData castData) {
        this.spellId = spellId;
        this.level = level;
        this.castSource = castSource;
        this.castData = castData;
    }

    public OnClientCastPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.level = buf.readInt();
        this.castSource = (CastSource)buf.readEnum(CastSource.class);
        if (buf.readBoolean()) {
            ICastDataSerializable tmp = SpellRegistry.getSpell(this.spellId).getEmptyCastData();
            tmp.readFromBuffer(buf);
            this.castData = tmp;
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.level);
        buf.writeEnum((Enum)this.castSource);
        ICastData iCastData = this.castData;
        if (iCastData instanceof ICastDataSerializable) {
            ICastDataSerializable castDataSerializable = (ICastDataSerializable)iCastData;
            buf.writeBoolean(true);
            castDataSerializable.writeToBuffer(buf);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static void handle(OnClientCastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientboundOnClientCast(packet.spellId, packet.level, packet.castSource, packet.castData));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

