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
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.block.radio.tower;

import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlockEntity;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class RadioTowerRenderer
implements BlockEntityRenderer<RadioTowerCoreBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/block/radio_tower_complete.png");
    private final RadioTowerModel model;

    public RadioTowerRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new RadioTowerModel(context.bakeLayer(RadioTowerModel.LAYER_LOCATION));
    }

    public void render(RadioTowerCoreBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!((Boolean)pBlockEntity.getBlockState().getValue((Property)RadioTowerCoreBlock.FORMED)).booleanValue()) {
            return;
        }
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, -7.5, 0.5);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        pPoseStack.translate(0.0, 1.0, 0.0);
        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)TEXTURE));
        this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay, -1);
        pPoseStack.popPose();
    }

    @NotNull
    public AABB getRenderBoundingBox(RadioTowerCoreBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB((double)pos.getX() - 5.0, (double)pos.getY() - 15.0, (double)pos.getZ() - 5.0, (double)pos.getX() + 6.0, (double)pos.getY() + 2.0, (double)pos.getZ() + 6.0);
    }
}

