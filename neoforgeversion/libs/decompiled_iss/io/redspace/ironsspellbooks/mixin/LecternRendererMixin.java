/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.BookModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.LecternRenderer
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.LecternBlock
 *  net.minecraft.world.level.block.entity.LecternBlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package io.redspace.ironsspellbooks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.LecternRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LecternRenderer.class})
public class LecternRendererMixin {
    @Shadow
    BookModel bookModel;

    @Inject(method={"render(Lnet/minecraft/world/level/block/entity/LecternBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"}, remap=false, at={@At(value="HEAD")}, cancellable=true)
    private void render(LecternBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, CallbackInfo ci) {
        block2: {
            block4: {
                ILecternPlaceable lecternPlaceable;
                ItemStack stack;
                block6: {
                    block5: {
                        block3: {
                            Item item;
                            BlockState blockstate = pBlockEntity.getBlockState();
                            if (!((Boolean)blockstate.getValue((Property)LecternBlock.HAS_BOOK)).booleanValue() || !((item = (stack = pBlockEntity.getBook()).getItem()) instanceof ILecternPlaceable)) break block2;
                            lecternPlaceable = (ILecternPlaceable)item;
                            pPoseStack.pushPose();
                            pPoseStack.translate(0.5f, 1.0625f, 0.5f);
                            float f = ((Direction)blockstate.getValue((Property)LecternBlock.FACING)).getClockWise().toYRot();
                            pPoseStack.mulPose(Axis.YP.rotationDegrees(-f));
                            pPoseStack.mulPose(Axis.ZP.rotationDegrees(67.5f));
                            Optional<ResourceLocation> textureOverride = lecternPlaceable.simpleTextureOverride(stack);
                            if (!textureOverride.isPresent()) break block3;
                            pPoseStack.translate(0.0f, -0.125f, 0.0f);
                            this.bookModel.setupAnim(0.0f, 0.1f, 0.9f, 1.2f);
                            VertexConsumer vertexconsumer = pBufferSource.getBuffer(RenderType.entitySolid((ResourceLocation)textureOverride.get()));
                            this.bookModel.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, -1);
                            break block4;
                        }
                        Item vertexconsumer = stack.getItem();
                        if (!(vertexconsumer instanceof SpellBook)) break block5;
                        SpellBook spellBook = (SpellBook)vertexconsumer;
                        break block6;
                    }
                    if (!stack.is(ItemRegistry.THE_CHRONICLE)) break block4;
                }
                pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                pPoseStack.translate(0.125f, -0.625f, 0.125f);
                lecternPlaceable.handleCustomLecternPosing(pPoseStack);
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderStatic(stack, ItemDisplayContext.HEAD, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
            }
            pPoseStack.popPose();
            ci.cancel();
        }
    }
}

