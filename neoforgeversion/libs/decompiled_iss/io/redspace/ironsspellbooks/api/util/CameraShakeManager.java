/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeCameraAngles
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.network.SyncAllCameraShakesPacket;
import io.redspace.ironsspellbooks.network.SyncCameraShakePacket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class CameraShakeManager {
    public static final ArrayList<CameraShakeData> cameraShakeData = new ArrayList();
    public static ArrayList<CameraShakeData> clientCameraShakeData = new ArrayList();
    private static int nextId = 0;
    private static final int fadeoutDuration = 20;
    private static final float fadeoutMultiplier = 0.05f;

    public static int getNextId() {
        return nextId++;
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Post event) {
        if (cameraShakeData.isEmpty()) {
            return;
        }
        ArrayList<CameraShakeData> completed = new ArrayList<CameraShakeData>();
        for (CameraShakeData data : cameraShakeData) {
            ++data.tickCount;
            if (data.tickCount < data.duration) continue;
            completed.add(data);
        }
        if (!completed.isEmpty()) {
            completed.forEach(CameraShakeManager::removeCameraShake);
        }
    }

    public static void addCameraShake(CameraShakeData data) {
        cameraShakeData.add(data);
        PacketDistributor.sendToAllPlayers((CustomPacketPayload)new SyncCameraShakePacket(data, false), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void removeCameraShake(CameraShakeData data) {
        if (cameraShakeData.removeIf(instance -> instance.id == data.id)) {
            PacketDistributor.sendToAllPlayers((CustomPacketPayload)new SyncCameraShakePacket(data, true), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public static void addClientCameraShake(CameraShakeData data) {
        clientCameraShakeData.add(data);
    }

    public static void removeClientCameraShake(CameraShakeData data) {
        clientCameraShakeData.removeIf(instance -> instance.id == data.id);
    }

    public static void doSync(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncAllCameraShakesPacket(cameraShakeData), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    @SubscribeEvent
    @OnlyIn(value=Dist.CLIENT)
    public static void handleCameraShake(ViewportEvent.ComputeCameraAngles event) {
        if (clientCameraShakeData.isEmpty()) {
            return;
        }
        Entity player = event.getCamera().getEntity();
        List<CameraShakeData> sortedActiveCameraShakes = clientCameraShakeData.stream().filter(data -> data.dimension.equals((Object)player.level.dimension())).sorted(Comparator.comparingDouble(o -> o.origin.distanceToSqr(player.position()))).toList();
        if (sortedActiveCameraShakes.isEmpty()) {
            return;
        }
        CameraShakeData cameraShake = sortedActiveCameraShakes.get(0);
        Vec3 closestPos = cameraShake.origin;
        float distanceMultiplier = 1.0f / (cameraShake.radius * cameraShake.radius);
        float fadeout = cameraShake.duration - cameraShake.tickCount >= 20 ? 1.0f : (float)(cameraShake.duration - cameraShake.tickCount) * 0.05f;
        fadeout = Math.clamp((float)fadeout, (float)0.0f, (float)1.0f);
        float intensity = (float)Mth.clampedLerp((double)1.0, (double)0.0, (double)(closestPos.distanceToSqr(player.position()) * (double)distanceMultiplier)) * fadeout;
        float f = (float)((double)player.tickCount + event.getPartialTick());
        float yaw = Mth.cos((float)(f * 1.5f)) * intensity * 0.5f;
        float pitch = Mth.cos((float)(f * 2.0f)) * intensity * 0.5f;
        float roll = Mth.sin((float)(f * 2.2f)) * intensity * 0.5f;
        event.setYaw(event.getYaw() + yaw);
        event.setRoll(event.getRoll() + roll);
        event.setPitch(event.getPitch() + pitch);
    }

    @SubscribeEvent
    @OnlyIn(value=Dist.CLIENT)
    public static void handleCameraShake(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().isSingleplayer() && Minecraft.getInstance().isPaused()) {
            return;
        }
        ArrayList<CameraShakeData> toRemove = new ArrayList<CameraShakeData>();
        for (CameraShakeData data : clientCameraShakeData) {
            ++data.tickCount;
            if (data.tickCount <= data.duration + 5) continue;
            toRemove.add(data);
        }
        clientCameraShakeData.removeAll(toRemove);
    }
}

