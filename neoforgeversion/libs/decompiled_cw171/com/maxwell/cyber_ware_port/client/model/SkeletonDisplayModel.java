/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.resources.ResourceLocation
 */
package com.maxwell.cyber_ware_port.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SkeletonDisplayModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"skeleton_display"), "main");
    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart hat;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;

    public SkeletonDisplayModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.head = root.getChild("head");
        this.hat = root.getChild("hat");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.5f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset((float)-5.0f, (float)2.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset((float)5.0f, (float)2.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset((float)-2.0f, (float)12.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), PartPose.offset((float)2.0f, (float)12.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)32);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0, 1.5, 0.0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, alpha);
        poseStack.popPose();
    }
}

