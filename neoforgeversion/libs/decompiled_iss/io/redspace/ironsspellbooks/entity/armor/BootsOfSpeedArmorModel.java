/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.BootsOfSpeedArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class BootsOfSpeedArmorModel
extends DefaultedItemGeoModel<BootsOfSpeedArmorItem> {
    public BootsOfSpeedArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
    }

    public ResourceLocation getModelResource(BootsOfSpeedArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/boots_of_speed.geo.json");
    }

    public ResourceLocation getTextureResource(BootsOfSpeedArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/boots_of_speed.png");
    }

    public ResourceLocation getAnimationResource(BootsOfSpeedArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

