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
 *  net.minecraft.world.level.block.HorizontalDirectionalBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.maxwell.cyber_ware_port.common.block.scanner;

import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlockEntity;
import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class ScannerBlockRenderer
implements BlockEntityRenderer<ScannerBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/block/scanner.png");
    private final ScannerBlockModel model;

    public ScannerBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ScannerBlockModel(context.bakeLayer(ScannerBlockModel.LAYER_LOCATION));
    }

    public void render(ScannerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 1.5, 0.5);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        BlockState blockState = pBlockEntity.getBlockState();
        Direction facing = (Direction)blockState.getValue((Property)HorizontalDirectionalBlock.FACING);
        float rotationDegrees = facing.getOpposite().toYRot();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));
        float animTime = 0.0f;
        if (pBlockEntity.isWorking()) {
            animTime = (float)pBlockEntity.getLevel().getGameTime() + pPartialTick;
        }
        this.model.setupMovingParts(pBlockEntity.isWorking(), animTime);
        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)TEXTURE));
        this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay, -1);
        pPoseStack.popPose();
    }
}

