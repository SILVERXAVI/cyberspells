/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleCyberwarePacket(int slotId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ToggleCyberwarePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"toggle_cyberware"));
    public static final StreamCodec<FriendlyByteBuf, ToggleCyberwarePacket> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, ToggleCyberwarePacket::slotId, ToggleCyberwarePacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)patt0$temp;
                CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
                ItemStack stack = data.getInstalledCyberware().getStackInSlot(this.slotId);
                ICyberware cw = CyberwareAPI.getCyberware(stack);
                if (!stack.isEmpty() && cw != null && cw.canToggle(stack)) {
                    if (!cw.isActive(stack)) {
                        for (int i = 0; i < data.getInstalledCyberware().getSlots(); ++i) {
                            if (i == this.slotId) continue;
                            ItemStack other = data.getInstalledCyberware().getStackInSlot(i);
                            ICyberware otherCw = CyberwareAPI.getCyberware(other);
                            if (other.isEmpty() || otherCw == null || !otherCw.isActive(other) || (cw.getBodyPartType(stack) == BodyPartType.NONE || cw.getBodyPartType(stack) != otherCw.getBodyPartType(other)) && !cw.isIncompatible(stack, other) && !otherCw.isIncompatible(other, stack)) continue;
                            player.sendSystemMessage((Component)Component.translatable((String)"cyberware.message.conflict_active").withStyle(ChatFormatting.RED));
                            return;
                        }
                    }
                    cw.toggle(stack);
                    data.recalculateCapacity(player);
                    data.syncToClient(player);
                }
            }
        });
    }
}

