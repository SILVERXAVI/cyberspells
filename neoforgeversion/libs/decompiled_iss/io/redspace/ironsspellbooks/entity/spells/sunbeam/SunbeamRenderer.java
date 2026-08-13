/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.spells.sunbeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.sunbeam.SunbeamEntity;
import io.redspace.ironsspellbooks.render.RenderHelper;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SunbeamRenderer
extends EntityRenderer<SunbeamEntity> {
    public SunbeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRender(SunbeamEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    public void render(SunbeamEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        float maxRadius = 2.5f;
        float minRadius = 0.005f;
        float deltaTicks = (float)entity.tickCount + partialTicks;
        float deltaUV = -deltaTicks % 10.0f;
        float max = Mth.frac((float)(deltaUV * 0.2f - (float)Mth.floor((float)(deltaUV * 0.1f))));
        float min = -1.0f + max;
        float f = deltaTicks / 15.0f;
        f *= f;
        float radius = Mth.clampedLerp((float)maxRadius, (float)minRadius, (float)f);
        VertexConsumer inner = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magic(SpellRenderingHelper.BEACON));
        float halfRadius = radius * 0.5f;
        float quarterRadius = halfRadius * 0.5f;
        float yMin = entity.onGround() ? 0.0f : Utils.findRelativeGroundLevel(entity.level, entity.position(), 8) - (float)entity.getY();
        for (int i = 0; i < 4; ++i) {
            RenderHelper.quadBuilder().vertex(-halfRadius, yMin, -halfRadius).uv(0.0f, min).normal(0.0f, 1.0f, 0.0f).vertex(-halfRadius, yMin, halfRadius).uv(1.0f, min).normal(0.0f, 1.0f, 0.0f).vertex(-halfRadius, 250.0f, halfRadius).uv(1.0f, max).normal(0.0f, 1.0f, 0.0f).vertex(-halfRadius, 250.0f, -halfRadius).uv(0.0f, max).normal(0.0f, 1.0f, 0.0f).color(Mth.clamp((float)(0.8f * f), (float)0.0f, (float)1.0f), Mth.clamp((float)(0.8f * f * f), (float)0.0f, (float)1.0f), Mth.clamp((float)(0.5f * f * f), (float)0.0f, (float)1.0f)).light(0xF000F0).overlay(OverlayTexture.NO_OVERLAY).matrix(poseStack.last().pose()).build(inner);
            RenderHelper.quadBuilder().vertex(-quarterRadius, yMin, -quarterRadius).uv(0.0f, min).normal(0.0f, 1.0f, 0.0f).vertex(-quarterRadius, yMin, quarterRadius).uv(1.0f, min).normal(0.0f, 1.0f, 0.0f).vertex(-quarterRadius, 250.0f, quarterRadius).uv(1.0f, max).normal(0.0f, 1.0f, 0.0f).vertex(-quarterRadius, 250.0f, -quarterRadius).uv(0.0f, max).normal(0.0f, 1.0f, 0.0f).color(Mth.clamp((float)(1.0f * f), (float)0.0f, (float)1.0f), Mth.clamp((float)(0.85f * f), (float)0.0f, (float)1.0f), Mth.clamp((float)(0.7f * f * f), (float)0.0f, (float)1.0f)).light(0xF000F0).overlay(OverlayTexture.NO_OVERLAY).matrix(poseStack.last().pose()).build(inner);
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(SunbeamEntity entity) {
        return SpellRenderingHelper.BEACON;
    }
}

