/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.SkullModel
 *  net.minecraft.client.model.SkullModelBase
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.blockentity.SkullBlockRenderer
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.SkullBlock
 *  net.minecraft.world.level.block.WallSkullBlock
 *  net.minecraft.world.level.block.entity.SkullBlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.maxwell.cyber_ware_port.common.block.cyberskull;

import com.maxwell.cyber_ware_port.client.ModClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CyberSkullRenderer
implements BlockEntityRenderer<SkullBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_wither_skeleton.png");
    private final SkullModel model;

    public CyberSkullRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SkullModel(context.bakeLayer(ModClientEvents.CYBER_SKULL_LAYER));
    }

    public void render(SkullBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        float animationProgress = pBlockEntity.getAnimation(pPartialTick);
        BlockState blockstate = pBlockEntity.getBlockState();
        boolean isWall = blockstate.getBlock() instanceof WallSkullBlock;
        Direction direction = isWall ? (Direction)blockstate.getValue((Property)WallSkullBlock.FACING) : null;
        float rotation = 22.5f * (float)(isWall ? (2 + direction.get2DDataValue()) * 4 : (Integer)blockstate.getValue((Property)SkullBlock.ROTATION));
        SkullBlockRenderer.renderSkull((Direction)direction, (float)rotation, (float)animationProgress, (PoseStack)pPoseStack, (MultiBufferSource)pBufferSource, (int)pPackedLight, (SkullModelBase)this.model, (RenderType)RenderType.entityTranslucent((ResourceLocation)TEXTURE));
    }
}

