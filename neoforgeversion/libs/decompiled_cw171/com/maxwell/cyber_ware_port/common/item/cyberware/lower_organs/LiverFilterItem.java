/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable$Result
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class LiverFilterItem
extends CyberwareItem {
    public LiverFilterItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_STOMACH).maxInstall(1));
    }

    @Override
    public boolean hasEnergyProperties(ItemStack stack) {
        return true;
    }

    @Override
    public void onPotionApplicable(MobEffectEvent.Applicable event, ItemStack stack, LivingEntity wearer) {
        int cost;
        CyberwareUserData data;
        if (((MobEffect)event.getEffectInstance().getEffect().value()).getCategory() == MobEffectCategory.HARMFUL && (data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).extractEnergy(cost = 50, false) == cost) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}

