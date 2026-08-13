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

import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EquipmentChangedPacket
implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EquipmentChangedPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"equipment_changed"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentChangedPacket> STREAM_CODEC = CustomPacketPayload.codec(EquipmentChangedPacket::write, EquipmentChangedPacket::new);

    public EquipmentChangedPacket() {
    }

    public EquipmentChangedPacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(EquipmentChangedPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientMagicData.updateSpellSelectionManager());
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

