/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.ArmedModel
 *  net.minecraft.client.model.HierarchicalModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.item.Items
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.entity.custom.CyberskeletonEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;

public class CyberskeletonModel<T extends CyberskeletonEntity>
extends HierarchicalModel<T>
implements ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberskeleton"), "main");
    private final ModelPart waist;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CyberskeletonModel(ModelPart root) {
        this.waist = root.getChild("waist");
        this.body = this.waist.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("rightArm");
        this.leftArm = this.body.getChild("leftArm");
        this.rightLeg = this.body.getChild("rightLeg");
        this.leftLeg = this.body.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset((float)0.0f, (float)12.0f, (float)0.0f));
        PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-12.0f, (float)0.0f));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-5.0f, (float)2.0f, (float)0.0f));
        body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offset((float)5.0f, (float)2.0f, (float)0.0f));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-2.0f, (float)12.0f, (float)0.0f));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offset((float)2.0f, (float)12.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
        this.head.xRot = headPitch * ((float)Math.PI / 180);
        this.rightArm.xRot = 0.0f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.xRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightArm.xRot += Mth.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 2.0f * limbSwingAmount * 0.5f;
        this.leftArm.xRot += Mth.cos((float)(limbSwing * 0.6662f)) * 2.0f * limbSwingAmount * 0.5f;
        this.rightLeg.xRot = Mth.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        boolean isAggressive = entity.isAggressive();
        boolean holdingBow = entity.getMainHandItem().is(Items.BOW);
        if (isAggressive) {
            if (holdingBow) {
                this.rightArm.yRot = this.head.yRot - 0.1f;
                this.leftArm.yRot = this.head.yRot + 0.1f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot -= 0.05f;
                this.leftArm.yRot += 0.3f;
            } else {
                float attackTime = entity.getAttackAnim(ageInTicks);
                float sin1 = Mth.sin((float)(attackTime * (float)Math.PI));
                float sin2 = Mth.sin((float)((1.0f - (1.0f - attackTime) * (1.0f - attackTime)) * (float)Math.PI));
                this.rightArm.zRot = 0.0f;
                this.leftArm.zRot = 0.0f;
                this.rightArm.yRot = -(0.1f - sin1 * 0.6f);
                this.leftArm.yRot = 0.1f - sin1 * 0.6f;
                this.rightArm.xRot = -1.5707964f;
                this.leftArm.xRot = -1.5707964f;
                this.rightArm.xRot -= sin1 * 1.2f - sin2 * 0.4f;
                this.leftArm.xRot -= sin1 * 1.2f - sin2 * 0.4f;
            }
        } else {
            this.rightArm.zRot += Mth.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
            this.leftArm.zRot -= Mth.cos((float)(ageInTicks * 0.09f)) * 0.05f + 0.05f;
            this.rightArm.xRot += Mth.sin((float)(ageInTicks * 0.067f)) * 0.05f;
            this.leftArm.xRot -= Mth.sin((float)(ageInTicks * 0.067f)) * 0.05f;
        }
    }

    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        this.waist.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        if (arm == HumanoidArm.RIGHT) {
            this.rightArm.translateAndRotate(poseStack);
        } else {
            this.leftArm.translateAndRotate(poseStack);
        }
    }

    public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int light, int overlay, int color) {
        this.waist.render(stack, buffer, light, overlay, color);
    }

    public ModelPart root() {
        return this.waist;
    }
}

