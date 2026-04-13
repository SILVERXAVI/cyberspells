/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.RegistryFriendlyByteBuf
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
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.payload;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.cyberware.CraftingHandsItem;
import com.perigrine3.createcybernetics.screen.custom.ExpandedInventoryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenExpandedInventoryPayload() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenExpandedInventoryPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"open_expanded_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenExpandedInventoryPayload> STREAM_CODEC = StreamCodec.of((buf, payload) -> {}, buf -> new OpenExpandedInventoryPayload());

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenExpandedInventoryPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            if (sp.isCreative() || sp.isSpectator()) {
                return;
            }
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (!CraftingHandsItem.hasInstalledEitherArm(data)) {
                return;
            }
            sp.openMenu((MenuProvider)new SimpleMenuProvider((id, inv, player) -> new ExpandedInventoryMenu(id, inv, ContainerLevelAccess.create((Level)player.level(), (BlockPos)player.blockPosition())), (Component)Component.translatable((String)"container.inventory")));
        });
    }
}

