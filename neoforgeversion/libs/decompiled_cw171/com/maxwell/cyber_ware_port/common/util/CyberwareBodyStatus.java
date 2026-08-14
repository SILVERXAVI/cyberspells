/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.util;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CyberwareBodyStatus {
    private final Set<BodyPartType> presentParts = EnumSet.noneOf(BodyPartType.class);
    private int armCount = 0;
    private int legCount = 0;
    private int cyberArmCount = 0;
    private int cyberLegCount = 0;

    public CyberwareBodyStatus(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); ++i) {
            BodyPartType type;
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (cw != null && (type = cw.getBodyPartType(stack)) != BodyPartType.NONE) {
                this.presentParts.add(type);
            }
            if (this.isArm(stack, cw)) {
                ++this.armCount;
                if (!this.isCybernetic(stack, cw)) continue;
                ++this.cyberArmCount;
                continue;
            }
            if (!this.isLeg(stack, cw)) continue;
            ++this.legCount;
            if (!this.isCybernetic(stack, cw)) continue;
            ++this.cyberLegCount;
        }
    }

    private boolean isCybernetic(ItemStack stack, ICyberware cw) {
        return cw != null && cw.getQuality(stack) > 0;
    }

    private boolean isArm(ItemStack stack, ICyberware cw) {
        if (stack.is((Item)ModItems.HUMAN_LEFT_ARM.get()) || stack.is((Item)ModItems.HUMAN_RIGHT_ARM.get())) {
            return true;
        }
        if (cw != null) {
            int slot = cw.getSlot(stack);
            return slot == RobosurgeonBlockEntity.SLOT_ARMS || slot == RobosurgeonBlockEntity.SLOT_ARMS + 1;
        }
        return false;
    }

    private boolean isLeg(ItemStack stack, ICyberware cw) {
        if (stack.is((Item)ModItems.HUMAN_LEFT_LEG.get()) || stack.is((Item)ModItems.HUMAN_RIGHT_LEG.get())) {
            return true;
        }
        if (cw != null) {
            int slot = cw.getSlot(stack);
            return slot == RobosurgeonBlockEntity.SLOT_LEGS || slot == RobosurgeonBlockEntity.SLOT_LEGS + 1;
        }
        return false;
    }

    public boolean hasPart(BodyPartType type) {
        return this.presentParts.contains((Object)type);
    }

    public int getArmCount() {
        return this.armCount;
    }

    public int getLegCount() {
        return this.legCount;
    }

    public int getCyberArmCount() {
        return this.cyberArmCount;
    }

    public int getCyberLegCount() {
        return this.cyberLegCount;
    }

    public boolean isHandFunctional() {
        return this.armCount >= 1;
    }
}

