/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Tick
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.arm;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

public class RapidFireFlywheelItem
extends CyberwareItem {
    public RapidFireFlywheelItem() {
        super(new CyberwareItem.Builder(8, RobosurgeonBlockEntity.SLOT_ARMS).maxInstall(1));
    }

    @Override
    public void onItemUseTick(LivingEntityUseItemEvent.Tick event, ItemStack stack, LivingEntity wearer) {
        int costPerTick;
        CyberwareUserData userData;
        if (event.getItem().getItem() instanceof BowItem && (userData = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).extractEnergy(costPerTick = 2, false) == costPerTick) {
            event.setDuration(event.getDuration() - 1);
        }
    }
}

