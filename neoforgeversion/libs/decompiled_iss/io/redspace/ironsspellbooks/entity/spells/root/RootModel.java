/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.root;

import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RootModel
extends GeoModel<RootEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/root.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/root.geo.json");
    public static final ResourceLocation ANIMS = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/root_animations.json");

    public ResourceLocation getTextureResource(RootEntity object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(RootEntity object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(RootEntity animatable) {
        return ANIMS;
    }
}

