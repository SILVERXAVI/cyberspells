/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.spells;

import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.world.entity.LivingEntity;

public class TargetedTargetAreaCastData
extends TargetEntityCastData {
    final TargetAreaCastData targetAreaCastData;

    public TargetedTargetAreaCastData(LivingEntity target, TargetedAreaEntity targetedAreaEntity) {
        super(target);
        this.targetAreaCastData = new TargetAreaCastData(target.position(), targetedAreaEntity);
    }

    public TargetedAreaEntity getAreaEntity() {
        return this.targetAreaCastData.getCastingEntity();
    }

    @Override
    public void reset() {
        super.reset();
        this.targetAreaCastData.reset();
    }
}

