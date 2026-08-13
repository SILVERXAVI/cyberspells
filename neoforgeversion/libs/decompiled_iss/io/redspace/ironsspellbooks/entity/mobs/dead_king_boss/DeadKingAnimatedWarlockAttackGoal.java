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
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.network.SyncAnimationPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class DeadKingAnimatedWarlockAttackGoal
extends WarlockAttackGoal {
    final DeadKingBoss deadKing;
    int meleeAnimTimer = -1;
    public DeadKingBoss.AttackType currentAttack;
    public DeadKingBoss.AttackType nextAttack;
    public DeadKingBoss.AttackType queueCombo;

    public DeadKingAnimatedWarlockAttackGoal(DeadKingBoss abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.deadKing = abstractSpellCastingMob;
        this.nextAttack = this.randomizeNextAttack(0.0f);
        this.wantsToMelee = true;
    }

    @Override
    public boolean isActing() {
        return super.isActing() || this.meleeAnimTimer > 0;
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        float meleeRange = this.meleeRange();
        if (this.meleeAnimTimer < 0 && (!this.wantsToMelee || distanceSquared > (double)(meleeRange * meleeRange) || this.spellCastingMob.isCasting())) {
            super.handleAttackLogic(distanceSquared);
            return;
        }
        this.mob.getLookControl().setLookAt((Entity)this.target);
        boolean bl = this.deadKing.isMeleeing = this.meleeAnimTimer > 0;
        if (this.meleeAnimTimer > 0) {
            this.forceFaceTarget();
            --this.meleeAnimTimer;
            if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer - 4)) {
                if (this.currentAttack == DeadKingBoss.AttackType.SLAM) {
                    this.mob.playSound((SoundEvent)SoundRegistry.DEAD_KING_SLAM.get());
                } else {
                    this.playSwingSound();
                }
            } else if (this.currentAttack.data.isHitFrame(this.meleeAnimTimer)) {
                Vec3 lunge = this.target.position().subtract(this.mob.position()).normalize().scale((double)0.35f);
                this.mob.push(lunge.x, lunge.y, lunge.z);
                if (this.currentAttack == DeadKingBoss.AttackType.SLAM) {
                    Vec3 slamPos = this.mob.position().add(this.mob.getForward().multiply(1.0, 0.0, 1.0).normalize().scale(2.5));
                    Vec3 bbHalf = new Vec3((double)meleeRange, (double)meleeRange, (double)meleeRange).scale(0.3);
                    float damage = (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5f;
                    this.mob.level.getEntitiesOfClass(LivingEntity.class, new AABB(slamPos.subtract(bbHalf), slamPos.add(bbHalf))).forEach(entity -> {
                        if (entity.isPickable() && !DamageSources.isFriendlyFireBetween((Entity)this.mob, (Entity)entity)) {
                            Player player;
                            entity.hurt(this.mob.level().damageSources().mobAttack((LivingEntity)this.mob), damage);
                            Vec3 impulse = slamPos.subtract(entity.position()).add(0.0, 0.75, 0.0).normalize().scale(Mth.lerp((double)(entity.distanceToSqr(this.mob.position()) / (double)(meleeRange * meleeRange)), (double)2.0, (double)0.5));
                            entity.setDeltaMovement(entity.getDeltaMovement().add(impulse));
                            entity.hurtMarked = true;
                            if (entity instanceof Player && (player = (Player)entity).isBlocking()) {
                                player.disableShield();
                            }
                        }
                    });
                } else if (distanceSquared <= (double)(meleeRange * meleeRange) && Utils.hasLineOfSight(this.mob.level, (Entity)this.mob, (Entity)this.target, true)) {
                    boolean flag = this.mob.doHurtTarget((Entity)this.target);
                    this.target.invulnerableTime = 0;
                    if (flag && this.currentAttack.data.isSingleHit() && (this.mob.getRandom().nextFloat() < 0.75f || this.target.isBlocking())) {
                        this.queueCombo = this.randomizeNextAttack(0.0f);
                    }
                }
            }
        } else if (this.queueCombo != null && this.target != null && !this.target.isDeadOrDying()) {
            this.nextAttack = this.queueCombo;
            this.queueCombo = null;
            this.doMeleeAction();
        } else if (this.meleeAnimTimer == 0) {
            this.nextAttack = this.randomizeNextAttack((float)distanceSquared);
            this.resetMeleeAttackInterval(distanceSquared);
            this.meleeAnimTimer = -1;
        } else if (distanceSquared < (double)(meleeRange * meleeRange) * 1.2 * 1.2) {
            if (this.hasLineOfSight && --this.meleeAttackDelay == 0) {
                this.doMeleeAction();
            } else if (this.meleeAttackDelay < 0) {
                this.resetMeleeAttackInterval(distanceSquared);
            }
        }
    }

    private DeadKingBoss.AttackType randomizeNextAttack(float distanceSquared) {
        return this.mob.getRandom().nextFloat() < 0.3f ? DeadKingBoss.AttackType.SLAM : DeadKingBoss.AttackType.DOUBLE_SWING;
    }

    private void forceFaceTarget() {
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
            PacketDistributor.sendToPlayersTrackingEntity((Entity)this.deadKing, new SyncAnimationPacket<DeadKingBoss>(this.currentAttack.toString(), this.deadKing), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        this.mob.getLookControl().setLookAt((Entity)this.target);
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
        this.mob.playSound((SoundEvent)SoundRegistry.DEAD_KING_SWING.get(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)this.mob.getRandom(), (int)9, (int)13) * 0.1f);
    }
}

