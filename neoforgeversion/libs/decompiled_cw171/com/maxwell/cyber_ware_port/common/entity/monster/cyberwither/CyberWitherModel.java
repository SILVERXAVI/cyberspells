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
package com.maxwell.cyber_ware_port.common.entity.monster.cyberwither;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
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

public class CyberWitherModel
extends HierarchicalModel<CyberWitherBoss> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"cyber_wither"), "main");
    private final ModelPart root;
    private final ModelPart centerHead;
    private final ModelPart rightHead;
    private final ModelPart leftHead;
    private final ModelPart ribcage;
    private final ModelPart tail;

    public CyberWitherModel(ModelPart pRoot) {
        this.root = pRoot;
        this.ribcage = pRoot.getChild("ribcage");
        this.tail = pRoot.getChild("tail");
        this.centerHead = pRoot.getChild("center_head");
        this.rightHead = pRoot.getChild("right_head");
        this.leftHead = pRoot.getChild("left_head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeDeformation deformation = CubeDeformation.NONE;
        partdefinition.addOrReplaceChild("shoulders", CubeListBuilder.create().texOffs(0, 16).addBox(-10.0f, 3.9f, -0.5f, 20.0f, 3.0f, 3.0f, deformation), PartPose.ZERO);
        partdefinition.addOrReplaceChild("ribcage", CubeListBuilder.create().texOffs(0, 22).addBox(0.0f, 0.0f, 0.0f, 3.0f, 10.0f, 3.0f, deformation).texOffs(24, 22).addBox(-4.0f, 1.5f, 0.5f, 11.0f, 2.0f, 2.0f, deformation).texOffs(24, 22).addBox(-4.0f, 4.0f, 0.5f, 11.0f, 2.0f, 2.0f, deformation).texOffs(24, 22).addBox(-4.0f, 6.5f, 0.5f, 11.0f, 2.0f, 2.0f, deformation), PartPose.offsetAndRotation((float)-2.0f, (float)6.9f, (float)-0.5f, (float)0.20420352f, (float)0.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 22).addBox(0.0f, 0.0f, 0.0f, 3.0f, 6.0f, 3.0f, deformation), PartPose.offsetAndRotation((float)-2.0f, (float)(6.9f + Mth.cos((float)0.20420352f) * 10.0f), (float)(-0.5f + Mth.sin((float)0.20420352f) * 10.0f), (float)0.83252203f, (float)0.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("center_head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f, deformation), PartPose.ZERO);
        CubeListBuilder headBuilder = CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -4.0f, -4.0f, 6.0f, 6.0f, 6.0f, deformation);
        partdefinition.addOrReplaceChild("right_head", headBuilder, PartPose.offset((float)-8.0f, (float)4.0f, (float)0.0f));
        partdefinition.addOrReplaceChild("left_head", headBuilder, PartPose.offset((float)10.0f, (float)4.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    private void setupHeadRotation(CyberWitherBoss pWither, ModelPart pPart, int pHead) {
        pPart.yRot = (pWither.getHeadYRot(pHead) - pWither.yBodyRot) * ((float)Math.PI / 180);
        pPart.xRot = pWither.getHeadXRot(pHead) * ((float)Math.PI / 180);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(CyberWitherBoss pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f = Mth.cos((float)(pAgeInTicks * 0.1f));
        this.ribcage.xRot = (0.065f + 0.05f * f) * (float)Math.PI;
        this.tail.setPos(-2.0f, 6.9f + Mth.cos((float)this.ribcage.xRot) * 10.0f, -0.5f + Mth.sin((float)this.ribcage.xRot) * 10.0f);
        this.tail.xRot = (0.265f + 0.1f * f) * (float)Math.PI;
        this.centerHead.yRot = pNetHeadYaw * ((float)Math.PI / 180);
        this.centerHead.xRot = pHeadPitch * ((float)Math.PI / 180);
    }

    public void prepareMobModel(CyberWitherBoss pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.setupHeadRotation(pEntity, this.rightHead, 1);
        this.setupHeadRotation(pEntity, this.leftHead, 2);
    }
}

