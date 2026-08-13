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
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.gui;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SelectSpellPacket
implements CustomPacketPayload {
    private final SpellSelection spellSelection;
    public static final CustomPacketPayload.Type<SelectSpellPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"select_spell"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SelectSpellPacket> STREAM_CODEC = CustomPacketPayload.codec(SelectSpellPacket::write, SelectSpellPacket::new);

    public SelectSpellPacket(SpellSelection spellSelection) {
        this.spellSelection = spellSelection;
    }

    public SelectSpellPacket(FriendlyByteBuf buf) {
        SpellSelection tmpSpellSelection = new SpellSelection();
        tmpSpellSelection.readFromBuffer(buf);
        this.spellSelection = tmpSpellSelection;
    }

    public void write(FriendlyByteBuf buf) {
        this.spellSelection.writeToBuffer(buf);
    }

    public static void handle(SelectSpellPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)patt0$temp;
                MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getSyncedData().setSpellSelection(packet.spellSelection);
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

