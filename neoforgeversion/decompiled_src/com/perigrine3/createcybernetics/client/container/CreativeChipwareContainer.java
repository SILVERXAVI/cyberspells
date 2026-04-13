/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.client.container;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CreativeChipwareContainer
implements Container {
    private final Player player;

    public CreativeChipwareContainer(Player player) {
        this.player = player;
    }

    private PlayerCyberwareData dataOrNull() {
        if (this.player == null) {
            return null;
        }
        if (!this.player.hasData(ModAttachments.CYBERWARE)) {
            return null;
        }
        return (PlayerCyberwareData)this.player.getData(ModAttachments.CYBERWARE);
    }

    public int getContainerSize() {
        return 2;
    }

    public boolean isEmpty() {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return true;
        }
        for (int i = 0; i < this.getContainerSize(); ++i) {
            if (d.getChipwareStack(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    public ItemStack getItem(int slot) {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return ItemStack.EMPTY;
        }
        return d.getChipwareStack(slot);
    }

    public ItemStack removeItem(int slot, int amount) {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return ItemStack.EMPTY;
        }
        ItemStack cur = d.getChipwareStack(slot);
        if (cur.isEmpty()) {
            return ItemStack.EMPTY;
        }
        d.setChipwareStack(slot, ItemStack.EMPTY);
        this.setChanged();
        return cur;
    }

    public ItemStack removeItemNoUpdate(int slot) {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return ItemStack.EMPTY;
        }
        ItemStack cur = d.getChipwareStack(slot);
        if (!cur.isEmpty()) {
            d.setChipwareStack(slot, ItemStack.EMPTY);
        }
        return cur;
    }

    public void setItem(int slot, ItemStack stack) {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return;
        }
        d.setChipwareStack(slot, stack);
        this.setChanged();
    }

    public void setChanged() {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return;
        }
        d.setDirty();
        this.player.setData(ModAttachments.CYBERWARE, (Object)d);
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public void clearContent() {
        PlayerCyberwareData d = this.dataOrNull();
        if (d == null) {
            return;
        }
        d.clearChipwareInventory();
        this.setChanged();
    }
}

