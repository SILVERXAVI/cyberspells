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
package com.maxwell.cyber_ware_port.common.item.cyberware.leg;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class DeployableWheelsItem
extends CyberwareItem {
    public DeployableWheelsItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_BOOTS).maxInstall(1).requires(new Supplier[]{ModItems.CYBER_LEG_RIGHT, ModItems.CYBER_LEG_LEFT}).energy(2, 0, 0, ICyberware.StackingRule.LINEAR).addAttribute((Holder<Attribute>)Attributes.STEP_HEIGHT, "deployable_wheels_step_height", 1.0, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public boolean canToggle(ItemStack stack) {
        return true;
    }
}

