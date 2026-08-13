/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.FogRenderer$FogData
 *  net.minecraft.client.renderer.FogRenderer$FogMode
 *  net.minecraft.client.renderer.FogRenderer$MobEffectFogFunction
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PlanarSightEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public PlanarSightEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.level.isClientSide && livingEntity == Minecraft.getInstance().player) {
            for (int i = 0; i < 3; ++i) {
                Vec3 pos = new Vec3(Utils.getRandomScaled(16.0), Utils.getRandomScaled(5.0) + 5.0, Utils.getRandomScaled(16.0)).add(livingEntity.position());
                Vec3 random = new Vec3(Utils.getRandomScaled(0.08f), Utils.getRandomScaled(0.08f), Utils.getRandomScaled(0.08f));
                livingEntity.level.addParticle((ParticleOptions)ParticleTypes.WHITE_ASH, pos.x, pos.y, pos.z, random.x, random.y, random.z);
            }
        }
        return true;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class EcholocationBlindnessFogFunction
    implements FogRenderer.MobEffectFogFunction {
        public Holder<MobEffect> getMobEffect() {
            return MobEffectRegistry.PLANAR_SIGHT;
        }

        public void setupFog(FogRenderer.FogData fogData, LivingEntity entity, MobEffectInstance mobEffectInstance, float p_234184_, float p_234185_) {
            float f = 160.0f;
            if (fogData.mode == FogRenderer.FogMode.FOG_SKY) {
                fogData.start = 0.0f;
                fogData.end = f * 0.25f;
            } else {
                fogData.start = -f * 0.5f;
                fogData.end = f;
            }
        }
    }
}

