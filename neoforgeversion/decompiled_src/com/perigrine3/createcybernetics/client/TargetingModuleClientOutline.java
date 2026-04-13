/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.ints.IntOpenHashSet
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.OutlineBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 */
package com.perigrine3.createcybernetics.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class TargetingModuleClientOutline {
    private static final IntSet TARGET_ID = new IntOpenHashSet(1);
    private static long expiresAtGameTime = 0L;

    private TargetingModuleClientOutline() {
    }

    public static void clearTarget() {
        TARGET_ID.clear();
        expiresAtGameTime = 0L;
    }

    public static void setTarget(int entityId, int durationTicks) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;
        if (level == null) {
            return;
        }
        if (player != null && entityId == player.getId()) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        if (entityId < 0) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        TARGET_ID.clear();
        TARGET_ID.add(entityId);
        long now = level.getGameTime();
        expiresAtGameTime = now + (long)Math.max(1, durationTicks);
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) {
            return;
        }
        if (TARGET_ID.isEmpty()) {
            return;
        }
        if (level.getGameTime() >= expiresAtGameTime) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        int id = TARGET_ID.iterator().nextInt();
        if (id == player.getId()) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        Entity ent = level.getEntity(id);
        if (!(ent instanceof LivingEntity)) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        LivingEntity living = (LivingEntity)ent;
        if (living == player) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        if (!living.isAlive()) {
            TargetingModuleClientOutline.clearTarget();
            return;
        }
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        OutlineBufferSource outlines = mc.renderBuffers().outlineBufferSource();
        outlines.setColor(255, 140, 0, 255);
        PoseStack poseStack = event.getPoseStack();
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        double x = living.getX() - cam.x;
        double y = living.getY() - cam.y;
        double z = living.getZ() - cam.z;
        dispatcher.render((Entity)living, x, y, z, living.getYRot(), partial, poseStack, (MultiBufferSource)outlines, 0xF000F0);
        outlines.endOutlineBatch();
    }
}

