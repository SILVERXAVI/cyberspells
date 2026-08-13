/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.fiery_dagger;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class FieryDaggerModel
extends GeoModel<FieryDaggerEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fiery_dagger.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/fiery_dagger.geo.json");

    public ResourceLocation getModelResource(FieryDaggerEntity animatable) {
        return MODEL;
    }

    public ResourceLocation getTextureResource(FieryDaggerEntity animatable) {
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(FieryDaggerEntity animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    @Nullable
    public RenderType getRenderType(FieryDaggerEntity animatable, ResourceLocation texture) {
        return RenderHelper.CustomerRenderType.magic(TEXTURE);
    }
}

