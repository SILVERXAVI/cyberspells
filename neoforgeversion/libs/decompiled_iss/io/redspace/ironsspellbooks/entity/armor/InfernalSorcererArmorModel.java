/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.InfernalSorcererArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class InfernalSorcererArmorModel
extends DefaultedItemGeoModel<InfernalSorcererArmorItem> {
    public InfernalSorcererArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
    }

    public ResourceLocation getModelResource(InfernalSorcererArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/infernal_sorcerer.geo.json");
    }

    public ResourceLocation getTextureResource(InfernalSorcererArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/infernal_sorcerer.png");
    }

    public ResourceLocation getAnimationResource(InfernalSorcererArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

