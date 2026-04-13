/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package com.perigrine3.createcybernetics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.entity.client.SmasherModel;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SmasherHighlightLayer
extends RenderLayer<SmasherEntity, SmasherModel<SmasherEntity>> {
    private static final ResourceLocation HIGHLIGHT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/smasherhighlight.png");

    public SmasherHighlightLayer(RenderLayerParent<SmasherEntity, SmasherModel<SmasherEntity>> parent) {
        super(parent);
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, SmasherEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        VertexConsumer vc = buffer.getBuffer(RenderType.eyes((ResourceLocation)HIGHLIGHT_TEXTURE));
        int overlay = LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, (float)0.0f);
        ((SmasherModel)this.getParentModel()).renderToBuffer(poseStack, vc, 0xF000F0, overlay, -1);
    }
}

