/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Added
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid="createcybernetics")
public final class NeuropozyneStackingHandler {
    private static final String NBT_STACK_GUARD_TICK = "cc_neuropozyne_stack_guard_tick";
    private static final int MAX_AMP = 255;

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }
        MobEffectInstance added = event.getEffectInstance();
        if (added == null) {
            return;
        }
        if (!added.is(ModEffects.NEUROPOZYNE)) {
            return;
        }
        MobEffectInstance old = event.getOldEffectInstance();
        if (old == null) {
            return;
        }
        long now = entity.level().getGameTime();
        CompoundTag pd = entity.getPersistentData();
        if (pd.getLong(NBT_STACK_GUARD_TICK) == now) {
            return;
        }
        int desiredAmp = Math.min(old.getAmplifier() + 1, 255);
        if (added.getAmplifier() == desiredAmp) {
            return;
        }
        pd.putLong(NBT_STACK_GUARD_TICK, now);
        entity.addEffect(new MobEffectInstance(added.getEffect(), added.getDuration(), desiredAmp, added.isAmbient(), added.isVisible(), added.showIcon()));
    }
}

