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
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

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

public class AdrenalinePumpItem
extends CyberwareItem {
    public AdrenalinePumpItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_STOMACH).maxInstall(1).energy(0, 0, 0, ICyberware.StackingRule.STATIC).eventCost(500));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        CyberwareUserData data;
        if (wearer.getHealth() < wearer.getMaxHealth() * 0.3f && !wearer.hasEffect(MobEffects.DAMAGE_BOOST) && this.tryConsumeEventEnergy(data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get()), stack)) {
            wearer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0, false, false));
            wearer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1, false, false));
        }
    }
}

