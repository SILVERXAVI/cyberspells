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
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LearnSpellPacket
implements CustomPacketPayload {
    private final byte hand;
    private final String spell;
    public static final CustomPacketPayload.Type<LearnSpellPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"learn_spell"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LearnSpellPacket> STREAM_CODEC = CustomPacketPayload.codec(LearnSpellPacket::write, LearnSpellPacket::new);

    public LearnSpellPacket(InteractionHand interactionHand, String spell) {
        this.hand = LearnSpellPacket.handToByte(interactionHand);
        this.spell = spell;
    }

    public LearnSpellPacket(FriendlyByteBuf buf) {
        this.hand = buf.readByte();
        this.spell = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.hand);
        buf.writeUtf(this.spell);
    }

    public static void handle(LearnSpellPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)patt0$temp;
                ItemStack itemStack = serverPlayer.getItemInHand(LearnSpellPacket.byteToHand(packet.hand));
                AbstractSpell spell = SpellRegistry.getSpell(packet.spell);
                SyncedSpellData data = MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getSyncedData();
                if (spell != SpellRegistry.none() && !data.isSpellLearned(spell) && itemStack.is((Item)ItemRegistry.ELDRITCH_PAGE.get()) && itemStack.getCount() > 0) {
                    data.learnSpell(spell);
                    if (!serverPlayer.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }
            }
        });
    }

    public static byte handToByte(InteractionHand hand) {
        return (byte)(hand == InteractionHand.MAIN_HAND ? 1 : 0);
    }

    public static InteractionHand byteToHand(byte b) {
        return b > 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

