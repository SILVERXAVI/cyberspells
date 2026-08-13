/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.entity.spells.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ShieldModel
extends EntityModel<ShieldEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"shield_model"), "main");
    private final ModelPart bb_main;

    public ShieldModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(4, 8).addBox(-6.0f, -3.0f, 0.0f, 12.0f, 8.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(7, 7).addBox(-3.0f, 5.0f, 0.0f, 6.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(9, 6).addBox(-1.0f, 6.0f, 0.0f, 2.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(5, 16).addBox(-5.0f, -4.0f, 0.0f, 10.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(7, 18).addBox(-3.0f, -6.0f, 0.0f, 6.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(6, 17).addBox(-4.0f, -5.0f, 0.0f, 8.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)).texOffs(8, 19).addBox(-2.0f, -7.0f, 0.0f, 4.0f, 1.0f, 0.1f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)32);
    }

    public void setupAnim(ShieldEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
        this.bb_main.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
    }
}

