/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonedSkeletonModel
extends GeoModel<SummonedSkeleton> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/summoned_skeleton.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/skeleton_mob.geo.json");
    public static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/casting_animations.json");

    public ResourceLocation getTextureResource(SummonedSkeleton object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(SummonedSkeleton object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/skeleton_mob.geo.json");
    }

    public ResourceLocation getAnimationResource(SummonedSkeleton animatable) {
        return ANIMATIONS;
    }
}

