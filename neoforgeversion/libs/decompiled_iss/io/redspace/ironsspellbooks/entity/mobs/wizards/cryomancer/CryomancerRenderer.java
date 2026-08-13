/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CryomancerRenderer
extends AbstractSpellCastingMobRenderer {
    public CryomancerRenderer(EntityRendererProvider.Context context) {
        super(context, new CryomancerModel());
    }
}

