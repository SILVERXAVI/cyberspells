/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.world.entity.LivingEntity
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 */
package io.redspace.ironsspellbooks.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeoLivingEntityRenderer<T extends LivingEntity>
extends GeoEntityRenderer<T> {
    public GeoLivingEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    public boolean shouldShowName(T animatable) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(animatable);
        float f = animatable.isCrouching() ? 32.0f : 64.0f;
        return !(d0 >= (double)(f * f)) && animatable.isCustomNameVisible();
    }
}

