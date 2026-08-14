/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;

public class InternalBatteryItem
extends CyberwareItem {
    public InternalBatteryItem() {
        super(new CyberwareItem.Builder(2, RobosurgeonBlockEntity.SLOT_STOMACH).maxInstall(4).energy(0, 0, 3000, ICyberware.StackingRule.LINEAR));
    }
}

