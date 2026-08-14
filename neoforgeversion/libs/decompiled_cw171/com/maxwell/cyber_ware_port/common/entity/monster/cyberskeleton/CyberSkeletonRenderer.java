/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberskeleton;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberskeleton.CyberSkeletonEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberskeleton.CyberSkeletonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CyberSkeletonRenderer
extends MobRenderer<CyberSkeletonEntity, CyberSkeletonModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_skeleton.png");

    public CyberSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, (EntityModel)new CyberSkeletonModel(context.bakeLayer(CyberSkeletonModel.LAYER_LOCATION)), 0.5f);
    }

    public ResourceLocation getTextureLocation(CyberSkeletonEntity entity) {
        return TEXTURE;
    }

    protected void scale(CyberSkeletonEntity entity, PoseStack poseStack, float partialTickTime) {
        if (entity.isBaby()) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        }
        super.scale((LivingEntity)entity, poseStack, partialTickTime);
    }
}

