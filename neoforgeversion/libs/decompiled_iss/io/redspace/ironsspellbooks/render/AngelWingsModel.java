/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.client.model.AgeableListModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class AngelWingsModel<T extends LivingEntity>
extends AgeableListModel<T> {
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    public static final String MAIN = "main";
    public static final String ANGEL_WINGS = "angel_wings";
    public static ModelLayerLocation ANGEL_WINGS_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"angel_wings"), "main");

    public AngelWingsModel(ModelPart pRoot) {
        this.leftWing = pRoot.getChild("left_wing");
        this.rightWing = pRoot.getChild("right_wing");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeDeformation cubedeformation = new CubeDeformation(1.0f);
        partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(22, 0).addBox(-10.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, cubedeformation), PartPose.offsetAndRotation((float)5.0f, (float)0.0f, (float)0.0f, (float)0.2617994f, (float)0.0f, (float)-0.2617994f));
        partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(22, 0).mirror().addBox(0.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, cubedeformation), PartPose.offsetAndRotation((float)-5.0f, (float)0.0f, (float)0.0f, (float)0.2617994f, (float)0.0f, (float)0.2617994f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)32);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of((Object)this.leftWing, (Object)this.rightWing);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f = 0.2617994f;
        float f1 = -0.2617994f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (pEntity.isFallFlying()) {
            float f4 = 1.0f;
            Vec3 vec3 = pEntity.getDeltaMovement();
            if (vec3.y < 0.0) {
                Vec3 vec31 = vec3.normalize();
                f4 = 1.0f - (float)Math.pow(-vec31.y, 1.5);
            }
            f = f4 * 0.34906584f + (1.0f - f4) * f;
            f1 = f4 * -1.5707964f + (1.0f - f4) * f1;
        } else if (pEntity.isCrouching()) {
            f = 0.6981317f;
            f1 = -0.7853982f;
            f2 = 3.0f;
            f3 = 0.08726646f;
        }
        this.leftWing.y = f2;
        if (pEntity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)pEntity;
            abstractclientplayer.elytraRotX += (f - abstractclientplayer.elytraRotX) * 0.1f;
            abstractclientplayer.elytraRotY += (f3 - abstractclientplayer.elytraRotY) * 0.1f;
            abstractclientplayer.elytraRotZ += (f1 - abstractclientplayer.elytraRotZ) * 0.1f;
            this.leftWing.xRot = abstractclientplayer.elytraRotX;
            this.leftWing.yRot = abstractclientplayer.elytraRotY;
            this.leftWing.zRot = abstractclientplayer.elytraRotZ;
        } else {
            this.leftWing.xRot = f;
            this.leftWing.zRot = f1;
            this.leftWing.yRot = f3;
        }
        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }
}

