/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.Optional;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AbyssalShroudEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public AbyssalShroudEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.level.isClientSide || damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION) || damageSource.is(DamageTypeTags.IS_FALL) || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        RandomSource random = livingEntity.getRandom();
        Level level = livingEntity.level;
        Vec3 sideStep = new Vec3(random.nextBoolean() ? 1.0 : -1.0, 0.0, -0.25);
        sideStep.yRot(livingEntity.getYRot());
        AbyssalShroudEffect.particleCloud(livingEntity);
        Vec3 ground = livingEntity.position().add(sideStep);
        ground = Utils.moveToRelativeGroundLevel(level, ground, 2, 1);
        EntityDimensions dimensions = livingEntity.getDimensions(livingEntity.getPose());
        Vec3 vec3 = ground.add(0.0, (double)dimensions.height() / 2.0, 0.0);
        VoxelShape voxelshape = Shapes.create((AABB)AABB.ofSize((Vec3)vec3, (double)(dimensions.width() + 0.2f), (double)(dimensions.height() + 0.2f), (double)(dimensions.width() + 0.2f)));
        Optional optional = level.findFreePosition(null, voxelshape, vec3, (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width());
        if (optional.isPresent()) {
            ground = ((Vec3)optional.get()).add(0.0, (double)(-dimensions.height() / 2.0f) + 1.0E-6, 0.0);
        }
        if (level.collidesWithSuffocatingBlock(null, AABB.ofSize((Vec3)ground.add(0.0, (double)(dimensions.height() / 2.0f), 0.0), (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width()))) {
            ground = livingEntity.position();
        }
        if (livingEntity.isPassenger()) {
            livingEntity.stopRiding();
        }
        if (!level.getBlockState(BlockPos.containing((Position)ground).below()).isAir()) {
            livingEntity.teleportTo(ground.x, ground.y, ground.z);
            AbyssalShroudEffect.particleCloud(livingEntity);
        }
        if (damageSource.getEntity() != null) {
            livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, damageSource.getEntity().getEyePosition().subtract(0.0, 0.15, 0.0));
        }
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (SoundEvent)SoundRegistry.ABYSSAL_TELEPORT.get(), SoundSource.AMBIENT, 1.0f, 0.9f + random.nextFloat() * 0.2f);
        return true;
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.position().add(0.0, (double)(entity.getBbHeight() / 2.0f), 0.0);
        MagicManager.spawnParticles(entity.level(), (ParticleOptions)ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 70, entity.getBbWidth() / 4.0f, entity.getBbHeight() / 5.0f, entity.getBbWidth() / 4.0f, 0.035, false);
    }

    @Override
    public void clientTick(LivingEntity entity, MobEffectInstance instance) {
        Vec3 backwards = entity.getForward().scale(0.003).reverse().add(0.0, 0.02, 0.0);
        RandomSource random = entity.getRandom();
        for (int i = 0; i < 2; ++i) {
            Vec3 motion = new Vec3((double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f), (double)(random.nextFloat() * 2.0f - 1.0f));
            motion = motion.scale((double)0.04f).add(backwards);
            entity.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, entity.getRandomX((double)0.4f), entity.getRandomY(), entity.getRandomZ((double)0.4f), motion.x, motion.y, motion.z);
        }
    }
}

