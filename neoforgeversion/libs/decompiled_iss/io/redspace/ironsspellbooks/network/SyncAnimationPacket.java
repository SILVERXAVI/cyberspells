/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncAnimationPacket<T extends Entity>
implements CustomPacketPayload {
    int entityId;
    String animationId;
    public static final CustomPacketPayload.Type<SyncAnimationPacket<?>> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAnimationPacket<?>> STREAM_CODEC = CustomPacketPayload.codec(SyncAnimationPacket::write, SyncAnimationPacket::new);

    public SyncAnimationPacket(String animationId, T entity) {
        this.entityId = entity.getId();
        this.animationId = animationId;
    }

    public SyncAnimationPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.animationId = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.animationId);
    }

    public static void handle(SyncAnimationPacket<?> packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(packet.entityId);
            if (entity instanceof IAnimatedAttacker) {
                IAnimatedAttacker animatedAttacker = (IAnimatedAttacker)entity;
                animatedAttacker.playAnimation(packet.animationId);
            }
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

