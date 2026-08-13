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
package io.redspace.ironsspellbooks.entity.spells.ice_spike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
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

public class IceSpikeRenderer
extends EntityRenderer<IceSpikeEntity> {
    private final IceSpikeModel model;

    public IceSpikeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new IceSpikeModel(pContext.bakeLayer(IceSpikeModel.LAYER_LOCATION));
    }

    public void render(IceSpikeEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (entity.tickCount < entity.getWaitTime()) {
            return;
        }
        float f = (float)entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        float anim = entity.getPositionOffset(partialTicks);
        poseStack.scale(1.0f, -1.0f, 1.0f);
        float scale = entity.getSpikeSize();
        scale = (scale - 1.0f) * 0.25f + 1.0f;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0f, -anim * 68.0f / 16.0f, 0.0f);
        this.model.setupAnim(entity, partialTicks, 0.0f, 0.0f, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

    public ResourceLocation getTextureLocation(IceSpikeEntity pEntity) {
        return IronsSpellbooks.id("textures/entity/ice_spike.png");
    }

    public static class IceSpikeModel
    extends EntityModel<IceSpikeEntity> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_spike"), "main");
        private final ModelPart bottom;
        private final ModelPart middle;
        private final ModelPart top;

        public IceSpikeModel(ModelPart root) {
            this.bottom = root.getChild("bottom");
            this.middle = root.getChild("middle");
            this.top = root.getChild("top");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -25.0f, -9.0f, 10.0f, 24.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-4.0f, (float)25.0f, (float)4.0f));
            PartDefinition cube_r1 = bottom.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 10.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)8.0f, (float)0.0f, (float)-8.0f, (float)0.3295f, (float)-0.1172f, (float)0.3295f));
            PartDefinition cube_r2 = bottom.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 10.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)-2.812f, (float)0.1172f, (float)2.812f));
            PartDefinition middle = partdefinition.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0f, -25.0f, -1.0f, 8.0f, 22.0f, 8.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-3.0f, (float)3.0f, (float)-3.0f));
            PartDefinition cube_r3 = middle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 8.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)6.0f, (float)0.0f, (float)6.0f, (float)-1.3526f, (float)-1.3526f, (float)1.5708f));
            PartDefinition cube_r4 = middle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 8.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)1.789f, (float)1.3526f, (float)1.5708f));
            PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(39, 38).addBox(-1.0f, -25.0f, -3.0f, 4.0f, 22.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-1.0f, (float)-19.0f, (float)1.0f));
            PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 8.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)2.0f, (float)0.0f, (float)-2.0f, (float)0.1719f, (float)-0.0302f, (float)0.1719f));
            PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 3).addBox(-5.0f, -10.0f, -1.0f, 6.0f, 8.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)-2.9697f, (float)0.0302f, (float)2.9697f));
            return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
        }

        public void setupAnim(IceSpikeEntity entity, float partialTicks, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            float scale = entity.getSpikeSize();
            this.top.visible = false;
            this.bottom.visible = false;
            int ypos = 26;
            if (scale >= 3.0f) {
                this.bottom.visible = true;
                this.bottom.y = ypos -= 26;
            }
            this.middle.y = ypos -= 22;
            if (scale >= 2.0f) {
                this.top.visible = true;
                this.top.y = ypos -= 22;
            }
        }

        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int pColor) {
            this.bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, pColor);
            this.middle.render(poseStack, vertexConsumer, packedLight, packedOverlay, pColor);
            this.top.render(poseStack, vertexConsumer, packedLight, packedOverlay, pColor);
        }
    }
}

