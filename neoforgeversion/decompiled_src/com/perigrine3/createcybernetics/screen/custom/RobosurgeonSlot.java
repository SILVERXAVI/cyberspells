/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.inventory.Slot
 */
package com.perigrine3.createcybernetics.screen.custom;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class RobosurgeonSlot
extends Slot {
    private final SlotType type;

    public RobosurgeonSlot(Container container, int index, int x, int y, SlotType type) {
        super(container, index, x, y);
        this.type = type;
    }

    public SlotType getType() {
        return this.type;
    }

    public static enum SlotType {
        BRAIN,
        EYES,
        HEART,
        LUNGS,
        ORGANS,
        LARM,
        RARM,
        LLEG,
        RLEG,
        SKIN,
        MUSCLE,
        BONE;

    }
}

