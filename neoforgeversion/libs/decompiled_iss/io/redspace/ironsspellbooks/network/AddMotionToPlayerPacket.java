/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddMotionToPlayerPacket(double x, double y, double z, boolean preserveMomentum) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<AddMotionToPlayerPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"add_motion_to_player"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AddMotionToPlayerPacket> STREAM_CODEC = CustomPacketPayload.codec(AddMotionToPlayerPacket::write, AddMotionToPlayerPacket::new);

    private AddMotionToPlayerPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeBoolean(this.preserveMomentum);
    }

    public static void handle(AddMotionToPlayerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                return true;
            }
            if (packet.preserveMomentum) {
                player.push(packet.x, packet.y, packet.z);
            } else {
                player.setDeltaMovement(packet.x, packet.y, packet.z);
            }
            return true;
        });
    }

    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull CustomPacketPayload.Type<AddMotionToPlayerPacket> type() {
        return TYPE;
    }
}

