/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.muscle;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class WiredReflexesItem
extends CyberwareItem {
    public WiredReflexesItem() {
        super(new CyberwareItem.Builder(10, RobosurgeonBlockEntity.SLOT_MUSCLE).maxInstall(3).energy(2, 0, 0, ICyberware.StackingRule.LINEAR).addAttribute((Holder<Attribute>)Attributes.ATTACK_SPEED, "wired_reflexes_attack_speed", 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }

    @Override
    public void onLivingDamagePre(LivingDamageEvent.Pre event, ItemStack stack, LivingEntity wearer) {
        if (event == null) {
            return;
        }
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity)entity;
            CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            int reflexCost = 10;
            if (stack.getCount() >= 3 && data.getEnergyStored() >= reflexCost) {
                data.extractEnergy(reflexCost, false);
                wearer.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.getEyePosition());
            }
        }
    }
}

