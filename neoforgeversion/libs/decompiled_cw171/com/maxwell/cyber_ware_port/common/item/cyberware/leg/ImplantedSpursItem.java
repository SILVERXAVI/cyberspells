/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.leg;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ImplantedSpursItem
extends CyberwareItem {
    public ImplantedSpursItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_BOOTS).maxInstall(4).addAttribute((Holder<Attribute>)Attributes.MOVEMENT_SPEED, "c0a9b8e0-1234-4567-89ab-cdef01234567", 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
}

