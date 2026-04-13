/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.api;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class InstalledCyberware {
    private ItemStack item = ItemStack.EMPTY;
    private CyberwareSlot slot = null;
    private int index = -1;
    private int humanityCost = 0;
    private boolean powered = true;

    public InstalledCyberware() {
    }

    public InstalledCyberware(ItemStack item, CyberwareSlot slot, int index, int humanityCost) {
        this.item = item.copy();
        this.slot = slot;
        this.index = index;
        this.humanityCost = humanityCost;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public CyberwareSlot getSlot() {
        return this.slot;
    }

    public int getIndex() {
        return this.index;
    }

    public int getHumanityCost() {
        return this.humanityCost;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (!this.item.isEmpty()) {
            tag.put("Item", this.item.save(provider));
        }
        if (this.slot != null) {
            tag.putString("Slot", this.slot.name());
            tag.putInt("Index", this.index);
        }
        tag.putInt("Humanity", this.humanityCost);
        tag.putBoolean("Powered", this.powered);
        return tag;
    }

    public static InstalledCyberware load(CompoundTag tag, HolderLookup.Provider provider) {
        InstalledCyberware c = new InstalledCyberware();
        c.item = tag.contains("Item", 10) ? ItemStack.parse((HolderLookup.Provider)provider, (Tag)tag.getCompound("Item")).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
        if (tag.contains("Slot", 8)) {
            c.slot = CyberwareSlot.valueOf(tag.getString("Slot"));
            c.index = tag.getInt("Index");
        } else {
            c.slot = null;
            c.index = -1;
        }
        c.humanityCost = tag.getInt("Humanity");
        c.powered = tag.getBoolean("Powered");
        return c;
    }
}

