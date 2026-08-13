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
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SummonedEntitiesCastData
implements ICastDataSerializable {
    protected Set<UUID> summons = new HashSet<UUID>();
    protected float maxHealthPool;

    public void add(Entity entity) {
        this.summons.add(entity.getUUID());
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            this.maxHealthPool += livingEntity.getMaxHealth();
        }
    }

    public void handleRemove(UUID uuid, MagicData ownerData, RecastInstance recastInstance) {
        this.summons.remove(uuid);
        if (this.summons.isEmpty()) {
            ownerData.getPlayerRecasts().removeRecast(recastInstance, RecastResult.USED_ALL_RECASTS);
        }
    }

    @Override
    public void reset() {
    }

    public float getMaxHealthPool() {
        return this.maxHealthPool;
    }

    public Set<UUID> getSummons() {
        return this.summons;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.summons.size());
        for (UUID uuid : this.summons) {
            buffer.writeUUID(uuid);
        }
        buffer.writeFloat(this.maxHealthPool);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        int i = buffer.readInt();
        for (int j = 0; j < i; ++j) {
            this.summons.add(buffer.readUUID());
        }
        this.maxHealthPool = buffer.readFloat();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        this.summons.forEach(uuid -> list.add((Object)NbtUtils.createUUID((UUID)uuid)));
        tag.put("summons", (Tag)list);
        tag.putFloat("maxHealthPool", this.maxHealthPool);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ListTag list = nbt.getList("summons", 11);
        list.forEach(tag -> this.summons.add(NbtUtils.loadUUID((Tag)tag)));
        this.maxHealthPool = nbt.getFloat("maxHealthPool");
    }
}

