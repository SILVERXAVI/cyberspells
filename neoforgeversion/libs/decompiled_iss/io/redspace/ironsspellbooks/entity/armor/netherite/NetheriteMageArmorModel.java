/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 */
package io.redspace.ironsspellbooks.entity.armor.netherite;

import io.redspace.ironsspellbooks.item.armor.NetheriteMageArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class NetheriteMageArmorModel
extends DefaultedItemGeoModel<NetheriteMageArmorItem> {
    public NetheriteMageArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
    }

    public ResourceLocation getModelResource(NetheriteMageArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/netherite_armor.geo.json");
    }

    public ResourceLocation getTextureResource(NetheriteMageArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/netherite.png");
    }

    public ResourceLocation getAnimationResource(NetheriteMageArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    }
}

