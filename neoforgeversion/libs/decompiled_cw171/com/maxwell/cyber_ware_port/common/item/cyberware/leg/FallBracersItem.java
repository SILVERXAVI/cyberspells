/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.event.entity.living.LivingFallEvent
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.leg;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

public class FallBracersItem
extends CyberwareItem {
    public FallBracersItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_LEGS).requires(new Supplier[]{ModItems.CYBER_LEG_RIGHT, ModItems.CYBER_LEG_LEFT}).maxInstall(1));
    }

    @Override
    public void onLivingFall(LivingFallEvent event, ItemStack stack, LivingEntity wearer) {
        float reduction = 4.0f;
        event.setDistance(Math.max(0.0f, event.getDistance() - reduction));
    }
}

