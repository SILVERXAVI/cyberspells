/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.ice_block;

import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceBlockModel
extends GeoModel<IceBlockProjectile> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/ice_block.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/ice_block_projectile.geo.json");
    public static final ResourceLocation ANIMS = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/ice_block_animations.json");

    public ResourceLocation getTextureResource(IceBlockProjectile object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(IceBlockProjectile object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(IceBlockProjectile animatable) {
        return ANIMS;
    }
}

