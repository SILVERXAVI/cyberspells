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

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CastErrorPacket
implements CustomPacketPayload {
    public final ErrorType errorType;
    public final String spellId;
    public static final CustomPacketPayload.Type<CastErrorPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cast_error"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CastErrorPacket> STREAM_CODEC = CustomPacketPayload.codec(CastErrorPacket::write, CastErrorPacket::new);

    public CastErrorPacket(ErrorType errorType, AbstractSpell spell) {
        this.spellId = spell.getSpellId();
        this.errorType = errorType;
    }

    public CastErrorPacket(FriendlyByteBuf buf) {
        this.errorType = (ErrorType)buf.readEnum(ErrorType.class);
        this.spellId = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeEnum((Enum)this.errorType);
        buf.writeUtf(this.spellId);
    }

    public static void handle(CastErrorPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientSpellCastHelper.handleCastErrorMessage(packet);
            return true;
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static enum ErrorType {
        COOLDOWN,
        MANA;

    }
}

