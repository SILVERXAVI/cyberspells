/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.block.portal_frame;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlock;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalRenderer;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

public class PortalFrameRenderer
implements BlockEntityRenderer<PortalFrameBlockEntity> {
    public PortalFrameRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(PortalFrameBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.clientIsConnected || ((DoubleBlockHalf)pBlockEntity.getBlockState().getValue(PortalFrameBlock.HALF)).equals((Object)DoubleBlockHalf.UPPER)) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        Direction direction = (Direction)pBlockEntity.getBlockState().getValue((Property)PortalFrameBlock.FACING);
        Vec3i n = direction.getNormal();
        Vec3 dir = new Vec3((double)n.getX(), 0.0, (double)n.getZ()).scale(-0.40625);
        poseStack.translate(dir.x, 0.0, dir.z);
        if (direction == Direction.EAST || direction == Direction.WEST) {
            poseStack.mulPose(Axis.YP.rotation(1.5707964f));
        }
        PortalRenderer.renderPortal(poseStack, pBufferSource, pBlockEntity.getLevel() == null ? 0 : (int)pBlockEntity.getLevel().getGameTime(), pPartialTick, false, pBlockEntity.getBlockState().is(BlockRegistry.POCKET_PORTAL_FRAME), pBlockEntity.getColor());
        poseStack.popPose();
    }
}

