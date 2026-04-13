/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.attachment.AttachmentSyncHandler
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.attachment.IAttachmentHolder
 *  net.neoforged.neoforge.attachment.IAttachmentSerializer
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.common.capabilities;

import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

public final class ModMobAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create((ResourceKey)NeoForgeRegistries.Keys.ATTACHMENT_TYPES, (String)"createcybernetics");
    public static final AttachmentType<PlayerCyberwareData> CYBERENTITY_CYBERWARE = AttachmentType.builder(PlayerCyberwareData::new).serialize((IAttachmentSerializer)new IAttachmentSerializer<CompoundTag, PlayerCyberwareData>(){

        public PlayerCyberwareData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            PlayerCyberwareData data = new PlayerCyberwareData();
            data.deserializeNBT(tag, provider);
            return data;
        }

        @Nullable
        public CompoundTag write(PlayerCyberwareData data, HolderLookup.Provider provider) {
            return data.serializeNBT(provider);
        }
    }).sync((AttachmentSyncHandler)new CyberentitySyncHandler()).build();

    public static void register(IEventBus bus) {
        ATTACHMENTS.register("cyberentity_cyberware", () -> CYBERENTITY_CYBERWARE);
        ATTACHMENTS.register(bus);
    }

    private ModMobAttachments() {
    }

    private static final class CyberentitySyncHandler
    implements AttachmentSyncHandler<PlayerCyberwareData> {
        private CyberentitySyncHandler() {
        }

        public void write(RegistryFriendlyByteBuf buf, PlayerCyberwareData attachment, boolean initialSync) {
            buf.writeNbt((Tag)attachment.serializeNBT((HolderLookup.Provider)buf.registryAccess()));
        }

        @Nullable
        public PlayerCyberwareData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable PlayerCyberwareData previousValue) {
            PlayerCyberwareData out = previousValue != null ? previousValue : new PlayerCyberwareData();
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                out.deserializeNBT(tag, (HolderLookup.Provider)buf.registryAccess());
            }
            return out;
        }

        public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
            return true;
        }
    }
}

