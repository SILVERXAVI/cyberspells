/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 */
package com.maxwell.cyber_ware_port.common.entity;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.item.Item;

public interface ICyberwareMob {
    default public List<Item> getSpecialDrops() {
        return Collections.emptyList();
    }

    default public List<Item> getForbiddenDrops() {
        return Collections.emptyList();
    }

    default public boolean isHighTierMob() {
        return false;
    }
}

