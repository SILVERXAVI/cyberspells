/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.neoforged.neoforge.common.util.INBTSerializable
 */
package io.redspace.ironsspellbooks.gui.overlays;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SpellSelection
implements ISerializable,
INBTSerializable<CompoundTag> {
    public String equipmentSlot;
    public int index;
    public String lastEquipmentSlot;
    public int lastIndex;

    public SpellSelection() {
        this.equipmentSlot = "";
        this.index = -1;
        this.lastEquipmentSlot = "";
        this.lastIndex = -1;
    }

    public SpellSelection(String equipmentSlot, int index) {
        this.equipmentSlot = equipmentSlot;
        this.index = index;
        this.lastEquipmentSlot = "";
        this.lastIndex = -1;
    }

    public SpellSelection(String equipmentSlot, int index, String lastEquipmentSlot, int lastIndex) {
        this.equipmentSlot = equipmentSlot;
        this.index = index;
        this.lastEquipmentSlot = lastEquipmentSlot;
        this.lastIndex = lastIndex;
    }

    public boolean isEmpty() {
        return this.index < 0;
    }

    public void makeSelection(String equipmentSlot, int index) {
        if (equipmentSlot != null && index >= 0) {
            this.lastEquipmentSlot = this.equipmentSlot;
            this.lastIndex = this.index;
            this.equipmentSlot = equipmentSlot;
            this.index = index;
        }
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.equipmentSlot);
        buffer.writeInt(this.index);
        buffer.writeUtf(this.lastEquipmentSlot);
        buffer.writeInt(this.lastIndex);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.equipmentSlot = buffer.readUtf();
        this.index = buffer.readInt();
        this.lastEquipmentSlot = buffer.readUtf();
        this.lastIndex = buffer.readInt();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("slot", this.equipmentSlot);
        compoundTag.putInt("index", this.index);
        compoundTag.putString("lastSlot", this.lastEquipmentSlot);
        compoundTag.putInt("lastIndex", this.lastIndex);
        return compoundTag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        this.equipmentSlot = compoundTag.getString("slot");
        this.index = compoundTag.getInt("index");
        this.lastEquipmentSlot = compoundTag.getString("lastSlot");
        this.lastIndex = compoundTag.getInt("lastIndex");
    }

    public String toString() {
        return String.format("equipmentSlot:%s, index:%d, lastEquipmentSlot:%s, lastIndex:%d", this.equipmentSlot, this.index, this.lastEquipmentSlot, this.lastIndex);
    }
}

