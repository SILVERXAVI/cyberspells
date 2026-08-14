/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable$Result
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.heart;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class CardiomechanicPumpItem
extends CyberwareItem {
    public CardiomechanicPumpItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_HEART).maxInstall(1).bodyPart(BodyPartType.HEART).incompatible(new Supplier[]{ModItems.HUMAN_HEART}).energy(2, 0, 0, ICyberware.StackingRule.STATIC));
    }

    @Override
    public void onPotionApplicable(MobEffectEvent.Applicable event, ItemStack stack, LivingEntity wearer) {
        int pumpCost;
        CyberwareUserData data;
        if (event.getEffectInstance().getEffect().is(MobEffects.WEAKNESS) && (data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).extractEnergy(pumpCost = 50, false) == pumpCost) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}

