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
package com.perigrine3.createcybernetics.client.model;

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

public final class ClawAttachmentModel
extends Model {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"claws"), "main");
    private static final float THICK = 0.5f;
    private static final float HALF = 0.25f;
    private final ModelPart bone;

    public ClawAttachmentModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(-2, -2).addBox(-0.25f, 0.0f, -4.3422f, 0.0f, 8.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.175f));
        bone.addOrReplaceChild("right_claw", CubeListBuilder.create().texOffs(-2, -1).addBox(-0.25f, 0.0f, -4.3422f, 0.0f, 7.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-1.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.1f));
        bone.addOrReplaceChild("left_claw", CubeListBuilder.create().texOffs(-2, -1).addBox(-0.25f, 0.0f, -4.3422f, 0.0f, 7.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)1.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-0.1f));
        return LayerDefinition.create((MeshDefinition)mesh, (int)16, (int)16);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

