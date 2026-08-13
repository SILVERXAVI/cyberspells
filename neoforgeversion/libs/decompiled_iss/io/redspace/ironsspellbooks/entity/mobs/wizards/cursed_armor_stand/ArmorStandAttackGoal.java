/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.ai.goal.WrappedGoal
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public class ArmorStandAttackGoal
extends GenericAnimatedWarlockAttackGoal<CursedArmorStandEntity> {
    public static final float PROTECTION_RANGE = 18.0f;
    public static final float PROTECTION_RANGE_SQR = 324.0f;

    public ArmorStandAttackGoal(CursedArmorStandEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
    }

    @Override
    protected AttackAnimationData getNextAttack(float distanceSquared) {
        if (this.moveList.isEmpty()) {
            return null;
        }
        if (((CursedArmorStandEntity)this.mob).getMainHandItem().isEmpty()) {
            return (AttackAnimationData)this.moveList.get(1);
        }
        return super.getNextAttack(distanceSquared);
    }

    @Override
    public void playSwingSound() {
        if (((CursedArmorStandEntity)this.mob).getMainHandItem().isEmpty()) {
            ((CursedArmorStandEntity)this.mob).playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)((CursedArmorStandEntity)this.mob).getRandom(), (int)9, (int)11) * 0.1f);
        } else {
            super.playSwingSound();
        }
    }

    @Override
    protected void doMovement(double distanceSquared) {
        if (((CursedArmorStandEntity)this.mob).spawn != null) {
            double boundaryDistanceSqr = ((CursedArmorStandEntity)this.mob).spawn.distanceToSqr(this.target.position());
            if (boundaryDistanceSqr > 324.0) {
                this.wantsToMelee = false;
                if (boundaryDistanceSqr > 576.0) {
                    ((CursedArmorStandEntity)this.mob).stopBeingAngry();
                    ((CursedArmorStandEntity)this.mob).setLastHurtByMob(null);
                    ((CursedArmorStandEntity)this.mob).setLastHurtByPlayer(null);
                    ((CursedArmorStandEntity)this.mob).setPersistentAngerTarget(null);
                    ((CursedArmorStandEntity)this.mob).setTarget(null);
                    ((CursedArmorStandEntity)this.mob).targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                    this.stop();
                    return;
                }
            } else {
                this.wantsToMelee = true;
            }
        }
        super.doMovement(distanceSquared);
    }
}

