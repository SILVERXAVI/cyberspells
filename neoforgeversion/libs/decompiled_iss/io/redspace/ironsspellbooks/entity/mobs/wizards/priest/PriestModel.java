/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.Item
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.priest;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class PriestModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/priest/priest.png");
    public static final ResourceLocation TEXTURE_ARMOR = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/priest/priest_armored.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/archevoker.geo.json");

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return object.getItemBySlot(EquipmentSlot.HEAD).is((Item)ItemRegistry.PRIEST_HELMET.get()) ? TEXTURE_ARMOR : TEXTURE;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        PriestEntity priest;
        super.setCustomAnimations(entity, instanceId, animationState);
        if (entity instanceof PriestEntity && (priest = (PriestEntity)entity).isUnhappy()) {
            if (Minecraft.getInstance().isPaused() || !entity.shouldBeExtraAnimated()) {
                return;
            }
            GeoBone head = this.getAnimationProcessor().getBone("head");
            head.setRotZ(0.3f * Mth.sin((float)(0.45f * ((float)entity.tickCount + animationState.getPartialTick()))));
            head.setRotX(-0.4f);
        }
    }
}

