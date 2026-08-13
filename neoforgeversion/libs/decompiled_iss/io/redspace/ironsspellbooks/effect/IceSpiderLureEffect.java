/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.IMobEffectEndCallback;
import io.redspace.ironsspellbooks.worldgen.IceSpiderPatrolSpawner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IceSpiderLureEffect
extends MobEffect
implements IMobEffectEndCallback {
    public IceSpiderLureEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            IceSpiderPatrolSpawner.performIceSpiderHuntSpawn(serverLevel, pLivingEntity, 8);
        }
    }
}

