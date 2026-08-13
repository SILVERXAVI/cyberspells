/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.magic_missile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class MagicMissileRenderer
extends EntityRenderer<MagicMissileProjectile> {
    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/magic_missile/magic_missile.png");
    private static final ResourceLocation FLARE = IronsSpellbooks.id("textures/entity/lens_flare.png");
    private final ModelPart body;

    public MagicMissileRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(FireballRenderer.MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public void render(MagicMissileProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.scale(0.35f, 0.35f, 0.35f);
        VertexConsumer consumer = bufferSource.getBuffer(this.renderType(this.getTextureLocation(entity)));
        this.body.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, RenderHelper.colorf(0.8f, 0.8f, 0.8f));
        poseStack.popPose();
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        float f = (float)entity.tickCount + partialTicks;
        float scale = 0.5f + Mth.sin((float)f) * 0.125f;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(((float)entity.tickCount + partialTicks) * 15.0f));
        consumer = bufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)FLARE));
        consumer.addVertex(poseMatrix, 0.0f, -1.0f, -1.0f).setColor(255, 180, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, 1.0f, -1.0f).setColor(255, 180, 255, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, 1.0f, 1.0f).setColor(255, 180, 255, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, 0.0f, -1.0f, 1.0f).setColor(255, 180, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 1.0f, 0.0f);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public RenderType renderType(ResourceLocation TEXTURE) {
        return RenderType.energySwirl((ResourceLocation)TEXTURE, (float)0.0f, (float)0.0f);
    }

    public ResourceLocation getTextureLocation(MagicMissileProjectile entity) {
        return TEXTURE;
    }
}

