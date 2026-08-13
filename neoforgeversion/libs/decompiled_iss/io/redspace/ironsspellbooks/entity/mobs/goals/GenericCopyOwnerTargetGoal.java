/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class GenericCopyOwnerTargetGoal
extends TargetGoal {
    private final Supplier<Entity> ownerGetter;

    public GenericCopyOwnerTargetGoal(PathfinderMob pMob, Supplier<Entity> ownerGetter) {
        super((Mob)pMob, false);
        this.ownerGetter = ownerGetter;
    }

    public boolean canUse() {
        IMagicSummon summon;
        Entity entity = this.ownerGetter.get();
        if (!(entity instanceof Mob)) {
            return false;
        }
        Mob owner = (Mob)entity;
        LivingEntity target = owner.getTarget();
        if (target == null) {
            return false;
        }
        return this.canAttack(target, TargetingConditions.DEFAULT) && (!(target instanceof IMagicSummon) || (summon = (IMagicSummon)target).getSummoner() != owner);
    }

    public void start() {
        LivingEntity target = ((Mob)this.ownerGetter.get()).getTarget();
        this.mob.setTarget(target);
        this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, (Object)target, 200L);
        super.start();
    }
}

