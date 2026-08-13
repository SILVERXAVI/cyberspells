/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.TarnishedCrownArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TarnishedCrownModel
extends GeoModel<TarnishedCrownArmorItem> {
    public ResourceLocation getModelResource(TarnishedCrownArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/tarnished_armor.geo.json");
    }

    public ResourceLocation getTextureResource(TarnishedCrownArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/tarnished.png");
    }

    public ResourceLocation getAnimationResource(TarnishedCrownArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

