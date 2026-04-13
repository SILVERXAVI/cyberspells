/*
 * Decompiled with CFR 0.152.
 */
package com.perigrine3.createcybernetics.common.surgery;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import java.util.EnumMap;
import java.util.Map;

public final class RobosurgeonSlotMap {
    private static final EnumMap<CyberwareSlot, int[]> SLOT_MAP = new EnumMap(CyberwareSlot.class);

    private RobosurgeonSlotMap() {
    }

    private static int[] range(int start, int count) {
        int[] out = new int[count];
        for (int i = 0; i < count; ++i) {
            out[i] = start + i;
        }
        return out;
    }

    public static int toInventoryIndex(CyberwareSlot slot, int index) {
        int[] arr = SLOT_MAP.get((Object)slot);
        if (arr == null) {
            return -1;
        }
        if (index < 0 || index >= arr.length) {
            return -1;
        }
        return arr[index];
    }

    public static CyberwareSlot getSlotForIndex(int inventoryIndex) {
        for (Map.Entry<CyberwareSlot, int[]> entry : SLOT_MAP.entrySet()) {
            for (int i : entry.getValue()) {
                if (i != inventoryIndex) continue;
                return entry.getKey();
            }
        }
        return null;
    }

    public static int mappedSize(CyberwareSlot slot) {
        int[] arr = SLOT_MAP.get((Object)slot);
        return arr == null ? 0 : arr.length;
    }

    public static Map<CyberwareSlot, int[]> getMapping() {
        return SLOT_MAP;
    }

    static {
        SLOT_MAP.put(CyberwareSlot.BRAIN, RobosurgeonSlotMap.range(0, 5));
        SLOT_MAP.put(CyberwareSlot.EYES, RobosurgeonSlotMap.range(5, 5));
        SLOT_MAP.put(CyberwareSlot.HEART, RobosurgeonSlotMap.range(10, 6));
        SLOT_MAP.put(CyberwareSlot.LUNGS, RobosurgeonSlotMap.range(16, 6));
        SLOT_MAP.put(CyberwareSlot.ORGANS, RobosurgeonSlotMap.range(22, 6));
        SLOT_MAP.put(CyberwareSlot.RARM, RobosurgeonSlotMap.range(28, 6));
        SLOT_MAP.put(CyberwareSlot.LARM, RobosurgeonSlotMap.range(34, 6));
        SLOT_MAP.put(CyberwareSlot.RLEG, RobosurgeonSlotMap.range(40, 5));
        SLOT_MAP.put(CyberwareSlot.LLEG, RobosurgeonSlotMap.range(45, 5));
        SLOT_MAP.put(CyberwareSlot.MUSCLE, RobosurgeonSlotMap.range(50, 5));
        SLOT_MAP.put(CyberwareSlot.BONE, RobosurgeonSlotMap.range(55, 5));
        SLOT_MAP.put(CyberwareSlot.SKIN, RobosurgeonSlotMap.range(60, 5));
    }
}

