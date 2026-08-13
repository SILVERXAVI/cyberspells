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
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network.spells;

import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GuidingBoltManagerStartTrackingPacket
implements CustomPacketPayload {
    private final UUID entity;
    private final List<Integer> projectileIds;
    public static final CustomPacketPayload.Type<GuidingBoltManagerStartTrackingPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"guiding_bolt_manager_start_tracking"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GuidingBoltManagerStartTrackingPacket> STREAM_CODEC = CustomPacketPayload.codec(GuidingBoltManagerStartTrackingPacket::write, GuidingBoltManagerStartTrackingPacket::new);

    public GuidingBoltManagerStartTrackingPacket(Entity entity, List<Projectile> projectiles) {
        this.entity = entity.getUUID();
        this.projectileIds = projectiles.stream().map(Entity::getId).toList();
    }

    public GuidingBoltManagerStartTrackingPacket(FriendlyByteBuf buf) {
        this.projectileIds = new ArrayList<Integer>();
        this.entity = buf.readUUID();
        int i = buf.readInt();
        for (int j = 0; j < i; ++j) {
            this.projectileIds.add(buf.readInt());
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.entity);
        buf.writeInt(this.projectileIds.size());
        for (Integer projectileId : this.projectileIds) {
            buf.writeInt(projectileId.intValue());
        }
    }

    public static void handle(GuidingBoltManagerStartTrackingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> GuidingBoltManager.handleClientboundStartTracking(packet.entity, packet.projectileIds));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

