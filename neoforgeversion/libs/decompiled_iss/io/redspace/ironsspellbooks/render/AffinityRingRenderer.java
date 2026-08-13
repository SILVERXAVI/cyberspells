/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AffinityRingRenderer
extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer renderer;
    private final ModelResourceLocation defaultModel = ModelResourceLocation.standalone((ResourceLocation)IronsSpellbooks.id("item/affinity_ring_evocation"));

    public AffinityRingRenderer(ItemRenderer renderDispatcher, EntityModelSet modelSet) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = renderDispatcher;
    }

    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        BakedModel model;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        if (!AffinityData.hasAffinityData(itemStack)) {
            model = this.renderer.getItemModelShaper().getModelManager().getModel(this.defaultModel);
        } else {
            ModelResourceLocation modelResource = ModelResourceLocation.standalone((ResourceLocation)AffinityRingRenderer.getAffinityRingModelLocation(AffinityData.getAffinityData(itemStack).getSpell().getSchoolType()));
            model = this.renderer.getItemModelShaper().getModelManager().getModel(modelResource);
        }
        if (transformType == ItemDisplayContext.GUI) {
            Lighting.setupForFlatItems();
            this.renderer.render(itemStack, transformType, false, poseStack, bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            Lighting.setupFor3DItems();
        } else {
            boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            this.renderer.render(itemStack, transformType, leftHand, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, model);
        }
        poseStack.popPose();
    }

    public static ResourceLocation getAffinityRingModelLocation(SchoolType schoolType) {
        return ResourceLocation.fromNamespaceAndPath((String)schoolType.getId().getNamespace(), (String)String.format("item/affinity_ring_%s", schoolType.getId().getPath()));
    }
}

