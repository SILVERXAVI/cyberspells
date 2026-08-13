/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.goals.HomeOwner;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ReturnToHomeAtNightGoal<T extends PathfinderMob>
extends WaterAvoidingRandomStrollGoal {
    T homeOwnerMob;

    public ReturnToHomeAtNightGoal(T pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
        this.homeOwnerMob = pMob;
    }

    public boolean canUse() {
        return ((HomeOwner)this.homeOwnerMob).getHome() != null && !this.mob.level.isDay() && super.canUse();
    }

    @Nullable
    protected Vec3 getPosition() {
        return ((HomeOwner)this.homeOwnerMob).getHome() == null ? super.getPosition() : Vec3.atBottomCenterOf((Vec3i)((HomeOwner)this.homeOwnerMob).getHome());
    }
}

