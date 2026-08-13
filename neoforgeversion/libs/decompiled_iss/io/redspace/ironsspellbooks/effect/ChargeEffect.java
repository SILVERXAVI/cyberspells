/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectCategory
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ChargeEffect
extends MagicMobEffect
implements ISyncedMobEffect {
    public static final float ATTACK_DAMAGE_PER_LEVEL = 0.1f;
    public static final float SPEED_PER_LEVEL = 0.2f;
    public static final float SPELL_POWER_PER_LEVEL = 0.05f;

    public ChargeEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
}

