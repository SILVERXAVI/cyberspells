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
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class NeuropozyneEffect
extends MobEffect {
    private static final int HUMANITY_PER_LEVEL = 25;
    private static final int SIDE_EFFECT_START_AMP = 5;
    private static final int SIDE_EFFECT_ROLL_INTERVAL_TICKS = 20;
    private static final float BASE_CHANCE_AT_START = 0.05f;
    private static final float CHANCE_PER_EXTRA_AMP = 0.05f;
    private static final float MAX_CHANCE = 0.75f;
    private static final int DEBUFF_DURATION_BASE = 80;
    private static final int DEBUFF_DURATION_PER_LEVEL = 40;

    public NeuropozyneEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        MobEffectInstance rejection;
        if (!(living instanceof Player)) {
            return true;
        }
        Player player = (Player)living;
        if (player.level().isClientSide) {
            return true;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data != null) {
            int bonus = (amplifier + 1) * 25;
            data.setHumanityBonus(bonus);
        }
        if (player.tickCount % 20 == 0 && (rejection = player.getEffect(ModEffects.CYBERWARE_REJECTION)) != null) {
            player.removeEffect(ModEffects.CYBERWARE_REJECTION);
        }
        if (amplifier >= 5 && player.tickCount % 20 == 0) {
            NeuropozyneEffect.rollSideEffects(player, amplifier);
        }
        return true;
    }

    public void onMobRemoved(LivingEntity living, int amplifier, Entity.RemovalReason reason) {
        if (!(living instanceof Player)) {
            return;
        }
        Player player = (Player)living;
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data != null) {
            data.clearHumanityBonus();
        }
    }

    private static void rollSideEffects(Player player, int amplifier) {
        int level = amplifier - 5 + 1;
        float chance = 0.05f + (float)(level - 1) * 0.05f;
        chance = Mth.clamp((float)chance, (float)0.0f, (float)0.75f);
        int debuffDuration = 80 + (level - 1) * 40;
        int debuffAmp = Math.min(2, (level - 1) / 2);
        NeuropozyneEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.WEAKNESS, chance * 1.0f, debuffDuration, debuffAmp);
        NeuropozyneEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.DIG_SLOWDOWN, chance * 0.85f, debuffDuration, debuffAmp);
        NeuropozyneEffect.maybeApply(player, (Holder<MobEffect>)MobEffects.CONFUSION, chance * 0.7f, debuffDuration, 0);
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
        player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, true, true));
    }
}

