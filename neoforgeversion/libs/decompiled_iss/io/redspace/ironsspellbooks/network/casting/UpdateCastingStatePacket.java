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

import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateCastingStatePacket
implements CustomPacketPayload {
    private final String spellId;
    private final int spellLevel;
    private final int castTime;
    private final CastSource castSource;
    private final String castingEquipmentSlot;
    public static final CustomPacketPayload.Type<UpdateCastingStatePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"update_casting_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCastingStatePacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateCastingStatePacket::write, UpdateCastingStatePacket::new);

    public UpdateCastingStatePacket(String spellId, int spellLevel, int castTime, CastSource castSource, String castingEquipmentSlot) {
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.castTime = castTime;
        this.castSource = castSource;
        this.castingEquipmentSlot = castingEquipmentSlot;
    }

    public UpdateCastingStatePacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.spellLevel = buf.readInt();
        this.castTime = buf.readInt();
        this.castSource = (CastSource)buf.readEnum(CastSource.class);
        this.castingEquipmentSlot = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.spellId);
        buf.writeInt(this.spellLevel);
        buf.writeInt(this.castTime);
        buf.writeEnum((Enum)this.castSource);
        buf.writeUtf(this.castingEquipmentSlot);
    }

    public static void handle(UpdateCastingStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.setClientCastState(packet.spellId, packet.spellLevel, packet.castTime, packet.castSource, packet.castingEquipmentSlot));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

