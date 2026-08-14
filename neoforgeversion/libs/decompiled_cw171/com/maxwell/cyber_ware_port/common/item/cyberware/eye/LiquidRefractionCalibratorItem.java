/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.eye;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public class LiquidRefractionCalibratorItem
extends CyberwareItem {
    public LiquidRefractionCalibratorItem() {
        super(new CyberwareItem.Builder(2, RobosurgeonBlockEntity.SLOT_EYES).maxInstall(1).requires(new Supplier[]{ModItems.CYBER_EYE}));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }
}

