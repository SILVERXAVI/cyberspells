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
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.particles;

import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HealParticlesPacket
implements CustomPacketPayload {
    private final Vec3 pos;
    public static final CustomPacketPayload.Type<HealParticlesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"heal_particles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HealParticlesPacket> STREAM_CODEC = CustomPacketPayload.codec(HealParticlesPacket::write, HealParticlesPacket::new);

    public HealParticlesPacket(Vec3 pos) {
        this.pos = pos;
    }

    public HealParticlesPacket(FriendlyByteBuf buf) {
        this.pos = buf.readVec3();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVec3(this.pos);
    }

    public static void handle(HealParticlesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientsideHealParticles(packet.pos));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

