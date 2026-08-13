/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WispAttackGoal
extends Goal {
    private LivingEntity target;
    private PathfinderMob wisp;
    private double speedModifier;

    public WispAttackGoal(PathfinderMob wisp, double speedModifier) {
        this.wisp = wisp;
        this.speedModifier = speedModifier;
    }

    public boolean canUse() {
        LivingEntity livingentity = this.wisp.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.wisp.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        double distanceSquared = this.target.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean hasLineOfSight = this.wisp.getSensing().hasLineOfSight((Entity)this.target);
        boolean moveResult = this.wisp.getNavigation().moveTo(this.target.getX(), this.target.getY(), this.target.getZ(), this.speedModifier);
        this.wisp.getLookControl().setLookAt((Entity)this.target, 180.0f, 180.0f);
    }

    private void doAction() {
    }
}

