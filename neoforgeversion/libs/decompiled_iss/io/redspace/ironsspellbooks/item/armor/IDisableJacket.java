/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 */
package io.redspace.ironsspellbooks.item.armor;

import net.minecraft.world.entity.EquipmentSlot;

public interface IDisableJacket {
    default public boolean disableForSlot(EquipmentSlot slot) {
        return true;
    }
}

