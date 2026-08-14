/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Rarity
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.item.Rarity;

public class CreativeBatteryItem
extends CyberwareItem {
    public CreativeBatteryItem() {
        super(new CyberwareItem.Builder(0, RobosurgeonBlockEntity.SLOT_STOMACH).maxInstall(1).energy(0, 1000000, 2000000000, ICyberware.StackingRule.STATIC).rarity(Rarity.EPIC));
    }
}

