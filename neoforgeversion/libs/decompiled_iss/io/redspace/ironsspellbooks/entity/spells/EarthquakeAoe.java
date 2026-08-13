/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EarthquakeAoe
extends AoeEntity
implements AntiMagicSusceptible {
    public static Map<UUID, EarthquakeAoe> clientEarthquakeOrigins = new HashMap<UUID, EarthquakeAoe>();
    private CameraShakeData cameraShakeData;
    private int slownessAmplifier;
    int waveAnim = -1;

    public EarthquakeAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public EarthquakeAoe(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.EARTHQUAKE_AOE.get()), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        SpellDamageSource damageSource = SpellRegistry.EARTHQUAKE_SPELL.get().getDamageSource((Entity)this, this.getOwner());
        DamageSources.ignoreNextKnockback(target);
        if (target.hurt((DamageSource)damageSource, this.getDamage())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, this.slownessAmplifier));
            target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.5, 0.0));
            target.hurtMarked = true;
        }
    }

    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }

    public void setSlownessAmplifier(int slownessAmplifier) {
        this.slownessAmplifier = slownessAmplifier;
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount == 1) {
            this.createScreenShake();
        }
        if (this.tickCount % 20 == 1) {
            this.playSound((SoundEvent)SoundRegistry.EARTHQUAKE_LOOP.get(), 2.0f, 0.9f + this.random.nextFloat() * 0.15f);
        }
        if (this.tickCount % this.reapplicationDelay == 1) {
            this.waveAnim = 0;
            this.playSound((SoundEvent)SoundRegistry.EARTHQUAKE_IMPACT.get(), 1.5f, 0.9f + this.random.nextFloat() * 0.2f);
        }
        if (!this.level.isClientSide) {
            float radius = this.getRadius();
            Level level = this.level;
            int intensity = Math.min((int)(radius * radius * 0.09f), 15);
            for (int i = 0; i < intensity; ++i) {
                Vec3 vec3 = this.position().add(this.uniformlyDistributedPointInRadius(radius));
                BlockPos blockPos = BlockPos.containing((Position)Utils.moveToRelativeGroundLevel(level, vec3, 4)).below();
                Utils.createTremorBlock(level, blockPos, 0.1f + this.random.nextFloat() * 0.2f);
            }
            if (this.waveAnim >= 0) {
                float circumference = (float)(this.waveAnim * 2) * 3.14f;
                int blocks = Mth.clamp((int)((int)circumference), (int)0, (int)250);
                float anglePerBlock = 360.0f / (float)blocks;
                for (int i = 0; i < blocks; ++i) {
                    Vec3 vec3 = new Vec3((double)((float)this.waveAnim * Mth.cos((float)(anglePerBlock * (float)i))), 0.0, (double)((float)this.waveAnim * Mth.sin((float)(anglePerBlock * (float)i))));
                    BlockPos blockPos = BlockPos.containing((Position)Utils.moveToRelativeGroundLevel(level, this.position().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1f + this.random.nextFloat() * 0.2f);
                }
                int n = this.waveAnim++;
                if ((float)n >= radius) {
                    this.waveAnim = -1;
                    if (this.tickCount + this.reapplicationDelay >= this.duration) {
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0.0, 5.0, 0.0);
    }

    protected void createScreenShake() {
        if (!this.level.isClientSide && !this.isRemoved()) {
            this.cameraShakeData = new CameraShakeData(this.level, this.duration - this.tickCount, this.position(), 15.0f);
            CameraShakeManager.addCameraShake(this.cameraShakeData);
        }
    }

    protected Vec3 uniformlyDistributedPointInRadius(float r) {
        float distance = r * (1.0f - this.random.nextFloat() * this.random.nextFloat());
        float theta = this.random.nextFloat() * 6.282f;
        return new Vec3((double)(distance * Mth.cos((float)theta)), (double)0.2f, (double)(distance * Mth.sin((float)theta)));
    }

    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
        if (!this.level.isClientSide) {
            CameraShakeManager.removeCameraShake(this.cameraShakeData);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)3.0f);
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Slowness", this.slownessAmplifier);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.slownessAmplifier = pCompound.getInt("Slowness");
        this.createScreenShake();
    }
}

