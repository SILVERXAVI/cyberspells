/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lung;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;

public class CompressedOxygenImplantItem
extends CyberwareItem {
    public CompressedOxygenImplantItem() {
        super(new CyberwareItem.Builder(3, RobosurgeonBlockEntity.SLOT_LUNGS).maxInstall(3).requires(new Supplier[]{ModItems.HUMAN_LUNGS}).energy(5, 0, 0, ICyberware.StackingRule.LINEAR));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (wearer.getAirSupply() < wearer.getMaxAirSupply() && wearer.tickCount % 20 == 0) {
            CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            int count = stack.getCount();
            int cost = this.getEnergyConsumption(stack) * count;
            if (data.getEnergyStored() >= cost) {
                data.extractEnergy(cost, false);
                int refillAmount = 15 * count;
                int newAir = Math.min(wearer.getAirSupply() + refillAmount, wearer.getMaxAirSupply());
                wearer.setAirSupply(newAir);
            }
        }
    }
}

