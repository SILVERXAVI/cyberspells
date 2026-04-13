/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.entity.client.CyberzombieHighlightLayer;
import com.perigrine3.createcybernetics.entity.client.CyberzombieModel;
import com.perigrine3.createcybernetics.entity.custom.CyberzombieEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CyberzombieRenderer
extends MobRenderer<CyberzombieEntity, CyberzombieModel<CyberzombieEntity>> {
    public CyberzombieRenderer(EntityRendererProvider.Context context) {
        super(context, new CyberzombieModel(context.bakeLayer(CyberzombieModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new CyberzombieHighlightLayer((RenderLayerParent<CyberzombieEntity, CyberzombieModel<CyberzombieEntity>>)this));
    }

    public ResourceLocation getTextureLocation(CyberzombieEntity cyberzombieEntity) {
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/cyberzombie.png");
    }

    protected void scale(CyberzombieEntity entity, PoseStack poseStack, float partialTickTime) {
        if (entity.isBaby()) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        }
        super.scale((LivingEntity)entity, poseStack, partialTickTime);
    }

    public void render(CyberzombieEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render((LivingEntity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}

