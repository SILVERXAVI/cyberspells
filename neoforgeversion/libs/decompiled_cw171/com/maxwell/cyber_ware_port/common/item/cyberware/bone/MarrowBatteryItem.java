/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.bone;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;

public class MarrowBatteryItem
extends CyberwareItem {
    public MarrowBatteryItem() {
        super(new CyberwareItem.Builder(2, RobosurgeonBlockEntity.SLOT_BONES).maxInstall(1).energy(0, 0, 1000, ICyberware.StackingRule.STATIC));
    }
}

