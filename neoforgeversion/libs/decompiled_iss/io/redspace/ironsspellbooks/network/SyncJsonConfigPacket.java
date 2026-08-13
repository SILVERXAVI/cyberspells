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
package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncJsonConfigPacket
implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncJsonConfigPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sync_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncJsonConfigPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncJsonConfigPacket::toBytes, SyncJsonConfigPacket::new);
    public final Map<ResourceLocation, byte[]> data;

    public SyncJsonConfigPacket(Map<ResourceLocation, byte[]> bytes) {
        this.data = bytes;
    }

    public SyncJsonConfigPacket(FriendlyByteBuf buf) {
        this.data = new HashMap<ResourceLocation, byte[]>();
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            ResourceLocation id = buf.readResourceLocation();
            byte[] bytes = new byte[buf.readInt()];
            buf.readBytes(bytes, 0, bytes.length);
            this.data.put(id, bytes);
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.data.size());
        for (Map.Entry<ResourceLocation, byte[]> entry : this.data.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeInt(entry.getValue().length);
            buf.writeBytes(entry.getValue());
        }
    }

    public static void handle(SyncJsonConfigPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            for (AbstractSpell spell : SpellRegistry.REGISTRY) {
                spell.resetRarityWeights();
            }
            SpellConfigManager.INSTANCE.handleClientSync(packet);
        });
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

