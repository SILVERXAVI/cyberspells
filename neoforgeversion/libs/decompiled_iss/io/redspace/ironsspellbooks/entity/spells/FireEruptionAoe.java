/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FireEruptionAoe
extends AoeEntity {
    int waveAnim = -1;

    public FireEruptionAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public FireEruptionAoe(Level level, float radius) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.FIRE_ERUPTION_AOE.get()), level);
        this.setRadius(radius);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        SpellDamageSource damageSource = SpellRegistry.RAISE_HELL_SPELL.get().getDamageSource((Entity)(this.getOwner() == null ? this : this.getOwner()));
        DamageSources.ignoreNextKnockback(target);
        if (target.hurt((DamageSource)damageSource, this.getDamage())) {
            target.igniteForSeconds(5.0f);
            target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.65, 0.0));
            target.hurtMarked = true;
        }
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
        float radius = this.getRadius();
        Level level = this.level;
        int n = this.waveAnim++;
        if ((float)n < radius) {
            if (!level.isClientSide) {
                BlockPos blockPos;
                Vec3 vec3;
                int i;
                if (this.waveAnim % 2 == 0) {
                    float volume = (float)(this.waveAnim + 8) / 16.0f;
                    this.playSound((SoundEvent)SoundRegistry.EARTHQUAKE_IMPACT.get(), volume, (float)Utils.random.nextIntBetweenInclusive(90, 110) * 0.01f);
                }
                float circumferenceMin = (float)((this.waveAnim - 1) * 2) * 3.14f;
                float circumferenceMax = (float)((this.waveAnim + 1) * 2) * 3.14f;
                int minBlocks = Mth.clamp((int)((int)circumferenceMin), (int)0, (int)250);
                int maxBlocks = Mth.clamp((int)((int)circumferenceMax), (int)0, (int)250);
                float anglePerBlockMin = 360.0f / (float)minBlocks;
                float anglePerBlockMax = 360.0f / (float)maxBlocks;
                for (i = 0; i < minBlocks; ++i) {
                    vec3 = new Vec3((double)((float)this.waveAnim * Mth.cos((float)(anglePerBlockMin * (float)i))), 0.0, (double)((float)this.waveAnim * Mth.sin((float)(anglePerBlockMin * (float)i))));
                    blockPos = BlockPos.containing((Position)Utils.moveToRelativeGroundLevel(level, this.position().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1f + this.random.nextFloat() * 0.2f);
                }
                for (i = 0; i < maxBlocks; ++i) {
                    if (this.random.nextFloat() < 0.15f) continue;
                    vec3 = new Vec3((double)((float)(this.waveAnim + 1) * Mth.cos((float)(anglePerBlockMax * (float)i))), 0.0, (double)((float)(this.waveAnim + 1) * Mth.sin((float)(anglePerBlockMax * (float)i))));
                    blockPos = BlockPos.containing((Position)Utils.moveToRelativeGroundLevel(level, this.position().add(vec3), 4));
                    if (!level.getBlockState(blockPos.below()).isFaceSturdy((BlockGetter)level, blockPos.below(), Direction.UP)) continue;
                    Utils.createTremorBlockWithState(level, Blocks.FIRE.defaultBlockState(), blockPos, 0.1f + this.random.nextFloat() * 0.2f);
                }
                List targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getInflation().x, this.getInflation().y, this.getInflation().z));
                int r1Sqr = this.waveAnim * this.waveAnim;
                int r2Sqr = (this.waveAnim + 1) * (this.waveAnim + 1);
                for (LivingEntity target : targets) {
                    double distanceSqr = target.distanceToSqr((Entity)this);
                    if (!this.canHitEntity((Entity)target) || !(distanceSqr >= (double)r1Sqr) || !(distanceSqr <= (double)r2Sqr) || !this.canHitTargetForGroundContext(target)) continue;
                    this.applyEffect(target);
                }
            } else {
                int particles = (int)((float)((this.waveAnim + 1) * 2) * 3.14f * 2.5f);
                float anglePerParticle = (float)Math.PI * 2 / (float)particles;
                for (int i = 0; i < particles; ++i) {
                    Vec3 trig = new Vec3((double)Mth.cos((float)(anglePerParticle * (float)i)), 0.0, (double)Mth.sin((float)(anglePerParticle * (float)i)));
                    float r = Mth.lerp((float)Utils.random.nextFloat(), (float)this.waveAnim, (float)(this.waveAnim + 1));
                    Vec3 pos = trig.scale((double)r).add(Utils.getRandomVec3(0.4)).add(this.position()).add(0.0, 0.5, 0.0);
                    Vec3 motion = trig.add(Utils.getRandomVec3(0.5)).scale(0.1);
                    level.addParticle(ParticleHelper.FIRE, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                }
            }
        } else {
            this.discard();
        }
    }

    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return !this.level.noCollision(target.getBoundingBox().move(new Vec3(0.0, -0.9999, 0.0)));
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0.0, 5.0, 0.0);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)3.0f);
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}

