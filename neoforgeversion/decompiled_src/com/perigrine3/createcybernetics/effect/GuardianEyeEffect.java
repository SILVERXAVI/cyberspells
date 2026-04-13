/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.particles.DustParticleOptions
 *  net.minecraft.core.particles.ParticleOptions
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
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.joml.Vector3f
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.custom.GuardianBeamEntity;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

public class GuardianEyeEffect
extends MobEffect {
    private static final int CHARGE_TICKS = 40;
    private static final int COOLDOWN_TICKS = 60;
    private static final double RANGE = 15.0;
    private static final float DAMAGE = 12.0f;
    private static final double BEAM_HALF_WIDTH = 0.75;
    private static final String KEY_USE_HELD = "cc_guardianEye_useHeld";
    private static final String KEY_CHARGE = "cc_guardianEye_charge";
    private static final String KEY_END_X = "cc_guardianEye_endX";
    private static final String KEY_END_Y = "cc_guardianEye_endY";
    private static final String KEY_END_Z = "cc_guardianEye_endZ";
    private static final String KEY_LAST_FIRE_TICK = "cc_guardianEye_lastFireTick";
    private static final String KEY_PREVIEW_BEAM_ID = "cc_guardianEye_previewBeamId";
    private static final String KEY_RING_TICKS = "cc_guardianEye_ringTicks";
    private static final String KEY_RING_TOTAL = "cc_guardianEye_ringTotal";
    private static final String KEY_RING_SX = "cc_guardianEye_ringSx";
    private static final String KEY_RING_SY = "cc_guardianEye_ringSy";
    private static final String KEY_RING_SZ = "cc_guardianEye_ringSz";
    private static final String KEY_RING_EX = "cc_guardianEye_ringEx";
    private static final String KEY_RING_EY = "cc_guardianEye_ringEy";
    private static final String KEY_RING_EZ = "cc_guardianEye_ringEz";

    public GuardianEyeEffect(MobEffectCategory category, int color) {
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
        CompoundTag ptag = player.getPersistentData();
        long now = level2.getGameTime();
        GuardianEyeEffect.tickTravelRing(level2, ptag);
        long lastFire = ptag.getLong(KEY_LAST_FIRE_TICK);
        boolean bl = onCooldown = now - lastFire < 60L;
        if (onCooldown) {
            ptag.putInt(KEY_CHARGE, 0);
            GuardianEyeEffect.cleanupPreviewBeam(level2, ptag);
            return true;
        }
        boolean useHeld = ptag.getBoolean(KEY_USE_HELD) || player.isUsingItem();
        boolean crouching = player.isCrouching();
        if (!useHeld || !crouching) {
            ptag.putInt(KEY_CHARGE, 0);
            GuardianEyeEffect.cleanupPreviewBeam(level2, ptag);
            return true;
        }
        int prevCharge = ptag.getInt(KEY_CHARGE);
        int charge = Mth.clamp((int)(prevCharge + 1), (int)0, (int)40);
        ptag.putInt(KEY_CHARGE, charge);
        Vec3 start = GuardianEyeEffect.computeMuzzle(player);
        Vec3 end = GuardianEyeEffect.computeBeamEnd(level2, player, start);
        ptag.putFloat(KEY_END_X, (float)end.x);
        ptag.putFloat(KEY_END_Y, (float)end.y);
        ptag.putFloat(KEY_END_Z, (float)end.z);
        GuardianBeamEntity preview = GuardianEyeEffect.getOrCreatePreviewBeam(level2, player, ptag, start, end);
        preview.setLife(2);
        preview.setStart(start);
        preview.setEnd(end);
        preview.moveTo(start.x, start.y, start.z, player.getYRot(), player.getXRot());
        if (charge == 1) {
            GuardianEyeEffect.playChargeSound(level2, player);
        }
        if (charge < 40) {
            return true;
        }
        ptag.putInt(KEY_CHARGE, 0);
        ptag.putLong(KEY_LAST_FIRE_TICK, now);
        LivingEntity hit = GuardianEyeEffect.hitClosestLivingInBeam(level2, player, start, end);
        if (hit != null) {
            end = hit.getEyePosition(1.0f);
            hit.hurt(level2.damageSources().magic(), 12.0f);
        }
        GuardianEyeEffect.initTravelRing(ptag, start, end);
        GuardianEyeEffect.spawnBeam(level2, player, start, end, 10);
        GuardianEyeEffect.spawnThickenerBeam(level2, player, start, end, 10);
        GuardianEyeEffect.playFireSound(level2, player);
        ptag.putInt(KEY_PREVIEW_BEAM_ID, 0);
        return true;
    }

    private static Vec3 computeBeamEnd(ServerLevel level, ServerPlayer player, Vec3 start) {
        Vec3 look = player.getViewVector(1.0f);
        Vec3 desiredEnd = start.add(look.scale(15.0));
        BlockHitResult hit = level.clip(new ClipContext(start, desiredEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player));
        return hit.getType() == HitResult.Type.MISS ? desiredEnd : hit.getLocation();
    }

    private static LivingEntity hitClosestLivingInBeam(ServerLevel level, ServerPlayer player, Vec3 start, Vec3 end) {
        Vec3 delta = end.subtract(start);
        double len = delta.length();
        if (len < 0.001) {
            return null;
        }
        Vec3 dir = delta.scale(1.0 / len);
        AABB box = new AABB(start, end).inflate(0.75);
        List candidates = level.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && e != player);
        LivingEntity best = null;
        double bestProj = Double.MAX_VALUE;
        for (LivingEntity e2 : candidates) {
            Vec3 closest;
            double dist2;
            Vec3 eye = e2.getEyePosition(1.0f);
            Vec3 to = eye.subtract(start);
            double proj = to.dot(dir);
            if (proj < 0.0 || proj > len || (dist2 = eye.distanceToSqr(closest = start.add(dir.scale(proj)))) > 0.5625 || !(proj < bestProj)) continue;
            bestProj = proj;
            best = e2;
        }
        return best;
    }

    private static void spawnBeam(ServerLevel level, ServerPlayer player, Vec3 start, Vec3 end, int life) {
        GuardianBeamEntity beam = new GuardianBeamEntity(ModEntities.GUARDIAN_BEAM.get(), (Level)level);
        beam.setShooterId(player.getId());
        beam.setStart(start);
        beam.setEnd(end);
        beam.setLife(life);
        beam.moveTo(start.x, start.y, start.z, player.getYRot(), player.getXRot());
        level.addFreshEntity((Entity)beam);
    }

    private static void spawnThickenerBeam(ServerLevel level, ServerPlayer player, Vec3 start, Vec3 end, int life) {
        Vec3 delta = end.subtract(start);
        double len = delta.length();
        if (len < 0.001) {
            return;
        }
        Vec3 dir = delta.scale(1.0 / len);
        Vec3 basis = Math.abs(dir.y) > 0.9 ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
        Vec3 right = dir.cross(basis).normalize();
        double off = 0.04;
        Vec3 s2 = start.add(right.scale(off));
        Vec3 e2 = end.add(right.scale(off));
        GuardianBeamEntity beam = new GuardianBeamEntity(ModEntities.GUARDIAN_BEAM.get(), (Level)level);
        beam.setShooterId(player.getId());
        beam.setStart(s2);
        beam.setEnd(e2);
        beam.setLife(life);
        beam.moveTo(s2.x, s2.y, s2.z, player.getYRot(), player.getXRot());
        level.addFreshEntity((Entity)beam);
    }

    private static GuardianBeamEntity getOrCreatePreviewBeam(ServerLevel level, ServerPlayer player, CompoundTag ptag, Vec3 start, Vec3 end) {
        GuardianBeamEntity beam;
        Entity existing;
        int id = ptag.getInt(KEY_PREVIEW_BEAM_ID);
        Entity entity = existing = id != 0 ? level.getEntity(id) : null;
        if (existing instanceof GuardianBeamEntity && (beam = (GuardianBeamEntity)existing).isAlive()) {
            return beam;
        }
        beam = new GuardianBeamEntity(ModEntities.GUARDIAN_BEAM.get(), (Level)level);
        beam.setShooterId(player.getId());
        beam.setLife(2);
        beam.setStart(start);
        beam.setEnd(end);
        beam.moveTo(start.x, start.y, start.z, player.getYRot(), player.getXRot());
        level.addFreshEntity((Entity)beam);
        ptag.putInt(KEY_PREVIEW_BEAM_ID, beam.getId());
        return beam;
    }

    private static void cleanupPreviewBeam(ServerLevel level, CompoundTag ptag) {
        int id = ptag.getInt(KEY_PREVIEW_BEAM_ID);
        if (id == 0) {
            return;
        }
        Entity e = level.getEntity(id);
        if (e instanceof GuardianBeamEntity) {
            GuardianBeamEntity beam = (GuardianBeamEntity)e;
            beam.discard();
        }
        ptag.putInt(KEY_PREVIEW_BEAM_ID, 0);
    }

    private static void initTravelRing(CompoundTag ptag, Vec3 start, Vec3 end) {
        int travel = 10;
        ptag.putInt(KEY_RING_TICKS, travel);
        ptag.putInt(KEY_RING_TOTAL, travel);
        ptag.putFloat(KEY_RING_SX, (float)start.x);
        ptag.putFloat(KEY_RING_SY, (float)start.y);
        ptag.putFloat(KEY_RING_SZ, (float)start.z);
        ptag.putFloat(KEY_RING_EX, (float)end.x);
        ptag.putFloat(KEY_RING_EY, (float)end.y);
        ptag.putFloat(KEY_RING_EZ, (float)end.z);
    }

    private static void tickTravelRing(ServerLevel level, CompoundTag ptag) {
        int ticks = ptag.getInt(KEY_RING_TICKS);
        if (ticks <= 0) {
            return;
        }
        int total = Math.max(1, ptag.getInt(KEY_RING_TOTAL));
        Vec3 start = new Vec3((double)ptag.getFloat(KEY_RING_SX), (double)ptag.getFloat(KEY_RING_SY), (double)ptag.getFloat(KEY_RING_SZ));
        Vec3 end = new Vec3((double)ptag.getFloat(KEY_RING_EX), (double)ptag.getFloat(KEY_RING_EY), (double)ptag.getFloat(KEY_RING_EZ));
        double progress = 1.0 - (double)ticks / (double)total;
        progress = Mth.clamp((double)progress, (double)0.0, (double)1.0);
        Vec3 delta = end.subtract(start);
        Vec3 center = start.add(delta.scale(progress));
        Vec3 dir = delta.lengthSqr() < 1.0E-6 ? new Vec3(0.0, 0.0, 1.0) : delta.normalize();
        GuardianEyeEffect.spawnRing(level, center, dir);
        ptag.putInt(KEY_RING_TICKS, ticks - 1);
    }

    private static void spawnRing(ServerLevel level, Vec3 center, Vec3 dir) {
        Vec3 basis = Math.abs(dir.y) > 0.9 ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
        Vec3 right = dir.cross(basis).normalize();
        Vec3 up = dir.cross(right).normalize();
        double radius = 0.14;
        int ringPts = 8;
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(0.42f, 0.8f, 0.24f), 0.75f);
        for (int k = 0; k < ringPts; ++k) {
            double ang = Math.PI * 2 * ((double)k / (double)ringPts);
            Vec3 off = right.scale(Math.cos(ang) * radius).add(up.scale(Math.sin(ang) * radius));
            Vec3 p = center.add(off);
            level.sendParticles((ParticleOptions)dust, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private static void playChargeSound(ServerLevel level, ServerPlayer player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GUARDIAN_AMBIENT, SoundSource.PLAYERS, 5.0f, 1.0f);
    }

    private static void playFireSound(ServerLevel level, ServerPlayer player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GUARDIAN_ATTACK, SoundSource.PLAYERS, 1.5f, 1.0f);
    }

    private static boolean hasGuardianEyeEffect(Player player) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (!(inst.getEffect().value() instanceof GuardianEyeEffect)) continue;
            return true;
        }
        return false;
    }

    private static Vec3 computeMuzzle(ServerPlayer player) {
        Vec3 eye = player.getEyePosition(1.0f);
        Vec3 look = player.getViewVector(1.0f);
        return eye.add(look.scale(0.35)).subtract(0.0, 0.18, 0.0);
    }

    public static void setUseHeld(ServerPlayer player, boolean held) {
        if (!GuardianEyeEffect.hasGuardianEyeEffect((Player)player)) {
            return;
        }
        player.getPersistentData().putBoolean(KEY_USE_HELD, held);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
    public static final class ClientInput {
        private static boolean lastUseHeld = false;

        private ClientInput() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) {
                return;
            }
            if (!GuardianEyeEffect.hasGuardianEyeEffect((Player)player)) {
                if (lastUseHeld) {
                    lastUseHeld = false;
                    PacketDistributor.sendToServer((CustomPacketPayload)new GuardianEyeUseHeldPayload(false), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
                return;
            }
            boolean useHeld = mc.options.keyUse.isDown();
            if (useHeld != lastUseHeld) {
                lastUseHeld = useHeld;
                PacketDistributor.sendToServer((CustomPacketPayload)new GuardianEyeUseHeldPayload(useHeld), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    public record GuardianEyeUseHeldPayload(boolean held) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<GuardianEyeUseHeldPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"guardian_eye_use_held"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GuardianEyeUseHeldPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.BOOL, GuardianEyeUseHeldPayload::held, GuardianEyeUseHeldPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

