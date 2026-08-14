/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.block.robosurgeon.surgeon;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.surgeon.SurgeryManager;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SurgerySyncHelper {
    public static boolean updateGhosts(ItemStackHandler body, IItemHandlerModifiable table) {
        boolean changed = false;
        for (int i = 0; i < table.getSlots(); ++i) {
            ItemStack b = body.getStackInSlot(i);
            ItemStack t = table.getStackInSlot(i);
            if (SurgeryManager.isGhost(t)) {
                if (b.isEmpty()) {
                    table.setStackInSlot(i, ItemStack.EMPTY);
                    changed = true;
                    continue;
                }
                ItemStack ghost = SurgerySyncHelper.createGhost(b);
                if (ItemStack.matches((ItemStack)t, (ItemStack)ghost)) continue;
                table.setStackInSlot(i, ghost);
                changed = true;
                continue;
            }
            if (t.isEmpty() && !b.isEmpty()) {
                table.setStackInSlot(i, SurgerySyncHelper.createGhost(b));
                changed = true;
                continue;
            }
            if (t.isEmpty() || b.isEmpty() || !ItemStack.matches((ItemStack)t, (ItemStack)b)) continue;
            table.setStackInSlot(i, SurgerySyncHelper.createGhost(b));
            changed = true;
        }
        return changed;
    }

    private static ItemStack createGhost(ItemStack stack) {
        ItemStack ghost = stack.copy();
        ghost.set((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)true);
        return ghost;
    }
}

