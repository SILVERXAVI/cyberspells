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

public class FieryExplosionParticlesPacket
implements CustomPacketPayload {
    private final Vec3 pos1;
    private final float radius;
    public static final CustomPacketPayload.Type<FieryExplosionParticlesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fiery_explosion_particles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FieryExplosionParticlesPacket> STREAM_CODEC = CustomPacketPayload.codec(FieryExplosionParticlesPacket::write, FieryExplosionParticlesPacket::new);

    public FieryExplosionParticlesPacket(Vec3 pos1, float radius) {
        this.pos1 = pos1;
        this.radius = radius;
    }

    public FieryExplosionParticlesPacket(FriendlyByteBuf buf) {
        this.pos1 = buf.readVec3();
        this.radius = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVec3(this.pos1);
        buf.writeFloat(this.radius);
    }

    public static void handle(FieryExplosionParticlesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientSpellCastHelper.handleClientboundFieryExplosion(packet.pos1, packet.radius));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

