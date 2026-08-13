/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.summoned_weapons;

import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedWeaponEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonedRapierModel
extends GeoModel<SummonedWeaponEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/summoned_weapons/summoned_rapier.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/summoned_rapier.geo.json");
    public static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/summoned_weapon_animations.json");

    public ResourceLocation getModelResource(SummonedWeaponEntity animatable) {
        return MODEL;
    }

    public ResourceLocation getTextureResource(SummonedWeaponEntity animatable) {
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(SummonedWeaponEntity animatable) {
        return ANIMATIONS;
    }
}

