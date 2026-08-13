/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SerializedTargetData
implements ICastDataSerializable {
    protected UUID targetUUID;

    public SerializedTargetData(Entity target) {
        this.targetUUID = target.getUUID();
    }

    public SerializedTargetData() {
        this.targetUUID = null;
    }

    @Override
    public void reset() {
    }

    @Nullable
    public Entity getTarget(ServerLevel level) {
        return level.getEntity(this.targetUUID);
    }

    public UUID getTargetUUID() {
        return this.targetUUID;
    }

    @Nullable
    public Vec3 getTargetPosition(ServerLevel level) {
        Entity target = this.getTarget(level);
        return target == null ? null : target.position();
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.targetUUID);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.targetUUID = buffer.readUUID();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("target", this.targetUUID);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.targetUUID = nbt.getUUID("target");
    }
}

