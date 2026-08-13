/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class MagmaThrowBossAbilityGoal<T extends Mob & IAnimatedAttacker>
extends Goal {
    int abilityTimer;
    int delay;
    boolean isUsing;
    final T mob;
    public static final int ANIM_DURATION = 40;
    public static final int ACTION_TIMESTAMP = 32;

    public MagmaThrowBossAbilityGoal(T mob) {
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.mob = mob;
    }

    public boolean canUse() {
        return this.mob.onGround() && this.mob.getTarget() != null && this.mob.distanceToSqr((Entity)this.mob.getTarget()) > 9.0 && this.delay-- <= 0;
    }

    public boolean canContinueToUse() {
        return this.isUsing;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        ++this.abilityTimer;
        this.mob.setSpeed(0.0f);
        this.mob.setZza(0.0f);
        this.mob.setXxa(0.0f);
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt((Entity)target);
        }
        if (this.abilityTimer == 32) {
            if (target != null) {
                double range = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
                List targets = ((Mob)this.mob).level.getEntitiesOfClass(target.getClass(), this.mob.getBoundingBox().inflate(range, 8.0, range));
                for (LivingEntity entity : targets) {
                    if (!Utils.hasLineOfSight(((Mob)this.mob).level, this.mob, (Entity)entity, false)) continue;
                    FireBomb fireBomb = this.fireBomb();
                    double horizontalSpeed = 0.5;
                    Vec3 horizontal = entity.position().subtract(this.mob.position()).multiply(1.0, 0.0, 1.0);
                    double distance = horizontal.length();
                    double ticks = distance / horizontalSpeed;
                    double y1 = entity.getY() - this.mob.getY();
                    double g = fireBomb.getGravity();
                    double verticalSpeed = (y1 + 0.5 * g * ticks * ticks) / ticks;
                    Vec3 estMovement = entity.getDeltaMovement().multiply(1.0, 0.0, 1.0).scale(ticks);
                    Vec3 trajectory = horizontal.normalize().scale(horizontalSpeed).add(0.0, verticalSpeed, 0.0).add(estMovement);
                    fireBomb.setDeltaMovement(trajectory);
                    ((Mob)this.mob).level.addFreshEntity((Entity)fireBomb);
                }
            }
            for (int i = 0; i < 4; ++i) {
                FireBomb fireBomb = this.fireBomb();
                Vec3 motion = Utils.getRandomVec3(0.85).add(0.0, 3.0, 0.0).scale(0.3);
                fireBomb.setDeltaMovement(motion);
                ((Mob)this.mob).level.addFreshEntity((Entity)fireBomb);
            }
        }
        if (this.abilityTimer >= 40) {
            this.isUsing = false;
        }
    }

    protected FireBomb fireBomb() {
        FireBomb fireBomb = new FireBomb(((Mob)this.mob).level, (LivingEntity)this.mob);
        fireBomb.moveTo(this.mob.getEyePosition());
        fireBomb.setDamage(20.0f);
        fireBomb.setAoeDamage(5.0f);
        fireBomb.setExplosionRadius(4.0f);
        return fireBomb;
    }

    public void start() {
        this.isUsing = true;
        this.abilityTimer = 0;
        this.delay = 80;
        ((IAnimatedAttacker)this.mob).serverTriggerAnimation("magma_throw");
        this.mob.getNavigation().stop();
    }
}

