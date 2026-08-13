/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CursedArmorStandRenderer
extends AbstractSpellCastingMobRenderer {
    public CursedArmorStandRenderer(EntityRendererProvider.Context context) {
        super(context, new CursedArmorStandModel());
    }
}

