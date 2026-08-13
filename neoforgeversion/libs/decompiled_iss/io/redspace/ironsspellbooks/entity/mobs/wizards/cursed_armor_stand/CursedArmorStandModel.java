/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.ArmorStandRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandEntity;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class CursedArmorStandModel
extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/cultist.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/armor_stand.geo.json");
    public static double[] rightArmPos = new double[]{0.0, 0.0, 0.0};

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return ArmorStandRenderer.DEFAULT_SKIN_LOCATION;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        CursedArmorStandEntity cursedArmorStandEntity;
        if (entity instanceof CursedArmorStandEntity && (cursedArmorStandEntity = (CursedArmorStandEntity)entity).isArmorStandFrozen()) {
            float f;
            CursedArmorStandEntity.Pose pose = cursedArmorStandEntity.getArmorstandPose();
            GeoBone head = this.getAnimationProcessor().getBone("head");
            GeoBone body = this.getAnimationProcessor().getBone("body");
            GeoBone torso = this.getAnimationProcessor().getBone("torso");
            GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
            GeoBone rightHand = this.getAnimationProcessor().getBone("bipedHandRight");
            GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
            GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
            GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
            switch (pose) {
                case DEFAULT: {
                    this.transformStack.pushRotation(leftArm, 0.17453292f, 0.0f, -0.17453292f);
                    this.transformStack.pushRotation(rightArm, 0.2617994f, 0.0f, 0.17453292f);
                    this.transformStack.pushRotation(leftLeg, (float)(-Math.PI) / 180, 0.0f, (float)(-Math.PI) / 180);
                    this.transformStack.pushRotation(rightLeg, (float)Math.PI / 180, 0.0f, (float)Math.PI / 180);
                    break;
                }
                case KNEELING: {
                    this.transformStack.pushPosition(rightLeg, 0.0f, 2.0f, -3.0f);
                    this.transformStack.pushRotation(rightLeg, -0.91629785f, 0.0f, 0.0f);
                    this.transformStack.pushPosition(leftLeg, (float)Math.PI / 180, 6.0f, -4.0f);
                    this.transformStack.pushRotation(rightArm, 1.5707964f, 0.7853982f, 1.5707964f);
                    this.transformStack.pushRotation(leftArm, 0.7853982f, 0.0f, 0.0f);
                    this.transformStack.pushRotation(head, -0.7853982f, 0.0f, 0.0f);
                    this.transformStack.pushRotation(torso, -0.17453292f, 0.0f, 0.0f);
                    this.transformStack.pushPosition(body, 0.0f, -6.0f, 0.0f);
                    break;
                }
                case HEROIC: {
                    this.transformStack.pushRotation(rightArm, 2.3125613f, 0.0f, 0.0f);
                    this.transformStack.pushRotation(leftArm, -0.34906584f, 0.0f, 0.0f);
                    this.transformStack.pushRotation(leftLeg, -0.20943952f, 0.0f, 0.0f);
                    this.transformStack.pushPosition(rightLeg, 0.0f, 0.0f, -2.0f);
                    this.transformStack.pushRotation(head, 0.43633232f, 0.0f, 0.0f);
                    break;
                }
                case STOIC: {
                    this.transformStack.pushRotationDegrees(rightArm, 80.0f, 35.0f, 0.0f);
                    this.transformStack.pushRotationDegrees(leftArm, 80.0f, -35.0f, 0.0f);
                    this.transformStack.pushRotationDegrees(rightHand, 0.0f, 90.0f, 0.0f);
                    this.transformStack.pushRotationDegrees(rightLeg, 0.0f, -1.0f, 0.0f);
                    this.transformStack.pushRotationDegrees(leftLeg, 0.0f, 1.0f, 0.0f);
                }
            }
            float partialTick = animationState.getPartialTick();
            if (cursedArmorStandEntity.helmetJiggle > 0) {
                f = this.elastic(1.0f - ((float)cursedArmorStandEntity.helmetJiggle - partialTick) / 15.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedHead"), 0.0f, f, 0.0f);
            }
            if (cursedArmorStandEntity.chestJiggle > 0) {
                f = this.elastic(1.0f - ((float)cursedArmorStandEntity.chestJiggle - partialTick) / 15.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedBody"), 0.0f, f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedRightArm"), 0.0f, f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedLeftArm"), 0.0f, -f * 0.75f, 0.0f);
            }
            if (cursedArmorStandEntity.legJiggle > 0) {
                f = this.elastic(1.0f - ((float)cursedArmorStandEntity.legJiggle - partialTick) / 15.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedRightLeg"), 0.0f, -f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedLeftLeg"), 0.0f, f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedRightLeg2"), 0.0f, -f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedLeftLeg2"), 0.0f, f * 0.75f, 0.0f);
            }
            if (cursedArmorStandEntity.bootJiggle > 0) {
                f = this.elastic(1.0f - ((float)cursedArmorStandEntity.bootJiggle - partialTick) / 15.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedRightFoot"), 0.0f, -f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedLeftFoot"), 0.0f, f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedRightFoot2"), 0.0f, -f * 0.75f, 0.0f);
                this.transformStack.pushRotation(this.getAnimationProcessor().getBone("armorBipedLeftFoot2"), 0.0f, f * 0.75f, 0.0f);
            }
            this.transformStack.popStack();
        } else {
            super.setCustomAnimations(entity, instanceId, animationState);
        }
    }

    private float elastic(float f) {
        float x = (float)(Math.pow(2.0, -10.0f * f) * (double)Mth.cos((float)((10.0f * f - 0.75f) * 2.0f * 0.6f * (float)Math.PI / 3.0f))) * 0.2f;
        if ((double)Math.abs(x) < 0.001) {
            return 0.0f;
        }
        return x;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }
}

