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
package com.maxwell.cyber_ware_port.common.block.surgerychamber;

import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlockEntity;
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

public class SurgeryChamberModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"surgery_chamber"), "main");
    private final ModelPart root;
    private final ModelPart door_left;
    private final ModelPart door_right;

    public SurgeryChamberModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.door_left = this.root.getChild("door_left");
        this.door_right = this.root.getChild("door_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(6.0f, -30.0f, -8.0f, 2.0f, 30.0f, 16.0f, new CubeDeformation(0.0f)).texOffs(52, 64).addBox(-6.0f, -30.0f, 6.0f, 12.0f, 30.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(0, 64).addBox(-6.0f, -1.0f, -8.0f, 12.0f, 1.0f, 14.0f, new CubeDeformation(0.0f)).texOffs(36, 18).addBox(-8.0f, -30.0f, -8.0f, 2.0f, 30.0f, 16.0f, new CubeDeformation(0.0f)).texOffs(36, 0).addBox(-8.0f, -32.0f, -8.0f, 16.0f, 2.0f, 16.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
        PartDefinition door_left = root.addOrReplaceChild("door_left", CubeListBuilder.create().texOffs(0, 79).addBox(-6.0f, -13.0f, 0.5f, 6.0f, 29.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offset((float)6.0f, (float)-17.0f, (float)-7.5f));
        PartDefinition door_right = root.addOrReplaceChild("door_right", CubeListBuilder.create().texOffs(0, 79).addBox(0.0f, -13.0f, 0.5f, 6.0f, 29.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-6.0f, (float)-17.0f, (float)-7.5f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)128, (int)128);
    }

    public void setupAnim(SurgeryChamberBlockEntity entity, float partialTick) {
        float scale;
        if (entity == null) {
            return;
        }
        float progress = entity.prevAnimationProgress + (entity.animationProgress - entity.prevAnimationProgress) * partialTick;
        this.door_left.xScale = scale = 1.0f - progress;
        this.door_right.xScale = scale;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        this.root.render(poseStack, vertexConsumer, i, i1, i2);
    }
}

