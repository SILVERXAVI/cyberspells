/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.magic_arrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MagicArrowRenderer
extends EntityRenderer<MagicArrowProjectile> {
    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/magic_arrow.png");

    public MagicArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(MagicArrowProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        MagicArrowRenderer.renderModel(poseStack, bufferSource);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public static void renderModel(PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.scale(0.13f, 0.13f, 0.13f);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magic(MagicArrowRenderer.getTextureLocation()));
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        poseStack.translate(-2.0f, 0.0f, 0.0f);
        for (int j = 0; j < 4; ++j) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            MagicArrowRenderer.vertex(poseMatrix, normalMatrix, consumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, 0xF000F0);
            MagicArrowRenderer.vertex(poseMatrix, normalMatrix, consumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, 0xF000F0);
            MagicArrowRenderer.vertex(poseMatrix, normalMatrix, consumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, 0xF000F0);
            MagicArrowRenderer.vertex(poseMatrix, normalMatrix, consumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, 0xF000F0);
        }
    }

    public static void vertex(Matrix4f pMatrix, Matrix3f pNormals, VertexConsumer pVertexBuilder, int pOffsetX, int pOffsetY, int pOffsetZ, float pTextureX, float pTextureY, int pNormalX, int p_113835_, int p_113836_, int pPackedLight) {
        pVertexBuilder.addVertex(pMatrix, (float)pOffsetX, (float)pOffsetY, (float)pOffsetZ).setColor(200, 200, 200, 255).setUv(pTextureX, pTextureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(pPackedLight).setNormal((float)pNormalX, (float)p_113836_, (float)p_113835_);
    }

    public ResourceLocation getTextureLocation(MagicArrowProjectile entity) {
        return MagicArrowRenderer.getTextureLocation();
    }

    public static ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}

