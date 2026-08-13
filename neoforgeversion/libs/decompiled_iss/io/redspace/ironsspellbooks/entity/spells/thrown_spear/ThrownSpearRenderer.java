/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ThrownTridentRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.entity.spells.thrown_spear;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.spells.thrown_spear.ThrownSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class ThrownSpearRenderer
extends EntityRenderer<ThrownSpear> {
    public ThrownSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(ThrownSpear entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack item = entity.getWeaponItem();
        if (item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-Mth.lerp((float)partialTicks, (float)entity.yRotO, (float)entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp((float)partialTicks, (float)entity.xRotO, (float)entity.getXRot()) + 10.0f));
        poseStack.translate(0.0, -1.5, 0.0);
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level, 0);
        if (entity.isChanneled()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(80.0f));
            LightningLanceRenderer.renderModel(poseStack, buffer, entity.tickCount);
        }
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(ThrownSpear entity) {
        return ThrownTridentRenderer.TRIDENT_LOCATION;
    }
}

