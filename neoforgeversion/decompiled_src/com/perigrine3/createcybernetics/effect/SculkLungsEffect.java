/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.effect;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class SculkLungsEffect
extends MobEffect {
    private static final int CHARGE_TICKS = 34;
    private static final int COOLDOWN_AFTER_FIRE_TICKS = 300;
    private static final double RANGE = 20.0;
    private static final float DAMAGE = 10.0f;
    private static final double KNOCKBACK_HORIZONTAL = 2.5;
    private static final double KNOCKBACK_VERTICAL = 0.5;
    private static final double KNOCKBACK_Y_CAP = 0.5;
    private static final String KEY_USE_HELD = "cc_sonic_useHeld";
    private static final String KEY_CHARGE = "cc_sonic_charge";
    private static final String KEY_LAST_FIRE_TICK = "cc_sonic_lastFireTick";

    public SculkLungsEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        boolean onCooldown;
        if (!(living instanceof ServerPlayer)) {
            return true;
        }
        ServerPlayer player = (ServerPlayer)living;
        Level level = player.level();
        if (!(level instanceof ServerLevel)) {
            return true;
        }
        ServerLevel level2 = (ServerLevel)level;
        CompoundTag tag = player.getPersistentData();
        long now = level2.getGameTime();
        boolean useHeld = tag.getBoolean(KEY_USE_HELD);
        boolean crouching = player.isCrouching();
        long lastFire = tag.getLong(KEY_LAST_FIRE_TICK);
        boolean bl = onCooldown = now - lastFire < 300L;
        if (!useHeld || !crouching || onCooldown) {
            tag.putInt(KEY_CHARGE, 0);
            return true;
        }
        int charge = tag.getInt(KEY_CHARGE) + 1;
        if (charge == 1) {
            SculkLungsEffect.playChargeSound(level2, player);
        }
        if (charge < 34) {
            tag.putInt(KEY_CHARGE, charge);
            return true;
        }
        tag.putInt(KEY_CHARGE, 0);
        Vec3 start = player.getEyePosition(1.0f);
        Vec3 look = player.getViewVector(1.0f);
        Vec3 end = start.add(look.scale(20.0));
        SculkLungsEffect.hitEntitiesInBeam(level2, player, start, end);
        SculkLungsEffect.spawnSonicParticles(level2, start, end);
        SculkLungsEffect.playBoomSound(level2, player);
        tag.putLong(KEY_LAST_FIRE_TICK, now);
        return true;
    }

    private static void hitEntitiesInBeam(ServerLevel level, ServerPlayer player, Vec3 start, Vec3 end) {
        Vec3 delta = end.subtract(start);
        double len = delta.length();
        if (len < 0.001) {
            return;
        }
        Vec3 dir = delta.scale(1.0 / len);
        double halfWidth = 1.25;
        AABB beamBox = new AABB(start, end).inflate(halfWidth);
        List hits = level.getEntitiesOfClass(LivingEntity.class, beamBox, e -> e.isAlive() && e != player);
        for (LivingEntity target : hits) {
            Vec3 toTarget = target.getEyePosition(1.0f).subtract(start);
            double proj = toTarget.dot(dir);
            if (proj < 0.0 || proj > len) continue;
            Vec3 closest = start.add(dir.scale(proj));
            double dist2 = target.getEyePosition(1.0f).distanceToSqr(closest);
            if (dist2 > halfWidth * halfWidth) continue;
            target.hurt(level.damageSources().sonicBoom((Entity)player), 10.0f);
            Vec3 knockDir = target.position().subtract(player.position()).normalize();
            double ky = Mth.clamp((double)0.5, (double)-0.5, (double)0.5);
            target.push(knockDir.x * 2.5, ky, knockDir.z * 2.5);
            target.hurtMarked = true;
        }
    }

    private static void spawnSonicParticles(ServerLevel level, Vec3 start, Vec3 end) {
        Vec3 delta = end.subtract(start);
        double len = delta.length();
        if (len < 0.001) {
            return;
        }
        Vec3 dir = delta.scale(1.0 / len);
        int steps = Mth.clamp((int)((int)(len * 8.0)), (int)8, (int)256);
        double step = len / (double)steps;
        for (int i = 0; i <= steps; ++i) {
            Vec3 p = start.add(dir.scale((double)i * step));
            level.sendParticles((ParticleOptions)ParticleTypes.SONIC_BOOM, p.x, p.y, p.z, 0, dir.x * 0.35, dir.y * 0.35, dir.z * 0.35, 1.0);
        }
    }

    private static void playChargeSound(ServerLevel level, ServerPlayer player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.PLAYERS, 1.0f, 1.25f);
    }

    private static void playBoomSound(ServerLevel level, ServerPlayer player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0f, 1.25f);
    }

    private static boolean hasThisEffect(Level level, ServerPlayer player) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (!(inst.getEffect().value() instanceof SculkLungsEffect)) continue;
            return true;
        }
        return false;
    }

    public static void setUseHeld(ServerPlayer player, boolean held) {
        if (!SculkLungsEffect.hasThisEffect(player.level(), player)) {
            return;
        }
        player.getPersistentData().putBoolean(KEY_USE_HELD, held);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
    public static final class ClientInput {
        private static boolean lastSentHeld = false;

        private ClientInput() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) {
                return;
            }
            boolean hasEffect = false;
            for (MobEffectInstance inst : player.getActiveEffects()) {
                if (!(inst.getEffect().value() instanceof SculkLungsEffect)) continue;
                hasEffect = true;
                break;
            }
            if (!hasEffect) {
                if (lastSentHeld) {
                    lastSentHeld = false;
                    PacketDistributor.sendToServer((CustomPacketPayload)new SonicUseHeldPayload(false), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
                return;
            }
            boolean held = mc.options.keyUse.isDown();
            if (held != lastSentHeld) {
                lastSentHeld = held;
                PacketDistributor.sendToServer((CustomPacketPayload)new SonicUseHeldPayload(held), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    public record SonicUseHeldPayload(boolean held) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<SonicUseHeldPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"sonic_use_held"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SonicUseHeldPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.BOOL, SonicUseHeldPayload::held, SonicUseHeldPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

