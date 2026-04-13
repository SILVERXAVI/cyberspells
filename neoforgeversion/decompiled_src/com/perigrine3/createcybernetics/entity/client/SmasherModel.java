/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
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
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.entity.client.SmasherAnimations;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
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

public class SmasherModel<T extends SmasherEntity>
extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"smasher"), "main");
    private final ModelPart body;
    private final ModelPart Torso;
    private final ModelPart body2;
    private final ModelPart Wires;
    private final ModelPart head2;
    private final ModelPart eye;
    private final ModelPart arm0;
    private final ModelPart arm1;
    private final ModelPart leg0;
    private final ModelPart leg1;

    public SmasherModel(ModelPart root) {
        this.body = root.getChild("body");
        this.Torso = this.body.getChild("Torso");
        this.body2 = this.Torso.getChild("body2");
        this.Wires = this.Torso.getChild("Wires");
        this.head2 = this.body.getChild("head2");
        this.eye = this.head2.getChild("eye");
        this.arm0 = this.body.getChild("arm0");
        this.arm1 = this.body.getChild("arm1");
        this.leg0 = this.body.getChild("leg0");
        this.leg1 = this.body.getChild("leg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset((float)0.0f, (float)-7.0f, (float)0.0f));
        PartDefinition Torso = body.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(58, 0).addBox(-4.5f, 4.5f, -1.5f, 9.0f, 7.0f, 6.0f, new CubeDeformation(0.5f)), PartPose.offset((float)0.0f, (float)-1.5f, (float)-1.5f));
        PartDefinition body2 = Torso.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset((float)0.0f, (float)0.1198f, (float)-1.3043f));
        PartDefinition body_r1 = body2.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(82, 5).addBox(-1.0f, -6.5f, -1.5f, 2.0f, 13.0f, 9.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)-0.2304f, (float)0.1827f, (float)0.2182f, (float)0.0f, (float)0.0f));
        PartDefinition body_r2 = body2.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(48, 5).addBox(5.0f, 0.5f, -6.0f, 4.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-7.0f, (float)-3.1198f, (float)0.3043f, (float)0.2182f, (float)0.0f, (float)0.0f));
        PartDefinition body_r3 = body2.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0f, -5.5f, -5.5f, 18.0f, 11.0f, 11.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)-0.1198f, (float)1.3043f, (float)0.2182f, (float)0.0f, (float)0.0f));
        PartDefinition Wires = Torso.addOrReplaceChild("Wires", CubeListBuilder.create(), PartPose.offset((float)-0.0687f, (float)-0.1132f, (float)-0.403f));
        PartDefinition body_r4 = Wires.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(73, 29).addBox(0.0f, -7.0f, -6.5f, 0.0f, 14.0f, 13.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)4.1006f, (float)5.2044f, (float)0.2814f, (float)-0.7418f, (float)-3.0543f, (float)-0.6109f));
        PartDefinition body_r5 = Wires.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(73, 29).addBox(0.0f, -7.0f, -6.5f, 0.0f, 14.0f, 13.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-3.8994f, (float)5.2044f, (float)0.2814f, (float)-0.9163f, (float)3.0543f, (float)0.6109f));
        PartDefinition body_r6 = Wires.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(73, 29).addBox(6.0f, -7.5f, -1.5f, 0.0f, 14.0f, 13.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-6.9313f, (float)-3.9973f, (float)-2.7186f, (float)0.2182f, (float)0.0f, (float)0.3054f));
        PartDefinition body_r7 = Wires.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(73, 18).addBox(6.0f, -7.5f, -1.5f, 0.0f, 14.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-0.9313f, (float)-6.9973f, (float)0.2814f, (float)0.0f, (float)3.1416f, (float)-1.0472f));
        PartDefinition body_r8 = Wires.addOrReplaceChild("body_r8", CubeListBuilder.create().texOffs(73, 18).addBox(6.0f, -7.5f, -1.5f, 0.0f, 14.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)7.0687f, (float)3.0027f, (float)0.2814f, (float)0.0f, (float)3.1416f, (float)1.0908f));
        PartDefinition body_r9 = Wires.addOrReplaceChild("body_r9", CubeListBuilder.create().texOffs(73, 16).addBox(6.0f, -7.5f, -1.5f, 0.0f, 14.0f, 12.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-3.9313f, (float)0.0027f, (float)-1.7186f, (float)0.2182f, (float)0.1309f, (float)0.0436f));
        PartDefinition body_r10 = Wires.addOrReplaceChild("body_r10", CubeListBuilder.create().texOffs(73, 16).addBox(6.0f, -7.5f, -1.5f, 0.0f, 14.0f, 12.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-7.9313f, (float)1.0027f, (float)-2.7186f, (float)0.2182f, (float)-0.0873f, (float)-0.0873f));
        PartDefinition head2 = body.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 58).addBox(-4.0f, -9.0f, -5.0f, 8.0f, 10.0f, 7.0f, new CubeDeformation(0.0f)).texOffs(58, 13).addBox(-4.0f, -9.0f, -6.0f, 8.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(58, 19).addBox(-4.0f, -4.0f, -6.0f, 8.0f, 2.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(30, 58).addBox(-4.0f, -2.0f, -6.0f, 8.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(30, 62).addBox(-1.0f, -2.0f, -8.0f, 2.0f, 4.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-5.0f, (float)-6.5f));
        PartDefinition head_r1 = head2.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(58, 16).addBox(-4.0f, -2.0f, -1.0f, 8.0f, 2.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)-4.0f, (float)-6.0f, (float)-0.48f, (float)0.0f, (float)0.0f));
        PartDefinition eye = head2.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(39, 63).addBox(-0.5f, -7.0f, -5.51f, 1.0f, 2.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)3.0f, (float)-0.5f));
        PartDefinition arm0 = body.addOrReplaceChild("arm0", CubeListBuilder.create().texOffs(0, 22).addBox(-15.0f, -2.5f, -5.0f, 6.0f, 30.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(110, 18).addBox(-14.0f, 9.5f, 0.0f, 4.0f, 6.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-5.0f, (float)0.0f));
        PartDefinition cube_r1 = arm0.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(116, 33).addBox(-3.0f, -2.0f, 0.0f, 6.0f, 4.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-10.0f, (float)-3.0f, (float)-2.0f, (float)0.0f, (float)0.3054f, (float)0.0f));
        PartDefinition arm3_r1 = arm0.addOrReplaceChild("arm3_r1", CubeListBuilder.create().texOffs(105, 0).addBox(-7.0f, -2.0f, -2.0f, 7.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-8.5f, (float)12.5f, (float)-2.0f, (float)-0.7854f, (float)0.0f, (float)0.0f));
        PartDefinition arm1 = body.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(24, 22).addBox(9.0f, -2.5f, -5.0f, 6.0f, 30.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(110, 18).addBox(10.0f, 9.5f, 0.0f, 4.0f, 6.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-5.0f, (float)0.0f));
        PartDefinition cube_r2 = arm1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(116, 41).addBox(-3.0f, -2.0f, 0.0f, 6.0f, 4.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)10.0f, (float)-3.0f, (float)-3.0f, (float)0.0f, (float)0.2182f, (float)0.0f));
        PartDefinition arm2_r1 = arm1.addOrReplaceChild("arm2_r1", CubeListBuilder.create().texOffs(105, 0).addBox(-7.0f, -2.0f, -2.0f, 7.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)15.5f, (float)12.5f, (float)-2.0f, (float)-0.7854f, (float)0.0f, (float)0.0f));
        PartDefinition leg0 = body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(48, 22).addBox(-3.5f, 0.0f, -3.0f, 6.0f, 21.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(110, 18).addBox(-2.5f, 7.0f, -4.0f, 4.0f, 6.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-4.0f, (float)10.0f, (float)0.0f));
        PartDefinition leg2_r1 = leg0.addOrReplaceChild("leg2_r1", CubeListBuilder.create().texOffs(105, 0).addBox(-7.5f, -2.0f, -2.0f, 7.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)3.5f, (float)10.5f, (float)0.0f, (float)-0.7854f, (float)0.0f, (float)0.0f));
        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(48, 49).addBox(-3.5f, 0.0f, -3.0f, 6.0f, 21.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(110, 18).addBox(-2.5f, 7.0f, -4.0f, 4.0f, 6.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offset((float)5.0f, (float)10.0f, (float)0.0f));
        PartDefinition leg3_r1 = leg1.addOrReplaceChild("leg3_r1", CubeListBuilder.create().texOffs(105, 0).addBox(-7.5f, -2.0f, -2.0f, 7.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)3.5f, (float)10.5f, (float)0.0f, (float)-0.7854f, (float)0.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)128, (int)128);
    }

    public void setupAnim(SmasherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animateWalk(SmasherAnimations.WALK_ANIM, limbSwing, limbSwingAmount, 2.0f, 2.5f);
        this.animate(SmasherEntity.idleAnimationState, SmasherAnimations.IDLE_ANIM, ageInTicks, 1.0f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp((float)headYaw, (float)-30.0f, (float)30.0f);
        headPitch = Mth.clamp((float)headPitch, (float)-25.0f, (float)45.0f);
        this.head2.yRot = headYaw * ((float)Math.PI / 180);
        this.head2.xRot = headPitch * ((float)Math.PI / 180);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public ModelPart root() {
        return this.body;
    }
}

