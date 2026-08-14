/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.heart;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PlateletDispatcherItem
extends CyberwareItem {
    public PlateletDispatcherItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_HEART).maxInstall(1).energy(2, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        float ratio = wearer.getHealth() / wearer.getMaxHealth();
        if (ratio >= 0.8f && ratio < 1.0f && wearer.tickCount % 40 == 0) {
            wearer.heal(1.0f);
        }
    }
}

