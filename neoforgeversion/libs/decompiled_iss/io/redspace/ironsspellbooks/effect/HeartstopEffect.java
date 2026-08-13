/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class HeartstopEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    private int duration;

    public HeartstopEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
        super.onEffectRemoved(pLivingEntity, pAmplifier);
        MagicData playerMagicData = MagicData.getPlayerMagicData(pLivingEntity);
        if (pLivingEntity.tickCount > 60) {
            pLivingEntity.hurt(DamageSources.get(pLivingEntity.level, ISSDamageTypes.HEARTSTOP), playerMagicData.getSyncedData().getHeartstopAccumulatedDamage());
        } else {
            pLivingEntity.kill();
        }
        playerMagicData.getSyncedData().setHeartstopAccumulatedDamage(0.0f);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        Player player;
        float damage;
        float f;
        int i;
        if (pLivingEntity.level.isClientSide && pLivingEntity instanceof Player && this.duration % Math.max(i = (int)(10.0f + 30.0f * (f = 1.0f - Mth.clamp((float)((damage = ClientMagicData.getSyncedSpellData((LivingEntity)(player = (Player)pLivingEntity)).getHeartstopAccumulatedDamage()) / player.getHealth()), (float)0.0f, (float)1.0f))), 1) == 0) {
            player.playSound(SoundEvents.WARDEN_HEARTBEAT, 1.0f, 0.85f);
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        this.duration = pDuration;
        return true;
    }
}

