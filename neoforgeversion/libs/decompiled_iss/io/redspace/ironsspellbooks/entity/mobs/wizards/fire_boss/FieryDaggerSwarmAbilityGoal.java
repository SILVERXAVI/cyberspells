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

public class FieryDaggerSwarmAbilityGoal
extends AnimatedActionGoal<FireBossEntity> {
    public static final int ANIM_DURATION = 25;
    public static final int ACTION_TIMESTAMP = 17;

    public FieryDaggerSwarmAbilityGoal(FireBossEntity mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        return ((FireBossEntity)this.mob).getTarget() != null;
    }

    @Override
    protected int getActionTimestamp() {
        return 17;
    }

    @Override
    protected int getActionDuration() {
        return 25;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(40, 80);
    }

    @Override
    protected String getAnimationId() {
        return "summon_fiery_daggers";
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
            ((FireBossEntity)this.mob).playSound((SoundEvent)SoundRegistry.FIRE_CAST.get(), 2.0f, (float)Utils.random.nextIntBetweenInclusive(80, 110) * 0.01f);
            Vec3 pos = ((FireBossEntity)this.mob).position();
            int count = 7;
            int delay = Utils.random.nextIntBetweenInclusive(30, 70);
            float yAngle = -Utils.getAngle(target.getX(), target.getZ(), ((FireBossEntity)this.mob).getX(), ((FireBossEntity)this.mob).getZ()) + 1.5707964f;
            for (int i = 0; i < count; ++i) {
                Vec3 offset = new Vec3(1.5 * (double)((FireBossEntity)this.mob).getScale(), 0.0, 0.0).zRot(Mth.lerp((float)((float)i / ((float)count - 1.0f)), (float)0.0f, (float)((float)(-Math.PI)))).yRot(yAngle).add(0.0, (double)((FireBossEntity)this.mob).getEyeHeight(), 0.0);
                FieryDaggerEntity dagger = new FieryDaggerEntity(((FireBossEntity)this.mob).level);
                dagger.setOwner((Entity)this.mob);
                dagger.ownerTrack = offset;
                dagger.setTarget((Entity)((FireBossEntity)this.mob).getTarget());
                dagger.setPos(pos.add(offset.yRot(((FireBossEntity)this.mob).getYRot())));
                dagger.delay = delay + i * 2;
                dagger.setDamage((float)(((FireBossEntity)this.mob).getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75));
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

