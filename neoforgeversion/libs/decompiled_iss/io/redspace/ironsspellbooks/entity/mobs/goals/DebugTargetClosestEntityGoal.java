/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class DebugTargetClosestEntityGoal
extends TargetGoal {
    @Nullable
    protected LivingEntity target;

    public DebugTargetClosestEntityGoal(Mob pMob) {
        super(pMob, false, false);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        this.findTarget();
        return this.target != null;
    }

    protected void findTarget() {
        LivingEntity tmp = this.target;
        this.target = this.mob.level().getNearestPlayer((Entity)this.mob, 40.0);
        if (tmp != this.target) {
            IronsSpellbooks.LOGGER.debug("DebugTargetClosestEntityGoal: Target Changed old:{} new:{}", (Object)tmp, (Object)this.target);
            this.mob.setTarget(this.target);
        }
    }

    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        this.target = pTarget;
    }
}

