/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.armor.pumpkin;

import io.redspace.ironsspellbooks.item.armor.PumpkinArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PumpkinArmorModel
extends GeoModel<PumpkinArmorItem> {
    public ResourceLocation getModelResource(PumpkinArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/pumpkin_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PumpkinArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/pumpkin.png");
    }

    public ResourceLocation getAnimationResource(PumpkinArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

