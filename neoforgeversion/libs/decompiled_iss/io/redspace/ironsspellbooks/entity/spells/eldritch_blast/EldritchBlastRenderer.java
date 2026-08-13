/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.spells.eldritch_blast;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class EldritchBlastRenderer
extends EntityRenderer<EldritchBlastVisualEntity> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"eldritch_blast_model"), "main");
    private static final ResourceLocation TEXTURE_CORE = IronsSpellbooks.id("textures/entity/eldritch_blast/core.png");
    private static final ResourceLocation TEXTURE_OVERLAY = IronsSpellbooks.id("textures/entity/eldritch_blast/overlay.png");
    private final ModelPart body;

    public EldritchBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 32.0f, 16.0f), PartPose.ZERO);
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public boolean shouldRender(EldritchBlastVisualEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    public void render(EldritchBlastVisualEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        float lifetime = 8.0f;
        float scalar = 0.25f;
        float length = 32.0f * scalar * scalar;
        float f = (float)entity.tickCount + partialTicks;
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot() - 90.0f));
        poseStack.scale(scalar, scalar, scalar);
        float alpha = Mth.clamp((float)(1.0f - f / lifetime), (float)0.0f, (float)1.0f);
        for (float i = 0.0f; i < entity.distance * 4.0f; i += length) {
            poseStack.translate(0.0f, length, 0.0f);
            VertexConsumer consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magicNoCull(TEXTURE_OVERLAY));
            poseStack.pushPose();
            float expansion = Mth.clampedLerp((float)1.2f, (float)0.0f, (float)(f / lifetime));
            poseStack.mulPose(Axis.YP.rotationDegrees(f * 5.0f));
            poseStack.scale(expansion, 1.0f, expansion);
            poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
            this.body.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int)((int)(alpha * 255.0f)), (int)255, (int)255, (int)255));
            poseStack.popPose();
            consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.darkGlow(TEXTURE_CORE));
            poseStack.pushPose();
            expansion = Mth.clampedLerp((float)1.0f, (float)0.0f, (float)(f / (lifetime - 5.0f)));
            poseStack.scale(expansion, 1.0f, expansion);
            poseStack.mulPose(Axis.YP.rotationDegrees(f * -10.0f));
            this.body.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, -1);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(EldritchBlastVisualEntity entity) {
        return TEXTURE_CORE;
    }
}

