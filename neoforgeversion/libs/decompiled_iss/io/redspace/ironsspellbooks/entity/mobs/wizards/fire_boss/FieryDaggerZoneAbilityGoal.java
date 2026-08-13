/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.AnimatedActionGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class FieryDaggerZoneAbilityGoal
extends AnimatedActionGoal<FireBossEntity> {
    public FieryDaggerZoneAbilityGoal(FireBossEntity mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        return ((FireBossEntity)this.mob).onGround() && ((FireBossEntity)this.mob).getTarget() != null && ((FireBossEntity)this.mob).distanceToSqr((Entity)((FireBossEntity)this.mob).getTarget()) > 36.0;
    }

    @Override
    protected int getActionTimestamp() {
        return 1;
    }

    @Override
    protected int getActionDuration() {
        return 5;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(50, 90);
    }

    @Override
    protected String getAnimationId() {
        return "instant_slash";
    }

    @Override
    public void tick() {
        if (((FireBossEntity)this.mob).getTarget() != null) {
            ((FireBossEntity)this.mob).attackGoal.setTarget(((FireBossEntity)this.mob).getTarget());
            ((FireBossEntity)this.mob).attackGoal.doMovement(((FireBossEntity)this.mob).distanceToSqr((Entity)((FireBossEntity)this.mob).getTarget()));
        }
        super.tick();
    }

    @Override
    protected void doAction() {
        LivingEntity target = ((FireBossEntity)this.mob).getTarget();
        if (target != null) {
            ((FireBossEntity)this.mob).playSound((SoundEvent)SoundRegistry.FIERY_DAGGER_THROW.get(), 2.0f, (float)Utils.random.nextIntBetweenInclusive(80, 110) * 0.01f);
            Vec3 start = ((FireBossEntity)this.mob).getEyePosition();
            Vec3 targetPos = target.position();
            Vec3 deltaAim = targetPos.subtract(start);
            for (int i = 0; i < 3; ++i) {
                Vec3 aim = start.add(deltaAim.yRot(0.7853982f * (float)(i - 1)));
                int delay = Utils.random.nextIntBetweenInclusive(10, 40);
                FieryDaggerEntity dagger = new FieryDaggerEntity(((FireBossEntity)this.mob).level);
                dagger.setOwner((Entity)this.mob);
                dagger.setPos(start);
                dagger.delay = delay;
                dagger.setDamage((float)(((FireBossEntity)this.mob).getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75));
                dagger.setExplosionRadius(4.0f + Utils.random.nextFloat() * 2.0f);
                dagger.setNoGravity(false);
                Vec3 horizontal = aim.subtract(start).multiply(1.0, 0.0, 1.0);
                double horizontalSpeed = (double)(1.0f * Mth.cos((float)0.7853982f)) + 0.5;
                double distance = horizontal.length();
                double ticks = distance / horizontalSpeed;
                double y1 = aim.y - start.y;
                double g = dagger.getGravity();
                double verticalSpeed = (y1 + 0.5 * g * ticks * ticks) / ticks;
                Vec3 trajectory = horizontal.normalize().scale(horizontalSpeed).add(0.0, verticalSpeed, 0.0);
                dagger.setDeltaMovement(trajectory);
                ((FireBossEntity)this.mob).level.addFreshEntity((Entity)dagger);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        ((FireBossEntity)this.mob).attackGoal.setTarget(null);
    }
}

