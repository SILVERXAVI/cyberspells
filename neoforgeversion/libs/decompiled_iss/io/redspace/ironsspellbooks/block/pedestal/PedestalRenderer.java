/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.DiggerItem
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.block.pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.block.pedestal.PedestalTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;

public class PedestalRenderer
implements BlockEntityRenderer<PedestalTile> {
    ItemRenderer itemRenderer;
    private static final Vec3 ITEM_POS = new Vec3(0.5, 1.5, 0.5);

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack heldItem = pedestalTile.getHeldItem();
        if (!heldItem.isEmpty()) {
            LocalPlayer player = Minecraft.getInstance().player;
            float bob = 0.0f;
            float rotation = (float)(player.tickCount * 2) + partialTick;
            this.renderItem(heldItem, ITEM_POS.add(0.0, (double)bob, 0.0), rotation, pedestalTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, PedestalTile pedestalTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int)pedestalTile.getBlockPos().asLong();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof DiggerItem) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0f));
        }
        poseStack.scale(0.65f, 0.65f, 0.65f);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor((BlockAndTintGetter)pedestalTile.getLevel(), (BlockPos)pedestalTile.getBlockPos()), packedOverlay, poseStack, bufferSource, pedestalTile.getLevel(), renderId);
        poseStack.popPose();
    }
}

