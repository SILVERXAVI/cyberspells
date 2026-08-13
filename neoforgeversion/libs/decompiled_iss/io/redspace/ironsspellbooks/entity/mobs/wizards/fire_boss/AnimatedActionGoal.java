/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class AnimatedActionGoal<T extends Mob & IAnimatedAttacker>
extends Goal {
    protected int abilityTimer;
    protected int delay;
    protected boolean isUsing;
    protected final T mob;

    public AnimatedActionGoal(T mob) {
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.mob = mob;
        this.delay = this.getCooldown();
    }

    public final boolean canUse() {
        return this.delay-- <= 0 && this.canStartAction();
    }

    public boolean canContinueToUse() {
        return this.isUsing;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected abstract boolean canStartAction();

    protected abstract int getActionTimestamp();

    protected abstract int getActionDuration();

    protected abstract int getCooldown();

    protected abstract String getAnimationId();

    protected abstract void doAction();

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt((Entity)target);
        }
        if (this.abilityTimer == this.getActionTimestamp()) {
            this.doAction();
        }
        if (this.abilityTimer >= this.getActionDuration()) {
            this.stop();
        }
        ++this.abilityTimer;
    }

    public void stop() {
        this.isUsing = false;
    }

    public void start() {
        this.isUsing = true;
        this.abilityTimer = 0;
        this.delay = this.getCooldown();
        ((IAnimatedAttacker)this.mob).serverTriggerAnimation(this.getAnimationId());
    }
}

