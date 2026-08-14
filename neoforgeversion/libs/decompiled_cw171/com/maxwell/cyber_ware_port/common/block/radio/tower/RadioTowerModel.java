/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
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
package com.maxwell.cyber_ware_port.common.block.radio.tower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class RadioTowerModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"radio_tower"), "main");
    private final ModelPart root;
    private final ModelPart bone3;
    private final ModelPart bone2;
    private final ModelPart bone;

    public RadioTowerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.bone3 = this.root.getChild("bone3");
        this.bone2 = this.root.getChild("bone2");
        this.bone = this.root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0f, -4.0f, -23.0f, 48.0f, 4.0f, 48.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 52).addBox(-2.0f, -80.0f, -2.0f, 4.0f, 160.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-5.0f, (float)-80.0f, (float)6.0f, (float)0.0869f, (float)0.7816f, (float)0.1231f));
        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 52).addBox(-2.0f, -80.0f, -2.0f, 4.0f, 160.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-5.0f, (float)-80.0f, (float)-4.0f, (float)-0.0873f, (float)0.7854f, (float)0.0f));
        PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0f, -80.0f, -2.0f, 4.0f, 160.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)5.0f, (float)-80.0f, (float)-4.0f, (float)-0.0869f, (float)0.7816f, (float)-0.1231f));
        PartDefinition cube_r4 = root.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0f, -80.0f, -2.0f, 4.0f, 160.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)5.0f, (float)-80.0f, (float)6.0f, (float)0.0873f, (float)0.7854f, (float)0.0f));
        PartDefinition bone3 = root.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(98, 87).addBox(3.0f, -2.0f, -7.5f, 11.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(64, 101).addBox(3.0f, -2.0f, 3.5f, 10.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-8.0f, (float)-68.0f, (float)1.5f));
        PartDefinition cube_r5 = bone3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(100, 80).addBox(-6.0f, -2.0f, -1.5f, 11.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)2.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition cube_r6 = bone3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 101).addBox(-5.0f, -2.0f, -1.5f, 10.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)14.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition bone2 = root.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(64, 87).addBox(1.0f, -2.0f, -9.5f, 14.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(96, 94).addBox(2.0f, -2.0f, 5.5f, 12.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-8.0f, (float)-45.0f, (float)1.5f));
        PartDefinition cube_r7 = bone2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 80).addBox(-7.0f, -2.0f, -1.5f, 15.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition cube_r8 = bone2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(64, 94).addBox(-6.0f, -2.0f, -1.5f, 13.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)16.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64, 52).addBox(-1.0f, -2.0f, -10.5f, 18.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(64, 59).addBox(-1.0f, -2.0f, 6.5f, 18.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-8.0f, (float)-26.0f, (float)1.5f));
        PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(64, 73).addBox(-9.0f, -2.0f, -1.5f, 18.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(64, 66).addBox(-9.0f, -2.0f, -1.5f, 18.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)16.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)256, (int)256);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        this.root.render(poseStack, vertexConsumer, i, i1, i2);
    }
}

