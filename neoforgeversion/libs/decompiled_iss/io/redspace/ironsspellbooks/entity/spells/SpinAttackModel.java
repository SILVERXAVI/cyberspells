/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.model.GeoModel;

public class SpinAttackModel
extends GeoModel<AbstractSpellCastingMob> {
    public static final ResourceLocation FIRE_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/fire_riptide.png");
    public static final ResourceLocation LIGHTNING_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/lightning_riptide.png");
    public static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.withDefaultNamespace((String)"textures/entity/trident_riptide.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/spin_attack_model.geo.json");

    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        SpinAttackType spinAttackType = ClientMagicData.getSyncedSpellData((LivingEntity)object).getSpinAttackType();
        return spinAttackType.textureId();
    }

    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return null;
    }

    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }
}

