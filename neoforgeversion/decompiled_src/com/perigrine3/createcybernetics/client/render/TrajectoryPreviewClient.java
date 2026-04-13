/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.OutlineBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.joml.Matrix4f
 */
package com.perigrine3.createcybernetics.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class TrajectoryPreviewClient {
    private static final int MAX_STEPS = 120;
    private static final float BOW_GRAVITY = 0.05f;
    private static final float BOW_DRAG = 0.99f;
    private static boolean externalEnable = true;

    private TrajectoryPreviewClient() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        HitTarget firstHit;
        ArrayList<Vec3> pts;
        boolean built;
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) {
            return;
        }
        if (!TrajectoryPreviewClient.shouldRender(player)) {
            return;
        }
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        boolean bl = built = TrajectoryPreviewClient.tryBuildBow(mc, player, level, partial, pts = new ArrayList<Vec3>(122), firstHit = new HitTarget()) || TrajectoryPreviewClient.tryBuildGenericThrowable(mc, player, level, partial, pts, firstHit) || TrajectoryPreviewClient.tryBuildCrossbow(mc, player, level, partial, pts, firstHit) || TrajectoryPreviewClient.tryBuildTrident(mc, player, level, partial, pts, firstHit);
        if (!built || pts.size() < 2) {
            return;
        }
        TrajectoryPreviewClient.renderLine(event, mc, level, pts);
        TrajectoryPreviewClient.renderFirstHitOverlay(event, mc, level, firstHit, partial);
    }

    private static boolean tryBuildBow(Minecraft mc, LocalPlayer player, ClientLevel level, float partial, List<Vec3> pts, HitTarget firstHit) {
        if (!player.isUsingItem()) {
            return false;
        }
        ItemStack using = player.getUseItem();
        if (using.isEmpty() || !(using.getItem() instanceof BowItem)) {
            return false;
        }
        int usedTicks = using.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks();
        float power = BowItem.getPowerForTime((int)usedTicks);
        if (power <= 0.05f) {
            return false;
        }
        Vec3 eye = player.getEyePosition(partial);
        Vec3 look = player.getViewVector(partial).normalize();
        double speed = (double)power * 3.0;
        Vec3 simPos = eye.add(0.0, -0.1, 0.0);
        Vec3 simVel = look.scale(speed);
        pts.clear();
        pts.add(simPos);
        firstHit.clear();
        Vec3 pos = simPos;
        Vec3 vel = simVel;
        for (int i = 0; i < 120; ++i) {
            Vec3 next = pos.add(vel);
            HitResult hit = TrajectoryPreviewClient.clipFirst(level, player, pos, next);
            if (hit.getType() != HitResult.Type.MISS) {
                pts.add(hit.getLocation());
                firstHit.setFrom(hit);
                break;
            }
            pts.add(next);
            vel = vel.scale((double)0.99f).add(0.0, (double)-0.05f, 0.0);
            pos = next;
        }
        if (pts.size() < 2) {
            return false;
        }
        TrajectoryPreviewClient.applyBowRenderOffset(player, look, pts);
        return true;
    }

    private static void applyBowRenderOffset(LocalPlayer player, Vec3 lookUnit, List<Vec3> pts) {
        if (pts.isEmpty()) {
            return;
        }
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        Vec3 right = lookUnit.cross(up);
        right = right.lengthSqr() < 1.0E-6 ? new Vec3(1.0, 0.0, 0.0) : right.normalize();
        int armSign = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        Vec3 delta0 = lookUnit.scale(0.45).add(right.scale(0.16 * (double)armSign)).add(0.0, 0.02, 0.0);
        int n = pts.size();
        if (n == 1) {
            pts.set(0, pts.get(0).add(delta0));
            return;
        }
        for (int i = 0; i < n; ++i) {
            double t = (double)i / (double)(n - 1);
            double w = 1.0 - t;
            if ((w *= w) <= 0.0) continue;
            pts.set(i, pts.get(i).add(delta0.scale(w)));
        }
    }

    private static HitResult clipFirst(ClientLevel level, LocalPlayer player, Vec3 from, Vec3 to) {
        BlockHitResult blockHit = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player));
        Vec3 blockLoc = blockHit.getLocation();
        double blockDist2 = from.distanceToSqr(blockLoc);
        EntityHitResult entityHit = TrajectoryPreviewClient.clipEntities(level, player, from, to);
        if (entityHit == null) {
            return blockHit;
        }
        Vec3 entLoc = entityHit.getLocation();
        double entDist2 = from.distanceToSqr(entLoc);
        if (blockHit.getType() == HitResult.Type.MISS) {
            return entityHit;
        }
        return entDist2 <= blockDist2 ? entityHit : blockHit;
    }

    private static EntityHitResult clipEntities(ClientLevel level, LocalPlayer player, Vec3 from, Vec3 to) {
        Vec3 delta = to.subtract(from);
        AABB sweep = new AABB(from, to).inflate(0.75);
        Entity best = null;
        Vec3 bestHit = null;
        double bestDist2 = Double.MAX_VALUE;
        List ents = level.getEntities((Entity)player, sweep, e -> e instanceof LivingEntity && e.isPickable() && e.isAlive());
        for (Entity e2 : ents) {
            Vec3 hit;
            double d2;
            AABB bb = e2.getBoundingBox().inflate(0.3);
            Optional opt = bb.clip(from, to);
            if (opt.isEmpty() || !((d2 = from.distanceToSqr(hit = (Vec3)opt.get())) < bestDist2)) continue;
            bestDist2 = d2;
            best = e2;
            bestHit = hit;
        }
        if (best == null) {
            return null;
        }
        return new EntityHitResult(best, bestHit);
    }

    private static boolean tryBuildGenericThrowable(Minecraft mc, LocalPlayer player, ClientLevel level, float partial, List<Vec3> pts, HitTarget firstHit) {
        return false;
    }

    private static boolean tryBuildCrossbow(Minecraft mc, LocalPlayer player, ClientLevel level, float partial, List<Vec3> pts, HitTarget firstHit) {
        return false;
    }

    private static boolean tryBuildTrident(Minecraft mc, LocalPlayer player, ClientLevel level, float partial, List<Vec3> pts, HitTarget firstHit) {
        return false;
    }

    private static void renderLine(RenderLevelStageEvent event, Minecraft mc, ClientLevel level, List<Vec3> pts) {
        RenderSystem.lineWidth((float)3.0f);
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffer.getBuffer(RenderType.lines());
        Matrix4f mat = poseStack.last().pose();
        int r = 255;
        int g = 160;
        int b = 40;
        int a = 255;
        for (int i = 0; i < pts.size() - 1; ++i) {
            Vec3 p0 = pts.get(i).subtract(cam);
            Vec3 p1 = pts.get(i + 1).subtract(cam);
            float nx = 0.0f;
            float ny = 1.0f;
            float nz = 0.0f;
            vc.addVertex(mat, (float)p0.x, (float)p0.y, (float)p0.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
            vc.addVertex(mat, (float)p1.x, (float)p1.y, (float)p1.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
        }
        buffer.endBatch(RenderType.lines());
    }

    private static void renderFirstHitOverlay(RenderLevelStageEvent event, Minecraft mc, ClientLevel level, HitTarget hit, float partial) {
        if (hit.type == HitTargetType.NONE) {
            return;
        }
        OutlineBufferSource outlines = mc.renderBuffers().outlineBufferSource();
        outlines.setColor(255, 160, 40, 255);
        PoseStack poseStack = event.getPoseStack();
        if (hit.type == HitTargetType.ENTITY) {
            Entity e = level.getEntity(hit.entityId);
            if (!(e instanceof LivingEntity)) {
                return;
            }
            LivingEntity living = (LivingEntity)e;
            if (!living.isAlive()) {
                return;
            }
            Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
            EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
            double x = living.getX() - cam.x;
            double y = living.getY() - cam.y;
            double z = living.getZ() - cam.z;
            dispatcher.render((Entity)living, x, y, z, living.getYRot(), partial, poseStack, (MultiBufferSource)outlines, 0xF000F0);
            outlines.endOutlineBatch();
            return;
        }
        if (hit.type == HitTargetType.BLOCK) {
            BlockPos pos = hit.blockPos;
            if (pos == null) {
                return;
            }
            Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
            AABB bb = level.getBlockState(pos).getShape((BlockGetter)level, pos).bounds().move(pos);
            if (bb.getSize() < 1.0E-6) {
                bb = new AABB(pos);
            }
            TrajectoryPreviewClient.renderBoxOutline(poseStack, mc.renderBuffers().bufferSource(), cam, bb);
        }
    }

    private static void renderBoxOutline(PoseStack poseStack, MultiBufferSource.BufferSource buffer, Vec3 cam, AABB worldBox) {
        VertexConsumer vc = buffer.getBuffer(RenderType.lines());
        Matrix4f mat = poseStack.last().pose();
        AABB base = worldBox.move(-cam.x, -cam.y, -cam.z);
        int r = 255;
        int g = 160;
        int bl = 40;
        int a = 255;
        float nx = 0.0f;
        float ny = 1.0f;
        float nz = 0.0f;
        double e = 0.0035;
        double[] s = new double[]{-e, e};
        TrajectoryPreviewClient.drawBoxLines(vc, mat, base, r, g, bl, a, nx, ny, nz);
        for (double ox : s) {
            for (double oy : s) {
                for (double oz : s) {
                    TrajectoryPreviewClient.drawBoxLines(vc, mat, base.move(ox, oy, oz), r, g, bl, a, nx, ny, nz);
                }
            }
        }
        buffer.endBatch(RenderType.lines());
    }

    private static void drawBoxLines(VertexConsumer vc, Matrix4f mat, AABB b, int r, int g, int bl, int a, float nx, float ny, float nz) {
        float x0 = (float)b.minX;
        float y0 = (float)b.minY;
        float z0 = (float)b.minZ;
        float x1 = (float)b.maxX;
        float y1 = (float)b.maxY;
        float z1 = (float)b.maxZ;
        TrajectoryPreviewClient.line(vc, mat, x0, y0, z0, x1, y0, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y0, z0, x1, y0, z1, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y0, z1, x0, y0, z1, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x0, y0, z1, x0, y0, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x0, y1, z0, x1, y1, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y1, z0, x1, y1, z1, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y1, z1, x0, y1, z1, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x0, y1, z1, x0, y1, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x0, y0, z0, x0, y1, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y0, z0, x1, y1, z0, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x1, y0, z1, x1, y1, z1, r, g, bl, a, nx, ny, nz);
        TrajectoryPreviewClient.line(vc, mat, x0, y0, z1, x0, y1, z1, r, g, bl, a, nx, ny, nz);
    }

    private static void line(VertexConsumer vc, Matrix4f mat, float x0, float y0, float z0, float x1, float y1, float z1, int r, int g, int b, int a, float nx, float ny, float nz) {
        vc.addVertex(mat, x0, y0, z0).setColor(r, g, b, a).setNormal(nx, ny, nz);
        vc.addVertex(mat, x1, y1, z1).setColor(r, g, b, a).setNormal(nx, ny, nz);
    }

    private static boolean shouldRender(LocalPlayer player) {
        if (!player.isUsingItem()) {
            return false;
        }
        if (!externalEnable) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        Item module = (Item)ModItems.EYEUPGRADES_TRAJECTORYCALCULATOR.get();
        Item hudjack = (Item)ModItems.EYEUPGRADES_HUDJACK.get();
        if (!TrajectoryPreviewClient.hasInstalled(data, module)) {
            return false;
        }
        return TrajectoryPreviewClient.hasInstalled(data, hudjack);
    }

    private static boolean hasInstalled(PlayerCyberwareData data, Item item) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != item) continue;
                return true;
            }
        }
        return false;
    }

    public static void setExternallyEnabled(boolean enabled) {
        externalEnable = enabled;
    }

    private static final class HitTarget {
        private HitTargetType type = HitTargetType.NONE;
        private BlockPos blockPos = null;
        private int entityId = -1;

        private HitTarget() {
        }

        void clear() {
            this.type = HitTargetType.NONE;
            this.blockPos = null;
            this.entityId = -1;
        }

        void setFrom(HitResult hit) {
            this.clear();
            if (hit instanceof EntityHitResult) {
                EntityHitResult ehr = (EntityHitResult)hit;
                this.type = HitTargetType.ENTITY;
                this.entityId = ehr.getEntity().getId();
                return;
            }
            if (hit instanceof BlockHitResult) {
                BlockHitResult bhr = (BlockHitResult)hit;
                this.type = HitTargetType.BLOCK;
                this.blockPos = bhr.getBlockPos();
            }
        }
    }

    private static enum HitTargetType {
        NONE,
        BLOCK,
        ENTITY;

    }
}

