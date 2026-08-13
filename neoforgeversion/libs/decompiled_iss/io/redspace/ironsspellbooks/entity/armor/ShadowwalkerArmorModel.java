/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.ShadowwalkerArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShadowwalkerArmorModel
extends GeoModel<ShadowwalkerArmorItem> {
    public ResourceLocation getModelResource(ShadowwalkerArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/shadowwalker_armor.geo.json");
    }

    public ResourceLocation getTextureResource(ShadowwalkerArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/shadowwalker.png");
    }

    public ResourceLocation getAnimationResource(ShadowwalkerArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

