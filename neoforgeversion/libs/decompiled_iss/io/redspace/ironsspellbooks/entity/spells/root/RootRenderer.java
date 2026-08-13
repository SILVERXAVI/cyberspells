/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.root;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.root.RootModel;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class RootRenderer
extends GeoLivingEntityRenderer<RootEntity> {
    public RootRenderer(EntityRendererProvider.Context context) {
        super(context, new RootModel());
    }

    public void preRender(PoseStack poseStack, RootEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        Entity rooted = animatable.getFirstPassenger();
        if (rooted != null) {
            float scale = rooted.getBbWidth() / 0.6f;
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, (Entity)animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}

