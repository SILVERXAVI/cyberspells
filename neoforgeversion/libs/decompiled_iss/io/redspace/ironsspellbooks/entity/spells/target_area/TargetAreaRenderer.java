/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.entity.spells.target_area;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TargetAreaRenderer
extends EntityRenderer<TargetedAreaEntity> {
    int fadeTick = -1;

    public TargetAreaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public ResourceLocation getTextureLocation(TargetedAreaEntity pEntity) {
        return null;
    }

    public void render(TargetedAreaEntity entity, float pEntityYaw, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        int i;
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl((ResourceLocation)SpellRenderingHelper.SOLID, (float)0.0f, (float)0.0f));
        Vector3f color = entity.getColor();
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float radius = entity.getRadius();
        int segments = (int)(5.0f * radius + 9.0f);
        float angle = (float)Math.PI * 2 / (float)segments;
        float entityY = (float)Mth.lerp((double)pPartialTick, (double)entity.yOld, (double)entity.getY());
        float[] heights = new float[6];
        for (i = 0; i < 6; ++i) {
            int degrees = i * 60;
            float x = radius * Mth.cos((float)((float)degrees * ((float)Math.PI / 180)));
            float z = radius * Mth.sin((float)((float)degrees * ((float)Math.PI / 180)));
            float y = Utils.findRelativeGroundLevel(entity.level, entity.position().add((double)x, (double)entity.getBbHeight(), (double)z), (int)(entity.getBbHeight() * 4.0f));
            heights[i] = y - entityY;
            if (!entity.level.collidesWithSuffocatingBlock(null, AABB.ofSize((Vec3)new Vec3((double)x, (double)y, (double)z), (double)0.1, (double)0.1, (double)0.1))) continue;
            heights[i] = 0.0f;
        }
        for (i = 0; i < segments; ++i) {
            float theta = angle * (float)i;
            float theta2 = angle * (float)(i + 1);
            float x1 = radius * Mth.cos((float)theta);
            float x2 = radius * Mth.cos((float)theta2);
            float z1 = radius * Mth.sin((float)theta);
            float z2 = radius * Mth.sin((float)theta2);
            int degrees = (int)(theta * 57.295776f);
            int degrees2 = (int)(theta2 * 57.295776f);
            int j = degrees / 60 % 6;
            float heightMin = heights[j];
            float heightMax = heights[(j + 1) % 6];
            float f = theta * 57.295776f % 60.0f / 60.0f;
            float f2 = theta2 * 57.295776f % 60.0f / 60.0f;
            float y1 = Mth.lerp((float)f, (float)heightMin, (float)heightMax);
            if (f2 < f) {
                heightMin = heightMax;
                heightMax = heights[(j + 2) % 6];
            }
            float y2 = Mth.lerp((float)f2, (float)heightMin, (float)heightMax);
            float alpha = 1.0f;
            if (entity.isFading()) {
                if (this.fadeTick < 0) {
                    this.fadeTick = entity.tickCount;
                }
                alpha = Mth.clampedLerp((float)1.0f, (float)0.0f, (float)(((float)entity.tickCount + pPartialTick - (float)this.fadeTick) / 10.0f));
            }
            consumer.addVertex(poseMatrix, x2, y2 - 0.6f, z2).setColor(color.x() * alpha, color.y() * alpha, color.z() * alpha, 1.0f).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light * 4).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, x2, y2 + 0.6f, z2).setColor(0, 0, 0, 1).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light * 4).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, x1, y1 + 0.6f, z1).setColor(0, 0, 0, 1).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light * 4).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseMatrix, x1, y1 - 0.6f, z1).setColor(color.x() * alpha, color.y() * alpha, color.z() * alpha, 1.0f).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light * 4).setNormal(0.0f, 1.0f, 0.0f);
        }
        poseStack.popPose();
    }

    private ParticleOptions particle(int i) {
        return switch (i) {
            case 0 -> ParticleHelper.SNOWFLAKE;
            case 1 -> ParticleHelper.UNSTABLE_ENDER;
            case 2 -> ParticleHelper.ACID_BUBBLE;
            case 3 -> ParticleHelper.BLOOD;
            case 4 -> ParticleHelper.WISP;
            case 5 -> ParticleHelper.ELECTRIC_SPARKS;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}

