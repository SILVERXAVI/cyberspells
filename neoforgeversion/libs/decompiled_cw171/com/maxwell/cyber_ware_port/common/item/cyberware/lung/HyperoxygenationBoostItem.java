/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;

public class HyperoxygenationBoostItem
extends CyberwareItem {
    public HyperoxygenationBoostItem() {
        super(new CyberwareItem.Builder(4, RobosurgeonBlockEntity.SLOT_LUNGS).maxInstall(3).energy(2, 0, 0, ICyberware.StackingRule.LINEAR));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (wearer.isSprinting()) {
            CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            int count = stack.getCount();
            int cost = this.getEnergyConsumption(stack) * count;
            if (data.getEnergyStored() >= cost) {
                data.extractEnergy(cost, false);
                wearer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 25, count - 1, false, false, false));
            }
        }
    }

    @Override
    public boolean hasEnergyProperties(ItemStack stack) {
        return true;
    }
}

