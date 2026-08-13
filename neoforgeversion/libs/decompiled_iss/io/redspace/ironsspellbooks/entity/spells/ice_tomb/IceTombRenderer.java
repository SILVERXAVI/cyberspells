/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.entity.spells.ice_tomb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class IceTombRenderer
extends EntityRenderer<IceTombEntity> {
    public static final ResourceLocation NOCULL = IronsSpellbooks.id("textures/entity/ice_tomb/ice_tomb.png");
    public static final ResourceLocation CULL = IronsSpellbooks.id("textures/entity/ice_tomb/ice_tomb_cull.png");
    private final IceTombModel model;

    public IceTombRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new IceTombModel(pContext.bakeLayer(IceTombModel.LAYER_LOCATION));
    }

    public void render(IceTombEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        float xScaleFactor = entity.getBbWidth() / entity.getType().getDimensions().width();
        float yScaleFactor = entity.getBbHeight() / entity.getType().getDimensions().height();
        poseStack.scale(xScaleFactor, -yScaleFactor, -xScaleFactor);
        poseStack.translate(0.0, -1.501, 0.0);
        this.model.setupAnim(entity, partialTicks, 0.0f, 0.0f, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)NOCULL));
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
        vertexconsumer = multiBufferSource.getBuffer(RenderType.entityTranslucentCull((ResourceLocation)CULL));
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    public ResourceLocation getTextureLocation(IceTombEntity pEntity) {
        return NOCULL;
    }

    public static class IceTombModel
    extends EntityModel<IceTombEntity> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_tomb"), "main");
        private final ModelPart model;

        public IceTombModel(ModelPart root) {
            this.model = root.getChild("model");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            PartDefinition bb_main = partdefinition.addOrReplaceChild("model", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -36.0f, -8.0f, 16.0f, 36.0f, 16.0f, new CubeDeformation(0.0f)).texOffs(40, 67).addBox(4.0f, -9.0f, 4.0f, 6.0f, 9.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(0, 52).addBox(1.0f, -24.0f, -11.0f, 10.0f, 24.0f, 10.0f, new CubeDeformation(0.0f)).texOffs(64, 0).addBox(-8.0f, -36.0f, -8.0f, 16.0f, 36.0f, 16.0f, new CubeDeformation(-0.01f)).texOffs(40, 52).addBox(-10.0f, -9.0f, -10.0f, 6.0f, 9.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(0, 86).addBox(-11.0f, -24.0f, 1.0f, 10.0f, 24.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
            return LayerDefinition.create((MeshDefinition)meshdefinition, (int)128, (int)128);
        }

        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int pColor) {
            this.model.render(poseStack, vertexConsumer, packedLight, packedOverlay, pColor);
        }

        public void setupAnim(IceTombEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        }
    }
}

