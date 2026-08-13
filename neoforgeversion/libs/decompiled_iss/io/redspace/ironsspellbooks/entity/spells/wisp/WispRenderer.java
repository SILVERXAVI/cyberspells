/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 */
package io.redspace.ironsspellbooks.entity.spells.wisp;

import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WispRenderer
extends GeoEntityRenderer<WispEntity> {
    public static final ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/wisp/wisp.png");

    public WispRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, (GeoModel)new WispModel());
        this.shadowRadius = 0.3f;
    }

    public ResourceLocation getTextureLocation(WispEntity animatable) {
        return textureLocation;
    }

    public RenderType getRenderType(WispEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.energySwirl((ResourceLocation)texture, (float)0.0f, (float)0.0f);
    }
}

