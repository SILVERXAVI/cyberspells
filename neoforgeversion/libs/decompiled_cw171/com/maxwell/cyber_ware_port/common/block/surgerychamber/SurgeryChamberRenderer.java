/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.maxwell.cyber_ware_port.common.block.surgerychamber;

import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlock;
import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlockEntity;
import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class SurgeryChamberRenderer
implements BlockEntityRenderer<SurgeryChamberBlockEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/block/surgery_chamber.png");
    private final SurgeryChamberModel model;

    public SurgeryChamberRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SurgeryChamberModel(context.bakeLayer(SurgeryChamberModel.LAYER_LOCATION));
    }

    public void render(SurgeryChamberBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getBlockState().getValue(SurgeryChamberBlock.HALF) == DoubleBlockHalf.UPPER) {
            return;
        }
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 1.5, 0.5);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        Direction facing = (Direction)pBlockEntity.getBlockState().getValue((Property)SurgeryChamberBlock.FACING);
        float angle = facing.toYRot();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(angle));
        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)TEXTURE));
        this.model.setupAnim(pBlockEntity, pPartialTick);
        this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay, -1);
        pPoseStack.popPose();
    }
}

