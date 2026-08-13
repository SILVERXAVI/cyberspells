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
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.wall_of_fire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.spells.wall_of_fire.WallOfFireEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WallOfFireRenderer
extends EntityRenderer<WallOfFireEntity> {
    private static ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace((String)"textures/block/fire_0.png");

    public WallOfFireRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(WallOfFireEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout((ResourceLocation)TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float height = 3.0f;
        Vec3 origin = entity.position();
        for (int i = 0; i < entity.subEntities.length - 1; ++i) {
            Vec3 start = entity.subEntities[i].position().subtract(origin);
            Vec3 end = entity.subEntities[i + 1].position().subtract(origin);
            int frameCount = 32;
            int frame = (entity.tickCount + i * 87) % frameCount;
            float uvPerFrame = 1.0f / (float)frameCount;
            float uvY = (float)frame * uvPerFrame;
            poseStack.pushPose();
            consumer.addVertex(poseMatrix, (float)start.x, (float)start.y, (float)start.z).setColor(255, 255, 255, 255).setUv(0.0f, uvY + uvPerFrame).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)start.x, (float)start.y + height, (float)start.z).setColor(255, 255, 255, 255).setUv(0.0f, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)end.x, (float)end.y + height, (float)end.z).setColor(255, 255, 255, 255).setUv(1.0f, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)end.x, (float)end.y, (float)end.z).setColor(255, 255, 255, 255).setUv(1.0f, uvY + uvPerFrame).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            consumer.addVertex(poseMatrix, (float)start.x, (float)start.y, (float)start.z).setColor(255, 255, 255, 255).setUv(0.0f, uvY + uvPerFrame).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)start.x, (float)start.y + height, (float)start.z).setColor(255, 255, 255, 255).setUv(0.0f, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)end.x, (float)end.y + height, (float)end.z).setColor(255, 255, 255, 255).setUv(1.0f, uvY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, (float)end.x, (float)end.y, (float)end.z).setColor(255, 255, 255, 255).setUv(1.0f, uvY + uvPerFrame).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
            poseStack.popPose();
        }
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(WallOfFireEntity entity) {
        return TEXTURE;
    }
}

