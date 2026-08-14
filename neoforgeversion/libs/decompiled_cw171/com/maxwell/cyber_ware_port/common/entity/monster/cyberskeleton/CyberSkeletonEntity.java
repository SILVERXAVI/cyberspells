/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RangedBowAttackGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.animal.IronGolem
 *  net.minecraft.world.entity.monster.Skeleton
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberskeleton;

import com.maxwell.cyber_ware_port.common.entity.ICyberwareMob;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.Arrays;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CyberSkeletonEntity
extends Skeleton
implements ICyberwareMob {
    private static final int MELEE_TRIGGER_DIST_SQR = 25;
    private static final int MELEE_COOLDOWN_TICKS = 100;
    public int meleeCooldown = 0;

    public CyberSkeletonEntity(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    @Override
    public List<Item> getSpecialDrops() {
        return Arrays.asList((Item)ModItems.RAPID_FIRE_FLYWHEEL.get(), (Item)ModItems.LINEAR_ACTUATORS.get());
    }

    @Override
    public List<Item> getForbiddenDrops() {
        return Arrays.asList((Item)ModItems.CYBER_ARM_LEFT.get(), (Item)ModItems.CYBER_ARM_RIGHT.get(), (Item)ModItems.CYBER_LEG_LEFT.get(), (Item)ModItems.CYBER_LEG_RIGHT.get(), (Item)ModItems.INTERNAL_DEFIBRILLATOR.get());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(4, (Goal)new CyberMeleeGoal(this, 1.2, false));
        this.goalSelector.addGoal(5, (Goal)new CyberBowAttackGoal(this, 1.0, 20, 15.0f));
        this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
    }

    protected boolean isSunBurnTick() {
        return false;
    }

    public void reassessWeaponGoal() {
    }

    public void aiStep() {
        super.aiStep();
        if (this.meleeCooldown > 0) {
            --this.meleeCooldown;
        }
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack weaponStack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, item -> item instanceof BowItem));
        ItemStack arrowStack = this.getProjectile(weaponStack);
        AbstractArrow arrow = ProjectileUtil.getMobArrow((LivingEntity)this, (ItemStack)arrowStack, (float)distanceFactor, (ItemStack)weaponStack);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.2, d2, 2.5f, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity((Entity)arrow);
    }

    static class CyberMeleeGoal
    extends MeleeAttackGoal {
        private final CyberSkeletonEntity skeleton;

        public CyberMeleeGoal(CyberSkeletonEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super((PathfinderMob)mob, speedModifier, followingTargetEvenIfNotSeen);
            this.skeleton = mob;
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            if (this.skeleton.getTarget() == null || this.skeleton.meleeCooldown > 0) {
                return false;
            }
            return this.skeleton.distanceToSqr((Entity)this.skeleton.getTarget()) <= 25.0;
        }

        public void stop() {
            super.stop();
            this.skeleton.meleeCooldown = 100;
        }
    }

    static class CyberBowAttackGoal
    extends RangedBowAttackGoal<CyberSkeletonEntity> {
        private final CyberSkeletonEntity skeleton;

        public CyberBowAttackGoal(CyberSkeletonEntity mob, double speedModifier, int attackIntervalMin, float attackRadius) {
            super((Mob)mob, speedModifier, attackIntervalMin, attackRadius);
            this.skeleton = mob;
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            if (this.skeleton.getTarget() == null || this.skeleton.meleeCooldown > 0) {
                return false;
            }
            return this.skeleton.distanceToSqr((Entity)this.skeleton.getTarget()) > 25.0;
        }
    }
}

