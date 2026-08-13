/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.level.block.LeavesBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.pathfinder.PathType
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.EnumSet;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class GenericFollowOwnerGoal
extends Goal {
    private final PathfinderMob mob;
    @Nullable
    private Entity owner;
    private Supplier<Entity> ownerGetter;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private float teleportDistance;
    private boolean canFly;

    public GenericFollowOwnerGoal(PathfinderMob pTamable, Supplier<Entity> ownerGetter, double pSpeedModifier, float pStartDistance, float pStopDistance, boolean canFly, float teleportDistance) {
        this.mob = pTamable;
        this.ownerGetter = ownerGetter;
        this.speedModifier = pSpeedModifier;
        this.navigation = pTamable.getNavigation();
        this.startDistance = pStartDistance;
        this.stopDistance = pStopDistance;
        this.teleportDistance = teleportDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.canFly = canFly;
    }

    public boolean canUse() {
        Entity livingentity = this.ownerGetter.get();
        if (livingentity == null) {
            return false;
        }
        if (this.mob.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = livingentity;
        return true;
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        }
        return !(this.mob.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        boolean flag = this.shouldTryTeleportToOwner();
        if (!flag) {
            this.mob.getLookControl().setLookAt(this.owner, 10.0f, (float)this.mob.getMaxHeadXRot());
        }
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (flag) {
                this.tryToTeleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }

    public void tryToTeleportToOwner() {
        Entity livingentity = this.ownerGetter.get();
        if (livingentity != null) {
            this.teleportToAroundBlockPos(livingentity.blockPosition());
        }
    }

    public boolean shouldTryTeleportToOwner() {
        Entity livingentity = this.ownerGetter.get();
        return livingentity != null && this.mob.distanceToSqr(livingentity) >= (double)(this.teleportDistance * this.teleportDistance);
    }

    private void teleportToAroundBlockPos(BlockPos pPos) {
        for (int i = 0; i < 10; ++i) {
            int j = this.mob.getRandom().nextIntBetweenInclusive(-3, 3);
            int k = this.mob.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) < 2 && Math.abs(k) < 2) continue;
            int l = this.mob.getRandom().nextIntBetweenInclusive(-1, 1);
            if (!this.maybeTeleportTo(pPos.getX() + j, pPos.getY() + l, pPos.getZ() + k)) continue;
            return;
        }
    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ) {
        if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
            return false;
        }
        this.mob.moveTo((double)pX + 0.5, (double)pY, (double)pZ + 0.5, this.mob.getYRot(), this.mob.getXRot());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pPos) {
        PathType pathtype = WalkNodeEvaluator.getPathTypeStatic((Mob)this.mob, (BlockPos)pPos);
        if (pathtype != PathType.WALKABLE) {
            return false;
        }
        BlockState blockstate = this.mob.level().getBlockState(pPos.below());
        if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
            return false;
        }
        BlockPos blockpos = pPos.subtract((Vec3i)this.mob.blockPosition());
        return this.mob.level().noCollision((Entity)this.mob, this.mob.getBoundingBox().move(blockpos));
    }
}

