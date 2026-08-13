/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WizardSupportGoal<T extends PathfinderMob & IMagicEntity>
extends Goal {
    protected final T mob;
    protected LivingEntity target;
    protected final double speedModifier;
    protected final int attackIntervalMin;
    protected final int attackIntervalMax;
    protected final float attackRadius;
    protected final float attackRadiusSqr;
    protected boolean shortCircuitTemp = false;
    protected boolean hasLineOfSight;
    protected int seeTime = 0;
    protected int attackTime = 0;
    protected boolean isFlying;
    protected final ArrayList<AbstractSpell> healingSpells = new ArrayList();
    protected final ArrayList<AbstractSpell> buffSpells = new ArrayList();
    protected float minSpellQuality = 0.1f;
    protected float maxSpellQuality = 0.3f;

    public WizardSupportGoal(T abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval) {
        this(abstractSpellCastingMob, pSpeedModifier, pAttackInterval, pAttackInterval);
    }

    public WizardSupportGoal(T abstractSpellCastingMob, double pSpeedModifier, int pAttackIntervalMin, int pAttackIntervalMax) {
        this.mob = abstractSpellCastingMob;
        this.speedModifier = pSpeedModifier;
        this.attackIntervalMin = pAttackIntervalMin;
        this.attackIntervalMax = pAttackIntervalMax;
        this.attackRadius = 20.0f;
        this.attackRadiusSqr = this.attackRadius * this.attackRadius;
    }

    public WizardSupportGoal<T> setSpells(List<AbstractSpell> healingSpells, List<AbstractSpell> buffSpells) {
        this.healingSpells.clear();
        this.buffSpells.clear();
        this.healingSpells.addAll(healingSpells);
        this.buffSpells.addAll(buffSpells);
        return this;
    }

    public WizardSupportGoal<T> setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        this.minSpellQuality = minSpellQuality;
        this.maxSpellQuality = maxSpellQuality;
        return this;
    }

    public WizardSupportGoal<T> setIsFlying() {
        this.isFlying = true;
        return this;
    }

    public boolean canUse() {
        LivingEntity livingentity = ((SupportMob)this.mob).getSupportTarget();
        if (livingentity != null && livingentity.isAlive() && Utils.shouldHealEntity(this.mob, livingentity)) {
            this.target = livingentity;
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone() && Utils.shouldHealEntity(this.mob, this.target);
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            return;
        }
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        this.hasLineOfSight = this.mob.getSensing().hasLineOfSight((Entity)this.target);
        this.seeTime = this.hasLineOfSight ? ++this.seeTime : 0;
        this.doMovement(distanceSquared);
        this.handleAttackLogic(distanceSquared);
    }

    protected void handleAttackLogic(double distanceSquared) {
        if (--this.attackTime == 0) {
            if (!((IMagicEntity)this.mob).isCasting()) {
                this.mob.lookAt((Entity)this.target, 180.0f, 180.0f);
                this.doSpellAction();
            }
            this.resetAttackTimer(distanceSquared);
        }
        if (((IMagicEntity)this.mob).isCasting()) {
            SpellData spellData = MagicData.getPlayerMagicData(this.mob).getCastingSpell();
            if (this.target.isDeadOrDying() || spellData.getSpell().shouldAIStopCasting(spellData.getLevel(), (Mob)this.mob, this.target)) {
                ((IMagicEntity)this.mob).cancelCast();
            }
        }
    }

    protected void resetAttackTimer(double distanceSquared) {
        float f = (float)Math.sqrt(distanceSquared) / this.attackRadius;
        this.attackTime = (int)(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
    }

    protected void doMovement(double distanceSquared) {
        float movementDebuff = ((IMagicEntity)this.mob).isCasting() ? 0.2f : 1.0f;
        double effectiveSpeed = (double)movementDebuff * this.speedModifier;
        if (distanceSquared < (double)this.attackRadiusSqr && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
            this.mob.lookAt((Entity)this.target, 30.0f, 30.0f);
        } else if (this.isFlying) {
            this.mob.getMoveControl().setWantedPosition(this.target.getX(), this.target.getY() + 2.0, this.target.getZ(), this.speedModifier);
        } else {
            this.mob.getNavigation().moveTo((Entity)this.target, effectiveSpeed);
        }
    }

    protected void doSpellAction() {
        int spellLevel = (int)((float)this.getNextSpellType().getMaxLevel() * Mth.lerp((float)this.mob.getRandom().nextFloat(), (float)this.minSpellQuality, (float)this.maxSpellQuality));
        spellLevel = Math.max(spellLevel, 1);
        AbstractSpell abstractSpell = this.getNextSpellType();
        if (!abstractSpell.shouldAIStopCasting(spellLevel, (Mob)this.mob, this.target)) {
            ((IMagicEntity)this.mob).initiateCastSpell(abstractSpell, spellLevel);
        }
        ((SupportMob)this.mob).setSupportTarget(null);
    }

    protected AbstractSpell getNextSpellType() {
        Mob mob;
        LivingEntity livingEntity;
        float shouldBuff = 0.0f;
        if (!this.buffSpells.isEmpty() && (livingEntity = this.target) instanceof Mob && (mob = (Mob)livingEntity).isAggressive()) {
            shouldBuff = this.target.getHealth() / this.target.getMaxHealth();
        }
        return this.getSpell(this.mob.getRandom().nextFloat() > shouldBuff ? this.healingSpells : this.buffSpells);
    }

    protected AbstractSpell getSpell(List<AbstractSpell> spells) {
        if (spells.isEmpty()) {
            return SpellRegistry.none();
        }
        return spells.get(this.mob.getRandom().nextInt(spells.size()));
    }

    public void start() {
        super.start();
    }
}

