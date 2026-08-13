/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.spectral_hammer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammerModel;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class SpectralHammerRenderer
extends GeoLivingEntityRenderer<SpectralHammer> {
    public SpectralHammerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpectralHammerModel());
        this.shadowRadius = 0.3f;
    }

    public ResourceLocation getTextureLocation(SpectralHammer animatable) {
        return SpectralHammerModel.textureResource;
    }

    public void preRender(PoseStack poseStack, SpectralHammer animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(2.0f, 2.0f, 2.0f);
        super.preRender(poseStack, (Entity)animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    public RenderType getRenderType(SpectralHammer animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        Vec2 vec2 = SpectralHammerRenderer.getEnergySwirlOffset(animatable, partialTick);
        return RenderType.energySwirl((ResourceLocation)texture, (float)vec2.x, (float)vec2.y);
    }

    private static float shittyNoise(float f) {
        return (float)(Math.sin(f / 4.0f) + 2.0 * Math.sin(f / 3.0f) + 3.0 * Math.sin(f / 2.0f) + 4.0 * Math.sin(f)) * 0.25f;
    }

    public static Vec2 getEnergySwirlOffset(SpectralHammer entity, float partialTicks, int offset) {
        float f = ((float)entity.tickCount + partialTicks) * 0.02f;
        return new Vec2(SpectralHammerRenderer.shittyNoise(1.2f * f + (float)offset), SpectralHammerRenderer.shittyNoise(f + 456.0f + (float)offset));
    }

    public static Vec2 getEnergySwirlOffset(SpectralHammer entity, float partialTicks) {
        return SpectralHammerRenderer.getEnergySwirlOffset(entity, partialTicks, 0);
    }
}

