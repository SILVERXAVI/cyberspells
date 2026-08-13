/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.UnknownNullability
 */
package io.redspace.ironsspellbooks.spells;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.UnknownNullability;

public class CastingMobAimingData
implements ICastDataSerializable {
    private Vec3 aimPosition = Vec3.ZERO;
    private Vec3 lastAimPosition = Vec3.ZERO;

    public void updateAim(Entity target, float strength) {
        Vec3 wanted = target.getBoundingBox().getCenter();
        if (this.aimPosition.equals((Object)Vec3.ZERO)) {
            this.aimPosition = wanted;
            this.lastAimPosition = wanted;
        } else {
            this.lastAimPosition = this.aimPosition;
            this.aimPosition = this.aimPosition.add(wanted.subtract(this.aimPosition).scale((double)strength));
        }
    }

    public Vec3 getAimPosition() {
        return this.aimPosition;
    }

    public Vec3 getAimPosition(float partialTick) {
        return this.lastAimPosition.add(this.aimPosition.subtract(this.lastAimPosition).scale((double)partialTick));
    }

    public Vec3 getForward(Entity host) {
        return this.aimPosition.subtract(host.getEyePosition()).normalize();
    }

    @Override
    public void reset() {
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt((int)(this.aimPosition.x * 10.0));
        buffer.writeInt((int)(this.aimPosition.y * 10.0));
        buffer.writeInt((int)(this.aimPosition.z * 10.0));
        buffer.writeInt((int)(this.lastAimPosition.x * 10.0));
        buffer.writeInt((int)(this.lastAimPosition.y * 10.0));
        buffer.writeInt((int)(this.lastAimPosition.z * 10.0));
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.aimPosition = new Vec3((double)buffer.readInt() / 10.0, (double)buffer.readInt() / 10.0, (double)buffer.readInt() / 10.0);
        this.lastAimPosition = new Vec3((double)buffer.readInt() / 10.0, (double)buffer.readInt() / 10.0, (double)buffer.readInt() / 10.0);
    }

    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return new CompoundTag();
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    }
}

