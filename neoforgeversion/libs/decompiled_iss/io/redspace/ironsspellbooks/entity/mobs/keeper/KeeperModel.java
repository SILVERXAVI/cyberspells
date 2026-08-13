/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import net.minecraft.resources.ResourceLocation;

public class KeeperModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/keeper/keeper.png");
    public static final ResourceLocation TEXTURE_RESTORED = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/keeper/keeper_restored.png");
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/citadel_keeper.geo.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        KeeperEntity keeper;
        return object instanceof KeeperEntity && (keeper = (KeeperEntity)object).isRestored() ? TEXTURE_RESTORED : TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return modelResource;
    }
}

