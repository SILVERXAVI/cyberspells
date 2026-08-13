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

import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncCooldownsPacket
implements CustomPacketPayload {
    private final Map<String, CooldownInstance> spellCooldowns;
    public static final CustomPacketPayload.Type<SyncCooldownsPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_cooldowns"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCooldownsPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncCooldownsPacket::write, SyncCooldownsPacket::new);

    public static String readSpellID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static CooldownInstance readCoolDownInstance(FriendlyByteBuf buffer) {
        int spellCooldown = buffer.readInt();
        int spellCooldownRemaining = buffer.readInt();
        return new CooldownInstance(spellCooldown, spellCooldownRemaining);
    }

    public static void writeSpellId(FriendlyByteBuf buf, String spellId) {
        buf.writeUtf(spellId);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, CooldownInstance cooldownInstance) {
        buf.writeInt(cooldownInstance.getSpellCooldown());
        buf.writeInt(cooldownInstance.getCooldownRemaining());
    }

    public SyncCooldownsPacket(Map<String, CooldownInstance> spellCooldowns) {
        this.spellCooldowns = spellCooldowns;
    }

    public SyncCooldownsPacket(FriendlyByteBuf buf) {
        this.spellCooldowns = buf.readMap(SyncCooldownsPacket::readSpellID, SyncCooldownsPacket::readCoolDownInstance);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.spellCooldowns, SyncCooldownsPacket::writeSpellId, SyncCooldownsPacket::writeCoolDownInstance);
    }

    public static void handle(SyncCooldownsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            PlayerCooldowns cooldowns = ClientMagicData.getCooldowns();
            cooldowns.clearCooldowns();
            packet.spellCooldowns.forEach((k, v) -> cooldowns.addCooldown((String)k, v.getSpellCooldown(), v.getCooldownRemaining()));
            ClientMagicData.resetClientCastState(null);
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

