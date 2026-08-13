/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WarlockAttackGoal
extends WizardAttackGoal {
    protected boolean wantsToMelee;
    protected int meleeTime;
    protected int meleeDecisionTime = this.mob.getRandom().nextIntBetweenInclusive(80, 200);
    protected float meleeBiasMin = 0.25f;
    protected float meleeBiasMax = 0.75f;
    protected float meleeMoveSpeedModifier;
    protected int meleeAttackIntervalMin;
    protected int meleeAttackIntervalMax;
    protected int meleeAttackDelay = -1;

    public WarlockAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.allowFleeing = false;
        this.meleeMoveSpeedModifier = (float)pSpeedModifier;
        this.meleeAttackIntervalMin = minAttackInterval;
        this.meleeAttackIntervalMax = maxAttackInterval;
    }

    @Override
    public void tick() {
        super.tick();
        if (++this.meleeTime > this.meleeDecisionTime) {
            this.meleeTime = 0;
            this.wantsToMelee = this.mob.getRandom().nextFloat() <= this.meleeBias();
            this.meleeDecisionTime = this.mob.getRandom().nextIntBetweenInclusive(60, 120);
        }
    }

    public float meleeRange() {
        return (float)(this.mob.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * (double)this.mob.getScale());
    }

    protected float meleeBias() {
        return Mth.clampedLerp((float)this.meleeBiasMin, (float)this.meleeBiasMax, (float)(this.mob.getHealth() / this.mob.getMaxHealth()));
    }

    @Override
    protected void doMovement(double distanceSquared) {
        if (!this.wantsToMelee) {
            super.doMovement(distanceSquared);
            return;
        }
        if (this.target.isDeadOrDying()) {
            this.mob.getNavigation().stop();
        } else {
            float meleeRange = this.meleeRange();
            this.mob.lookAt((Entity)this.target, 30.0f, 30.0f);
            float speed = (float)this.movementSpeed();
            if (distanceSquared > (double)(meleeRange * meleeRange)) {
                this.mob.setXxa(0.0f);
                if (this.mob.tickCount % 5 == 0) {
                    this.mob.getNavigation().moveTo((Entity)this.target, (double)this.meleeMoveSpeedModifier);
                }
            } else {
                this.mob.getNavigation().stop();
                float strafeForwards = 0.5f * this.meleeMoveSpeedModifier * (4.0 * distanceSquared > (double)(meleeRange * meleeRange) ? 1.5f : -1.0f);
                if (++this.strafeTime > 25 && this.mob.getRandom().nextDouble() < 0.1) {
                    this.strafingClockwise = !this.strafingClockwise;
                    this.strafeTime = 0;
                }
                float strafeDir = this.strafingClockwise ? 1.0f : -1.0f;
                this.mob.getMoveControl().strafe(strafeForwards, speed * strafeDir);
            }
            this.mob.getLookControl().setLookAt((Entity)this.target);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.meleeAttackDelay = -1;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float meleeRange = this.meleeRange();
        if (!this.wantsToMelee || distanceSquared > (double)(meleeRange * meleeRange) || this.spellCastingMob.isCasting()) {
            super.handleAttackLogic(distanceSquared);
        } else if (--this.meleeAttackDelay <= 0) {
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.doMeleeAction();
        }
    }

    protected void doMeleeAction() {
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        this.mob.doHurtTarget((Entity)this.target);
        this.resetMeleeAttackInterval(distanceSquared);
    }

    public WarlockAttackGoal setMeleeBias(float meleeBiasMin, float meleeBiasMax) {
        this.meleeBiasMin = meleeBiasMin;
        this.meleeBiasMax = meleeBiasMax;
        return this;
    }

    @Override
    public WarlockAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells, List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (WarlockAttackGoal)super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    @Override
    public WarlockAttackGoal setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        return (WarlockAttackGoal)super.setSpellQuality(minSpellQuality, maxSpellQuality);
    }

    @Override
    public WarlockAttackGoal setSingleUseSpell(AbstractSpell spellType, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (WarlockAttackGoal)super.setSingleUseSpell(spellType, minDelay, maxDelay, minLevel, maxLevel);
    }

    @Override
    public WarlockAttackGoal setIsFlying() {
        return (WarlockAttackGoal)super.setIsFlying();
    }

    public WarlockAttackGoal setMeleeMovespeedModifier(float meleeMovespeedModifier) {
        this.meleeMoveSpeedModifier = meleeMovespeedModifier;
        return this;
    }

    public WarlockAttackGoal setMeleeAttackInverval(int min, int max) {
        this.meleeAttackIntervalMax = max;
        this.meleeAttackIntervalMin = min;
        return this;
    }

    @Override
    protected double movementSpeed() {
        return this.wantsToMelee ? (double)this.meleeMoveSpeedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2.0 : super.movementSpeed();
    }

    protected void resetMeleeAttackInterval(double distanceSquared) {
        float f = (float)Math.sqrt(distanceSquared) / this.spellcastingRange;
        this.meleeAttackDelay = Math.max(1, Mth.floor((float)(f * (float)(this.meleeAttackIntervalMax - this.meleeAttackIntervalMin) + (float)this.meleeAttackIntervalMin)));
    }
}

