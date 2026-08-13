/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.entity.mobs;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;

public interface SupportMob {
    @Nullable
    public LivingEntity getSupportTarget();

    public void setSupportTarget(LivingEntity var1);
}

