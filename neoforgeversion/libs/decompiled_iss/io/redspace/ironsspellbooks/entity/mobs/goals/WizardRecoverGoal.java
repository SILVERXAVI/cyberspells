/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WizardRecoverGoal
extends Goal {
    protected final PathfinderMob mob;
    protected final IMagicEntity spellCastingMob;
    protected final int minDelay;
    protected final int maxDelay;
    protected int delay = 15;

    public WizardRecoverGoal(IMagicEntity mob) {
        this(mob, 50, 120);
    }

    public WizardRecoverGoal(IMagicEntity mob, int minDelay, int maxDelay) {
        PathfinderMob m;
        this.spellCastingMob = mob;
        if (!(mob instanceof PathfinderMob)) {
            throw new IllegalStateException("Unable to add " + ((Object)((Object)this)).getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
        this.mob = m = (PathfinderMob)mob;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    public boolean canUse() {
        return this.mob.getTarget() == null && this.mob.getHealth() < this.mob.getMaxHealth() && !this.spellCastingMob.isDrinkingPotion() && !this.spellCastingMob.isCasting() && --this.delay <= 0;
    }

    public void start() {
        this.spellCastingMob.startDrinkingPotion();
        this.delay = this.mob.getRandom().nextIntBetweenInclusive(this.minDelay, this.maxDelay);
    }
}

