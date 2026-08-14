/*
 * Decompiled with CFR 0.152.
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.skin;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;

public class SyntheticSkinItem
extends CyberwareItem {
    public SyntheticSkinItem() {
        super(new CyberwareItem.Builder(0, RobosurgeonBlockEntity.SLOT_SKIN).maxInstall(1));
    }
}

