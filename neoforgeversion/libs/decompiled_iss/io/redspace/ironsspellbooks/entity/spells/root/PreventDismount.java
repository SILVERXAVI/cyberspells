/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.spells.root;

import net.minecraft.world.entity.Entity;

public interface PreventDismount {
    default public boolean canEntityDismount(Entity entity) {
        return false;
    }
}

