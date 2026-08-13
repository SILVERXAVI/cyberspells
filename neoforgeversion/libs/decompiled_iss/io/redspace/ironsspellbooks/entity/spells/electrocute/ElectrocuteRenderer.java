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
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.electrocute;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.electrocute.ElectrocuteProjectile;
import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
public class ElectrocuteRenderer
extends EntityRenderer<ElectrocuteProjectile> {
    private static ResourceLocation[] TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/electric_beams/beam_1.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_2.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_3.png"), IronsSpellbooks.id("textures/entity/electric_beams/beam_4.png")};
    private static ResourceLocation SOLID = IronsSpellbooks.id("textures/entity/electric_beams/solid.png");

    public ElectrocuteRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(ElectrocuteProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        Vec3 to;
        Vec3 from;
        int i;
        float width;
        if (entity.getOwner() == null) {
            return;
        }
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.translate(0.0f, entity.getEyeHeight() * 0.5f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getOwner().getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getOwner().getXRot()));
        poseStack.translate(0.0, 0.0, 0.1);
        List<Vec3> segments = entity.getBeamCache();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive((ResourceLocation)this.getTextureLocation(entity)));
        float height = width = 0.3f;
        Vec3 start = Vec3.ZERO;
        for (i = 0; i < segments.size() - 1; i += 2) {
            from = segments.get(i).add(start);
            to = segments.get(i + 1).add(start);
            this.drawHull(from, to, width, height, pose, consumer, 0, 156, 255, 30);
            this.drawHull(from, to, width * 0.55f, height * 0.55f, pose, consumer, 63, 178, 255, 30);
        }
        consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magicNoCull(this.getTextureLocation(entity)));
        for (i = 0; i < segments.size() - 1; i += 2) {
            from = segments.get(i).add(start);
            to = segments.get(i + 1).add(start);
            this.drawHull(from, to, width * 0.2f, height * 0.2f, pose, consumer, 255, 255, 255, 255);
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        this.drawQuad(from.subtract(0.0, (double)(height * 0.5f), 0.0), to.subtract(0.0, (double)(height * 0.5f), 0.0), width, 0.0f, pose, consumer, r, g, b, a);
        this.drawQuad(from.add(0.0, (double)(height * 0.5f), 0.0), to.add(0.0, (double)(height * 0.5f), 0.0), width, 0.0f, pose, consumer, r, g, b, a);
        this.drawQuad(from.subtract((double)(width * 0.5f), 0.0, 0.0), to.subtract((double)(width * 0.5f), 0.0, 0.0), 0.0f, height, pose, consumer, r, g, b, a);
        this.drawQuad(from.add((double)(width * 0.5f), 0.0, 0.0), to.add((double)(width * 0.5f), 0.0, 0.0), 0.0f, height, pose, consumer, r, g, b, a);
    }

    public void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        consumer.addVertex(poseMatrix, (float)from.x - halfWidth, (float)from.y - halfHeight, (float)from.z).setColor(r, g, b, a).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)from.x + halfWidth, (float)from.y + halfHeight, (float)from.z).setColor(r, g, b, a).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)to.x + halfWidth, (float)to.y + halfHeight, (float)to.z).setColor(r, g, b, a).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)to.x - halfWidth, (float)to.y - halfHeight, (float)to.z).setColor(r, g, b, a).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
    }

    public ResourceLocation getTextureLocation(ElectrocuteProjectile p_115264_) {
        return SOLID;
    }
}

