/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.block.robosurgeon.surgeon;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SurgeryManager {
    public static void execute(ServerPlayer player, IItemHandlerModifiable table, ItemStackHandler body) {
        ArrayList<ItemStack> ejectList = new ArrayList<ItemStack>();
        for (int i = 0; i < table.getSlots(); ++i) {
            ItemStack tableStack = table.getStackInSlot(i);
            if (SurgeryManager.isGhost(tableStack)) continue;
            ItemStack oldPart = body.getStackInSlot(i);
            if (!oldPart.isEmpty()) {
                ejectList.add(oldPart.copy());
            }
            ItemStack insertedDeducted = table.extractItem(i, 64, false);
            body.setStackInSlot(i, insertedDeducted);
        }
        SurgeryManager.resolveConflicts(body, ejectList);
        for (ItemStack stack : ejectList) {
            ItemEntity itemEntity;
            if (stack.isEmpty() || player.getInventory().add(stack) || (itemEntity = player.drop(stack, false)) == null) continue;
            itemEntity.setNoPickUpDelay();
            itemEntity.setUnlimitedLifetime();
        }
    }

    private static void resolveConflicts(ItemStackHandler body, List<ItemStack> ejectList) {
        block0: for (int i = 0; i < body.getSlots(); ++i) {
            ItemStack s1 = body.getStackInSlot(i);
            ICyberware cw1 = CyberwareAPI.getCyberware(s1);
            if (cw1 == null) continue;
            for (int j = i + 1; j < body.getSlots(); ++j) {
                int q2;
                ItemStack s2 = body.getStackInSlot(j);
                ICyberware cw2 = CyberwareAPI.getCyberware(s2);
                if (cw2 == null) continue;
                boolean conflict = false;
                if (cw1.getBodyPartType(s1) != BodyPartType.NONE && cw1.getBodyPartType(s1) == cw2.getBodyPartType(s2)) {
                    conflict = true;
                }
                if (!conflict && (cw1.isIncompatible(s1, s2) || cw2.isIncompatible(s2, s1))) {
                    conflict = true;
                }
                if (!conflict) continue;
                int q1 = cw1.getQuality(s1);
                int loserIndex = q1 > (q2 = cw2.getQuality(s2)) ? j : (q2 > q1 ? i : j);
                ItemStack loserStack = body.getStackInSlot(loserIndex);
                ejectList.add(loserStack.copy());
                body.setStackInSlot(loserIndex, ItemStack.EMPTY);
                if (loserIndex == i) continue block0;
            }
        }
    }

    public static boolean isGhost(ItemStack s) {
        return !s.isEmpty() && (Boolean)s.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false) != false;
    }
}

