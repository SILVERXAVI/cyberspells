/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.target.TargetGoal
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.AABB
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class GenericDefendVillageTargetGoal
extends TargetGoal {
    private final Mob protector;
    @Nullable
    private LivingEntity potentialTarget;
    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);

    public GenericDefendVillageTargetGoal(Mob mob) {
        super(mob, false, true);
        this.protector = mob;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        AABB aabb = this.protector.getBoundingBox().inflate(10.0, 8.0, 10.0);
        List list = this.protector.level.getNearbyEntities(Villager.class, this.attackTargeting, (LivingEntity)this.protector, aabb);
        List list1 = this.protector.level.getNearbyPlayers(this.attackTargeting, (LivingEntity)this.protector, aabb);
        for (Villager villager : list) {
            for (Player player : list1) {
                int i = villager.getPlayerReputation(player);
                if (i > -100) continue;
                this.potentialTarget = player;
            }
        }
        if (this.potentialTarget == null) {
            return false;
        }
        return !(this.potentialTarget instanceof Player) || !this.potentialTarget.isSpectator() && !((Player)this.potentialTarget).isCreative();
    }

    public void start() {
        this.protector.setTarget(this.potentialTarget);
        super.start();
    }
}

