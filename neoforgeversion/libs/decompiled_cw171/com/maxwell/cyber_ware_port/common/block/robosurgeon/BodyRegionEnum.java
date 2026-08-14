/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.block.robosurgeon;

public enum BodyRegionEnum {
    EYES,
    BRAIN,
    HEART,
    LUNGS,
    STOMACH,
    SKIN,
    MUSCLE,
    BONES,
    ARMS,
    HANDS,
    LEGS,
    BOOTS;

    public static final int SLOTS_PER_PART = 9;

    public static int getTotalSlots() {
        return BodyRegionEnum.values().length * 9;
    }

    public int getStartSlot() {
        return this.ordinal() * 9;
    }
}

