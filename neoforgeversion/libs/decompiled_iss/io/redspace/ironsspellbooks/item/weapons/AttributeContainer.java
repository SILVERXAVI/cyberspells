/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeContainer(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
    public AttributeModifier createModifier(String slot) {
        String attributeName = ResourceLocation.parse((String)this.attribute.getRegisteredName()).getPath();
        return new AttributeModifier(IronsSpellbooks.id(String.format("%s_%s_modifier", slot, attributeName)), this.value, this.operation);
    }
}

