/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Plane
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.perigrine3.createcybernetics.effect;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SyntheticSetulesEffect
extends MobEffect {
    private static final Map<UUID, Integer> WALL_GRACE_TICKS = new ConcurrentHashMap<UUID, Integer>();
    private static final int WALL_GRACE = 8;
    private static final double CLIMB_SPEED = 0.22;
    private static final double MAX_UP = 0.3;
    private static final float INPUT_EPS = 0.01f;
    private static final double WALL_SAMPLE_DIST = 0.08;

    public SyntheticSetulesEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        boolean nearWall;
        if (!(entity instanceof Player)) {
            return true;
        }
        Player player = (Player)entity;
        UUID id = player.getUUID();
        if (player.horizontalCollision) {
            WALL_GRACE_TICKS.put(id, 8);
        } else {
            Integer t = WALL_GRACE_TICKS.get(id);
            if (t != null) {
                int next = t - 1;
                if (next <= 0) {
                    WALL_GRACE_TICKS.remove(id);
                } else {
                    WALL_GRACE_TICKS.put(id, next);
                }
            }
        }
        boolean shift = player.isShiftKeyDown();
        float zza = player.zza;
        float xxa = player.xxa;
        boolean hasMoveInput = Math.abs(zza) > 0.01f || Math.abs(xxa) > 0.01f;
        boolean onWallCollision = player.horizontalCollision || WALL_GRACE_TICKS.containsKey(id);
        boolean bl = nearWall = onWallCollision || SyntheticSetulesEffect.isNearWall(player);
        if (!nearWall) {
            return true;
        }
        Vec3 v = player.getDeltaMovement();
        if (shift && !hasMoveInput) {
            player.setDeltaMovement(0.0, 0.0, 0.0);
            player.hurtMarked = true;
            return true;
        }
        if (hasMoveInput && onWallCollision) {
            double speed = 0.22 + 0.03 * (double)amplifier;
            double newY = Math.min(speed, 0.3);
            player.setDeltaMovement(v.x, newY, v.z);
            player.hurtMarked = true;
            return true;
        }
        return true;
    }

    private static boolean isNearWall(Player player) {
        Level level = player.level();
        AABB box = player.getBoundingBox();
        double y1 = box.maxY - 0.1;
        double y0 = box.minY + 0.1;
        if (y1 <= y0) {
            return false;
        }
        AABB body = new AABB(box.minX, y0, box.minZ, box.maxX, y1, box.maxZ);
        BlockPos base = player.blockPosition();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (!SyntheticSetulesEffect.hitsWallAt(level, body, base, dir, 0) && !SyntheticSetulesEffect.hitsWallAt(level, body, base, dir, 1)) continue;
            return true;
        }
        return false;
    }

    private static boolean hitsWallAt(Level level, AABB body, BlockPos base, Direction dir, int yOff) {
        BlockPos pos = base.offset(dir.getStepX(), yOff, dir.getStepZ());
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        VoxelShape shape = state.getCollisionShape((BlockGetter)level, pos);
        if (shape.isEmpty()) {
            return false;
        }
        AABB probe = body.move((double)dir.getStepX() * 0.08, 0.0, (double)dir.getStepZ() * 0.08);
        for (AABB aabb : shape.toAabbs()) {
            AABB worldAabb = aabb.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            if (!worldAabb.intersects(probe)) continue;
            return true;
        }
        return false;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}

