/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class KeeperAnimatedWarlockAttackGoal
extends WarlockAttackGoal {
    final KeeperEntity keeper;
    int meleeAnimTimer = -1;
    public KeeperEntity.AttackType currentAttack;
    public KeeperEntity.AttackType nextAttack;
    public KeeperEntity.AttackType queueCombo;
    private boolean hasLunged;
    private boolean hasHitLunge;
    private Vec3 oldLungePos;

    public KeeperAnimatedWarlockAttackGoal(KeeperEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.keeper = abstractSpellCastingMob;
        this.nextAttack = this.randomizeNextAttack(0.0f);
        this.wantsToMelee = true;
    }

    @Override
    protected float meleeBias() {
        return 1.0f;
    }

    @Override
    public boolean isActing() {
        return super.isActing() || this.meleeAnimTimer > 0;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float meleeRange = this.meleeRange();
        float distance = Mth.sqrt((float)((float)distanceSquared));
        this.mob.getLookControl().setLookAt((Entity)this.target);
        if (this.meleeAnimTimer > 0) {
            this.forceFaceTarget();
            --this.meleeAnimTimer;
            if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer - 4)) {
                if (this.currentAttack != KeeperEntity.AttackType.Lunge) {
                    this.playSwingSound();
                }
            } else if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer)) {
                if (this.currentAttack != KeeperEntity.AttackType.Lunge) {
                    Vec3 lunge = this.target.position().subtract(this.mob.position()).normalize().scale((double)0.55f);
                    this.mob.push(lunge.x, lunge.y, lunge.z);
                    if (distance <= meleeRange && Utils.hasLineOfSight(this.mob.level, (Entity)this.mob, (Entity)this.target, true)) {
                        boolean flag = this.mob.doHurtTarget((Entity)this.target);
                        this.target.invulnerableTime = 0;
                        if (flag) {
                            this.playImpactSound();
                            if (this.currentAttack.data.isSingleHit() && (this.mob.getRandom().nextFloat() < 0.75f || this.target.isBlocking())) {
                                this.queueCombo = this.randomizeNextAttack(0.0f);
                            }
                        }
                    }
                } else {
                    if (!this.hasLunged) {
                        Vec3 lunge = this.target.position().subtract(this.mob.position()).normalize().multiply(2.4, 0.5, 2.4).add(0.0, 0.15, 0.0);
                        this.mob.push(lunge.x, lunge.y, lunge.z);
                        this.oldLungePos = this.mob.position();
                        this.mob.getNavigation().stop();
                        this.hasLunged = true;
                        this.playSwingSound();
                    }
                    if (!this.hasHitLunge && distance <= meleeRange * 0.45f) {
                        if (this.mob.doHurtTarget((Entity)this.target)) {
                            this.playImpactSound();
                        }
                        Vec3 knockback = this.oldLungePos.subtract(this.target.position());
                        this.target.knockback(1.0, knockback.x, knockback.z);
                        this.hasHitLunge = true;
                    }
                }
            }
        } else if (this.queueCombo != null && this.target != null && !this.target.isDeadOrDying()) {
            this.nextAttack = this.queueCombo;
            this.queueCombo = null;
            this.doMeleeAction();
        } else if (this.meleeAnimTimer == 0) {
            this.nextAttack = this.randomizeNextAttack(distance);
            this.resetMeleeAttackInterval(distanceSquared);
            this.meleeAnimTimer = -1;
        } else {
            int n = this.nextAttack == KeeperEntity.AttackType.Lunge ? 3 : 1;
            if (distance < meleeRange * (float)n) {
                if (this.hasLineOfSight && --this.meleeAttackDelay == 0) {
                    this.doMeleeAction();
                } else if (this.meleeAttackDelay < 0) {
                    this.resetMeleeAttackInterval(distanceSquared);
                }
            } else if (--this.meleeAttackDelay < 0) {
                this.resetMeleeAttackInterval(distanceSquared);
                this.nextAttack = this.randomizeNextAttack(distance);
            }
        }
    }

    private KeeperEntity.AttackType randomizeNextAttack(float distance) {
        int i;
        float meleeRange = this.meleeRange();
        if (distance < meleeRange * 1.5f) {
            i = KeeperEntity.AttackType.values().length - 1;
        } else {
            if (this.mob.getRandom().nextFloat() < 0.25f && distance > meleeRange * 2.5f) {
                return KeeperEntity.AttackType.Lunge;
            }
            i = KeeperEntity.AttackType.values().length;
        }
        return KeeperEntity.AttackType.values()[this.mob.getRandom().nextInt(i)];
    }

    private void forceFaceTarget() {
        if (this.hasLunged) {
            return;
        }
        double d0 = this.target.getX() - this.mob.getX();
        double d1 = this.target.getZ() - this.mob.getZ();
        float yRot = (float)(Mth.atan2((double)d1, (double)d0) * 57.2957763671875) - 90.0f;
        this.mob.setYBodyRot(yRot);
        this.mob.setYHeadRot(yRot);
        this.mob.setYRot(yRot);
    }

    @Override
    protected void doMeleeAction() {
        this.currentAttack = this.nextAttack;
        if (this.currentAttack != null) {
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.meleeAnimTimer = this.currentAttack.data.lengthInTicks;
            this.hasLunged = false;
            this.hasHitLunge = false;
            PacketDistributor.sendToPlayersTrackingEntity((Entity)this.keeper, new SyncAnimationPacket<KeeperEntity>(this.currentAttack.toString(), this.keeper), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        float meleeRange = this.meleeRange();
        if (this.target.isDeadOrDying()) {
            this.mob.getNavigation().stop();
        } else if (distanceSquared > (double)(meleeRange * meleeRange)) {
            this.mob.getNavigation().moveTo((Entity)this.target, this.speedModifier * (double)1.3f);
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
        this.mob.playSound((SoundEvent)SoundRegistry.KEEPER_SWING.get(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)this.mob.getRandom(), (int)9, (int)13) * 0.1f);
    }

    public void playImpactSound() {
        this.mob.playSound((SoundEvent)SoundRegistry.KEEPER_SWORD_IMPACT.get(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)this.mob.getRandom(), (int)9, (int)13) * 0.1f);
    }
}

