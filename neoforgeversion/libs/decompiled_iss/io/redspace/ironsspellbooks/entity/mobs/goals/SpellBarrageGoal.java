/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class SpellBarrageGoal
extends Goal {
    protected static final int interval = 5;
    protected final PathfinderMob mob;
    protected final IMagicEntity spellCastingMob;
    protected LivingEntity target;
    protected final int attackIntervalMin;
    protected final int attackIntervalMax;
    protected final float attackRadius;
    protected final float attackRadiusSqr;
    protected final int projectileCount;
    protected final AbstractSpell spell;
    protected int attackTime;
    protected final int minSpellLevel;
    protected final int maxSpellLevel;

    public SpellBarrageGoal(IMagicEntity abstractSpellCastingMob, AbstractSpell spell, int minLevel, int maxLevel, int pAttackIntervalMin, int pAttackIntervalMax, int projectileCount) {
        PathfinderMob m;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.spellCastingMob = abstractSpellCastingMob;
        if (!(abstractSpellCastingMob instanceof PathfinderMob)) {
            throw new IllegalStateException("Unable to add " + ((Object)((Object)this)).getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
        this.mob = m = (PathfinderMob)abstractSpellCastingMob;
        this.attackIntervalMin = pAttackIntervalMin;
        this.attackIntervalMax = pAttackIntervalMax;
        this.attackRadius = 20.0f;
        this.attackRadiusSqr = this.attackRadius * this.attackRadius;
        this.minSpellLevel = minLevel;
        this.maxSpellLevel = maxLevel;
        this.projectileCount = projectileCount;
        this.spell = spell;
        this.resetAttackTimer();
    }

    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || this.spellCastingMob.isCasting()) {
            return false;
        }
        if (this.attackTime <= -5 * (this.projectileCount - 1)) {
            this.resetAttackTimer();
        }
        --this.attackTime;
        return this.attackTime <= 0 && this.attackTime % 5 == 0;
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void stop() {
        this.target = null;
        if (this.attackTime > 0) {
            this.attackTime = -this.projectileCount * 5 - 1;
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            return;
        }
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        if (distanceSquared < (double)this.attackRadiusSqr) {
            this.mob.getLookControl().setLookAt((Entity)this.target, 45.0f, 45.0f);
            this.spellCastingMob.initiateCastSpell(this.spell, this.mob.getRandom().nextIntBetweenInclusive(this.minSpellLevel, this.maxSpellLevel));
            this.stop();
        }
    }

    protected void resetAttackTimer() {
        this.attackTime = this.mob.getRandom().nextIntBetweenInclusive(this.attackIntervalMin, this.attackIntervalMax);
    }
}

