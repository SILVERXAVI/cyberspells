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
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.blood_slash;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BloodSlashRenderer
extends EntityRenderer<BloodSlashProjectile> {
    private static ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/blood_slash/blood_slash_large.png");
    private static ResourceLocation[] TEXTURES = new ResourceLocation[]{ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_0.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_1.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_2.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_3.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_4.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_5.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_6.png"), ResourceLocation.withDefaultNamespace((String)"textures/particle/sweep_7.png")};

    public BloodSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(BloodSlashProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp((float)partialTicks, (float)entity.yRotO, (float)entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp((float)partialTicks, (float)entity.xRotO, (float)entity.getXRot())));
        ++entity.animationTime;
        poseStack.mulPose(Axis.ZP.rotationDegrees((float)(entity.animationSeed % 30 - 15) * (float)Math.sin((double)entity.animationTime * 0.015)));
        float oldWith = (float)entity.oldBB.getXsize();
        float width = entity.getBbWidth();
        width = oldWith + (width - oldWith) * Math.min(partialTicks, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-15.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-10.0f));
        this.drawSlash(pose, entity, bufferSource, light, width, 4);
        poseStack.mulPose(Axis.YP.rotationDegrees(30.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(20.0f));
        this.drawSlash(pose, entity, bufferSource, light, width, 0);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, BloodSlashProjectile entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity, offset)));
        float halfWidth = width * 0.5f;
        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(90, 0, 10, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(90, 0, 10, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(90, 0, 10, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(90, 0, 10, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0f, 1.0f, 0.0f);
    }

    public ResourceLocation getTextureLocation(BloodSlashProjectile entity) {
        int frame = entity.animationTime / 4 % TEXTURES.length;
        return TEXTURES[frame];
    }

    private ResourceLocation getTextureLocation(BloodSlashProjectile entity, int offset) {
        int frame = (entity.animationTime / 6 + offset) % TEXTURES.length;
        return TEXTURES[frame];
    }
}

