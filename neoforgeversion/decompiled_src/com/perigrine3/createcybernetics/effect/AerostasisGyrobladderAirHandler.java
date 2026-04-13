/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Pre
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.AerostasisGyrobladderEffect;
import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class AerostasisGyrobladderAirHandler {
    public static final String NBT_O2 = "cc_gyro_o2_ticks";

    private AerostasisGyrobladderAirHandler() {
    }

    public static int getO2(Player player) {
        int maxAir = player.getMaxAirSupply();
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(NBT_O2)) {
            tag.putInt(NBT_O2, maxAir);
        }
        return Mth.clamp((int)tag.getInt(NBT_O2), (int)0, (int)maxAir);
    }

    public static void setO2(Player player, int value) {
        int maxAir = player.getMaxAirSupply();
        player.getPersistentData().putInt(NBT_O2, Mth.clamp((int)value, (int)0, (int)maxAir));
    }

    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (!player.hasEffect(ModEffects.AEROSTASIS_GYROBLADDER_EFFECT)) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        if (player.isInWaterOrBubble()) {
            return;
        }
        int maxAir = player.getMaxAirSupply();
        int o2 = AerostasisGyrobladderAirHandler.getO2(player);
        boolean holdingBreath = AerostasisGyrobladderEffect.isJumpHeldServer(player);
        o2 = holdingBreath ? Math.max(0, o2 - 1) : Math.min(maxAir, o2 + 4);
        AerostasisGyrobladderAirHandler.setO2(player, o2);
        int preAir = Mth.clamp((int)(o2 - 4), (int)-20, (int)maxAir);
        player.setAirSupply(preAir);
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (!player.hasEffect(ModEffects.AEROSTASIS_GYROBLADDER_EFFECT)) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        if (player.isInWaterOrBubble()) {
            return;
        }
        player.setAirSupply(AerostasisGyrobladderAirHandler.getO2(player));
    }
}

