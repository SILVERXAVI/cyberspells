/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.HorseModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.entity.AbstractHorseRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.entity.mobs.horse;

import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpectralSteedRenderer
extends AbstractHorseRenderer<SummonedHorse, HorseModel<SummonedHorse>> {
    public SpectralSteedRenderer(EntityRendererProvider.Context p_174167_) {
        super(p_174167_, new HorseModel(p_174167_.bakeLayer(ModelLayers.HORSE)), 1.1f);
    }

    public ResourceLocation getTextureLocation(SummonedHorse pEntity) {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/horse/spectral_steed.png");
    }
}

