/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.util.DefaultRandomPos
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WizardAttackGoal
extends Goal {
    protected LivingEntity target;
    protected final double speedModifier;
    protected final int spellAttackIntervalMin;
    protected final int spellAttackIntervalMax;
    protected float spellcastingRange;
    protected float spellcastingRangeSqr;
    protected boolean shortCircuitTemp = false;
    protected boolean hasLineOfSight;
    protected int seeTime = 0;
    protected int strafeTime;
    protected boolean strafingClockwise;
    protected int spellAttackDelay = -1;
    protected int projectileCount;
    protected AbstractSpell singleUseSpell = SpellRegistry.none();
    protected int singleUseDelay;
    protected int singleUseLevel;
    protected boolean isFlying;
    protected boolean allowFleeing;
    protected int fleeCooldown;
    protected final ArrayList<AbstractSpell> attackSpells = new ArrayList();
    protected final ArrayList<AbstractSpell> defenseSpells = new ArrayList();
    protected final ArrayList<AbstractSpell> movementSpells = new ArrayList();
    protected final ArrayList<AbstractSpell> supportSpells = new ArrayList();
    protected ArrayList<AbstractSpell> lastSpellCategory = this.attackSpells;
    protected float minSpellQuality = 0.1f;
    protected float maxSpellQuality = 0.4f;
    protected boolean drinksPotions;
    protected final PathfinderMob mob;
    protected final IMagicEntity spellCastingMob;

    public WizardAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackInterval) {
        this(abstractSpellCastingMob, pSpeedModifier, pAttackInterval, pAttackInterval);
    }

    public WizardAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int pAttackIntervalMin, int pAttackIntervalMax) {
        PathfinderMob m;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.spellCastingMob = abstractSpellCastingMob;
        if (!(abstractSpellCastingMob instanceof PathfinderMob)) {
            throw new IllegalStateException("Unable to add " + ((Object)((Object)this)).getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
        this.mob = m = (PathfinderMob)abstractSpellCastingMob;
        this.speedModifier = pSpeedModifier;
        this.spellAttackIntervalMin = pAttackIntervalMin;
        this.spellAttackIntervalMax = pAttackIntervalMax;
        this.spellcastingRange = 20.0f;
        this.spellcastingRangeSqr = this.spellcastingRange * this.spellcastingRange;
        this.allowFleeing = true;
    }

    public WizardAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells, List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        this.attackSpells.clear();
        this.defenseSpells.clear();
        this.movementSpells.clear();
        this.supportSpells.clear();
        this.attackSpells.addAll(attackSpells);
        this.defenseSpells.addAll(defenseSpells);
        this.movementSpells.addAll(movementSpells);
        this.supportSpells.addAll(supportSpells);
        return this;
    }

    public WizardAttackGoal setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        this.minSpellQuality = minSpellQuality;
        this.maxSpellQuality = maxSpellQuality;
        return this;
    }

    public WizardAttackGoal setSingleUseSpell(AbstractSpell abstractSpell, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        this.singleUseSpell = abstractSpell;
        this.singleUseDelay = Utils.random.nextIntBetweenInclusive(minDelay, maxDelay);
        this.singleUseLevel = Utils.random.nextIntBetweenInclusive(minLevel, maxLevel);
        return this;
    }

    public WizardAttackGoal setIsFlying() {
        this.isFlying = true;
        return this;
    }

    public WizardAttackGoal setDrinksPotions() {
        this.drinksPotions = true;
        return this;
    }

    public WizardAttackGoal setAllowFleeing(boolean allowFleeing) {
        this.allowFleeing = allowFleeing;
        return this;
    }

    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return this.mob.canAttack(this.target);
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.spellAttackDelay = -1;
        this.mob.setAggressive(false);
        this.mob.getMoveControl().strafe(0.0f, 0.0f);
        this.mob.getNavigation().stop();
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
        this.seeTime = this.hasLineOfSight ? ++this.seeTime : --this.seeTime;
        this.doMovement(distanceSquared);
        if (this.mob.getLastHurtByMobTimestamp() == this.mob.tickCount - 1) {
            this.spellAttackDelay = (int)(Mth.lerp((float)0.6f, (float)this.spellAttackDelay, (float)0.0f) + 1.0f);
        }
        this.handleAttackLogic(distanceSquared);
        --this.singleUseDelay;
    }

    protected void handleAttackLogic(double distanceSquared) {
        if (this.seeTime < -50) {
            return;
        }
        if (--this.spellAttackDelay == 0) {
            this.resetSpellAttackTimer(distanceSquared);
            if (!this.spellCastingMob.isCasting() && !this.spellCastingMob.isDrinkingPotion()) {
                this.doSpellAction();
            }
        } else if (this.spellAttackDelay < 0) {
            this.resetSpellAttackTimer(distanceSquared);
        }
        if (this.spellCastingMob.isCasting()) {
            SpellData spellData = MagicData.getPlayerMagicData((LivingEntity)this.mob).getCastingSpell();
            if (this.target.isDeadOrDying() || spellData.getSpell().shouldAIStopCasting(spellData.getLevel(), (Mob)this.mob, this.target)) {
                this.spellCastingMob.cancelCast();
            }
        }
    }

    public boolean isActing() {
        return this.spellCastingMob.isCasting() || this.spellCastingMob.isDrinkingPotion();
    }

    protected void resetSpellAttackTimer(double distanceSquared) {
        float f = (float)Math.sqrt(distanceSquared) / this.spellcastingRange;
        this.spellAttackDelay = Math.max(1, Mth.floor((float)(f * (float)(this.spellAttackIntervalMax - this.spellAttackIntervalMin) + (float)this.spellAttackIntervalMin)));
    }

    protected void doMovement(double distanceSquared) {
        double speed = (double)(this.spellCastingMob.isCasting() ? 0.75f : 1.0f) * this.movementSpeed();
        this.mob.lookAt((Entity)this.target, 30.0f, 30.0f);
        float fleeDist = 0.275f;
        float ss = this.getStrafeMultiplier();
        if (this.allowFleeing && !this.spellCastingMob.isCasting() && this.spellAttackDelay > 10 && --this.fleeCooldown <= 0 && distanceSquared < (double)(this.spellcastingRangeSqr * (fleeDist * fleeDist))) {
            Vec3 flee = DefaultRandomPos.getPosAway((PathfinderMob)this.mob, (int)16, (int)7, (Vec3)this.target.position());
            if (flee != null) {
                this.mob.getNavigation().moveTo(flee.x, flee.y, flee.z, speed * 1.5);
            } else {
                this.mob.getMoveControl().strafe(-((float)speed) * ss, (float)speed * ss);
            }
        } else if (distanceSquared < (double)this.spellcastingRangeSqr && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
            if (++this.strafeTime > 25 && this.mob.getRandom().nextDouble() < 0.1) {
                this.strafingClockwise = !this.strafingClockwise;
                this.strafeTime = 0;
            }
            float strafeForward = (distanceSquared * 6.0 < (double)this.spellcastingRangeSqr ? -1.0f : 0.5f) * 0.2f * (float)this.speedModifier;
            int strafeDir = this.strafingClockwise ? 1 : -1;
            this.mob.getMoveControl().strafe(strafeForward * ss, (float)speed * (float)strafeDir * ss);
            if (this.mob.horizontalCollision && this.mob.getRandom().nextFloat() < 0.1f) {
                this.tryJump();
            }
        } else if (this.mob.tickCount % 5 == 0) {
            if (this.isFlying) {
                this.mob.getMoveControl().setWantedPosition(this.target.getX(), this.target.getY() + 2.0, this.target.getZ(), this.speedModifier);
            } else {
                this.mob.getNavigation().moveTo((Entity)this.target, this.speedModifier);
            }
        }
    }

    protected double movementSpeed() {
        return this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2.0;
    }

    protected void tryJump() {
        BlockPos blockposAbove;
        BlockState blockstateAbove;
        VoxelShape voxelshapeAbove;
        Vec3 nextBlock = new Vec3((double)this.mob.xxa, 0.0, (double)this.mob.zza).normalize();
        BlockPos blockpos = BlockPos.containing((Position)this.mob.position().add(nextBlock));
        BlockState blockstate = this.mob.level.getBlockState(blockpos);
        VoxelShape voxelshape = blockstate.getCollisionShape((BlockGetter)this.mob.level, blockpos);
        if (!voxelshape.isEmpty() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES) && (voxelshapeAbove = (blockstateAbove = this.mob.level.getBlockState(blockposAbove = blockpos.above())).getCollisionShape((BlockGetter)this.mob.level, blockposAbove)).isEmpty()) {
            this.mob.getJumpControl().jump();
            this.mob.setXxa(this.mob.xxa * 5.0f);
            this.mob.setZza(this.mob.zza * 5.0f);
        }
    }

    protected void doSpellAction() {
        if (!this.spellCastingMob.getHasUsedSingleAttack() && this.singleUseSpell != SpellRegistry.none() && this.singleUseDelay <= 0) {
            this.spellCastingMob.setHasUsedSingleAttack(true);
            this.spellCastingMob.initiateCastSpell(this.singleUseSpell, this.singleUseLevel);
            this.fleeCooldown = 7 + this.singleUseSpell.getCastTime(this.singleUseLevel);
        } else {
            AbstractSpell spell = this.getNextSpellType();
            int spellLevel = (int)((float)spell.getMaxLevel() * Mth.lerp((float)this.mob.getRandom().nextFloat(), (float)this.minSpellQuality, (float)this.maxSpellQuality));
            if (!spell.shouldAIStopCasting(spellLevel = Math.max(spellLevel, 1), (Mob)this.mob, this.target)) {
                this.spellCastingMob.initiateCastSpell(spell, spellLevel);
                this.fleeCooldown = 7 + spell.getCastTime(spellLevel);
            } else {
                this.spellAttackDelay = 5;
            }
        }
    }

    protected AbstractSpell getNextSpellType() {
        TreeMap<Integer, ArrayList<AbstractSpell>> weightedSpells = new TreeMap<Integer, ArrayList<AbstractSpell>>();
        int attackWeight = this.getAttackWeight();
        int defenseWeight = this.getDefenseWeight() - (this.lastSpellCategory == this.defenseSpells ? 100 : 0);
        int movementWeight = this.getMovementWeight() - (this.lastSpellCategory == this.movementSpells ? 50 : 0);
        int supportWeight = this.getSupportWeight() - (this.lastSpellCategory == this.supportSpells ? 100 : 0);
        int total = 0;
        if (!this.attackSpells.isEmpty() && attackWeight > 0) {
            weightedSpells.put(total += attackWeight, this.attackSpells);
        }
        if (!this.defenseSpells.isEmpty() && defenseWeight > 0) {
            weightedSpells.put(total += defenseWeight, this.defenseSpells);
        }
        if (!this.movementSpells.isEmpty() && movementWeight > 0) {
            weightedSpells.put(total += movementWeight, this.movementSpells);
        }
        if ((!this.supportSpells.isEmpty() || this.drinksPotions) && supportWeight > 0) {
            weightedSpells.put(total += supportWeight, this.supportSpells);
        }
        if (total > 0) {
            ArrayList spellList;
            int seed = this.mob.getRandom().nextInt(total);
            this.lastSpellCategory = spellList = (ArrayList)weightedSpells.higherEntry(seed).getValue();
            if (this.drinksPotions && spellList == this.supportSpells && (this.supportSpells.isEmpty() || this.mob.getRandom().nextFloat() < 0.5f)) {
                this.spellCastingMob.startDrinkingPotion();
                return SpellRegistry.none();
            }
            return (AbstractSpell)spellList.get(this.mob.getRandom().nextInt(spellList.size()));
        }
        return SpellRegistry.none();
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    protected int getAttackWeight() {
        int baseWeight = 80;
        if (!this.hasLineOfSight || this.target == null) {
            return 0;
        }
        float targetHealth = this.target.getHealth() / this.target.getMaxHealth();
        int targetHealthWeight = (int)((1.0f - targetHealth) * (float)baseWeight * 0.75f);
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        int distanceWeight = (int)(1.0 - distanceSquared / (double)this.spellcastingRangeSqr * -60.0);
        return baseWeight + targetHealthWeight + distanceWeight;
    }

    protected int getDefenseWeight() {
        int baseWeight = -20;
        if (this.target == null) {
            return baseWeight;
        }
        float x = this.mob.getHealth();
        float m = this.mob.getMaxHealth();
        int healthWeight = (int)(50.0f * (-(x * x * x) / (m * m * m) + 1.0f));
        float targetHealth = this.target.getHealth() / this.target.getMaxHealth();
        int targetHealthWeight = (int)(1.0f - targetHealth) * -35;
        int threatWeight = this.projectileCount * 95;
        return baseWeight + healthWeight + targetHealthWeight + threatWeight;
    }

    protected int getMovementWeight() {
        if (this.target == null) {
            return 0;
        }
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        double distancePercent = Mth.clamp((double)(distanceSquared / (double)this.spellcastingRangeSqr), (double)0.0, (double)1.0);
        int distanceWeight = (int)(distancePercent * 50.0);
        int losWeight = this.hasLineOfSight ? 0 : 80;
        float healthInverted = 1.0f - this.mob.getHealth() / this.mob.getMaxHealth();
        float distanceInverted = (float)(1.0 - distancePercent);
        int runWeight = (int)(400.0f * healthInverted * healthInverted * distanceInverted * distanceInverted);
        return distanceWeight + losWeight + runWeight;
    }

    protected int getSupportWeight() {
        int baseWeight = -15;
        if (this.target == null) {
            return baseWeight;
        }
        float health = 1.0f - this.mob.getHealth() / this.mob.getMaxHealth();
        int healthWeight = (int)(200.0f * health);
        double distanceSquared = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        double distancePercent = Mth.clamp((double)(distanceSquared / (double)this.spellcastingRangeSqr), (double)0.0, (double)1.0);
        int distanceWeight = (int)((1.0 - distancePercent) * -75.0);
        return baseWeight + healthWeight + distanceWeight;
    }

    public boolean isInterruptable() {
        return !this.isActing();
    }

    public float getStrafeMultiplier() {
        return 1.0f;
    }
}

