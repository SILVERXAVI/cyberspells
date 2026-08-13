/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells;

import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.spells.EntityCastData;
import net.minecraft.world.phys.Vec3;

public class TargetAreaCastData
extends EntityCastData {
    Vec3 center;

    public TargetAreaCastData(Vec3 center, TargetedAreaEntity entity) {
        super(entity);
        this.center = center;
    }

    public Vec3 getCenter() {
        return this.center;
    }

    @Override
    public TargetedAreaEntity getCastingEntity() {
        return (TargetedAreaEntity)super.getCastingEntity();
    }
}

