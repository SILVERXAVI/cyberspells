/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.skin;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;

public class TargetedImmunosuppressantItem
extends CyberwareItem {
    public TargetedImmunosuppressantItem() {
        super(new CyberwareItem.Builder(-25, RobosurgeonBlockEntity.SLOT_SKIN).maxInstall(8).energy(3, 0, 0, ICyberware.StackingRule.LINEAR));
    }
}

