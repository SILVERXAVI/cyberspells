/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class MultiTargetEntityCastData
implements ICastDataSerializable {
    private List<UUID> targetUUIDs = new ArrayList<UUID>();

    public MultiTargetEntityCastData(Entity ... targets) {
        Arrays.stream(targets).forEach(target -> this.targetUUIDs.add(target.getUUID()));
    }

    @Override
    public void reset() {
        this.targetUUIDs.clear();
    }

    public List<UUID> getTargets() {
        return this.targetUUIDs;
    }

    public void addTarget(Entity entity) {
        this.targetUUIDs.add(entity.getUUID());
    }

    public void addTarget(UUID uuid) {
        this.targetUUIDs.add(uuid);
    }

    public boolean isTargeted(Entity entity) {
        return this.targetUUIDs.contains(entity.getUUID());
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.targetUUIDs.size());
        this.targetUUIDs.forEach(arg_0 -> ((FriendlyByteBuf)buffer).writeUUID(arg_0));
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.targetUUIDs = new ArrayList<UUID>();
        int i = buffer.readInt();
        for (int j = 0; j < i; ++j) {
            this.targetUUIDs.add(buffer.readUUID());
        }
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag uuids = new ListTag();
        this.targetUUIDs.stream().map(NbtUtils::createUUID).forEach(arg_0 -> uuids.add(arg_0));
        tag.put("targets", (Tag)uuids);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.targetUUIDs = new ArrayList<UUID>();
        ListTag listTag = nbt.getList("targets", 11);
        listTag.stream().map(NbtUtils::loadUUID).forEach(this.targetUUIDs::add);
    }
}

