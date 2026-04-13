/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.ProjectileImpactEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.ModEffects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class ProjectileDodgeEffect
extends MobEffect {
    private static final int TELEPORT_ATTEMPTS = 64;
    private static final double TELEPORT_RANGE = 64.0;
    private static final String NBT_NEXT_DODGE_TICK = "cc_projectile_dodge_nextTick";
    private static final int DODGE_COOLDOWN_TICKS = 0;

    public ProjectileDodgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    private static boolean tryEndermanStyleTeleport(ServerPlayer player, int attempts, double cubeSize) {
        ServerLevel level = player.serverLevel();
        RandomSource random = player.getRandom();
        double startX = player.getX();
        double startY = player.getY();
        double startZ = player.getZ();
        for (int i = 0; i < attempts; ++i) {
            double z;
            double y;
            double fromX = player.getX();
            double fromY = player.getY();
            double fromZ = player.getZ();
            double x = startX + (random.nextDouble() - 0.5) * cubeSize;
            if (!ProjectileDodgeEffect.tryTeleportToCandidate(player, level, x, y = startY + (double)(random.nextInt((int)cubeSize) - (int)cubeSize / 2), z = startZ + (random.nextDouble() - 0.5) * cubeSize)) continue;
            ProjectileDodgeEffect.spawnEndermanTeleportParticles(level, fromX, fromY, fromZ);
            ProjectileDodgeEffect.spawnEndermanTeleportParticles(level, player.getX(), player.getY(), player.getZ());
            level.playSound(null, fromX, fromY, fromZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    private static boolean tryTeleportToCandidate(ServerPlayer player, ServerLevel level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing((double)x, (double)y, (double)z);
        if (!level.hasChunkAt(pos)) {
            return false;
        }
        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }
        int minY = level.getMinBuildHeight();
        while (pos.getY() > minY && ProjectileDodgeEffect.blocksMotion(level.getBlockState(pos))) {
            pos = pos.below();
        }
        if (pos.getY() <= minY) {
            return false;
        }
        if (ProjectileDodgeEffect.blocksMotion(level.getBlockState(pos))) {
            return false;
        }
        BlockPos below = pos.below();
        if (!ProjectileDodgeEffect.blocksMotion(level.getBlockState(below))) {
            return false;
        }
        if (!level.getFluidState(pos).isEmpty()) {
            return false;
        }
        double tx = (double)pos.getX() + 0.5;
        double ty = pos.getY();
        double tz = (double)pos.getZ() + 0.5;
        AABB moved = player.getBoundingBox().move(tx - player.getX(), ty - player.getY(), tz - player.getZ());
        if (!level.noCollision((Entity)player, moved)) {
            return false;
        }
        if (level.containsAnyLiquid(moved)) {
            return false;
        }
        return player.teleportTo(level, tx, ty, tz, Set.of(), player.getYRot(), player.getXRot());
    }

    public static void spawnEndermanTeleportParticles(ServerLevel level, double x, double y, double z) {
        level.sendParticles((ParticleOptions)ParticleTypes.PORTAL, x, y + 1.0, z, 96, 0.6, 1.2, 0.6, 0.0);
    }

    private static boolean blocksMotion(BlockState state) {
        return state.blocksMotion();
    }

    @EventBusSubscriber(modid="createcybernetics")
    public static final class Events {
        @SubscribeEvent
        public static void onProjectileImpact(ProjectileImpactEvent event) {
            HitResult hit = event.getRayTraceResult();
            if (hit.getType() != HitResult.Type.ENTITY) {
                return;
            }
            EntityHitResult ehr = (EntityHitResult)hit;
            Entity entity = ehr.getEntity();
            if (!(entity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)entity;
            if (!player.hasEffect(ModEffects.PROJECTILE_DODGE_EFFECT)) {
                return;
            }
            if (player.isPassenger()) {
                return;
            }
            Projectile projectile = event.getProjectile();
            if (projectile == null) {
                return;
            }
            long gameTime = player.serverLevel().getGameTime();
            CompoundTag tag = player.getPersistentData();
            if (tag.getLong(ProjectileDodgeEffect.NBT_NEXT_DODGE_TICK) > gameTime) {
                return;
            }
            if (ProjectileDodgeEffect.tryEndermanStyleTeleport(player, 64, 64.0)) {
                event.setCanceled(true);
                projectile.discard();
                tag.putLong(ProjectileDodgeEffect.NBT_NEXT_DODGE_TICK, gameTime + 0L);
            }
        }

        @SubscribeEvent
        public static void onIncomingDamage(LivingIncomingDamageEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (!player.hasEffect(ModEffects.PROJECTILE_DODGE_EFFECT)) {
                return;
            }
            if (player.isPassenger()) {
                return;
            }
            Entity entity = event.getSource().getDirectEntity();
            if (!(entity instanceof Projectile)) {
                return;
            }
            Projectile proj = (Projectile)entity;
            long gameTime = player.serverLevel().getGameTime();
            CompoundTag tag = player.getPersistentData();
            if (tag.getLong(ProjectileDodgeEffect.NBT_NEXT_DODGE_TICK) > gameTime) {
                return;
            }
            if (ProjectileDodgeEffect.tryEndermanStyleTeleport(player, 64, 64.0)) {
                event.setCanceled(true);
                proj.discard();
                tag.putLong(ProjectileDodgeEffect.NBT_NEXT_DODGE_TICK, gameTime + 0L);
            }
        }
    }
}

