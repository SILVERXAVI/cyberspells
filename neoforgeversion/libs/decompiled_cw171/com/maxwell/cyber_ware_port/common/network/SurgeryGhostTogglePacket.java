/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SurgeryGhostTogglePacket(BlockPos pos, int slotId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SurgeryGhostTogglePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"surgery_ghost_toggle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SurgeryGhostTogglePacket> STREAM_CODEC = StreamCodec.composite((StreamCodec)BlockPos.STREAM_CODEC, SurgeryGhostTogglePacket::pos, (StreamCodec)ByteBufCodecs.VAR_INT, SurgeryGhostTogglePacket::slotId, SurgeryGhostTogglePacket::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity be;
            ServerPlayer player;
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer && (player = (ServerPlayer)patt0$temp).level().isLoaded(this.pos) && (be = player.level().getBlockEntity(this.pos)) instanceof RobosurgeonBlockEntity) {
                CyberwareUserData data;
                ItemStackHandler body;
                ItemStack installed;
                RobosurgeonBlockEntity tile = (RobosurgeonBlockEntity)be;
                ItemStackHandler itemHandler = tile.getItemHandler();
                ItemStack currentStack = itemHandler.getStackInSlot(this.slotId);
                boolean changed = false;
                if (!currentStack.isEmpty() && ((Boolean)currentStack.getOrDefault(CyberWare.GHOST_COMPONENT, (Object)false)).booleanValue()) {
                    itemHandler.setStackInSlot(this.slotId, ItemStack.EMPTY);
                    changed = true;
                } else if (currentStack.isEmpty() && !(installed = (body = (data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).getInstalledCyberware()).getStackInSlot(this.slotId)).isEmpty()) {
                    ItemStack ghost = installed.copy();
                    ghost.set(CyberWare.GHOST_COMPONENT, (Object)true);
                    itemHandler.setStackInSlot(this.slotId, ghost);
                    changed = true;
                }
                if (changed) {
                    tile.setChanged();
                    player.level().sendBlockUpdated(this.pos, tile.getBlockState(), tile.getBlockState(), 3);
                }
            }
        });
    }
}

