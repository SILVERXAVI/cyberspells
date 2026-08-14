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
package com.maxwell.cyber_ware_port.common.block.cwb;

import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlockEntity;
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

public class CyberWareWorkBenchModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"cyberwareworkbenchmodel"), "main");
    private final ModelPart root;
    private final ModelPart hammer;

    public CyberWareWorkBenchModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.hammer = this.root.getChild("hammer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(0.0f)).texOffs(34, 32).addBox(-4.0f, -32.0f, 4.0f, 8.0f, 16.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 32).addBox(-4.0f, -32.0f, -5.0f, 8.0f, 8.0f, 9.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
        PartDefinition hammer = root.addOrReplaceChild("hammer", CubeListBuilder.create().texOffs(0, 49).addBox(-3.0f, -0.5f, -3.5f, 6.0f, 2.0f, 7.0f, new CubeDeformation(0.0f)).texOffs(26, 52).addBox(-1.0f, -7.5f, -1.5f, 2.0f, 7.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-23.5f, (float)-0.5f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void setupAnim(CyberwareWorkbenchBlockEntity pBlockEntity, float pPartialTick) {
        float currentOffset;
        if (pBlockEntity == null) {
            return;
        }
        float progress = pBlockEntity.getRenderProgress(pPartialTick);
        float initialY = -23.5f;
        float t1 = 0.1668f;
        float t2 = 0.6668f;
        float targetOffset = 6.0f;
        if (progress < t1) {
            float t = progress / t1;
            currentOffset = 0.0f + (targetOffset - 0.0f) * t;
        } else if (progress < t2) {
            currentOffset = targetOffset;
        } else {
            float t = (progress - t2) / (1.0f - t2);
            currentOffset = targetOffset + (0.0f - targetOffset) * t;
        }
        this.hammer.y = initialY + currentOffset;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        this.root.render(poseStack, vertexConsumer, i, i1, i2);
    }
}

