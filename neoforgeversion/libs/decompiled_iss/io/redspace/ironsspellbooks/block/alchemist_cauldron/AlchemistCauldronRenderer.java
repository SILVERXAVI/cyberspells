/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.joml.Matrix4f
 *  org.joml.Vector2i
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import io.redspace.ironsspellbooks.gui.overlays.ScreenTooltipOverlay;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class AlchemistCauldronRenderer
implements BlockEntityRenderer<AlchemistCauldronTile> {
    ItemRenderer itemRenderer;
    private static final Vec3 ITEM_POS = new Vec3(0.5, 1.5, 0.5);

    public AlchemistCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(AlchemistCauldronTile cauldron, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockHitResult blockHitResult;
        HitResult f2;
        int waterLevel = cauldron.getFluidAmount();
        float waterOffset = Mth.lerp((float)((float)waterLevel / 1000.0f), (float)0.25f, (float)0.9f);
        if (waterLevel > 0) {
            this.renderWater(cauldron, poseStack, bufferSource, packedLight, waterOffset);
        }
        NonNullList<ItemStack> floatingItems = cauldron.inputItems;
        for (int i = 0; i < floatingItems.size(); ++i) {
            ItemStack itemStack = (ItemStack)floatingItems.get(i);
            if (itemStack.isEmpty()) continue;
            float f2 = waterLevel > 0 ? (float)cauldron.getLevel().getGameTime() + partialTick : 15.0f;
            Vec2 floatOffset = this.getFloatingItemOffset(f2, i * 587);
            float yRot = (f2 + (float)(i * 213)) / (float)(i + 1) * 1.5f;
            this.renderItem(itemStack, new Vec3((double)floatOffset.x, (double)(waterOffset + (float)i * 0.01f), (double)floatOffset.y), yRot, cauldron, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && Math.abs(player.getX() - (double)cauldron.getBlockPos().getX()) < 5.0 && Math.abs(player.getY() - (double)cauldron.getBlockPos().getY()) < 5.0 && Math.abs(player.getZ() - (double)cauldron.getBlockPos().getZ()) < 5.0 && player.isCrouching() && (f2 = Minecraft.getInstance().hitResult) instanceof BlockHitResult && (blockHitResult = (BlockHitResult)f2).getBlockPos().equals((Object)cauldron.getBlockPos())) {
            ArrayList<Component> text = new ArrayList<Component>();
            text.add((Component)Component.translatable((String)"block.irons_spellbooks.alchemist_cauldron").withStyle(new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.WHITE}));
            List<FluidStack> fluids = cauldron.fluidInventory.fluids();
            if (fluids.isEmpty()) {
                text.add((Component)Component.translatable((String)"ui.irons_spellbooks.empty").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
            } else {
                ArrayList<ObjectIntImmutablePair> fluidInfo = new ArrayList<ObjectIntImmutablePair>();
                for (int i = fluids.size() - 1; i >= 0; --i) {
                    FluidStack fluid = fluids.get(i);
                    fluidInfo.add(new ObjectIntImmutablePair((Object)fluid.getFluidType().getDescription(fluid).copy().withStyle(ChatFormatting.DARK_AQUA), fluid.getAmount()));
                }
                for (ObjectIntImmutablePair info : fluidInfo) {
                    text.add((Component)Component.literal((String)"  ").append((Component)info.left()).append(": ").append((Component)Component.literal((String)(info.rightInt() + "mb")).withStyle(ChatFormatting.GOLD)));
                }
            }
            ScreenTooltipOverlay.renderTooltip(text, (sw, sh, mx, my, tw, th) -> new Vector2i(sw / 2 + 30, sh / 2 - th / 2));
        }
    }

    public Vec2 getFloatingItemOffset(float time, int offset) {
        float xspeed = offset % 2 == 0 ? 0.0075f : 0.025f * (1.0f + (float)(offset % 88) * 0.001f);
        float yspeed = offset % 2 == 0 ? 0.025f : 0.0075f * (1.0f + (float)(offset % 88) * 0.001f);
        float x = (time + (float)offset) * xspeed;
        x = (Math.abs(x % 2.0f - 1.0f) + 1.0f) / 2.0f;
        float y = (time + (float)offset + 4356.0f) * yspeed;
        y = (Math.abs(y % 2.0f - 1.0f) + 1.0f) / 2.0f;
        x = Mth.lerp((float)x, (float)-0.2f, (float)0.75f);
        y = Mth.lerp((float)y, (float)-0.2f, (float)0.75f);
        return new Vec2(x, y);
    }

    private void renderWater(AlchemistCauldronTile cauldron, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float waterOffset) {
        float totalFluid;
        Matrix4f pose = poseStack.last().pose();
        float runningFluid = totalFluid = (float)cauldron.getFluidAmount();
        float f = 0.0f;
        float padding = 0.0625f;
        for (FluidStack fluid : cauldron.fluidInventory.fluids()) {
            int skylight = packedLight >> 4 & 0xF;
            int luminosity = Math.max(skylight, fluid.getFluidType().getLightLevel(fluid));
            int fluidlight = packedLight & 0xF00000 | luminosity << 4;
            IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of((Fluid)fluid.getFluid());
            Function spriteAtlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
            TextureAtlasSprite texture = (TextureAtlasSprite)spriteAtlas.apply(clientFluid.getStillTexture(fluid.getFluid().defaultFluidState(), (BlockAndTintGetter)cauldron.getLevel(), cauldron.getBlockPos()));
            VertexConsumer consumer = texture.wrap(bufferSource.getBuffer(RenderType.translucent()));
            Vector3f rgb = this.colorFromLong(clientFluid.getTintColor(fluid) & clientFluid.getTintColor(fluid.getFluid().defaultFluidState(), (BlockAndTintGetter)cauldron.getLevel(), cauldron.getBlockPos()));
            float opacity = runningFluid / totalFluid;
            runningFluid -= (float)fluid.getAmount();
            consumer.addVertex(pose, 1.0f - padding, waterOffset + f, 0.0f + padding).setColor(rgb.x(), rgb.y(), rgb.z(), opacity).setUv(1.0f - padding, 0.0f + padding).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fluidlight).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(pose, 0.0f + padding, waterOffset + f, 0.0f + padding).setColor(rgb.x(), rgb.y(), rgb.z(), opacity).setUv(0.0f + padding, 0.0f + padding).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fluidlight).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(pose, 0.0f + padding, waterOffset + f, 1.0f - padding).setColor(rgb.x(), rgb.y(), rgb.z(), opacity).setUv(0.0f + padding, 1.0f - padding).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fluidlight).setNormal(0.0f, 1.0f, 0.0f);
            consumer.addVertex(pose, 1.0f - padding, waterOffset + f, 1.0f - padding).setColor(rgb.x(), rgb.y(), rgb.z(), opacity).setUv(1.0f - padding, 1.0f - padding).setOverlay(OverlayTexture.NO_OVERLAY).setLight(fluidlight).setNormal(0.0f, 1.0f, 0.0f);
            f += 0.001f;
        }
    }

    private Vector3f colorFromLong(long color) {
        return new Vector3f((float)(color >> 16 & 0xFFL) / 255.0f, (float)(color >> 8 & 0xFFL) / 255.0f, (float)(color & 0xFFL) / 255.0f);
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, AlchemistCauldronTile tile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int)tile.getBlockPos().asLong();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.scale(0.4f, 0.4f, 0.4f);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor((BlockAndTintGetter)tile.getLevel(), (BlockPos)tile.getBlockPos()), packedOverlay, poseStack, bufferSource, tile.getLevel(), renderId);
        poseStack.popPose();
    }
}

