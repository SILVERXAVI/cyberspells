/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.neoforge.attachment.IAttachmentHolder
 *  net.neoforged.neoforge.attachment.IAttachmentSerializer
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class PlayerMagicProvider
implements IAttachmentSerializer<CompoundTag, MagicData> {
    public MagicData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        MagicData magicData;
        if (holder instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)holder;
            magicData = new MagicData(serverPlayer);
        } else {
            magicData = new MagicData(true);
        }
        MagicData magicData2 = magicData;
        magicData2.loadNBTData(tag, provider);
        return magicData2;
    }

    @Nullable
    public CompoundTag write(MagicData attachment, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        attachment.saveNBTData(tag, provider);
        return tag;
    }
}

