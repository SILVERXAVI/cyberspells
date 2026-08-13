/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.black_hole;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BlackHoleRenderer
extends EntityRenderer<BlackHole> {
    private static final ResourceLocation CENTER_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/black_hole.png");
    private static final ResourceLocation BEAM_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/beam.png");
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);

    public BlackHoleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(BlackHole entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() / 2.0, 0.0);
        float entityScale = entity.getBbWidth() * 0.025f;
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        Vec3 normalToCamera = this.entityRenderDispatcher.camera.getPosition().subtract(entity.getBoundingBox().getCenter()).normalize().scale(2.0);
        poseStack.translate(normalToCamera.x, normalToCamera.y, normalToCamera.z);
        poseStack.scale(0.5f * entityScale, 0.5f * entityScale, 0.5f * entityScale);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)CENTER_TEXTURE));
        float centerScale = 3.0f;
        consumer.addVertex(poseMatrix, 0.0f, -centerScale, -centerScale).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, centerScale, -centerScale).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, centerScale, centerScale).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, -centerScale, centerScale).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() / 2.0, 0.0);
        float animationProgress = ((float)entity.tickCount + partialTicks) / 200.0f;
        float fadeProgress = 0.5f;
        RandomSource randomSource = RandomSource.create((long)432L);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl((ResourceLocation)BEAM_TEXTURE, (float)0.0f, (float)0.0f));
        float segments = Math.min(animationProgress, 0.8f);
        int i = 0;
        while ((float)i < (segments + segments * segments) / 2.0f * 40.0f) {
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0f));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0f));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0f + animationProgress * 90.0f));
            float size1 = (randomSource.nextFloat() * 10.0f + 5.0f + fadeProgress * 5.0f) * entityScale * 0.4f;
            Matrix4f matrix = poseStack.last().pose();
            Matrix3f normalMatrix2 = poseStack.last().normal();
            BlackHoleRenderer.drawTriangle(vertexConsumer, matrix, normalMatrix2, size1);
            ++i;
        }
        poseStack.popPose();
        super.render((Entity)entity, pEntityYaw, partialTicks, poseStack, bufferSource, pPackedLight);
    }

    public ResourceLocation getTextureLocation(BlackHole pEntity) {
        return IcicleRenderer.TEXTURE;
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float size) {
        consumer.addVertex(poseMatrix, 0.0f, 0.0f, 0.0f).setColor(255, 0, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, 3.0f * size, -1.0f * size).setColor(0, 0, 0, 0).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, 3.0f * size, 1.0f * size).setColor(0, 0, 0, 0).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, 0.0f, 0.0f).setColor(255, 0, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
    }
}

