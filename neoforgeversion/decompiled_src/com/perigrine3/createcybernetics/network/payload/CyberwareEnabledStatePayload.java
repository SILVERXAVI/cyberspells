/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.payload;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CyberwareEnabledStatePayload(String slotName, int index, boolean enabled) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CyberwareEnabledStatePayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_enabled_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CyberwareEnabledStatePayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.STRING_UTF8, CyberwareEnabledStatePayload::slotName, (StreamCodec)ByteBufCodecs.VAR_INT, CyberwareEnabledStatePayload::index, (StreamCodec)ByteBufCodecs.BOOL, CyberwareEnabledStatePayload::enabled, CyberwareEnabledStatePayload::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CyberwareEnabledStatePayload msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            CyberwareSlot slot;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            if (!mc.player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)mc.player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            try {
                slot = CyberwareSlot.valueOf(msg.slotName());
            }
            catch (IllegalArgumentException ex) {
                return;
            }
            data.setEnabled(slot, msg.index(), msg.enabled());
        });
    }
}

