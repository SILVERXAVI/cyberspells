/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 */
package com.maxwell.cyber_ware_port.common.item.base;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum CyberwareSlotType {
    EYES("cyberware_slot.cyber_ware_port.eyes"),
    BRAIN("cyberware_slot.cyber_ware_port.brain"),
    HEART("cyberware_slot.cyber_ware_port.heart"),
    LUNGS("cyberware_slot.cyber_ware_port.lungs"),
    STOMACH("cyberware_slot.cyber_ware_port.stomach"),
    SKIN("cyberware_slot.cyber_ware_port.skin"),
    MUSCLE("cyberware_slot.cyber_ware_port.muscle"),
    BONES("cyberware_slot.cyber_ware_port.bones"),
    ARMS("cyberware_slot.cyber_ware_port.arms"),
    HANDS("cyberware_slot.cyber_ware_port.hands"),
    LEGS("cyberware_slot.cyber_ware_port.legs"),
    BOOTS("cyberware_slot.cyber_ware_port.boots"),
    UNKNOWN("cyberware_slot.cyber_ware_port.unknown");

    private final String translationKey;

    private CyberwareSlotType(String translationKey) {
        this.translationKey = translationKey;
    }

    public static CyberwareSlotType fromId(int id) {
        if (id < 0) {
            return UNKNOWN;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_EYES)) {
            return EYES;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_BRAIN)) {
            return BRAIN;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_HEART)) {
            return HEART;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_LUNGS)) {
            return LUNGS;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_STOMACH)) {
            return STOMACH;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_SKIN)) {
            return SKIN;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_MUSCLE)) {
            return MUSCLE;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_BONES)) {
            return BONES;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_ARMS)) {
            return ARMS;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_HANDS)) {
            return HANDS;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_LEGS)) {
            return LEGS;
        }
        if (CyberwareSlotType.isInRange(id, RobosurgeonBlockEntity.SLOT_BOOTS)) {
            return BOOTS;
        }
        return UNKNOWN;
    }

    private static boolean isInRange(int id, int startId) {
        return id >= startId && id < startId + 9;
    }

    public MutableComponent getDisplayName() {
        return Component.translatable((String)this.translationKey);
    }
}

