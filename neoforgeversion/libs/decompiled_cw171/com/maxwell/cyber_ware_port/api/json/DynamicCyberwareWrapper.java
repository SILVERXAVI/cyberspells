/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.api.json;

import com.google.common.collect.Multimap;
import com.maxwell.cyber_ware_port.api.json.CyberwareData;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DynamicCyberwareWrapper
implements ICyberware {
    private final CyberwareData data;

    public DynamicCyberwareWrapper(CyberwareData data) {
        this.data = data;
    }

    @Override
    public int getEssenceCost(ItemStack stack) {
        return this.data.essence;
    }

    @Override
    public int getSlot(ItemStack stack) {
        return this.data.slotId;
    }

    @Override
    public boolean isPristine(ItemStack stack) {
        return this.data.isPristine;
    }

    @Override
    public void setPristine(ItemStack stack, boolean isPristine) {
    }

    @Override
    public int getMaxInstallAmount(ItemStack stack) {
        return this.data.maxInstall;
    }

    @Override
    public Set<Item> getPrerequisites(ItemStack stack) {
        return this.data.prerequisites;
    }

    @Override
    public Set<Item> getIncompatibleItems(ItemStack stack) {
        return this.data.incompatibleItems;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return this.data.attributeModifiers;
    }

    @Override
    public boolean hasEnergyProperties(ItemStack stack) {
        return this.data.hasEnergyProperties;
    }

    @Override
    public ICyberware.StackingRule getStackingEnergyRule(ItemStack stack) {
        return this.data.stackingRule;
    }

    @Override
    public int getEnergyConsumption(ItemStack stack) {
        int base = this.data.energyConsumption;
        return this.isPristine(stack) ? base : base * 2;
    }

    @Override
    public int getEnergyGeneration(ItemStack stack) {
        int base = this.data.energyGeneration;
        return this.isPristine(stack) ? base : base / 2;
    }

    @Override
    public int getEnergyStorage(ItemStack stack) {
        int base = this.data.energyStorage;
        return this.isPristine(stack) ? base : base / 2;
    }
}

