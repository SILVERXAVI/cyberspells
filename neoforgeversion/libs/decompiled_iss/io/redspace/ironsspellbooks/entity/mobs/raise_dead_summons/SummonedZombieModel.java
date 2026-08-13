/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonedZombieModel
extends GeoModel<SummonedZombie> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/summoned_zombie.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/abstract_casting_mob.geo.json");
    public static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/casting_animations.json");

    public ResourceLocation getTextureResource(SummonedZombie object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(SummonedZombie object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(SummonedZombie animatable) {
        return ANIMATIONS;
    }
}

