/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.muscle;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class MyomerMuscleReplacementItem
extends CyberwareItem {
    public MyomerMuscleReplacementItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_MUSCLE).maxInstall(10).energy(6, 0, 0, ICyberware.StackingRule.LINEAR).incompatible(new Supplier[]{ModItems.HUMAN_MUSCLE}).bodyPart(BodyPartType.MUSCLE).addAttribute((Holder<Attribute>)Attributes.MOVEMENT_SPEED, "a1b2c3d4-e5f6-7890-1234-56789abcdef0", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttribute((Holder<Attribute>)Attributes.ATTACK_DAMAGE, "0fedcba9-8765-4321-0987-654321fedcba", 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }
}

