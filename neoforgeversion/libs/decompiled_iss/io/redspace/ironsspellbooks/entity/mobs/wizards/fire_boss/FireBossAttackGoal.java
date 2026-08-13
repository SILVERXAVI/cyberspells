/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossAttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.InvokeDaggerKeyframe;
import io.redspace.ironsspellbooks.particle.FlameStrikeParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec3;

public class FireBossAttackGoal
extends GenericAnimatedWarlockAttackGoal<FireBossEntity> {
    private static final AttributeModifier MODIFIER_FIRE_BALLER = new AttributeModifier(IronsSpellbooks.id("fireballer"), 0.5, AttributeModifier.Operation.ADD_VALUE);
    int fireballcooldown;

    public FireBossAttackGoal(FireBossEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
    }

    @Override
    protected void doMovement(double distanceSquared) {
        double speed = (double)(this.spellCastingMob.isCasting() ? 0.75f : 1.0f) * this.movementSpeed();
        ((FireBossEntity)this.mob).lookAt((Entity)this.target, 30.0f, 30.0f);
        float meleeRange = this.meleeRange();
        float strafeMultiplier = this.getStrafeMultiplier();
        if (distanceSquared < (double)this.spellcastingRangeSqr && this.seeTime >= 5) {
            ((FireBossEntity)this.mob).getNavigation().stop();
            if (++this.strafeTime > 40 && ((FireBossEntity)this.mob).getRandom().nextDouble() < 0.08) {
                this.strafingClockwise = !this.strafingClockwise;
                this.strafeTime = 0;
            }
            float strafeForward = this.meleeMoveSpeedModifier;
            strafeForward = distanceSquared > (double)(meleeRange * meleeRange * 3.0f * 3.0f) ? (strafeForward *= 2.0f) : (distanceSquared > (double)(meleeRange * meleeRange) * 0.75 * 0.75 ? (strafeForward *= 1.3f) : (strafeForward *= -1.15f));
            int strafeDir = this.strafingClockwise ? 1 : -1;
            ((FireBossEntity)this.mob).getMoveControl().strafe(strafeForward * strafeMultiplier, (float)speed * (float)strafeDir * strafeMultiplier);
        } else if (((FireBossEntity)this.mob).tickCount % 5 == 0) {
            ((FireBossEntity)this.mob).setXxa(0.0f);
            ((FireBossEntity)this.mob).getNavigation().moveTo((Entity)this.target, this.speedModifier);
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    protected void onHitFrame(AttackKeyframe attackKeyframe, float meleeRange) {
        if (attackKeyframe instanceof InvokeDaggerKeyframe) {
            ((FireBossEntity)this.mob).procSpectralDagger();
            ((FireBossEntity)this.mob).playSound((SoundEvent)SoundRegistry.FIRE_BOSS_ACCENT.get(), 3.0f, 1.0f);
        } else {
            super.onHitFrame(attackKeyframe, meleeRange);
            if (attackKeyframe instanceof FireBossAttackKeyframe) {
                FireBossAttackKeyframe fireKeyframe = (FireBossAttackKeyframe)attackKeyframe;
                boolean mirrored = fireKeyframe.swingData.mirrored();
                boolean vertical = fireKeyframe.swingData.vertical();
                Vec3 forward = ((FireBossEntity)this.mob).getForward();
                float reach = 2.0f * ((FireBossEntity)this.mob).getScale();
                Vec3 hitLocation = ((FireBossEntity)this.mob).getBoundingBox().getCenter().add(((FireBossEntity)this.mob).getForward().multiply((double)reach, 0.5, (double)reach));
                MagicManager.spawnParticles(((FireBossEntity)this.mob).level, new FlameStrikeParticleOptions((float)forward.x, (float)forward.y, (float)forward.z, mirrored, vertical, ((FireBossEntity)this.mob).getScale()), hitLocation.x, hitLocation.y, hitLocation.z, 1, 0.0, 0.0, 0.0, 0.0, true);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        ((FireBossEntity)this.mob).getAttribute((Holder)AttributeRegistry.CAST_TIME_REDUCTION).removeModifier(MODIFIER_FIRE_BALLER);
    }

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        boolean delayNextAttack;
        int shortcut;
        float meleeRange = this.meleeRange();
        if (this.fireballcooldown > 0) {
            if (this.fireballcooldown == 180) {
                ((FireBossEntity)this.mob).getAttribute((Holder)AttributeRegistry.CAST_TIME_REDUCTION).removeModifier(MODIFIER_FIRE_BALLER);
            }
            --this.fireballcooldown;
        } else if (!((FireBossEntity)this.mob).onGround() && distanceSquared > (double)(meleeRange * meleeRange * 2.0f * 2.0f) && !this.isActing()) {
            ((FireBossEntity)this.mob).getAttribute((Holder)AttributeRegistry.CAST_TIME_REDUCTION).addOrUpdateTransientModifier(MODIFIER_FIRE_BALLER);
            ((FireBossEntity)this.mob).initiateCastSpell(SpellRegistry.FIREBALL_SPELL.get(), 5);
            this.fireballcooldown = 200;
            return;
        }
        if (this.meleeAnimTimer > 0 && this.currentAttack != null && this.meleeAnimTimer < (shortcut = 5) && this.currentAttack.attacks.keySet().intStream().noneMatch(i -> i > this.currentAttack.lengthInTicks - shortcut)) {
            this.meleeAnimTimer = 0;
        }
        boolean bl = delayNextAttack = ((FireBossEntity)this.mob).spectralDaggerActive() || !((FireBossEntity)this.mob).onGround() && ((FireBossEntity)this.mob).getRandom().nextBoolean();
        if (delayNextAttack) {
            ++this.meleeAttackDelay;
        }
        super.handleAttackLogic(distanceSquared);
    }

    @Override
    protected void doMeleeAction() {
        super.doMeleeAction();
        if (this.currentAttack != null) {
            float r = this.meleeRange();
            if (((FireBossEntity)this.mob).distanceToSqr((Entity)this.target) > 0.5625 * (double)r * (double)r) {
                int i = this.currentAttack.attacks.keySet().intStream().sorted().findFirst().orElse(0);
                ((FireBossEntity)this.mob).getMoveControl().triggerCustomMovement(i + 5, f -> new Vec3(0.0, 0.0, 0.5 * (double)(1.0f + this.currentAttack.rangeMultiplier)));
            }
        }
    }

    @Override
    protected double movementSpeed() {
        return this.meleeMoveSpeedModifier;
    }

    @Override
    public void playSwingSound() {
        ((FireBossEntity)this.mob).playSound((SoundEvent)SoundRegistry.HELLRAZOR_SWING.get(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)((FireBossEntity)this.mob).getRandom(), (int)9, (int)11) * 0.1f);
    }
}

