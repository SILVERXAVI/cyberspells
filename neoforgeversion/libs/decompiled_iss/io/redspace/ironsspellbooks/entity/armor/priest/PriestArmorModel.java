/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor.priest;

import io.redspace.ironsspellbooks.item.armor.PriestArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PriestArmorModel
extends DefaultedItemGeoModel<PriestArmorItem> {
    public PriestArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"armor/priest"));
    }

    public ResourceLocation getModelResource(PriestArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/priest_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PriestArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/priest.png");
    }

    public ResourceLocation getAnimationResource(PriestArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

