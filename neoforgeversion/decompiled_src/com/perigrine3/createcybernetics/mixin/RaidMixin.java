/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.raid.Raid
 *  net.minecraft.world.entity.raid.Raider
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.perigrine3.createcybernetics.mixin;

import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Raid.class})
public abstract class RaidMixin {
    @Shadow
    @Final
    private ServerLevel level;
    @Unique
    private boolean createcybernetics$spawnedSmasher = false;

    @Inject(method={"spawnGroup"}, at={@At(value="TAIL")})
    private void createcybernetics$maybeSpawnSmasher(BlockPos pos, CallbackInfo ci) {
        if (this.createcybernetics$spawnedSmasher) {
            return;
        }
        Raid raid = (Raid)this;
        int wave = raid.getGroupsSpawned();
        if (wave < 3) {
            return;
        }
        int omenLevel = raid.getRaidOmenLevel();
        float chance = RaidMixin.createcybernetics$computeSmasherChance(this.level.getDifficulty(), omenLevel, wave);
        if (chance <= 0.0f) {
            return;
        }
        if (this.level.getRandom().nextFloat() >= chance) {
            return;
        }
        SmasherEntity smasher = (SmasherEntity)ModEntities.SMASHER.get().create((Level)this.level);
        if (smasher == null) {
            return;
        }
        smasher.moveTo((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, this.level.getRandom().nextFloat() * 360.0f, 0.0f);
        smasher.finalizeSpawn((ServerLevelAccessor)this.level, this.level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
        this.level.addFreshEntity((Entity)smasher);
        raid.addWaveMob(wave, (Raider)smasher, true);
        this.createcybernetics$spawnedSmasher = true;
    }

    @Unique
    private static float createcybernetics$computeSmasherChance(Difficulty difficulty, int omenLevel, int wave) {
        float chance = 0.08f;
        chance += 0.1f * (float)(wave - 2);
        chance += 0.07f * (float)(omenLevel - 1);
        return Mth.clamp((float)(chance += (switch (difficulty) {
            default -> throw new MatchException(null, null);
            case Difficulty.PEACEFUL -> -1.0f;
            case Difficulty.EASY -> -0.01f;
            case Difficulty.NORMAL -> 0.0f;
            case Difficulty.HARD -> 0.5f;
        })), (float)0.0f, (float)0.85f);
    }
}

