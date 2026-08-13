/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.model.ItemTransform
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.joml.Quaternionf
 */
package io.redspace.ironsspellbooks.item.weapons.pyrium_staff;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffHeadModel;
import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffOrbModel;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class PyriumStaffRenderer
extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer renderer;
    public final BakedModel haftModel;
    public final PyriumStaffHeadModel headModel;
    public final PyriumStaffOrbModel orbModel;

    public PyriumStaffRenderer(ItemRenderer renderDispatcher, EntityModelSet modelSet) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = renderDispatcher;
        this.haftModel = this.renderer.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"item/pyrium_staff_haft")));
        this.headModel = new PyriumStaffHeadModel(modelSet.bakeLayer(PyriumStaffHeadModel.LAYER_LOCATION));
        this.orbModel = new PyriumStaffOrbModel(modelSet.bakeLayer(PyriumStaffOrbModel.LAYER_LOCATION));
    }

    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        if (transformType == ItemDisplayContext.GUI) {
            Lighting.setupForEntityInInventory();
            this.render(poseStack, bufferSource, itemStack, transformType, 0xF000F0, OverlayTexture.NO_OVERLAY, false);
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            Lighting.setupFor3DItems();
        } else {
            boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            this.render(poseStack, bufferSource, itemStack, transformType, combinedLightIn, combinedOverlayIn, leftHand);
        }
        poseStack.popPose();
    }

    private void render(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, ItemDisplayContext transformType, int combinedLightIn, int combinedOverlayIn, boolean leftHanded) {
        this.renderer.render(itemStack, transformType, leftHanded, poseStack, bufferSource, combinedLightIn, combinedOverlayIn, this.haftModel);
        poseStack.pushPose();
        ItemTransform transform = this.haftModel.getTransforms().getTransform(transformType);
        this.applyTransform(transform, leftHanded, poseStack);
        poseStack.mulPose(Axis.ZP.rotationDegrees(135.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        poseStack.translate(0.0, -0.3984375, 0.0);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        this.headModel.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect((MultiBufferSource)bufferSource, (RenderType)this.headModel.renderType(), (boolean)false, (boolean)itemStack.hasFoil()), combinedLightIn, combinedOverlayIn);
        poseStack.translate(0.0, -0.296875, 0.0);
        float f = MinecraftInstanceHelper.getPlayer() == null ? 0.0f : ((float)MinecraftInstanceHelper.getPlayer().tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true)) * 0.75f;
        float scale = (Mth.sin((float)(f * 0.5f)) + Mth.sin((float)(3.0f * f))) / 2.0f * 0.04f + 1.0f;
        poseStack.translate(0.0f, Mth.sin((float)(f * 0.3f)) / 32.0f, 0.0f);
        poseStack.scale(scale, scale, scale);
        this.orbModel.renderToBuffer(poseStack, bufferSource.getBuffer(this.orbModel.renderType()), 0xF000F0, combinedOverlayIn);
        poseStack.popPose();
    }

    public void applyTransform(ItemTransform transform, boolean pLeftHand, PoseStack pPoseStack) {
        if (transform != ItemTransform.NO_TRANSFORM) {
            float f = transform.rotation.x();
            float f1 = transform.rotation.y();
            float f2 = transform.rotation.z();
            if (pLeftHand) {
                f1 = -f1;
                f2 = -f2;
            }
            int i = pLeftHand ? -1 : 1;
            pPoseStack.translate((float)i * transform.translation.x(), transform.translation.y(), transform.translation.z());
            pPoseStack.mulPose(new Quaternionf().rotationXYZ(f * ((float)Math.PI / 180), f1 * ((float)Math.PI / 180), f2 * ((float)Math.PI / 180)));
            pPoseStack.scale(transform.scale.x(), transform.scale.y(), transform.scale.x());
        }
    }
}

