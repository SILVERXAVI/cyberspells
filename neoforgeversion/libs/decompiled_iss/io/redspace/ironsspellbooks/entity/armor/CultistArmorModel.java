/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.CultistArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class CultistArmorModel
extends DefaultedItemGeoModel<CultistArmorItem> {
    public CultistArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
    }

    public ResourceLocation getModelResource(CultistArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/cultist_armor.geo.json");
    }

    public ResourceLocation getTextureResource(CultistArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/cultist.png");
    }

    public ResourceLocation getAnimationResource(CultistArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

