/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EvasionEffect
extends CustomDescriptionMobEffect
implements ISyncedMobEffect {
    public EvasionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        return Component.translatable((String)"tooltip.irons_spellbooks.evasion_description", (Object[])new Object[]{amp}).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectAdded(pLivingEntity, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().setEvasionHitsRemaining(pAmplifier);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.level.isClientSide || damageSource.is(DamageTypeTags.IS_FALL) || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION)) {
            return false;
        }
        SyncedSpellData data = MagicData.getPlayerMagicData(livingEntity).getSyncedData();
        data.subtractEvasionHit();
        if (data.getEvasionHitsRemaining() < 0) {
            livingEntity.removeEffect(MobEffectRegistry.EVASION);
        }
        double d0 = livingEntity.getX();
        double d1 = livingEntity.getY();
        double d2 = livingEntity.getZ();
        double maxRadius = 12.0;
        Level level = livingEntity.level;
        RandomSource random = livingEntity.getRandom();
        for (int i = 0; i < 16; ++i) {
            double minRadius = maxRadius / 2.0;
            Vec3 vec = new Vec3((double)random.nextInt((int)minRadius, (int)maxRadius), 0.0, 0.0);
            int degrees = random.nextInt(360);
            vec = vec.yRot((float)degrees * ((float)Math.PI / 180));
            double x = d0 + vec.x;
            double y = Mth.clamp((double)(livingEntity.getY() + ((double)livingEntity.getRandom().nextInt((int)maxRadius) - maxRadius / 2.0)), (double)level.getMinBuildHeight(), (double)(level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1));
            double z = d2 + vec.z;
            if (livingEntity.isPassenger()) {
                livingEntity.stopRiding();
            }
            if (livingEntity.randomTeleport(x, y, z, true)) {
                if (damageSource.getEntity() != null) {
                    livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, damageSource.getEntity().getEyePosition());
                }
                level.playSound((Player)null, d0, d1, d2, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 2.0f, 1.0f);
                break;
            }
            if (!(maxRadius > 2.0)) continue;
            maxRadius -= 1.0;
        }
        EvasionEffect.particleCloud(livingEntity);
        return true;
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.position().add(0.0, (double)(entity.getBbHeight() / 2.0f), 0.0);
        MagicManager.spawnParticles(entity.level, (ParticleOptions)ParticleTypes.PORTAL, pos.x, pos.y, pos.z, 70, entity.getBbWidth() / 4.0f, entity.getBbHeight() / 5.0f, entity.getBbWidth() / 4.0f, 0.035, false);
    }
}

