/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class DeadKingModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE_NORMAL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/dead_king/dead_king.png");
    public static final ResourceLocation TEXTURE_CORPSE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/dead_king/dead_king_resting.png");
    public static final ResourceLocation TEXTURE_ENRAGED = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/dead_king/dead_king_enraged.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/dead_king.geo.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        if (object instanceof DeadKingBoss) {
            DeadKingBoss boss = (DeadKingBoss)object;
            if (boss.isPhase(DeadKingBoss.Phases.FinalPhase)) {
                return TEXTURE_ENRAGED;
            }
            return TEXTURE_NORMAL;
        }
        return TEXTURE_CORPSE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        GeoBone jaw = this.getAnimationProcessor().getBone("jaw");
        GeoBone hair1 = this.getAnimationProcessor().getBone("hair");
        GeoBone hair2 = this.getAnimationProcessor().getBone("hair2");
        float f = (float)entity.tickCount + animationState.getPartialTick();
        if (jaw == null || hair1 == null || hair2 == null) {
            return;
        }
        jaw.setRotX(Mth.sin((float)(f * 0.05f)) * 5.0f * ((float)Math.PI / 180));
        hair1.setRotX((Mth.sin((float)(f * 0.1f)) * 10.0f - 30.0f) * ((float)Math.PI / 180));
        hair2.setRotX(Mth.sin((float)(f * 0.15f)) * 15.0f * ((float)Math.PI / 180));
    }
}

