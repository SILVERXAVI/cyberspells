/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Quaternionf
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.entity.custom.GuardianBeamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class GuardianBeamRenderer
extends EntityRenderer<GuardianBeamEntity> {
    private static final ResourceLocation TEX = ResourceLocation.withDefaultNamespace((String)"textures/entity/guardian_beam.png");

    public GuardianBeamRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public boolean shouldRender(GuardianBeamEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    public void render(GuardianBeamEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Vec3 start = entity.getStart();
        Vec3 end = entity.getEnd();
        Vec3 delta = end.subtract(start);
        float len = (float)delta.length();
        if (len < 0.001f) {
            return;
        }
        float dx = (float)delta.x;
        float dy = (float)delta.y;
        float dz = (float)delta.z;
        float yawRad = (float)Mth.atan2((double)dx, (double)dz);
        float pitchRad = (float)Mth.atan2((double)dy, (double)Mth.sqrt((float)(dx * dx + dz * dz)));
        float power = Mth.clamp((float)entity.getPower(), (float)0.0f, (float)1.0f);
        boolean charging = entity.isCharging();
        float baseW = 0.06f;
        float extraW = 0.16f * power;
        float w = baseW + extraW;
        w = !charging ? (w *= 1.35f) : (w *= 1.1f);
        float glowW = w * 1.75f;
        int baseR = 58;
        int baseG = 176;
        int baseB = 158;
        float pulse = 0.6f + 0.4f * (0.5f + 0.5f * Mth.sin((float)(((float)entity.tickCount + partialTick) * 0.35f)));
        int coreA = charging ? (int)(Mth.lerp((float)power, (float)70.0f, (float)170.0f) * pulse) : 230;
        int glowA = charging ? (int)(Mth.lerp((float)power, (float)25.0f, (float)95.0f) * pulse) : 120;
        float t = ((float)entity.tickCount + partialTick) * 0.02f;
        float v0 = -t;
        float v1 = len * 0.5f - t;
        int overlay = OverlayTexture.NO_OVERLAY;
        int light = 0xF000F0;
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotateY(yawRad));
        poseStack.mulPose(new Quaternionf().rotateX(-pitchRad));
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)TEX));
        PoseStack.Pose pose = poseStack.last();
        GuardianBeamRenderer.emitQuad(vc, pose, -glowW, 0.0f, 0.0f, glowW, 0.0f, 0.0f, glowW, 0.0f, len, -glowW, 0.0f, len, 0.0f, v0, 1.0f, v1, baseR, baseG, baseB, glowA, overlay, light, 0.0f, 1.0f, 0.0f);
        GuardianBeamRenderer.emitQuad(vc, pose, 0.0f, -glowW, 0.0f, 0.0f, glowW, 0.0f, 0.0f, glowW, len, 0.0f, -glowW, len, 0.0f, v0, 1.0f, v1, baseR, baseG, baseB, glowA, overlay, light, 1.0f, 0.0f, 0.0f);
        int coreR = Mth.clamp((int)(baseR + (int)(25.0f * power)), (int)0, (int)255);
        int coreG = Mth.clamp((int)(baseG + (int)(55.0f * power)), (int)0, (int)255);
        int coreB = Mth.clamp((int)(baseB + (int)(25.0f * power)), (int)0, (int)255);
        GuardianBeamRenderer.emitQuad(vc, pose, -w, 0.0f, 0.0f, w, 0.0f, 0.0f, w, 0.0f, len, -w, 0.0f, len, 0.0f, v0, 1.0f, v1, coreR, coreG, coreB, coreA, overlay, light, 0.0f, 1.0f, 0.0f);
        GuardianBeamRenderer.emitQuad(vc, pose, 0.0f, -w, 0.0f, 0.0f, w, 0.0f, 0.0f, w, len, 0.0f, -w, len, 0.0f, v0, 1.0f, v1, coreR, coreG, coreB, coreA, overlay, light, 1.0f, 0.0f, 0.0f);
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static void emitQuad(VertexConsumer vc, PoseStack.Pose pose, float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float u0, float v0, float u1, float v1, int r, int g, int b, int a, int overlay, int light, float nx, float ny, float nz) {
        vc.addVertex(pose, x0, y0, z0).setColor(r, g, b, a).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x1, y1, z1).setColor(r, g, b, a).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x2, y2, z2).setColor(r, g, b, a).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(pose, x3, y3, z3).setColor(r, g, b, a).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }

    public ResourceLocation getTextureLocation(GuardianBeamEntity entity) {
        return TEX;
    }
}

