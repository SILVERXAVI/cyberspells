/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(value=Dist.CLIENT)
public class VoidTentacleEmissiveLayer
extends GeoRenderLayer<VoidTentacle> {
    public static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/void_tentacle/void_tentacle_emissive.png");

    public VoidTentacleEmissiveLayer(GeoEntityRenderer entityRendererIn) {
        super((GeoRenderer)entityRendererIn);
    }

    public void render(PoseStack poseStack, VoidTentacle animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        renderType = RenderType.eyes((ResourceLocation)TEXTURE);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        poseStack.pushPose();
        float f = Mth.sin((float)((float)(((double)((float)animatable.tickCount + partialTick) + (animatable.getX() + animatable.getZ()) * 500.0) * (double)0.15f))) * 0.5f + 0.5f;
        this.getRenderer().actuallyRender(poseStack, (GeoAnimatable)animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, 0xF000F0, OverlayTexture.NO_OVERLAY, Utils.packRGB(new Vector3f(f, f, f)));
        poseStack.popPose();
    }
}

