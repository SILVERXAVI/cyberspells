/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class FindSupportableTargetGoal<M extends Mob>
extends NearestAttackableTargetGoal<LivingEntity> {
    SupportMob supportMob;

    public FindSupportableTargetGoal(M pMob, Class pTargetType, boolean pMustSee, Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, pTargetType, 5, pMustSee, false, pTargetPredicate);
        this.supportMob = (SupportMob)pMob;
    }

    public void start() {
        super.start();
        this.supportMob.setSupportTarget(this.target);
        this.mob.setTarget(null);
    }
}

