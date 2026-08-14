/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.risk;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.risk.SurgeryAlert;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SurgeryAnalyzer {
    public static SurgeryAlert check(List<Slot> slots, int maxTolerance) {
        ICyberware cw;
        EnumSet<BodyPartType> futureParts = EnumSet.noneOf(BodyPartType.class);
        ArrayList<ItemStack> futureItems = new ArrayList<ItemStack>();
        int projectedCost = 0;
        int armCount = 0;
        int legCount = 0;
        for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS && i < slots.size(); ++i) {
            ItemStack stack = slots.get(i).getItem();
            if (stack.isEmpty() || (cw = CyberwareAPI.getCyberware(stack)) == null) continue;
            projectedCost += cw.getEssenceCost(stack) * stack.getCount();
            BodyPartType type = cw.getBodyPartType(stack);
            if (type != BodyPartType.NONE) {
                futureParts.add(type);
            }
            futureItems.add(stack);
            int targetSlot = cw.getSlot(stack);
            if (targetSlot == RobosurgeonBlockEntity.SLOT_ARMS || targetSlot == RobosurgeonBlockEntity.SLOT_ARMS + 1) {
                ++armCount;
                continue;
            }
            if (targetSlot != RobosurgeonBlockEntity.SLOT_LEGS && targetSlot != RobosurgeonBlockEntity.SLOT_LEGS + 1) continue;
            ++legCount;
        }
        for (ItemStack stack : futureItems) {
            cw = CyberwareAPI.getCyberware(stack);
            if (cw == null) continue;
            for (Item req : cw.getPrerequisites(stack)) {
                boolean found = false;
                for (ItemStack other : futureItems) {
                    if (other.getItem() != req) continue;
                    found = true;
                    break;
                }
                if (found) continue;
                return SurgeryAlert.create("cyberware.risk.missing_requirement", ChatFormatting.RED);
            }
        }
        int remainingTolerance = maxTolerance - projectedCost;
        if (!futureParts.contains((Object)BodyPartType.BRAIN)) {
            return SurgeryAlert.create("cyberware.risk.missing_brain", ChatFormatting.RED);
        }
        if (!futureParts.contains((Object)BodyPartType.HEART)) {
            return SurgeryAlert.create("cyberware.risk.missing_heart", ChatFormatting.RED);
        }
        if (!futureParts.contains((Object)BodyPartType.MUSCLE)) {
            return SurgeryAlert.create("cyberware.risk.missing_muscle", ChatFormatting.RED);
        }
        if (remainingTolerance <= 0) {
            return SurgeryAlert.create("cyberware.risk.zero_tolerance", ChatFormatting.RED);
        }
        if (!futureParts.contains((Object)BodyPartType.LUNGS)) {
            return SurgeryAlert.create("cyberware.risk.missing_lungs", ChatFormatting.GOLD);
        }
        if (legCount == 0) {
            return SurgeryAlert.create("cyberware.risk.missing_legs_both", ChatFormatting.GOLD);
        }
        if (!futureParts.contains((Object)BodyPartType.SKIN)) {
            return SurgeryAlert.create("cyberware.risk.missing_skin", ChatFormatting.YELLOW);
        }
        if (!futureParts.contains((Object)BodyPartType.BONES)) {
            return SurgeryAlert.create("cyberware.risk.missing_bones", ChatFormatting.RED);
        }
        if (!futureParts.contains((Object)BodyPartType.EYES)) {
            return SurgeryAlert.create("cyberware.risk.missing_eyes", ChatFormatting.YELLOW);
        }
        if (armCount == 0) {
            return SurgeryAlert.create("cyberware.risk.missing_arm_left", ChatFormatting.YELLOW);
        }
        if (armCount == 1) {
            return SurgeryAlert.create("cyberware.risk.missing_arm_right", ChatFormatting.YELLOW);
        }
        if (legCount == 1) {
            return SurgeryAlert.create("cyberware.risk.missing_leg_single", ChatFormatting.YELLOW);
        }
        if (!futureParts.contains((Object)BodyPartType.STOMACH)) {
            return SurgeryAlert.create("cyberware.risk.missing_stomach", ChatFormatting.YELLOW);
        }
        int dangerThreshold = (int)((float)maxTolerance * 0.25f);
        if (remainingTolerance < dangerThreshold) {
            return SurgeryAlert.create("cyberware.risk.low_tolerance", ChatFormatting.YELLOW);
        }
        return null;
    }
}

