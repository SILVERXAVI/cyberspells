/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.registries.BuiltInRegistries
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
import java.util.Objects;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShockwaveParticlesPacket
implements CustomPacketPayload {
    Vec3 pos;
    float radius;
    String particleName;
    public static final CustomPacketPayload.Type<ShockwaveParticlesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"shockwave_particles"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShockwaveParticlesPacket> STREAM_CODEC = CustomPacketPayload.codec(ShockwaveParticlesPacket::write, ShockwaveParticlesPacket::new);

    public ShockwaveParticlesPacket(Vec3 pos, float radius, ParticleType particleType) {
        this.pos = pos;
        this.radius = radius;
        this.particleName = Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey((Object)particleType)).toString();
    }

    public ShockwaveParticlesPacket(FriendlyByteBuf buf) {
        this.pos = buf.readVec3();
        this.radius = buf.readFloat();
        this.particleName = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVec3(this.pos);
        buf.writeFloat(this.radius);
        buf.writeUtf(this.particleName);
    }

    public static void handle(ShockwaveParticlesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            try {
                ParticleType type = (ParticleType)BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)packet.particleName));
                ClientSpellCastHelper.handleClientboundShockwaveParticle(packet.pos, packet.radius, type);
            }
            catch (Exception exception) {
                // empty catch block
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

