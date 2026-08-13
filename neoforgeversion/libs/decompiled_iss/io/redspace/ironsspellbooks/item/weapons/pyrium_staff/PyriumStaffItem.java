/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item$Properties
 */
package io.redspace.ironsspellbooks.item.weapons.pyrium_staff;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.world.item.Item;

public class PyriumStaffItem
extends StaffItem {
    public PyriumStaffItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomRendering() {
        return true;
    }
}

