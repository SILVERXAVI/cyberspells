/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.arm;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class RetractableClawsItem
extends CyberwareItem {
    public RetractableClawsItem() {
        super(new CyberwareItem.Builder(10, RobosurgeonBlockEntity.SLOT_HANDS).maxInstall(4).requires(new Supplier[]{ModItems.CYBER_ARM_LEFT, ModItems.CYBER_ARM_RIGHT}));
    }

    @Override
    public void onLivingDamagePre(LivingDamageEvent.Pre event, ItemStack stack, LivingEntity wearer) {
        if (event != null && event.getSource() != null && event.getSource().getEntity() == wearer && wearer.getMainHandItem().isEmpty()) {
            float bonusDamage = 1.0f * (float)stack.getCount();
            event.setNewDamage(event.getNewDamage() + bonusDamage);
        }
    }
}

