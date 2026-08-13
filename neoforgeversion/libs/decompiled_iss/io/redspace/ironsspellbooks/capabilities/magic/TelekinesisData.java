/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.world.entity.LivingEntity;

public class TelekinesisData
extends TargetEntityCastData {
    private float distance;
    private final int minDistance;

    public TelekinesisData(float distance, LivingEntity target, int minDistance) {
        super(target);
        this.distance = distance;
        this.minDistance = minDistance;
    }

    public float getDistance() {
        return Math.max(this.distance, (float)this.minDistance);
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}

