/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import java.util.EnumSet;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class GenericOwnerHurtTargetGoal
extends TargetGoal {
    private final Supplier<Entity> owner;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public GenericOwnerHurtTargetGoal(Mob entity, Supplier<Entity> ownerGetter) {
        super(entity, false);
        this.owner = ownerGetter;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        IMagicSummon summon;
        LivingEntity livingEntity;
        Entity entity = this.owner.get();
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity owner = (LivingEntity)entity;
        this.ownerLastHurt = owner.getLastHurtMob();
        int i = owner.getLastHurtMobTimestamp();
        return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && (!((livingEntity = this.ownerLastHurt) instanceof IMagicSummon) || (summon = (IMagicSummon)livingEntity).getSummoner() != owner);
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        Entity owner = this.owner.get();
        if (owner instanceof LivingEntity) {
            LivingEntity livingOwner = (LivingEntity)owner;
            this.timestamp = livingOwner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}

