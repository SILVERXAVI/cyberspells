/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class MomentHurtByTargetGoal
extends HurtByTargetGoal {
    int forcedAggroTime;
    float intensity;
    boolean isOutnumbered;

    public MomentHurtByTargetGoal(PathfinderMob pMob, Class<?> ... pToIgnoreDamage) {
        super(pMob, (Class[])pToIgnoreDamage);
    }

    public void tick() {
        super.tick();
        if (this.timestamp != this.mob.getLastHurtByMobTimestamp()) {
            this.timestamp = this.mob.getLastHurtByMobTimestamp();
            if (this.mob.getLastHurtByMob() != null && this.mob.getLastHurtByMob() != this.targetMob) {
                this.isOutnumbered = true;
                this.forcedAggroTime -= 20;
                this.intensity *= 0.8f;
            } else if (this.isOutnumbered) {
                this.forcedAggroTime += (int)(20.0f * this.intensity);
            }
        }
    }

    public void start() {
        super.start();
        this.forcedAggroTime = 40 + this.mob.getRandom().nextInt(80) + this.mob.getRandom().nextInt(80);
        this.intensity = 1.0f;
        this.isOutnumbered = false;
    }

    public boolean canContinueToUse() {
        return (!this.isOutnumbered || --this.forcedAggroTime > 0) && super.canContinueToUse();
    }
}

