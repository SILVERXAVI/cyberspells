/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class FireBossMoveControl
extends MoveControl {
    int customMovementTimer;
    int customMovementDuration;
    Function<Float, Vec3> currentCustomMovementControl;

    public FireBossMoveControl(Mob pMob) {
        super(pMob);
    }

    public void tick() {
        if (this.customMovementTimer > 0) {
            --this.customMovementTimer;
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                float f = Math.clamp((float)((float)this.customMovementTimer / (float)this.customMovementDuration), (float)0.0f, (float)1.0f);
                Vec3 movement = this.currentCustomMovementControl.apply(Float.valueOf(f)).scale(this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float angle = -Utils.getAngle(this.mob.getX(), this.mob.getZ(), target.getX(), target.getZ()) - 1.5707964f;
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(movement.yRot(angle).scale((double)(f * f))));
                float slowdownRange = (float)this.mob.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * this.mob.getScale() * 0.9f;
                if (this.mob.distanceToSqr((Entity)target) < (double)(slowdownRange * slowdownRange)) {
                    this.customMovementTimer -= 2;
                }
            } else {
                this.customMovementTimer = 0;
            }
        } else {
            super.tick();
        }
    }

    protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
        double d1;
        double d0 = this.wantedX - this.mob.getX();
        if (d0 * d0 + (d1 = this.wantedZ - this.mob.getZ()) * d1 < 0.5) {
            return pSourceAngle;
        }
        return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * 0.25f);
    }

    public void triggerCustomMovement(int duration, Function<Float, Vec3> progressToMovement) {
        this.currentCustomMovementControl = progressToMovement;
        this.customMovementTimer = duration;
        this.customMovementDuration = duration;
    }
}

