/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.cranium;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CranialBroadcasterItem
extends CyberwareItem {
    public CranialBroadcasterItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_BRAIN).maxInstall(1).energy(2, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
    }
}

