/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.Item
 */
package com.maxwell.cyber_ware_port.api.json;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class CyberwareData {
    public final Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ArrayListMultimap.create();
    public int essence = 20;
    public int slotId = 0;
    public int maxInstall = 1;
    public boolean isPristine = true;
    public ICyberware.StackingRule stackingRule = ICyberware.StackingRule.STATIC;
    public Set<Item> incompatibleItems = new HashSet<Item>();
    public Set<Item> prerequisites = new HashSet<Item>();
    public boolean hasEnergyProperties = false;
    public int energyConsumption = 0;
    public int energyGeneration = 0;
    public int energyStorage = 0;
}

