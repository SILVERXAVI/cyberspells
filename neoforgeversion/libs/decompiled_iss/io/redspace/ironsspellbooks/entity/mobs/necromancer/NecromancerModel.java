/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.entity.mobs.necromancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class NecromancerModel
extends AbstractSpellCastingMobModel {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/necromancer.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/skeleton_mob.geo.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }
}

