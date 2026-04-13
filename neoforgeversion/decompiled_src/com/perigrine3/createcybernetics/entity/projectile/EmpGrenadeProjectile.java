/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.DustParticleOptions
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.ThrowableItemProjectile
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  org.joml.Vector3f
 */
package com.perigrine3.createcybernetics.entity.projectile;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

public class EmpGrenadeProjectile
extends ThrowableItemProjectile {
    private static final double EFFECT_RADIUS = 6.0;
    private static final int EMP_DURATION_TICKS = 200;
    private static final int AURA_EVERY_TICKS = 5;
    private static final int AURA_REFRESH_TICKS = 40;
    private static final double VISUAL_RADIUS = 6.0;
    private static final int TOTAL_FX_TICKS = 600;
    private static final int PRIMARY_BURST_TICKS = 6;
    private static final int SECONDARY_BURST_TICKS = 10;
    private static final int SECONDARY_HOLD_TICKS = 6;
    private static final int SECONDARY_CYCLE_TICKS = 16;
    private static final DustParticleOptions BLUE_DUST = new DustParticleOptions(new Vector3f(0.65f, 0.85f, 1.0f), 1.15f);
    private boolean fxRunning = false;
    private boolean empApplied = false;
    private long fxStartGameTime = -1L;
    private long fxEndGameTime = -1L;

    public EmpGrenadeProjectile(Level level, LivingEntity owner) {
        super(ModEntities.EMP_GRENADE_PROJECTILE.get(), owner, level);
    }

    public EmpGrenadeProjectile(EntityType<? extends EmpGrenadeProjectile> type, Level level) {
        super(type, level);
    }

    protected Item getDefaultItem() {
        return (Item)ModItems.EMP_GRENADE.get();
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() == HitResult.Type.MISS) {
            return;
        }
        this.detonateNoExplosion();
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            return;
        }
        if (!this.fxRunning) {
            return;
        }
        Level level = this.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel sl = (ServerLevel)level;
        long now = this.level().getGameTime();
        if (this.fxStartGameTime < 0L) {
            this.fxStartGameTime = now;
        }
        if (this.fxEndGameTime < 0L) {
            this.fxEndGameTime = this.fxStartGameTime + 600L;
        }
        if (now >= this.fxEndGameTime) {
            this.discard();
            return;
        }
        int fxAge = (int)Math.max(0L, now - this.fxStartGameTime);
        this.runPrimarySphereFx(sl, fxAge);
        this.runSecondaryLoopSphereFx(sl, fxAge);
        this.applyEmpAura();
    }

    private void detonateNoExplosion() {
        if (this.level().isClientSide) {
            return;
        }
        if (this.fxRunning) {
            return;
        }
        long now = this.level().getGameTime();
        if (!this.empApplied) {
            this.empApplied = true;
            this.level().playSound(null, this.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0f, 1.2f);
            for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0), LivingEntity::isAlive)) {
                target.addEffect(new MobEffectInstance(ModEffects.EMP, 200, 0));
            }
        }
        this.fxRunning = true;
        this.fxStartGameTime = now;
        this.fxEndGameTime = now + 600L;
        this.setDeltaMovement(0.0, 0.0, 0.0);
        this.setNoGravity(true);
    }

    private void applyEmpAura() {
        if (this.level().isClientSide) {
            return;
        }
        if (this.tickCount % 5 != 0) {
            return;
        }
        for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0), LivingEntity::isAlive)) {
            target.addEffect(new MobEffectInstance(ModEffects.EMP, 40, 0));
        }
    }

    private void runPrimarySphereFx(ServerLevel sl, int fxAge) {
        double r;
        if (fxAge < 6) {
            double t = (double)(fxAge + 1) / 6.0;
            r = 6.0 * t;
        } else {
            r = 6.0;
        }
        int count = fxAge < 6 ? 90 : 45;
        double speed = fxAge < 6 ? 0.08 : 0.015;
        this.spawnShell(sl, r, count, speed, true, true);
    }

    private void runSecondaryLoopSphereFx(ServerLevel sl, int fxAge) {
        double speed;
        int count;
        double r;
        int cycleAge = fxAge % 16;
        if (cycleAge < 10) {
            double t = (double)(cycleAge + 1) / 10.0;
            r = 6.0 * t;
            count = 42;
            speed = 0.045;
        } else {
            r = 6.0;
            count = 18;
            speed = 0.01;
        }
        this.spawnShell(sl, r, count, speed, true, true);
    }

    private void spawnShell(ServerLevel sl, double r, int count, double outwardSpeed, boolean spawnSpark, boolean spawnBlueDust) {
        double cx = this.getX();
        double cy = this.getY() + 0.1;
        double cz = this.getZ();
        RandomSource rand = sl.getRandom();
        for (int i = 0; i < count; ++i) {
            double u = rand.nextDouble();
            double v = rand.nextDouble();
            double theta = Math.PI * 2 * u;
            double phi = Math.acos(2.0 * v - 1.0);
            double sinPhi = Math.sin(phi);
            double dx = sinPhi * Math.cos(theta);
            double dy = Math.cos(phi);
            double dz = sinPhi * Math.sin(theta);
            double px = cx + dx * r;
            double py = cy + dy * r;
            double pz = cz + dz * r;
            if (spawnBlueDust) {
                sl.sendParticles((ParticleOptions)BLUE_DUST, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }
            if (!spawnSpark) continue;
            sl.sendParticles((ParticleOptions)ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            sl.sendParticles((ParticleOptions)ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, dx, dy, dz, outwardSpeed);
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("cc_fxRunning", this.fxRunning);
        tag.putBoolean("cc_empApplied", this.empApplied);
        tag.putLong("cc_fxStart", this.fxStartGameTime);
        tag.putLong("cc_fxEnd", this.fxEndGameTime);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.fxRunning = tag.getBoolean("cc_fxRunning");
        this.empApplied = tag.getBoolean("cc_empApplied");
        this.fxStartGameTime = tag.contains("cc_fxStart") ? tag.getLong("cc_fxStart") : -1L;
        long l = this.fxEndGameTime = tag.contains("cc_fxEnd") ? tag.getLong("cc_fxEnd") : -1L;
        if (this.fxRunning) {
            this.setDeltaMovement(0.0, 0.0, 0.0);
            this.setNoGravity(true);
        }
    }
}

