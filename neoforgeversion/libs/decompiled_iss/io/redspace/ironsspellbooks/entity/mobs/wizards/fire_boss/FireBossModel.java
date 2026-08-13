/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.WalkAnimationState
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector2f
 *  org.joml.Vector3d
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class FireBossModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_boss/tyros.png");
    public static final ResourceLocation TEXTURE_SOUL_MODE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_boss/tyros_soul_mode.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/tyros.geo.json");
    private static final float tilt = 0.2617994f;
    private static final Vector3f forward = new Vector3f(0.0f, 0.0f, Mth.sin((float)0.2617994f) * -12.0f);
    int lastTick;

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        FireBossEntity fireBossEntity;
        if (object instanceof FireBossEntity && (fireBossEntity = (FireBossEntity)object).isSoulMode()) {
            return TEXTURE_SOUL_MODE;
        }
        return TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        if (Minecraft.getInstance().isPaused()) {
            return;
        }
        if (entity instanceof FireBossEntity) {
            GeoBone rightArm;
            FireBossEntity fireBossEntity = (FireBossEntity)entity;
            this.handleParticles(fireBossEntity);
            float partialTick = animationState.getPartialTick();
            Vector2f limbSwing = this.getLimbSwing(entity, entity.walkAnimation, partialTick);
            fireBossEntity.isAnimatingDampener = entity.isAnimating() ? Mth.lerp((float)(0.15f * partialTick), (float)fireBossEntity.isAnimatingDampener, (float)0.0f) : Mth.lerp((float)(0.05f * partialTick), (float)fireBossEntity.isAnimatingDampener, (float)1.0f);
            if (entity.getMainHandItem().is(ItemRegistry.HELLRAZOR) || entity.getMainHandItem().is(ItemRegistry.DECREPIT_SCYTHE)) {
                rightArm = this.getAnimationProcessor().getBone("right_arm");
                GeoBone rightHand = this.getAnimationProcessor().getBone("bipedHandRight");
                Vector3f armPose = new Vector3f(-30.0f, -30.0f, 10.0f);
                armPose.mul((float)Math.PI / 180 * fireBossEntity.isAnimatingDampener);
                this.transformStack.pushRotation(rightArm, armPose);
                Vector3f scythePos = new Vector3f(-5.0f, 0.0f, -48.0f);
                scythePos.mul((float)Math.PI / 180 * fireBossEntity.isAnimatingDampener);
                this.transformStack.pushRotation(rightHand, scythePos);
                if (!entity.isAnimating()) {
                    float walkDampener = Mth.cos((float)(limbSwing.y() * 0.6662f + (float)Math.PI)) * 2.0f * limbSwing.x() * 0.5f * -0.75f;
                    this.transformStack.pushRotation(rightArm, walkDampener, 0.0f, 0.0f);
                }
            }
            if (fireBossEntity.isHalfHealthAttacking()) {
                rightArm = this.getAnimationProcessor().getBone("right_arm");
                GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
                GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
                GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
                float f = (float)fireBossEntity.tickCount + partialTick;
                this.bobBone(rightArm, f * 3.0f, -4.0f);
                this.bobBone(leftArm, f * 3.0f, 4.0f);
                this.bobBone(rightLeg, f, -1.5f);
                this.bobBone(leftLeg, f + 90.0f, 1.5f);
            }
        }
        super.setCustomAnimations(entity, instanceId, animationState);
    }

    public void handleParticles(FireBossEntity entity) {
        Vec3 random;
        GeoBone particleEmitter = this.getAnimationProcessor().getBone("particle_emitter");
        GeoBone body = this.getAnimationProcessor().getBone("body");
        GeoBone offhand = this.getAnimationProcessor().getBone("bipedHandLeft");
        if (entity.clientDaggerParticles) {
            if (offhand.isTrackingMatrices()) {
                Vec3 random2;
                int i;
                Vector3d pos = offhand.getWorldPosition();
                for (i = 0; i < 15; ++i) {
                    random2 = Utils.getRandomVec3(0.25);
                    entity.level.addParticle(ParticleHelper.FIERY_SPARKS, pos.x, pos.y, pos.z, random2.x, random2.y, random2.z);
                }
                for (i = 0; i < 15; ++i) {
                    random2 = Utils.getRandomVec3(0.25);
                    entity.level.addParticle(ParticleHelper.EMBERS, pos.x, pos.y, pos.z, random2.x, random2.y, random2.z);
                }
                for (i = 0; i < 5; ++i) {
                    random2 = Utils.getRandomVec3(0.08);
                    entity.level.addParticle((ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), pos.x + random2.x, pos.y + random2.y, pos.z + random2.z, random2.x, random2.y, random2.z);
                }
                entity.clientDaggerParticles = false;
            } else {
                offhand.setTrackingMatrices(true);
            }
        }
        if (entity.isSpawning()) {
            body.setTrackingMatrices(true);
            if (this.lastTick != entity.tickCount) {
                int particles = entity.getSpawnWalkPercent(0.0f) > 0.7f ? 0 : (int)(10.0f * entity.getSpawnWalkPercent(0.0f));
                this.lastTick = entity.tickCount;
                Vector3d pos = body.getWorldPosition();
                for (int i = 0; i < particles; ++i) {
                    random = Utils.getRandomVec3(0.5);
                    entity.level.addParticle((ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), pos.x + random.x, pos.y + 1.0 + random.y * 2.0, pos.z + random.z, 200.0, 0.0, 0.0);
                }
            }
        } else {
            body.setTrackingMatrices(false);
        }
        if (entity.isSoulMode()) {
            particleEmitter.setTrackingMatrices(true);
            if (this.lastTick != entity.tickCount) {
                this.lastTick = entity.tickCount;
                Vec3 entityMotion = entity.getDeltaMovement().add(0.0, entity.getGravity(), 0.0);
                Vector3d headPos = particleEmitter.getWorldPosition().add(entityMotion.x * 3.0, entityMotion.y * 3.0, entityMotion.z * 3.0).add(0.0, 0.2 * (double)entity.getScale(), 0.0);
                for (int i = 0; i < 1; ++i) {
                    random = Utils.getRandomVec3(0.25);
                    entity.level.addParticle(ParticleHelper.FIRE, headPos.x + random.x, headPos.y + random.y, headPos.z + random.z, entityMotion.x * 0.5, entityMotion.y * 0.5, entityMotion.z * 0.5);
                }
            }
        } else {
            particleEmitter.setTrackingMatrices(false);
        }
    }

    @Override
    protected Vector2f getLimbSwing(AbstractSpellCastingMob entity, WalkAnimationState walkAnimationState, float partialTick) {
        Vector2f swing = super.getLimbSwing(entity, walkAnimationState, partialTick);
        swing.mul(0.6f, 1.0f);
        return swing;
    }
}

