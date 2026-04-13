/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.screen.slot;

import com.perigrine3.createcybernetics.util.ModTags;
import java.util.function.BooleanSupplier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DataShardSlot
extends Slot {
    private final BooleanSupplier activeCheck;

    public DataShardSlot(Container container, int index, int x, int y, BooleanSupplier activeCheck) {
        super(container, index, x, y);
        this.activeCheck = activeCheck;
    }

    public boolean isActive() {
        return this.activeCheck.getAsBoolean();
    }

    public boolean mayPlace(ItemStack stack) {
        return this.isActive() && !stack.isEmpty() && stack.is(ModTags.Items.DATA_SHARDS);
    }

    public int getMaxStackSize() {
        return 1;
    }
}

