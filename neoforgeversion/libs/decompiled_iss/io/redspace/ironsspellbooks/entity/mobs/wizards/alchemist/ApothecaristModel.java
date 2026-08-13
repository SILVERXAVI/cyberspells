/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class ApothecaristModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/apothecarist.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/piglin_casting_mob.geo.json");
    private static final float tilt = 0.17453292f;
    private static final Vector3f forward = new Vector3f(0.0f, 0.0f, Mth.sin((float)0.17453292f) * -12.0f);

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        float partialTick = animationState.getPartialTick();
        GeoBone leftEar = this.getAnimationProcessor().getBone("left_ear");
        GeoBone rightEar = this.getAnimationProcessor().getBone("right_ear");
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone body = this.getAnimationProcessor().getBone("body");
        GeoBone torso = this.getAnimationProcessor().getBone("torso");
        GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
        this.transformStack.pushPosition(head, forward);
        this.transformStack.pushPosition(rightArm, forward);
        this.transformStack.pushPosition(leftArm, forward);
        this.transformStack.pushPosition(torso, forward);
        this.transformStack.pushRotation(torso, -0.17453292f, 0.0f, 0.0f);
        this.transformStack.pushPosition(rightLeg, forward);
        this.transformStack.pushPosition(leftLeg, new Vector3f(0.0f, 0.0f, 1.0f));
        if (entity.swingTime > 0) {
            float rot = Mth.lerp((float)(((float)entity.swingTime - partialTick) / 10.0f), (float)0.0f, (float)((float)Math.PI));
            this.transformStack.pushRotation(rightArm, rot, 0.0f, 0.0f);
        }
        if (leftEar != null && rightEar != null) {
            Vector2f walkanimation = this.getLimbSwing(entity, entity.walkAnimation, partialTick);
            float r = Mth.cos((float)(walkanimation.y * 0.6662f + (float)Math.PI)) * 2.0f * walkanimation.x * 0.5f;
            r *= 0.3f;
            this.transformStack.pushRotation(leftEar, 0.0f, 0.0f, -(r += 0.25132743f));
            this.transformStack.pushRotation(rightEar, 0.0f, 0.0f, r);
        }
        super.setCustomAnimations(entity, instanceId, animationState);
    }
}

