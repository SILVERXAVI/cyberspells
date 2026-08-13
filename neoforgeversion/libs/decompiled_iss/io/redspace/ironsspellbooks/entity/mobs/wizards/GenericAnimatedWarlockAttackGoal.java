/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class GenericAnimatedWarlockAttackGoal<T extends PathfinderMob & IMagicEntity>
extends WarlockAttackGoal {
    protected List<AttackAnimationData> moveList = new ArrayList<AttackAnimationData>();
    protected final T mob;
    protected int meleeAnimTimer = -1;
    @Nullable
    public AttackAnimationData currentAttack;
    @Nullable
    public AttackAnimationData nextAttack;
    @Nullable
    public AttackAnimationData queueCombo;
    float comboChance = 0.3f;

    public GenericAnimatedWarlockAttackGoal(T abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super((IMagicEntity)abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.wantsToMelee = true;
        this.mob = abstractSpellCastingMob;
    }

    @Override
    public boolean isActing() {
        return super.isActing() || this.isMeleeing();
    }

    public boolean isMeleeing() {
        return this.meleeAnimTimer > 0;
    }

    @Override
    public void start() {
        super.start();
        this.nextAttack = this.getNextAttack(0.0f);
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float meleeRange = this.meleeRange();
        float rangeMultiplier = this.nextAttack == null ? 1.0f : this.nextAttack.rangeMultiplier;
        float procRangeSqr = meleeRange * meleeRange * rangeMultiplier * rangeMultiplier * 1.2f * 1.2f;
        if (this.meleeAnimTimer < 0 && (!this.wantsToMelee || distanceSquared > (double)procRangeSqr || ((IMagicEntity)this.mob).isCasting())) {
            super.handleAttackLogic(distanceSquared);
            return;
        }
        this.mob.getLookControl().setLookAt((Entity)this.target);
        if (this.meleeAnimTimer > 0 && this.currentAttack != null) {
            this.forceFaceTarget();
            --this.meleeAnimTimer;
            if (this.currentAttack.isHitFrame(this.meleeAnimTimer)) {
                AttackKeyframe attackData = this.currentAttack.getHitFrame(this.meleeAnimTimer);
                this.onHitFrame(attackData, meleeRange);
            }
            if (this.currentAttack.canCancel && (this.currentAttack.isSingleHit() || this.currentAttack.lengthInTicks - this.meleeAnimTimer > this.currentAttack.attacks.keySet().intStream().sorted().findFirst().orElse(0))) {
                Vec3 delta = this.mob.position().subtract(this.target.position());
                double modifiedDistanceSquared = delta.x * delta.x + delta.y * delta.y * 0.5 * 0.5 + delta.z * delta.z;
                if (modifiedDistanceSquared > (double)(meleeRange * meleeRange) * 1.5 * 1.5) {
                    this.stopMeleeAction();
                }
            }
        } else if (this.queueCombo != null && this.target != null && !this.target.isDeadOrDying()) {
            this.nextAttack = this.queueCombo;
            this.queueCombo = null;
            this.doMeleeAction();
        } else if (this.meleeAnimTimer == 0) {
            this.nextAttack = this.getNextAttack((float)distanceSquared);
            this.resetMeleeAttackInterval(distanceSquared);
            this.meleeAnimTimer = -1;
        } else if (distanceSquared < (double)procRangeSqr) {
            if (this.hasLineOfSight && --this.meleeAttackDelay == 0) {
                this.doMeleeAction();
            } else if (this.meleeAttackDelay < 0) {
                this.resetMeleeAttackInterval(distanceSquared);
            }
        }
    }

    protected void onHitFrame(AttackKeyframe attackKeyframe, float meleeRange) {
        this.playSwingSound();
        float f = -Utils.getAngle(this.mob.getX(), this.mob.getZ(), this.target.getX(), this.target.getZ()) - 1.5707964f;
        Vec3 lunge = attackKeyframe.lungeVector().yRot(f);
        this.doLunge(lunge, meleeRange);
        Vec3 forward = this.mob.getForward();
        List targets = this.currentAttack.areaAttackThreshold.isEmpty() ? List.of(this.target) : ((PathfinderMob)this.mob).level.getEntitiesOfClass(this.target.getClass(), this.mob.getBoundingBox().inflate((double)this.spellcastingRange), entity -> forward.dot(entity.position().subtract(this.mob.position()).normalize()) >= (double)this.currentAttack.areaAttackThreshold.get().floatValue());
        for (LivingEntity target : targets) {
            if (!(target.distanceToSqr(this.mob) <= (double)(meleeRange * meleeRange)) || !Utils.hasLineOfSight(((PathfinderMob)this.mob).level, this.mob, (Entity)target, true)) continue;
            this.handleDamaging(target, attackKeyframe);
        }
    }

    protected void doLunge(Vec3 vector, float meleeRange) {
        this.mob.push(vector.x, vector.y, vector.z);
    }

    protected boolean handleDamaging(LivingEntity target, AttackKeyframe attackData) {
        boolean flag = this.mob.doHurtTarget((Entity)target);
        target.invulnerableTime = 0;
        float f = -Utils.getAngle(this.mob.getX(), this.mob.getZ(), target.getX(), target.getZ()) - 1.5707964f;
        if (flag) {
            this.playImpactSound();
            if (attackData.extraKnockback() != Vec3.ZERO) {
                target.setDeltaMovement(target.getDeltaMovement().add(attackData.extraKnockback().yRot(f)));
            }
            if (this.currentAttack.isSingleHit()) {
                float f2 = this.mob.getRandom().nextFloat();
                int n = target.isBlocking() ? 2 : 1;
                if (f2 < this.comboChance * (float)n) {
                    this.queueCombo = this.getNextAttack(0.0f);
                }
            }
        }
        return flag;
    }

    protected AttackAnimationData getNextAttack(float distanceSquared) {
        if (this.moveList.isEmpty()) {
            return null;
        }
        return this.moveList.get(this.mob.getRandom().nextInt(this.moveList.size()));
    }

    private void forceFaceTarget() {
        double d0 = this.target.getX() - this.mob.getX();
        double d1 = this.target.getZ() - this.mob.getZ();
        float yRot = (float)(Mth.atan2((double)d1, (double)d0) * 57.2957763671875) - 90.0f;
        this.mob.setYBodyRot(yRot);
        this.mob.setYHeadRot(yRot);
        this.mob.setYRot(yRot);
    }

    public void stopMeleeAction() {
        if (this.currentAttack != null) {
            this.meleeAnimTimer = 0;
            PacketDistributor.sendToPlayersTrackingEntity(this.mob, new SyncAnimationPacket<T>("", this.mob), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @Override
    protected void doMeleeAction() {
        this.currentAttack = this.nextAttack;
        if (this.currentAttack != null) {
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.meleeAnimTimer = this.currentAttack.lengthInTicks;
            PacketDistributor.sendToPlayersTrackingEntity(this.mob, new SyncAnimationPacket<T>(this.currentAttack.animationId, this.mob), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() || this.meleeAnimTimer > 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.meleeAnimTimer = -1;
        this.queueCombo = null;
    }

    public void playSwingSound() {
        this.mob.playSound((SoundEvent)SoundRegistry.GENERIC_BLADE_SWING.get(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)this.mob.getRandom(), (int)12, (int)18) * 0.1f);
    }

    public void playImpactSound() {
    }

    public GenericAnimatedWarlockAttackGoal<T> setMoveset(List<AttackAnimationData> moveset) {
        this.moveList = moveset;
        this.nextAttack = this.getNextAttack(0.0f);
        return this;
    }

    public GenericAnimatedWarlockAttackGoal<T> setComboChance(float comboChance) {
        this.comboChance = comboChance;
        return this;
    }
}

