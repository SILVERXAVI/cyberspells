/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.monster.Vex
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericProtectOwnerTargetGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedVex
extends Vex
implements IMagicSummon {
    public SummonedVex(EntityType<? extends Vex> pEntityType, Level pLevel) {
        super((EntityType)EntityRegistry.SUMMONED_VEX.get(), pLevel);
        this.xpReward = 0;
    }

    @Deprecated(forRemoval=true)
    public SummonedVex(Level pLevel, LivingEntity owner) {
        this((EntityType<? extends Vex>)((EntityType)EntityRegistry.SUMMONED_VEX.get()), pLevel);
        this.setSummoner(owner);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(4, (Goal)new VexChargeAttackGoal());
        this.goalSelector.addGoal(7, (Goal)new GenericFollowOwnerGoal((PathfinderMob)this, this::getSummoner, 0.65f, 35.0f, 10.0f, true, 50.0f));
        this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0f));
        this.goalSelector.addGoal(16, (Goal)new VexRandomMoveGoal());
        this.targetSelector.addGoal(1, (Goal)new GenericOwnerHurtByTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(2, (Goal)new GenericOwnerHurtTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(3, (Goal)new GenericCopyOwnerTargetGoal((PathfinderMob)this, this::getSummoner));
        this.targetSelector.addGoal(4, (Goal)new GenericHurtByTargetGoal((PathfinderMob)this, entity -> entity == this.getSummoner()).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, (Goal)new GenericProtectOwnerTargetGoal((Mob)this, this::getSummoner));
    }

    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo((Entity)pPlayer);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack((Mob)this, pEntity, SpellRegistry.SUMMON_VEX_SPELL.get().getDamageSource((Entity)this, this.getSummoner()));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.shouldIgnoreDamage(pSource)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Deprecated(forRemoval=true)
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) {
            return;
        }
        SummonManager.setOwner((Entity)this, (Entity)owner);
    }

    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    public void onRemovedFromLevel() {
        this.onRemovedHelper((Entity)this);
        super.onRemovedFromLevel();
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), (ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    class VexChargeAttackGoal
    extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = SummonedVex.this.getTarget();
            if (livingentity != null && livingentity.isAlive() && !SummonedVex.this.getMoveControl().hasWanted() && SummonedVex.this.random.nextInt(VexChargeAttackGoal.reducedTickDelay((int)7)) == 0) {
                return SummonedVex.this.distanceToSqr((Entity)livingentity) > 4.0;
            }
            return false;
        }

        public boolean canContinueToUse() {
            return SummonedVex.this.getMoveControl().hasWanted() && SummonedVex.this.isCharging() && SummonedVex.this.getTarget() != null && SummonedVex.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = SummonedVex.this.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                SummonedVex.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
            }
            SummonedVex.this.setIsCharging(true);
            SummonedVex.this.playSound(SoundEvents.VEX_CHARGE, 1.0f, 1.0f);
        }

        public void stop() {
            SummonedVex.this.setIsCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = SummonedVex.this.getTarget();
            if (livingentity != null) {
                if (SummonedVex.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    SummonedVex.this.doHurtTarget((Entity)livingentity);
                    SummonedVex.this.setIsCharging(false);
                } else {
                    double d0 = SummonedVex.this.distanceToSqr((Entity)livingentity);
                    if (d0 < 9.0) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        SummonedVex.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
                    }
                }
            }
        }
    }

    class VexRandomMoveGoal
    extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !SummonedVex.this.getMoveControl().hasWanted() && SummonedVex.this.random.nextInt(VexRandomMoveGoal.reducedTickDelay((int)7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = SummonedVex.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = SummonedVex.this.blockPosition();
            }
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(SummonedVex.this.random.nextInt(15) - 7, SummonedVex.this.random.nextInt(11) - 5, SummonedVex.this.random.nextInt(15) - 7);
                if (!SummonedVex.this.level().isEmptyBlock(blockpos1)) continue;
                SummonedVex.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5, (double)blockpos1.getY() + 0.5, (double)blockpos1.getZ() + 0.5, 0.25);
                if (SummonedVex.this.getTarget() != null) break;
                SummonedVex.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5, (double)blockpos1.getY() + 0.5, (double)blockpos1.getZ() + 0.5, 180.0f, 20.0f);
                break;
            }
        }
    }
}

