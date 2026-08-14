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
package com.maxwell.cyber_ware_port.client.model;

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

public class PlayerInternalPartsModel
extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"modid", (String)"playerinternalpartsmodel"), "main");
    private final ModelPart root;
    private final ModelPart bone;
    private final ModelPart muscal;
    private final ModelPart skin;

    public PlayerInternalPartsModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root.getChild("root");
        this.bone = this.root.getChild("bone");
        this.muscal = this.root.getChild("muscal");
        this.skin = this.root.getChild("skin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, -2.0f, -5.0f, 10.0f, 2.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-4.0f, (float)0.0f));
        PartDefinition muscal = root.addOrReplaceChild("muscal", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0f, -4.0f, -5.0f, 10.0f, 2.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        PartDefinition skin = root.addOrReplaceChild("skin", CubeListBuilder.create().texOffs(0, 24).addBox(-5.0f, -2.0f, -5.0f, 10.0f, 2.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void setVisibleLayer(int type) {
        this.skin.visible = false;
        this.muscal.visible = false;
        this.bone.visible = false;
        switch (type) {
            case 0: {
                this.skin.visible = true;
                break;
            }
            case 1: {
                this.muscal.visible = true;
                break;
            }
            case 2: {
                this.bone.visible = true;
            }
        }
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        this.root.render(poseStack, vertexConsumer, i, i1, i2);
    }
}

