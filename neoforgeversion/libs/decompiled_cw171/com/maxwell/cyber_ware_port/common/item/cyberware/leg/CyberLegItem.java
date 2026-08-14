/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.registries.DeferredHolder
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.leg;

import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CyberLegItem
extends CyberwareItem {
    public CyberLegItem(int slotId, DeferredHolder<Item, CyberwareItem> incompatibleHumanPart, BodyPartType bodyPartType) {
        super(new CyberwareItem.Builder(7, slotId).maxInstall(1).incompatible(new Supplier[]{incompatibleHumanPart}).bodyPart(bodyPartType).energy(0, 10, 0, ICyberware.StackingRule.STATIC));
    }
}

