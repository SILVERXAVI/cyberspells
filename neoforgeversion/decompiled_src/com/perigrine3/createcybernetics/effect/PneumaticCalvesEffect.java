/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Added
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Remove
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class PneumaticCalvesEffect
extends MobEffect {
    private static final String NBT_LAST_USE_TICK = "cc_calves_lastSprintJumpTick";
    private static final int COOLDOWN_TICKS = 5;
    private static final double FORWARD_BOOST = 0.9;
    private static final double VERTICAL_BOOST = 0.35;
    private static final double WIND_BEHIND_DIST = 0.8;
    private static final double WIND_Y_OFFSET = 0.2;
    private static final String NBT_LAST_CROUCH_JUMP_TICK = "cc_calves_lastCrouchJumpTick";
    private static final int CROUCH_COOLDOWN_TICKS = 8;
    private static final double CROUCH_VERTICAL_BOOST = 1.0;
    private static final double CROUCH_UPWARD_ONLY_DAMPEN = 0.0;

    public PneumaticCalvesEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    private static boolean hasThisEffect(Player player) {
        return player.hasEffect(ModEffects.PNEUMATIC_CALVES_EFFECT);
    }

    private static void resetState(Player player) {
        player.getPersistentData().remove(NBT_LAST_USE_TICK);
    }

    private static void clearState(Player player) {
        player.getPersistentData().remove(NBT_LAST_USE_TICK);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
            long last;
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (!PneumaticCalvesEffect.hasThisEffect(player)) {
                return;
            }
            if (player.level().isClientSide) {
                return;
            }
            if (!PneumaticCalvesEffect.hasThisEffect(player)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            CompoundTag tag = player.getPersistentData();
            long now = player.level().getGameTime();
            if (player.isCrouching() && now - (last = tag.getLong(PneumaticCalvesEffect.NBT_LAST_CROUCH_JUMP_TICK)) >= 8L) {
                Level level;
                tag.putLong(PneumaticCalvesEffect.NBT_LAST_CROUCH_JUMP_TICK, now);
                if (!data.hasSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), CyberwareSlot.RLEG) && !data.hasSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), CyberwareSlot.LLEG)) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 0.4f, 1.5f);
                }
                if ((level = player.level()) instanceof ServerLevel) {
                    ServerLevel sl = (ServerLevel)level;
                    sl.sendParticles((ParticleOptions)ParticleTypes.CLOUD, player.getX(), player.getY() + 0.05, player.getZ(), 10, 0.2, 0.05, 0.2, 0.02);
                }
                Events.doCrouchJumpBoost(player);
                return;
            }
            if (!player.isSprinting()) {
                return;
            }
            if (player.getVehicle() != null) {
                return;
            }
            if (player.isInWaterOrBubble()) {
                return;
            }
            if (player.onClimbable()) {
                return;
            }
            last = tag.getLong(PneumaticCalvesEffect.NBT_LAST_USE_TICK);
            if (now - last < 5L) {
                return;
            }
            tag.putLong(PneumaticCalvesEffect.NBT_LAST_USE_TICK, now);
            if (!data.hasSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), CyberwareSlot.RLEG) && !data.hasSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), CyberwareSlot.LLEG)) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 0.25f, 1.35f);
            }
            Vec3 look = player.getLookAngle();
            Vec3 forward = new Vec3(look.x, 0.0, look.z);
            forward = forward.lengthSqr() < 1.0E-6 ? Vec3.directionFromRotation((float)0.0f, (float)player.getYRot()) : forward.normalize();
            Vec3 behind = forward.scale(-1.0);
            Vec3 windPos = player.position().add(behind.scale(0.8)).add(0.0, 0.2, 0.0);
            Level level = player.level();
            if (level instanceof ServerLevel) {
                ServerLevel sl = (ServerLevel)level;
                sl.sendParticles((ParticleOptions)ParticleTypes.CLOUD, windPos.x, windPos.y, windPos.z, 8, 0.18, 0.1, 0.18, 0.02);
            }
            Vec3 impulse = new Vec3(forward.x * 0.9, 0.35, forward.z * 0.9);
            player.setDeltaMovement(player.getDeltaMovement().add(impulse));
            player.hasImpulse = true;
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                sp.hurtMarked = true;
            }
        }

        @SubscribeEvent
        public static void onEffectAdded(MobEffectEvent.Added event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (event.getEffectInstance() == null) {
                return;
            }
            if (event.getEffectInstance().getEffect() != ModEffects.PNEUMATIC_CALVES_EFFECT.value()) {
                return;
            }
            PneumaticCalvesEffect.resetState(player);
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (event.getEffectInstance() == null) {
                return;
            }
            if (event.getEffectInstance().getEffect() != ModEffects.PNEUMATIC_CALVES_EFFECT.value()) {
                return;
            }
            PneumaticCalvesEffect.clearState(player);
        }

        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (event.getEffectInstance() == null) {
                return;
            }
            if (event.getEffectInstance().getEffect() != ModEffects.PNEUMATIC_CALVES_EFFECT.value()) {
                return;
            }
            PneumaticCalvesEffect.clearState(player);
        }

        private static void doCrouchJumpBoost(Player player) {
            Vec3 impulse = new Vec3(0.0, 1.0, 0.0);
            player.setDeltaMovement(player.getDeltaMovement().add(impulse));
            player.hasImpulse = true;
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                sp.hurtMarked = true;
            }
        }

        private Events() {
        }
    }
}

