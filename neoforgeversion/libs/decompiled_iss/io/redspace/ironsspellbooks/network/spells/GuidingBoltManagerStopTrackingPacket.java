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
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.spells;

import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GuidingBoltManagerStopTrackingPacket
implements CustomPacketPayload {
    private final UUID entity;
    public static final CustomPacketPayload.Type<GuidingBoltManagerStopTrackingPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"guiding_bolt_manager_stop_tracking"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GuidingBoltManagerStopTrackingPacket> STREAM_CODEC = CustomPacketPayload.codec(GuidingBoltManagerStopTrackingPacket::write, GuidingBoltManagerStopTrackingPacket::new);

    public GuidingBoltManagerStopTrackingPacket(Entity entity) {
        this.entity = entity.getUUID();
    }

    public GuidingBoltManagerStopTrackingPacket(FriendlyByteBuf buf) {
        this.entity = buf.readUUID();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.entity);
    }

    public static void handle(GuidingBoltManagerStopTrackingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> GuidingBoltManager.handleClientboundStopTracking(packet.entity));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

