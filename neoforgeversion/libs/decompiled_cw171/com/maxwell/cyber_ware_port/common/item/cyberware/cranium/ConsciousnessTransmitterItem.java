/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.cranium;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;

public class ConsciousnessTransmitterItem
extends CyberwareItem {
    public ConsciousnessTransmitterItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_BRAIN).maxInstall(1).incompatible(new Supplier[]{ModItems.CORTICAL_STACK}));
    }
}

