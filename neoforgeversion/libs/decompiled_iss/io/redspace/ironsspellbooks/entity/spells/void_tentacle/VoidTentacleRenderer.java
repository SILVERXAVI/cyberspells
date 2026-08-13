/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 */
package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacleEmissiveLayer;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacleModel;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VoidTentacleRenderer
extends GeoLivingEntityRenderer<VoidTentacle> {
    public VoidTentacleRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidTentacleModel());
        this.addRenderLayer(new VoidTentacleEmissiveLayer(this));
        this.shadowRadius = 1.0f;
    }
}

