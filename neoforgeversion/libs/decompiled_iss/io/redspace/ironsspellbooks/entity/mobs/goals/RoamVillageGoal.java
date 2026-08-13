/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.GlobalPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy
 *  net.minecraft.world.entity.ai.village.poi.PoiTypes
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RoamVillageGoal
extends PatrolNearLocationGoal {
    GlobalPos villagePoi;
    int searchCooldown;

    public RoamVillageGoal(PathfinderMob pMob, float radius, double pSpeedModifier) {
        super(pMob, radius, pSpeedModifier);
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        if (this.villagePoi != null) {
            return Vec3.atBottomCenterOf((Vec3i)this.villagePoi.pos());
        }
        return super.getPosition();
    }

    public boolean canUse() {
        if (this.villagePoi == null && this.searchCooldown-- <= 0) {
            this.findVillagePoi();
            this.searchCooldown = 200;
        }
        boolean canUse = (this.mob.level.isDay() || this.isDuringRaid()) && this.villagePoi != null && super.canUse();
        return canUse;
    }

    private boolean isDuringRaid() {
        return false;
    }

    protected void findVillagePoi() {
        Level level = this.mob.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Optional optional1 = serverLevel.getPoiManager().find(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), x -> true, this.mob.blockPosition(), 100, PoiManager.Occupancy.ANY);
            optional1.ifPresent(blockPos -> {
                this.villagePoi = GlobalPos.of((ResourceKey)serverLevel.dimension(), (BlockPos)blockPos);
            });
        }
    }
}

