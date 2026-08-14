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
package com.maxwell.cyber_ware_port.common.block.scanner;

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

public class ScannerBlockModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"scanner_block"), "main");
    private final ModelPart root;
    private final ModelPart scanner;
    private final ModelPart scanner_part;

    public ScannerBlockModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.scanner = root.getChild("scanner");
        this.scanner_part = this.scanner.getChild("scanner_part");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-21.0f, -5.0f, -8.0f, 16.0f, 6.0f, 16.0f, new CubeDeformation(0.0f)).texOffs(64, 11).addBox(-21.0f, -14.0f, -8.0f, 1.0f, 9.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(32, 22).addBox(-21.0f, -15.0f, -8.0f, 1.0f, 1.0f, 15.0f, new CubeDeformation(0.0f)).texOffs(64, 0).addBox(-21.0f, -15.0f, 7.0f, 1.0f, 10.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(60, 62).addBox(-6.0f, -15.0f, -8.0f, 1.0f, 10.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(64, 21).addBox(-6.0f, -14.0f, 7.0f, 1.0f, 9.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 22).addBox(-6.0f, -15.0f, -7.0f, 1.0f, 1.0f, 15.0f, new CubeDeformation(0.0f)), PartPose.offset((float)13.0f, (float)23.0f, (float)0.0f));
        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 54).addBox(-0.5f, -0.5f, -7.5f, 1.0f, 1.0f, 14.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-12.5f, (float)-14.5f, (float)7.5f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 54).addBox(-0.5f, -0.5f, -7.5f, 1.0f, 1.0f, 14.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)-12.5f, (float)-14.5f, (float)-7.5f, (float)0.0f, (float)1.5708f, (float)0.0f));
        PartDefinition scanner = partdefinition.addOrReplaceChild("scanner", CubeListBuilder.create().texOffs(32, 38).addBox(-0.5f, -1.5f, -7.5f, 1.0f, 1.0f, 15.0f, new CubeDeformation(0.0f)).texOffs(0, 38).addBox(-0.5f, 0.5f, -7.5f, 1.0f, 1.0f, 15.0f, new CubeDeformation(0.0f)), PartPose.offset((float)5.5f, (float)8.5f, (float)0.5f));
        PartDefinition scanner_part = scanner.addOrReplaceChild("scanner_part", CubeListBuilder.create().texOffs(60, 54).addBox(-1.0f, -3.0f, -2.0f, 3.0f, 5.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-0.5f, (float)2.5f, (float)-5.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)128, (int)128);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.scanner.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public void setupMovingParts(boolean isWorking, float time) {
        float scannerAmp = 5.5f;
        float scannerOff = 0.0f;
        float partAmp = 3.0f;
        float partOff = 0.0f;
        float homeScannerX = 5.5f;
        float homePartZ = -5.0f;
        if (isWorking) {
            this.scanner.x = scannerOff + (float)Math.sin(time * 0.15f) * scannerAmp;
            this.scanner_part.z = partOff + (float)Math.cos(time * 0.3f) * partAmp;
        } else {
            this.scanner.x = homeScannerX;
            this.scanner_part.z = homePartZ;
        }
    }
}

