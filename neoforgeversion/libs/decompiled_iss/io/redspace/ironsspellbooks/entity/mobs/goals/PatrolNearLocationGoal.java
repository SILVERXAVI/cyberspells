/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.util.LandRandomPos
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.function.Supplier;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PatrolNearLocationGoal
extends WaterAvoidingRandomStrollGoal {
    Vec3 origin;
    Supplier<Vec3> originHolder = () -> ((PathfinderMob)pMob).position();
    float radiusSqr;

    public PatrolNearLocationGoal(PathfinderMob pMob, float radius, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
        this.radiusSqr = radius * radius;
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 f = super.getPosition();
        if (this.origin == null) {
            this.origin = this.originHolder.get();
        }
        if (this.mob.position().horizontalDistanceSqr() > (double)this.radiusSqr) {
            f = LandRandomPos.getPosTowards((PathfinderMob)this.mob, (int)8, (int)4, (Vec3)this.origin);
        }
        return f;
    }
}

