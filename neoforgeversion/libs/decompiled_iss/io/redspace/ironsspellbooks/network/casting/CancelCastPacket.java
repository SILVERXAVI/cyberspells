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
package io.redspace.ironsspellbooks.network.casting;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CancelCastPacket
implements CustomPacketPayload {
    private final boolean triggerCooldown;
    public static final CustomPacketPayload.Type<CancelCastPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cancel_cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CancelCastPacket> STREAM_CODEC = CustomPacketPayload.codec(CancelCastPacket::write, CancelCastPacket::new);

    public CancelCastPacket(boolean triggerCooldown) {
        this.triggerCooldown = triggerCooldown;
    }

    public CancelCastPacket(FriendlyByteBuf buf) {
        this.triggerCooldown = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.triggerCooldown);
    }

    public static void cancelCast(ServerPlayer serverPlayer, boolean triggerCooldown) {
        MagicData playerMagicData;
        if (serverPlayer != null && (playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer)).isCasting()) {
            SpellData spellData = playerMagicData.getCastingSpell();
            if (triggerCooldown) {
                MagicHelper.MAGIC_MANAGER.addCooldown(serverPlayer, spellData.getSpell(), playerMagicData.getCastSource());
            }
            if (playerMagicData.getCastSource() == CastSource.SCROLL && spellData.getSpell().getCastType() == CastType.CONTINUOUS) {
                Scroll.attemptRemoveScrollAfterCast(serverPlayer);
            }
            playerMagicData.getCastingSpell().getSpell().onServerCastComplete(serverPlayer.level, spellData.getLevel(), (LivingEntity)serverPlayer, playerMagicData, true);
        }
    }

    public static void handle(CancelCastPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)patt0$temp;
                CancelCastPacket.cancelCast(serverPlayer, packet.triggerCooldown);
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

