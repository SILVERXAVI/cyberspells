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
 *  net.minecraft.world.entity.Entity
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.portal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PortalRenderer
extends EntityRenderer<PortalEntity> {
    private static final ResourceLocation ROUND_PORTAL = IronsSpellbooks.id("textures/entity/portal/portal_round.png");
    private static final ResourceLocation ELDRITCH_ROUND_PORTAL = IronsSpellbooks.id("textures/entity/portal/pocket_dimension_portal_round.png");
    private static final ResourceLocation ELDRITCH_SQUARE_PORTAL = IronsSpellbooks.id("textures/entity/portal/pocket_dimension_portal_square.png");
    private static final ResourceLocation SQUARE_PORTAL = IronsSpellbooks.id("textures/entity/portal/portal_square.png");
    private static final ResourceLocation SQUARE_COLOR_PORTAL = IronsSpellbooks.id("textures/entity/portal/portal_square_color.png");
    static int frameCount = 10;
    static int ticksPerFrame = 2;

    public PortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PortalEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        PortalRenderer.renderPortal(poseStack, bufferSource, entity.tickCount, partialTicks, true, -1);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public static void renderPortal(PoseStack poseStack, MultiBufferSource buffer, int animationTick, float partialTicks, boolean round, int color) {
        PortalRenderer.renderPortal(poseStack, buffer, animationTick, partialTicks, round, false, color);
    }

    public static void renderPortal(PoseStack poseStack, MultiBufferSource buffer, int animationTick, float partialTicks, boolean round, boolean eldritch, int color) {
        poseStack.pushPose();
        poseStack.scale(0.0625f, 0.0625f, 0.0625f);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        ResourceLocation texture = round ? (eldritch ? ELDRITCH_ROUND_PORTAL : ROUND_PORTAL) : (eldritch ? ELDRITCH_SQUARE_PORTAL : (color == -1 ? SQUARE_PORTAL : SQUARE_COLOR_PORTAL));
        VertexConsumer consumer = buffer.getBuffer(RenderHelper.CustomerRenderType.darkGlow(texture));
        int anim = animationTick / ticksPerFrame % frameCount;
        float uvMin = (float)anim / (float)frameCount;
        float uvMax = (float)(anim + 1) / (float)frameCount;
        PortalRenderer.vertex(poseMatrix, normalMatrix, consumer, -8.0f, 0.0f, 0.0f, uvMin, 0.0f, color);
        PortalRenderer.vertex(poseMatrix, normalMatrix, consumer, 8.0f, 0.0f, 0.0f, uvMax, 0.0f, color);
        PortalRenderer.vertex(poseMatrix, normalMatrix, consumer, 8.0f, 32.0f, 0.0f, uvMax, 1.0f, color);
        PortalRenderer.vertex(poseMatrix, normalMatrix, consumer, -8.0f, 32.0f, 0.0f, uvMin, 1.0f, color);
        poseStack.popPose();
    }

    public static void renderPortal(PoseStack poseStack, MultiBufferSource buffer, int animationTick, float partialTicks, boolean round) {
        PortalRenderer.renderPortal(poseStack, buffer, animationTick, partialTicks, round, -1);
    }

    public static void vertex(Matrix4f pMatrix, Matrix3f pNormals, VertexConsumer pVertexBuilder, float pOffsetX, float pOffsetY, float pOffsetZ, float pTextureX, float pTextureY, int color) {
        int r = 255;
        int g = 255;
        int b = 255;
        if (color != -1) {
            r = (color & 0xFF0000) >> 16;
            g = (color & 0xFF00) >> 8;
            b = color & 0xFF;
        }
        pVertexBuilder.addVertex(pMatrix, pOffsetX, pOffsetY, pOffsetZ).setColor(r, g, b, 100).setUv(pTextureX, pTextureY).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        return ROUND_PORTAL;
    }
}

