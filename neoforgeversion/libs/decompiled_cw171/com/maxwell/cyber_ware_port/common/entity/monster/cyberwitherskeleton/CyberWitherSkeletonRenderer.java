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
package com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton.CyberWitherSkeletonEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton.CyberWitherSkeletonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CyberWitherSkeletonRenderer
extends MobRenderer<CyberWitherSkeletonEntity, CyberWitherSkeletonModel> {
    private static final ResourceLocation NORMAL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_wither_skeleton.png");

    public CyberWitherSkeletonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, (EntityModel)new CyberWitherSkeletonModel(pContext.bakeLayer(CyberWitherSkeletonModel.LAYER_LOCATION)), 0.5f);
    }

    public ResourceLocation getTextureLocation(CyberWitherSkeletonEntity cyberWitherSkeletonEntity) {
        return NORMAL_TEXTURE;
    }

    protected void scale(CyberWitherSkeletonEntity entity, PoseStack poseStack, float partialTickTime) {
        if (entity.isBaby()) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        }
        super.scale((LivingEntity)entity, poseStack, partialTickTime);
    }
}

