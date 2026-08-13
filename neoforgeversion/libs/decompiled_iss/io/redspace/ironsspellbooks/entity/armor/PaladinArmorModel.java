/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.PaladinArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PaladinArmorModel
extends DefaultedItemGeoModel<PaladinArmorItem> {
    public PaladinArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
    }

    public ResourceLocation getModelResource(PaladinArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/paladin_chestplate.geo.json");
    }

    public ResourceLocation getTextureResource(PaladinArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/paladin_chestplate.png");
    }

    public ResourceLocation getAnimationResource(PaladinArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

