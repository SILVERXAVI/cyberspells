/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.attachment.IAttachmentSerializer
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicProvider;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create((ResourceKey)NeoForgeRegistries.Keys.ATTACHMENT_TYPES, (String)"irons_spellbooks");
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MagicData>> MAGIC_DATA = ATTACHMENT_TYPES.register("magic_data", () -> AttachmentType.builder(holder -> {
        MagicData magicData;
        if (holder instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)holder;
            magicData = new MagicData(serverPlayer);
        } else {
            magicData = new MagicData();
        }
        return magicData;
    }).serialize((IAttachmentSerializer)new PlayerMagicProvider()).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<IArmorCapeProvider.CapeData>> CAPE_DATA = ATTACHMENT_TYPES.register("cape_data", () -> AttachmentType.builder(holder -> new IArmorCapeProvider.CapeData()).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}

