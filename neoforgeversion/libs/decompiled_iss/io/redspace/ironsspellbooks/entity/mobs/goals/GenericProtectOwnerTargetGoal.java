/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class GenericProtectOwnerTargetGoal
extends TargetGoal {
    private final Supplier<Entity> owner;
    private int intervalToCheck;
    private final int maxIntensity = 100;
    private int currentIntensity;

    public GenericProtectOwnerTargetGoal(Mob entity, Supplier<Entity> getOwner) {
        super(entity, false);
        this.owner = getOwner;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        Entity entity = this.owner.get();
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity owner = (LivingEntity)entity;
        if (--this.intervalToCheck <= 0) {
            List entities = owner.level.getEntitiesOfClass(Mob.class, owner.getBoundingBox().inflate(16.0, 8.0, 16.0), potentionalAggressor -> {
                IMagicSummon summon;
                LivingEntity patt0$temp;
                return potentionalAggressor.getTarget() != null && (potentionalAggressor.getTarget().getUUID().equals(owner.getUUID()) || (patt0$temp = potentionalAggressor.getTarget()) instanceof IMagicSummon && (summon = (IMagicSummon)patt0$temp).getSummoner() != null && summon.getSummoner().getUUID().equals(owner.getUUID())) && Utils.hasLineOfSight(this.mob.level, this.mob.getEyePosition(), potentionalAggressor.getEyePosition(), false);
            });
            if (entities.isEmpty()) {
                this.currentIntensity = Math.max(0, this.currentIntensity - 10);
                return false;
            }
            this.mob.setTarget((LivingEntity)entities.stream().min(Comparator.comparingDouble(o -> o.distanceToSqr((Entity)owner))).orElse((Mob)entities.getFirst()));
            return true;
        }
        int i = owner.getLastHurtByMobTimestamp();
        int tick = owner.tickCount;
        int combatIntervalModifier = Math.clamp((long)((tick - i) / 5), (int)0, (int)200);
        int intensityModifier = 100 - this.currentIntensity;
        this.intervalToCheck = 20 + combatIntervalModifier + intensityModifier;
        return false;
    }

    public void start() {
        this.currentIntensity = 100;
        super.start();
    }
}

