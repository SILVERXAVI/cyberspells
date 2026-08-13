/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.wisp;

import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WispModel
extends GeoModel<WispEntity> {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/wisp.geo.json");
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/wisp/wisp.png");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wisp.animation.json");

    public ResourceLocation getModelResource(WispEntity object) {
        return modelResource;
    }

    public ResourceLocation getTextureResource(WispEntity object) {
        return textureResource;
    }

    public ResourceLocation getAnimationResource(WispEntity animatable) {
        return animationResource;
    }
}

