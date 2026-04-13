/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Added
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Remove
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.AerostasisGyrobladderAirHandler;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AerostasisGyrobladderEffect
extends MobEffect {
    static final String NBT_ACTIVE = "cc_gyro_active";
    static final String NBT_OLD_FLY_SPEED = "cc_gyro_oldFlySpeedBits";
    static final String NBT_OLD_MAYFLY = "cc_gyro_oldMayfly";
    static final String NBT_JUMP_HELD = "cc_gyro_jumpHeld";
    private static final float GYRO_FLY_SPEED = 0.025f;
    private static final double FLOAT_UP_Y = 0.04;
    private static final double START_FLOAT_Y = 0.04;

    public AerostasisGyrobladderEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return true;
        }
        if (!(entity instanceof Player)) {
            return true;
        }
        Player player = (Player)entity;
        if (player.isCreative() || player.isSpectator()) {
            return true;
        }
        int air = AerostasisGyrobladderAirHandler.getO2(player);
        boolean jumpHeld = AerostasisGyrobladderEffect.isJumpHeldServer(player);
        if (air <= 0) {
            if (AerostasisGyrobladderEffect.isGyroActive(player)) {
                AerostasisGyrobladderEffect.stopGyroFlight(player);
            }
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "gyrobladder_speed");
            return true;
        }
        if (jumpHeld) {
            AerostasisGyrobladderEffect.ensureGyroFlightConfigured(player);
            AerostasisGyrobladderEffect.setFlying(player, true);
            Vec3 v = player.getDeltaMovement();
            player.setDeltaMovement(v.x, Math.max(v.y, 0.04), v.z);
            player.fallDistance = 0.0f;
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "gyrobladder_speed");
        } else {
            if (AerostasisGyrobladderEffect.isGyroActive(player)) {
                AerostasisGyrobladderEffect.stopGyroFlight(player);
            }
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "gyrobladder_speed");
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    static boolean isJumpHeldServer(Player player) {
        return player.getPersistentData().getBoolean(NBT_JUMP_HELD);
    }

    static void setJumpHeldServer(Player player, boolean held) {
        player.getPersistentData().putBoolean(NBT_JUMP_HELD, held);
    }

    static boolean isGyroActive(Player player) {
        return player.getPersistentData().getBoolean(NBT_ACTIVE);
    }

    static void ensureGyroFlightConfigured(Player player) {
        CompoundTag tag = player.getPersistentData();
        if (!tag.getBoolean(NBT_ACTIVE)) {
            tag.putInt(NBT_OLD_FLY_SPEED, Float.floatToIntBits(player.getAbilities().getFlyingSpeed()));
            tag.putBoolean(NBT_OLD_MAYFLY, player.getAbilities().mayfly);
            tag.putBoolean(NBT_ACTIVE, true);
        }
        player.getAbilities().mayfly = true;
        player.getAbilities().setFlyingSpeed(0.025f);
        player.onUpdateAbilities();
    }

    static void stopGyroFlight(Player player) {
        boolean oldMayfly;
        CompoundTag tag = player.getPersistentData();
        player.getAbilities().flying = false;
        player.getAbilities().mayfly = oldMayfly = tag.getBoolean(NBT_OLD_MAYFLY);
        if (tag.getBoolean(NBT_ACTIVE)) {
            float oldSpeed = Float.intBitsToFloat(tag.getInt(NBT_OLD_FLY_SPEED));
            player.getAbilities().setFlyingSpeed(oldSpeed);
        }
        tag.remove(NBT_ACTIVE);
        tag.remove(NBT_OLD_FLY_SPEED);
        tag.remove(NBT_OLD_MAYFLY);
        player.onUpdateAbilities();
    }

    static void setFlying(Player player, boolean flying) {
        if (player.getAbilities().flying == flying) {
            return;
        }
        player.getAbilities().flying = flying;
        player.onUpdateAbilities();
    }

    private static boolean hasThisEffect(Player player) {
        return player.hasEffect(ModEffects.AEROSTASIS_GYROBLADDER_EFFECT);
    }

    public static void handleJumpHeldPayload(ServerPlayer player, boolean held) {
        if (player == null) {
            return;
        }
        if (player.hasEffect(ModEffects.AEROSTASIS_GYROBLADDER_EFFECT)) {
            AerostasisGyrobladderEffect.setJumpHeldServer((Player)player, held);
        } else {
            AerostasisGyrobladderEffect.setJumpHeldServer((Player)player, false);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
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
            if (event.getEffectInstance().getEffect() != ModEffects.AEROSTASIS_GYROBLADDER_EFFECT.value()) {
                return;
            }
            AerostasisGyrobladderEffect.setJumpHeldServer(player, false);
            if (AerostasisGyrobladderEffect.isGyroActive(player)) {
                AerostasisGyrobladderEffect.stopGyroFlight(player);
            }
        }

        @SubscribeEvent
        public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
            if (!AerostasisGyrobladderEffect.hasThisEffect(player)) {
                return;
            }
            if (AerostasisGyrobladderAirHandler.getO2(player) <= 0) {
                return;
            }
            AerostasisGyrobladderEffect.setJumpHeldServer(player, true);
            AerostasisGyrobladderEffect.ensureGyroFlightConfigured(player);
            AerostasisGyrobladderEffect.setFlying(player, true);
            Vec3 v = player.getDeltaMovement();
            player.setDeltaMovement(v.x, 0.04, v.z);
            player.fallDistance = 0.0f;
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
            if (event.getEffectInstance().getEffect() != ModEffects.AEROSTASIS_GYROBLADDER_EFFECT.value()) {
                return;
            }
            AerostasisGyrobladderEffect.setJumpHeldServer(player, false);
            if (AerostasisGyrobladderEffect.isGyroActive(player)) {
                AerostasisGyrobladderEffect.stopGyroFlight(player);
            }
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "gyrobladder_speed");
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
            if (event.getEffectInstance().getEffect() != ModEffects.AEROSTASIS_GYROBLADDER_EFFECT.value()) {
                return;
            }
            AerostasisGyrobladderEffect.setJumpHeldServer(player, false);
            if (AerostasisGyrobladderEffect.isGyroActive(player)) {
                AerostasisGyrobladderEffect.stopGyroFlight(player);
            }
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "gyrobladder_speed");
        }

        private Events() {
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientInput {
        private static boolean lastSent = false;
        private static boolean lastHasEffect = false;

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            boolean heldNow;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            boolean hasEffect = AerostasisGyrobladderEffect.hasThisEffect((Player)mc.player);
            boolean bl = heldNow = hasEffect && mc.options.keyJump.isDown();
            if (hasEffect && !lastHasEffect) {
                PacketDistributor.sendToServer((CustomPacketPayload)new GyroJumpHeldPayload(heldNow), (CustomPacketPayload[])new CustomPacketPayload[0]);
                lastSent = heldNow;
            } else if (heldNow != lastSent) {
                PacketDistributor.sendToServer((CustomPacketPayload)new GyroJumpHeldPayload(heldNow), (CustomPacketPayload[])new CustomPacketPayload[0]);
                lastSent = heldNow;
            }
            if (!hasEffect) {
                if (lastSent) {
                    PacketDistributor.sendToServer((CustomPacketPayload)new GyroJumpHeldPayload(false), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
                lastSent = false;
            }
            lastHasEffect = hasEffect;
        }

        private ClientInput() {
        }
    }

    public record GyroJumpHeldPayload(boolean held) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<GyroJumpHeldPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gyro_jump_held"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GyroJumpHeldPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.BOOL, GyroJumpHeldPayload::held, GyroJumpHeldPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

