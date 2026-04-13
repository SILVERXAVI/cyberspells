/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.ItemInHandLayer
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.entity.client.CyberskeletonHighlightLayer;
import com.perigrine3.createcybernetics.entity.client.CyberskeletonModel;
import com.perigrine3.createcybernetics.entity.custom.CyberskeletonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CyberskeletonRenderer
extends MobRenderer<CyberskeletonEntity, CyberskeletonModel<CyberskeletonEntity>> {
    public CyberskeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new CyberskeletonModel(context.bakeLayer(CyberskeletonModel.LAYER_LOCATION)), 0.5f);
        this.addLayer((RenderLayer)new ItemInHandLayer((RenderLayerParent)this, context.getItemInHandRenderer()));
        this.addLayer(new CyberskeletonHighlightLayer((RenderLayerParent<CyberskeletonEntity, CyberskeletonModel<CyberskeletonEntity>>)this));
    }

    public ResourceLocation getTextureLocation(CyberskeletonEntity cyberskeletonEntity) {
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/cyberskeleton.png");
    }

    public void render(CyberskeletonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render((LivingEntity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}

