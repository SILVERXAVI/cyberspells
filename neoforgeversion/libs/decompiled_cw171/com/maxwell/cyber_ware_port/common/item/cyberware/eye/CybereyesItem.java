/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.eye;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CybereyesItem
extends CyberwareItem {
    public CybereyesItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_EYES).maxInstall(1).bodyPart(BodyPartType.EYES).incompatible(new Supplier[]{ModItems.HUMAN_EYES}).energy(1, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (!this.isActive(stack)) {
            return;
        }
        if (wearer.hasEffect(MobEffects.BLINDNESS)) {
            wearer.removeEffect(MobEffects.BLINDNESS);
        }
    }
}

