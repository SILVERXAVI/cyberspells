/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 */
package io.redspace.ironsspellbooks.entity.spells.ice_block;

import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockModel;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceBlockRenderer
extends GeoEntityRenderer<IceBlockProjectile> {
    public IceBlockRenderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new IceBlockModel());
        this.shadowRadius = 1.5f;
    }
}

