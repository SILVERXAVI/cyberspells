/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySelector
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GenericHurtByTargetGoal
extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private static final int ALERT_RANGE_Y = 10;
    private boolean alertSameType;
    private int timestamp;
    Predicate<LivingEntity> toIgnoreDamage;
    @Nullable
    private Class<?>[] toIgnoreAlert;

    public GenericHurtByTargetGoal(PathfinderMob pMob, Predicate<LivingEntity> pToIgnoreDamage) {
        super((Mob)pMob, true);
        this.toIgnoreDamage = pToIgnoreDamage;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        int i = this.mob.getLastHurtByMobTimestamp();
        LivingEntity livingentity = this.mob.getLastHurtByMob();
        if (livingentity == null || livingentity.isAlliedTo((Entity)this.mob)) {
            return false;
        }
        if (i != this.timestamp && livingentity != null) {
            if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            }
            if (this.toIgnoreDamage.test(livingentity)) {
                return false;
            }
            return this.canAttack(livingentity, HURT_BY_TARGETING);
        }
        return false;
    }

    public GenericHurtByTargetGoal setAlertOthers(Class<?> ... pReinforcementTypes) {
        this.alertSameType = true;
        this.toIgnoreAlert = pReinforcementTypes;
        return this;
    }

    public void start() {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, (Object)this.mob.getLastHurtByMob(), 200L);
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        if (this.alertSameType) {
            this.alertOthers();
        }
        super.start();
    }

    protected void alertOthers() {
        double d0 = this.getFollowDistance();
        AABB aabb = AABB.unitCubeFromLowerCorner((Vec3)this.mob.position()).inflate(d0, 10.0, d0);
        List list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), aabb, EntitySelector.NO_SPECTATORS);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Mob mob = (Mob)iterator.next();
            if (this.mob == mob || mob.getTarget() != null || this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)mob).getOwner() || mob.isAlliedTo((Entity)this.mob.getLastHurtByMob())) continue;
            if (this.toIgnoreAlert != null) {
                boolean flag = false;
                for (Class<?> oclass : this.toIgnoreAlert) {
                    if (mob.getClass() != oclass) continue;
                    flag = true;
                    break;
                }
                if (flag) continue;
            }
            this.alertOther(mob, this.mob.getLastHurtByMob());
        }
        return;
    }

    protected void alertOther(Mob pMob, LivingEntity pTarget) {
        pMob.setTarget(pTarget);
    }
}

