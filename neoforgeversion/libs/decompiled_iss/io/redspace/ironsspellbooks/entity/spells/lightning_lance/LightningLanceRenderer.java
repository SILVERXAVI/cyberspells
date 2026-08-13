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
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.lightning_lance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class LightningLanceRenderer
extends EntityRenderer<LightningLanceProjectile> {
    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/lightning_lance/lightning_lance.png");

    public LightningLanceRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(LightningLanceProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        LightningLanceRenderer.renderModel(poseStack, bufferSource, entity.getAge());
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public static void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int animOffset) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl((ResourceLocation)TEXTURE, (float)0.0f, (float)0.0f));
        int framecout = 7;
        int anim = animOffset % framecout;
        float uvMin = (float)anim / (float)framecout;
        float uvMax = (float)(anim + 1) / (float)framecout;
        float halfWidth = 2.0f;
        float halfHeight = 1.0f;
        float angleCorrection = 50.0f;
        float texturefix = -0.2f;
        poseStack.mulPose(Axis.XP.rotationDegrees(angleCorrection));
        consumer.addVertex(poseMatrix, 0.0f, -halfWidth, -halfHeight + texturefix).setColor(255, 255, 255, 255).setUv(uvMin, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, halfWidth, -halfHeight + texturefix).setColor(255, 255, 255, 255).setUv(uvMin, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, halfWidth, halfHeight + texturefix).setColor(255, 255, 255, 255).setUv(uvMax, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, -halfWidth, halfHeight + texturefix).setColor(255, 255, 255, 255).setUv(uvMax, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-angleCorrection));
        poseStack.mulPose(Axis.YP.rotationDegrees(-angleCorrection));
        consumer.addVertex(poseMatrix, -halfWidth - texturefix, 0.0f, -halfHeight).setColor(255, 255, 255, 255).setUv(uvMin, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth - texturefix, 0.0f, -halfHeight).setColor(255, 255, 255, 255).setUv(uvMin, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth - texturefix, 0.0f, halfHeight).setColor(255, 255, 255, 255).setUv(uvMax, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, -halfWidth - texturefix, 0.0f, halfHeight).setColor(255, 255, 255, 255).setUv(uvMax, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(angleCorrection));
    }

    public ResourceLocation getTextureLocation(LightningLanceProjectile entity) {
        return TEXTURE;
    }
}

