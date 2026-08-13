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

public class FlamethrowerParticlesPacket
implements CustomPacketPayload {
    private final Vec3 position;
    private final Vec3 forward;
    public static final CustomPacketPayload.Type<FlamethrowerParticlesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"flamethrower_particles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlamethrowerParticlesPacket> STREAM_CODEC = CustomPacketPayload.codec(FlamethrowerParticlesPacket::write, FlamethrowerParticlesPacket::new);

    public FlamethrowerParticlesPacket(Vec3 position, Vec3 forward) {
        this.position = position;
        this.forward = forward;
    }

    public FlamethrowerParticlesPacket(FriendlyByteBuf buf) {
        this.position = buf.readVec3();
        this.forward = buf.readVec3();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVec3(this.position);
        buf.writeVec3(this.forward);
    }

    public static void handle(FlamethrowerParticlesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientboundFlamethrowerParticles(packet.position, packet.forward));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

