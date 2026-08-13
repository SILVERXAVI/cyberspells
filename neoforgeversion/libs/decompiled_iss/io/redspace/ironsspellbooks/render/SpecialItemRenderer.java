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

public class SpecialItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer renderer;
    public final BakedModel guiModel;
    public final BakedModel normalModel;

    public SpecialItemRenderer(ItemRenderer renderDispatcher, EntityModelSet modelSet, String name) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = renderDispatcher;
        this.guiModel = this.renderer.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)("item/" + name + "_gui"))));
        this.normalModel = this.renderer.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)("item/" + name + "_normal"))));
    }

    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        if (transformType == ItemDisplayContext.GUI) {
            Lighting.setupForFlatItems();
            BakedModel model = this.guiModel;
            this.renderer.render(itemStack, transformType, false, poseStack, bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY, model);
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            Lighting.setupFor3DItems();
        } else {
            BakedModel model = this.normalModel;
            boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            this.renderer.render(itemStack, transformType, leftHand, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, model);
        }
        poseStack.popPose();
    }
}

