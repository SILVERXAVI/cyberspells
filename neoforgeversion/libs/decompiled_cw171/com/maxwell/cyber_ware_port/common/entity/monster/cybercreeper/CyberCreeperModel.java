/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
package com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper;

import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperEntity;
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

public class CyberCreeperModel
extends HierarchicalModel<CyberCreeperEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"cyber_creeper"), "main");
    public static final ModelLayerLocation ARMOR_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"cyber_creeper"), "armor");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;

    public CyberCreeperModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.leg0 = this.body.getChild("leg0");
        this.leg1 = this.body.getChild("leg1");
        this.leg2 = this.body.getChild("leg2");
        this.leg3 = this.body.getChild("leg3");
    }

    public static LayerDefinition createBodyLayer() {
        return CyberCreeperModel.createBodyLayer(CubeDeformation.NONE);
    }

    public static LayerDefinition createArmorLayer() {
        return CyberCreeperModel.createBodyLayer(new CubeDeformation(2.0f));
    }

    private static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, -18.0f, -2.0f, 8.0f, 12.0f, 4.0f, deformation), PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f));
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, deformation), PartPose.offset((float)0.0f, (float)-18.0f, (float)0.0f));
        body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, deformation), PartPose.offset((float)-2.0f, (float)-6.0f, (float)4.0f));
        body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, deformation), PartPose.offset((float)2.0f, (float)-6.0f, (float)4.0f));
        body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, deformation), PartPose.offset((float)-2.0f, (float)-6.0f, (float)-4.0f));
        body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 6.0f, 4.0f, deformation), PartPose.offset((float)2.0f, (float)-6.0f, (float)-4.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)32);
    }

    public ModelPart root() {
        return this.body;
    }

    public void setupAnim(CyberCreeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
        this.head.xRot = headPitch * ((float)Math.PI / 180);
        this.leg0.xRot = Mth.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
        this.leg1.xRot = Mth.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        this.leg2.xRot = Mth.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        this.leg3.xRot = Mth.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
    }
}

