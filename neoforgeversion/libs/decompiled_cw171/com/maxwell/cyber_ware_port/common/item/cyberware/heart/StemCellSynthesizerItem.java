/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.heart;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;

public class StemCellSynthesizerItem
extends CyberwareItem {
    public StemCellSynthesizerItem() {
        super(new CyberwareItem.Builder(10, RobosurgeonBlockEntity.SLOT_HEART).maxInstall(1).energy(50, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (wearer.getHealth() < wearer.getMaxHealth() && wearer.tickCount % 100 == 0) {
            CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            int cost = this.getEnergyConsumption(stack);
            if (data.getEnergyStored() >= cost) {
                data.extractEnergy(cost, false);
                wearer.heal(1.0f);
            }
        }
    }
}

