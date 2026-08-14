/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberwither;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherArmorLayer;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CyberWitherBossRenderer
extends MobRenderer<CyberWitherBoss, CyberWitherModel> {
    private static final ResourceLocation WITHER_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/wither/cyber_wither.png");
    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/wither/cyber_wither_invulnerable.png");
    private static final ResourceLocation BEAM_LOCATION = ResourceLocation.withDefaultNamespace((String)"textures/entity/guardian_beam.png");

    public CyberWitherBossRenderer(EntityRendererProvider.Context context) {
        super(context, (EntityModel)new CyberWitherModel(context.bakeLayer(CyberWitherModel.LAYER_LOCATION)), 1.0f);
        this.addLayer((RenderLayer)new CyberWitherArmorLayer((RenderLayerParent<CyberWitherBoss, CyberWitherModel>)this, context.getModelSet()));
    }

    public void render(CyberWitherBoss pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render((LivingEntity)pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        this.renderChains(pEntity, pPartialTicks, pPoseStack, pBuffer);
    }

    private void renderChains(CyberWitherBoss wither, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        for (int i = 1; i <= 3; ++i) {
            Entity minion;
            int entityId = wither.getMinionId(i);
            if (entityId == -1 || (minion = wither.level().getEntity(entityId)) == null) continue;
            this.renderSingleChain(wither, minion, partialTick, poseStack, buffer);
        }
    }

    private void renderSingleChain(CyberWitherBoss wither, Entity minion, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        double witherX = Mth.lerp((double)partialTick, (double)wither.xo, (double)wither.getX());
        double witherY = Mth.lerp((double)partialTick, (double)wither.yo, (double)wither.getY());
        double witherZ = Mth.lerp((double)partialTick, (double)wither.zo, (double)wither.getZ());
        double minionX = Mth.lerp((double)partialTick, (double)minion.xo, (double)minion.getX());
        double minionY = Mth.lerp((double)partialTick, (double)minion.yo, (double)minion.getY());
        double minionZ = Mth.lerp((double)partialTick, (double)minion.zo, (double)minion.getZ());
        float dx = (float)(minionX - witherX);
        float dy = (float)(minionY + (double)minion.getEyeHeight() * 0.5 - (witherY + (double)wither.getEyeHeight() * 0.5));
        float dz = (float)(minionZ - witherZ);
        float distH = Mth.sqrt((float)(dx * dx + dz * dz));
        float distTotal = Mth.sqrt((float)(dx * dx + dy * dy + dz * dz));
        poseStack.pushPose();
        poseStack.translate(0.0, (double)wither.getEyeHeight() * 0.5, 0.0);
        poseStack.mulPose(Axis.YP.rotation((float)(1.5707963267948966 - Math.atan2(dz, dx))));
        poseStack.mulPose(Axis.XP.rotation((float)(-Math.atan2(dy, distH))));
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)BEAM_LOCATION));
        float beamWidth = 0.15f;
        float vScale = distTotal * 0.5f;
        float age = (float)wither.tickCount + partialTick;
        float vOffset = age * 0.05f * -1.0f;
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        this.drawVertex(vertexconsumer, matrix4f, matrix3f, distTotal, beamWidth, 0.0f, 1.0f, 0.5f, 1.0f, vOffset, vScale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        pose = poseStack.last();
        matrix4f = pose.pose();
        matrix3f = pose.normal();
        this.drawVertex(vertexconsumer, matrix4f, matrix3f, distTotal, beamWidth, 0.0f, 1.0f, 0.5f, 1.0f, vOffset, vScale);
        poseStack.popPose();
    }

    private void drawVertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float length, float width, float r, float g, float b, float alpha, float vOffset, float vScale) {
        consumer.addVertex(pose, -width, 0.0f, 0.0f).setColor(r, g, b, alpha).setUv(0.0f, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(0xF000F0, 0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(pose, width, 0.0f, 0.0f).setColor(r, g, b, alpha).setUv(1.0f, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(0xF000F0, 0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(pose, width, 0.0f, length).setColor(r, g, b, alpha).setUv(1.0f, vOffset + vScale).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(0xF000F0, 0).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(pose, -width, 0.0f, length).setColor(r, g, b, alpha).setUv(0.0f, vOffset + vScale).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(0xF000F0, 0).setNormal(0.0f, 1.0f, 0.0f);
    }

    protected int getBlockLightLevel(CyberWitherBoss pEntity, BlockPos pPos) {
        return 15;
    }

    public boolean shouldRender(CyberWitherBoss pEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        if (super.shouldRender((Entity)pEntity, pCamera, pCamX, pCamY, pCamZ)) {
            return true;
        }
        for (int i = 1; i <= 3; ++i) {
            Entity minion;
            int entityId = pEntity.getMinionId(i);
            if (entityId == -1 || (minion = pEntity.level().getEntity(entityId)) == null) continue;
            Vec3 start = pEntity.position();
            Vec3 end = minion.position();
            if (!pCamera.isVisible(new AABB(start.x, start.y, start.z, end.x, end.y, end.z))) continue;
            return true;
        }
        return false;
    }

    public ResourceLocation getTextureLocation(CyberWitherBoss pEntity) {
        int invulTicks = pEntity.getInvulnerableTicks();
        if (invulTicks > 0) {
            return WITHER_LOCATION;
        }
        return pEntity.isPowered() ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }

    protected float getWhiteOverlayProgress(CyberWitherBoss pEntity, float pPartialTicks) {
        int invulTicks = pEntity.getInvulnerableTicks();
        if (invulTicks > 0) {
            float progress = 1.0f - ((float)invulTicks - pPartialTicks) / 220.0f;
            if (invulTicks < 10) {
                return 1.0f;
            }
            float flash = Mth.sin((float)(progress * progress * 20.0f * (float)Math.PI));
            return Mth.clamp((float)flash, (float)0.0f, (float)0.6f);
        }
        return super.getWhiteOverlayProgress((LivingEntity)pEntity, pPartialTicks);
    }

    protected void scale(CyberWitherBoss pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float f = 2.0f;
        int i = pLivingEntity.getInvulnerableTicks();
        if (i > 0) {
            f -= ((float)i - pPartialTickTime) / 220.0f * 0.5f;
        }
        pPoseStack.scale(f, f, f);
    }
}

