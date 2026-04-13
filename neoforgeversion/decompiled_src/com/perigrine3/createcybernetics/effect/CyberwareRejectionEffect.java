/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.damage.ModDamageSources;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CyberwareRejectionEffect
extends MobEffect {
    private static final float DANGER_THRESHOLD = 0.25f;
    private static final float MIN_CHANCE = 0.1f;
    private static final float MAX_CHANCE = 0.85f;
    private static final int DEBUFF_BASE = 80;
    private static final int DEBUFF_EXTRA = 240;
    private static final float DAMAGE_CHANCE_PER_TICK = 0.003f;

    public CyberwareRejectionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        float percent;
        if (!(living instanceof Player)) {
            return true;
        }
        Player player = (Player)living;
        if (player.level().isClientSide) {
            return true;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return true;
        }
        int currentHumanity = data.getHumanity();
        int maxHumanity = Math.max(100, currentHumanity);
        float f = percent = maxHumanity <= 0 ? 0.0f : (float)currentHumanity / (float)maxHumanity;
        if (percent > 0.25f) {
            return true;
        }
        float progress = (0.25f - percent) / 0.25f;
        progress = Mth.clamp((float)progress, (float)0.0f, (float)1.0f);
        float chance = 0.1f + progress * 0.75f;
        int durationTicks = 80 + Mth.floor((float)(progress * 240.0f));
        int debuffAmp = progress >= 0.66f ? 2 : (progress >= 0.33f ? 1 : 0);
        CyberwareRejectionEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.WEAKNESS, chance * 1.0f, durationTicks, debuffAmp);
        CyberwareRejectionEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.DIG_SLOWDOWN, chance * 0.9f, durationTicks, debuffAmp);
        CyberwareRejectionEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.CONFUSION, chance * 0.8f, durationTicks, 0);
        if (player.getRandom().nextFloat() < 0.003f) {
            float base = 1 << Math.min(30, amplifier);
            float scaled = base * (0.25f + 0.75f * progress);
            player.hurt(ModDamageSources.cyberwareRejection(player.level(), (Entity)player, null), scaled);
        }
        return true;
    }

    private static void maybeApply(Player player, Holder<MobEffect> effect, float chance, int duration, int amplifier) {
        if (chance <= 0.0f) {
            return;
        }
        if (player.getRandom().nextFloat() >= chance) {
            return;
        }
        MobEffectInstance existing = player.getEffect(effect);
        if (existing != null && existing.getDuration() > duration / 2) {
            return;
        }
        player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
    }
}

