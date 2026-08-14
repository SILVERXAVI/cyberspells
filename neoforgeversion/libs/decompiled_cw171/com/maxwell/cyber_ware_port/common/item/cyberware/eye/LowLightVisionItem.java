/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.eye;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LowLightVisionItem
extends CyberwareItem {
    public LowLightVisionItem() {
        super(new CyberwareItem.Builder(2, RobosurgeonBlockEntity.SLOT_EYES).maxInstall(1).energy(2, 0, 0, ICyberware.StackingRule.STATIC).requires(new Supplier[]{ModItems.CYBER_EYE}));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnergyConsumption(ItemStack stack) {
        return this.isActive(stack) ? super.getEnergyConsumption(stack) : 0;
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (this.isActive(stack)) {
            wearer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
        }
    }
}

