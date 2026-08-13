/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PyromancerRenderer
extends AbstractSpellCastingMobRenderer {
    public PyromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new PyromancerModel());
    }
}

