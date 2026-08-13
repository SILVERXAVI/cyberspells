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
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.item.weapons.pyrium_staff;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class PyriumStaffHeadModel
extends Model {
    public static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/item/pyrium_staff_head.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(IronsSpellbooks.id("pyrium_staff_head"), "main");
    private final ModelPart root;

    public PyriumStaffHeadModel(ModelPart pRoot) {
        super(RenderType::entityCutout);
        this.root = pRoot;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -1.0f, -1.5f, 2.0f, 1.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        PartDefinition forespike_r1 = bb_main.addOrReplaceChild("forespike_r1", CubeListBuilder.create().texOffs(13, 10).addBox(0.0f, -9.0f, 0.0f, 0.0f, 13.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)1.0472f, (float)0.0f));
        PartDefinition forespike_r2 = bb_main.addOrReplaceChild("forespike_r2", CubeListBuilder.create().texOffs(13, 10).addBox(0.0f, -9.0f, 0.0f, 0.0f, 13.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-1.0472f, (float)0.0f));
        PartDefinition forespike_r3 = bb_main.addOrReplaceChild("forespike_r3", CubeListBuilder.create().texOffs(13, 10).addBox(0.0f, -9.0f, 0.0f, 0.0f, 13.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation((float)0.0f, (float)0.0f, (float)-0.5f, (float)0.0f, (float)3.1416f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)32, (int)32);
    }

    public RenderType renderType() {
        return this.renderType(TEXTURE);
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
    }
}

