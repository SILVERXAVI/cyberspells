/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.AnimationUtils
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
package com.maxwell.cyber_ware_port.common.entity.monster.cyberzombie;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberzombie.CyberZombieEntity;
import net.minecraft.client.model.AnimationUtils;
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

public class CyberZombieModel
extends HierarchicalModel<CyberZombieEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"cyber_zombie"), "main");
    private final ModelPart waist;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CyberZombieModel(ModelPart root) {
        this.waist = root.getChild("waist");
        this.body = this.waist.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("rightArm");
        this.leftArm = this.body.getChild("leftArm");
        this.rightLeg = this.body.getChild("rightLeg");
        this.leftLeg = this.body.getChild("leftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset((float)0.0f, (float)12.0f, (float)0.0f));
        PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)-12.0f, (float)0.0f));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(0.5f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-5.0f, (float)2.0f, (float)0.0f));
        rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset((float)-1.0f, (float)7.0f, (float)1.0f));
        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offset((float)5.0f, (float)2.0f, (float)0.0f));
        leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset((float)1.0f, (float)7.0f, (float)1.0f));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offset((float)-1.9f, (float)12.0f, (float)0.0f));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offset((float)1.9f, (float)12.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)32);
    }

    public static void animateZombieArms(ModelPart pLeftArm, ModelPart pRightArm, boolean pIsAggressive, float pAttackTime, float pAgeInTicks) {
        float f2;
        float f = Mth.sin((float)(pAttackTime * (float)Math.PI));
        float f1 = Mth.sin((float)((1.0f - (1.0f - pAttackTime) * (1.0f - pAttackTime)) * (float)Math.PI));
        pRightArm.zRot = 0.0f;
        pLeftArm.zRot = 0.0f;
        pRightArm.yRot = -(0.1f - f * 0.6f);
        pLeftArm.yRot = 0.1f - f * 0.6f;
        pRightArm.xRot = f2 = (float)(-Math.PI) / (pIsAggressive ? 1.5f : 2.25f);
        pLeftArm.xRot = f2;
        pRightArm.xRot += f * 1.2f - f1 * 0.4f;
        pLeftArm.xRot += f * 1.2f - f1 * 0.4f;
        AnimationUtils.bobArms((ModelPart)pRightArm, (ModelPart)pLeftArm, (float)pAgeInTicks);
    }

    public void setupAnim(CyberZombieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
        this.head.xRot = headPitch * ((float)Math.PI / 180);
        this.rightArm.xRot = 0.0f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.xRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos((float)(limbSwing * 0.6662f)) * 1.4f * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos((float)(limbSwing * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount;
        CyberZombieModel.animateZombieArms(this.leftArm, this.rightArm, entity.isAggressive(), this.attackTime, ageInTicks);
    }

    public ModelPart root() {
        return this.waist;
    }
}

