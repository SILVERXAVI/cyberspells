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
import com.perigrine3.createcybernetics.entity.client.SmasherHighlightLayer;
import com.perigrine3.createcybernetics.entity.client.SmasherModel;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SmasherRenderer
extends MobRenderer<SmasherEntity, SmasherModel<SmasherEntity>> {
    public SmasherRenderer(EntityRendererProvider.Context context) {
        super(context, new SmasherModel(context.bakeLayer(SmasherModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new SmasherHighlightLayer((RenderLayerParent<SmasherEntity, SmasherModel<SmasherEntity>>)this));
    }

    public ResourceLocation getTextureLocation(SmasherEntity entity) {
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/smasher.png");
    }

    public void render(SmasherEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render((LivingEntity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}

