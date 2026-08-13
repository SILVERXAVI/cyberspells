/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.block.scroll_forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeBlock;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ScrollForgeRenderer
implements BlockEntityRenderer<ScrollForgeTile> {
    private static final ResourceLocation PAPER_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/block/scroll_forge_paper.png");
    private static final ResourceLocation SIGIL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/block/scroll_forge_sigil.png");
    ItemRenderer itemRenderer;
    private static final Vec3 INK_POS = new Vec3(0.175, 0.876, 0.25);
    private static final Vec3 FOCUS_POS = new Vec3(0.75, 0.876, 0.4);
    private static final Vec3 PAPER_POS = new Vec3(0.5, 0.876, 0.7);

    public ScrollForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ScrollForgeTile scrollForgeTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack inkStack = scrollForgeTile.getStackInSlot(0);
        ItemStack paperStack = scrollForgeTile.getStackInSlot(1);
        ItemStack focusStack = scrollForgeTile.getItemHandler().getStackInSlot(2);
        if (!inkStack.isEmpty() && inkStack.getItem() instanceof InkItem) {
            this.renderItem(inkStack, INK_POS, 15.0f, scrollForgeTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (!focusStack.isEmpty() && focusStack.is(ModTags.SCHOOL_FOCUS)) {
            this.renderItem(focusStack, FOCUS_POS, 5.0f, scrollForgeTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (!paperStack.isEmpty() && paperStack.is(Items.PAPER)) {
            poseStack.pushPose();
            this.rotatePoseWithBlock(poseStack, scrollForgeTile);
            poseStack.translate(ScrollForgeRenderer.PAPER_POS.x, ScrollForgeRenderer.PAPER_POS.y, ScrollForgeRenderer.PAPER_POS.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(85.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout((ResourceLocation)PAPER_TEXTURE));
            int light = LevelRenderer.getLightColor((BlockAndTintGetter)scrollForgeTile.getLevel(), (BlockPos)scrollForgeTile.getBlockPos());
            this.drawQuad(0.45f, poseStack.last(), consumer, light);
            poseStack.popPose();
        }
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, ScrollForgeTile scrollForgeTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int)scrollForgeTile.getBlockPos().asLong();
        this.rotatePoseWithBlock(poseStack, scrollForgeTile);
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-yRot));
        poseStack.scale(0.45f, 0.45f, 0.45f);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor((BlockAndTintGetter)scrollForgeTile.getLevel(), (BlockPos)scrollForgeTile.getBlockPos()), packedOverlay, poseStack, bufferSource, scrollForgeTile.getLevel(), renderId);
        poseStack.popPose();
    }

    private void drawQuad(float width, PoseStack.Pose pose, VertexConsumer consumer, int light) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5f;
        consumer.addVertex(poseMatrix, -halfWidth, 0.0f, -halfWidth).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, -1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth, 0.0f, -halfWidth).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, -1.0f, 0.0f);
        consumer.addVertex(poseMatrix, halfWidth, 0.0f, halfWidth).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, -1.0f, 0.0f);
        consumer.addVertex(poseMatrix, -halfWidth, 0.0f, halfWidth).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, -1.0f, 0.0f);
    }

    private void rotatePoseWithBlock(PoseStack poseStack, ScrollForgeTile scrollForgeTile) {
        Vec3 center = new Vec3(0.5, 0.5, 0.5);
        poseStack.translate(center.x, center.y, center.z);
        poseStack.mulPose(Axis.YP.rotationDegrees((float)this.getBlockFacingDegrees(scrollForgeTile)));
        poseStack.translate(-center.x, -center.y, -center.z);
    }

    private int getBlockFacingDegrees(ScrollForgeTile tileEntity) {
        BlockState block = tileEntity.getLevel().getBlockState(tileEntity.getBlockPos());
        if (block.getBlock() instanceof ScrollForgeBlock) {
            Direction facing = (Direction)block.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
            return switch (facing) {
                case Direction.NORTH -> 180;
                case Direction.EAST -> 90;
                case Direction.WEST -> -90;
                default -> 0;
            };
        }
        return 0;
    }
}

