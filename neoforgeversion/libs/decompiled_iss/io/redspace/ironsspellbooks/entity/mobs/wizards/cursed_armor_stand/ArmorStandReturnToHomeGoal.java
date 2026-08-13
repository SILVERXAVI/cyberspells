/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand;

import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ArmorStandReturnToHomeGoal
extends WaterAvoidingRandomStrollGoal {
    CursedArmorStandEntity mob;
    int stuckTimer;
    private static final int MAX_INTERVAL = 10;
    private static final float CLOSE_DISTANCE = 2.0f;
    boolean closingFinalDistance;
    Vec3 lastStuckPos = Vec3.ZERO;
    int stuckCounter;
    private static final double ARRIVED_THRESHOLD = 0.1;
    private static final double ATHRESHOLD_SQR = 0.010000000000000002;

    public ArmorStandReturnToHomeGoal(CursedArmorStandEntity pMob, double pSpeedModifier) {
        super((PathfinderMob)pMob, pSpeedModifier);
        this.mob = pMob;
        this.interval = 10;
    }

    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        }
        if (this.mob.isArmorStandFrozen()) {
            return false;
        }
        Vec3 vec3 = this.getPosition();
        if (vec3 == null) {
            return false;
        }
        this.wantedX = vec3.x;
        this.wantedY = vec3.y;
        this.wantedZ = vec3.z;
        this.forceTrigger = false;
        return true;
    }

    public boolean canContinueToUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        }
        if (this.mob.getNavigation().isDone()) {
            double distance = this.homeDistanceSqr();
            if (distance <= 0.010000000000000002) {
                return false;
            }
            if (distance <= 4.0) {
                this.closingFinalDistance = true;
                return true;
            }
            return false;
        }
        return true;
    }

    public void tick() {
        if (this.mob.tickCount % 200 == 0) {
            Vec3 currpos = this.mob.position();
            double distance = this.lastStuckPos.distanceToSqr(currpos);
            if (distance < 25.0) {
                ++this.stuckCounter;
            } else {
                this.lastStuckPos = currpos;
                this.stuckCounter = 0;
            }
            if (this.stuckCounter > 2) {
                this.mob.spawn = this.mob.position();
                this.stop();
                return;
            }
        }
        if (this.closingFinalDistance && this.mob.getNavigation().isDone() && this.mob.spawn != null) {
            Vec3 delta = this.mob.spawn.subtract(this.mob.position());
            double currDistance = delta.lengthSqr();
            double d0 = this.mob.spawn.x - this.mob.getX();
            double d1 = this.mob.spawn.z - this.mob.getZ();
            float f = (float)(Mth.atan2((double)d1, (double)d0) * 180.0 / 3.1415927410125732) - 90.0f;
            this.mob.setYRot(f);
            this.mob.setYBodyRot(f);
            this.mob.getMoveControl().strafe((float)this.speedModifier, 0.0f);
            if (currDistance > 4.0) {
                this.closingFinalDistance = false;
            } else if (currDistance < 0.010000000000000002) {
                this.stop();
            }
        } else {
            super.tick();
        }
    }

    public void start() {
        this.mob.setXxa(0.0f);
        super.start();
        this.stuckTimer = 0;
        this.interval = 10;
    }

    public void stop() {
        super.stop();
        this.closingFinalDistance = false;
        if (this.homeDistanceSqr() <= 0.010000000000000002) {
            this.mob.setArmorStandFrozen(true);
        }
    }

    private double homeDistanceSqr() {
        return this.mob.spawn == null ? 0.0 : this.mob.distanceToSqr(this.mob.spawn);
    }

    @Nullable
    protected Vec3 getPosition() {
        return this.mob.spawn;
    }
}

