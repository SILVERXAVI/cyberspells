/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class ElytraFlightSkillHandler {
    private static final double BASE_MAX_HORIZ_SPEED = 1.7;
    private static final double MAX_HORIZ_SPEED_CAP = 5.0;
    private static final double ACCEL_PER_TICK_PER_EXTRA_MULT = 0.02;
    private static final double STEER_LERP_PER_EXTRA_MULT = 0.15;
    private static final double MAX_STEER_LERP = 0.65;

    private ElytraFlightSkillHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        double extraMult;
        Player player = event.getEntity();
        if (player == null) {
            return;
        }
        if (!player.isFallFlying()) {
            return;
        }
        double speedMult = player.getAttributeValue(ModAttributes.ELYTRA_SPEED);
        double handlingMult = player.getAttributeValue(ModAttributes.ELYTRA_HANDLING);
        if (!Double.isFinite(speedMult)) {
            speedMult = 1.0;
        }
        if (!Double.isFinite(handlingMult)) {
            handlingMult = 1.0;
        }
        if (speedMult <= 1.0 && handlingMult <= 1.0) {
            return;
        }
        Vec3 vel = player.getDeltaMovement();
        Vec3 look = player.getLookAngle();
        if (speedMult > 1.0) {
            extraMult = speedMult - 1.0;
            double accel = 0.02 * extraMult;
            vel = vel.add(look.x * accel, 0.0, look.z * accel);
        }
        if (handlingMult > 1.0) {
            extraMult = handlingMult - 1.0;
            double steer = 0.15 * extraMult;
            steer = Mth.clamp((double)steer, (double)0.0, (double)0.65);
            Vec3 horiz = new Vec3(vel.x, 0.0, vel.z);
            double hLen = horiz.length();
            Vec3 lookHoriz = new Vec3(look.x, 0.0, look.z);
            if (hLen > 1.0E-6 && lookHoriz.lengthSqr() > 1.0E-12) {
                Vec3 desiredHoriz = lookHoriz.normalize().scale(hLen);
                Vec3 steered = horiz.lerp(desiredHoriz, steer);
                vel = new Vec3(steered.x, vel.y, steered.z);
            }
        }
        double max = 1.7 * Math.max(1.0, speedMult);
        max = Mth.clamp((double)max, (double)0.05, (double)5.0);
        Vec3 horiz2 = new Vec3(vel.x, 0.0, vel.z);
        double h2 = horiz2.length();
        if (h2 > max) {
            double scale = max / h2;
            vel = new Vec3(vel.x * scale, vel.y, vel.z * scale);
        }
        player.setDeltaMovement(vel);
    }
}

