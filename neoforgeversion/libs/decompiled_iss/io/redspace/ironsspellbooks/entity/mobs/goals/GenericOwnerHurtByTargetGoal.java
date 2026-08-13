/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class GenericOwnerHurtByTargetGoal
extends TargetGoal {
    private final Mob entity;
    private final Supplier<Entity> owner;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public GenericOwnerHurtByTargetGoal(Mob entity, Supplier<Entity> getOwner) {
        super(entity, false);
        this.entity = entity;
        this.owner = getOwner;
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
        this.ownerLastHurtBy = owner.getLastHurtByMob();
        if (this.ownerLastHurtBy == null || this.ownerLastHurtBy.isAlliedTo((Entity)this.mob)) {
            return false;
        }
        int i = owner.getLastHurtByMobTimestamp();
        return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && (!((livingEntity = this.ownerLastHurtBy) instanceof IMagicSummon) || (summon = (IMagicSummon)livingEntity).getSummoner() != owner);
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, (Object)this.ownerLastHurtBy, 200L);
        Entity owner = this.owner.get();
        if (owner instanceof LivingEntity) {
            LivingEntity livingOwner = (LivingEntity)owner;
            this.timestamp = livingOwner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}

