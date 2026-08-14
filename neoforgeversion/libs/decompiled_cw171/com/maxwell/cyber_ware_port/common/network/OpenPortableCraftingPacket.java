/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.CraftingMenu
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.maxwell.cyber_ware_port.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenPortableCraftingPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenPortableCraftingPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"open_portable_crafting"));
    public static final StreamCodec<FriendlyByteBuf, OpenPortableCraftingPacket> STREAM_CODEC = StreamCodec.unit((Object)new OpenPortableCraftingPacket());

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)patt0$temp;
                player.openMenu((MenuProvider)new SimpleMenuProvider((id, inv, p) -> new CraftingMenu(this, id, inv, ContainerLevelAccess.create((Level)player.level(), (BlockPos)player.blockPosition())){

                    public boolean stillValid(Player pPlayer) {
                        return true;
                    }
                }, (Component)Component.translatable((String)"container.crafting")));
            }
        });
    }
}

