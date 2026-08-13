/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.WalkAnimationState
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector2f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoReplacedEntity
 *  software.bernie.geckolib.animation.AnimatableManager
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.constant.DataTickets
 *  software.bernie.geckolib.model.DefaultedEntityGeoModel
 */
package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class AbstractSpellCastingMobModel
extends DefaultedEntityGeoModel<AbstractSpellCastingMob> {
    protected TransformStack transformStack = new TransformStack();
    private long lastRenderedInstance = -1L;

    public AbstractSpellCastingMobModel() {
        super(IronsSpellbooks.id("spellcastingmob"));
    }

    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return AbstractSpellCastingMob.modelResource;
    }

    public abstract ResourceLocation getTextureResource(AbstractSpellCastingMob var1);

    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    public void handleAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState, float partialTick) {
        boolean isReRender;
        AnimatableManager manager = entity.getAnimatableInstanceCache().getManagerForId(instanceId);
        Double currentTick = (Double)animationState.getData(DataTickets.TICK);
        double currentFrameTime = entity instanceof Entity || entity instanceof GeoReplacedEntity ? currentTick + (double)partialTick : currentTick - manager.getFirstTickTime();
        boolean bl = isReRender = !manager.isFirstTick() && currentFrameTime == manager.getLastUpdateTime();
        if (isReRender && instanceId == this.lastRenderedInstance) {
            return;
        }
        this.lastRenderedInstance = instanceId;
        this.transformStack.resetDirty();
        super.handleAnimations((GeoAnimatable)entity, instanceId, animationState, partialTick);
        this.transformStack.popStack();
    }

    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations((GeoAnimatable)entity, instanceId, animationState);
        if (!entity.shouldBeExtraAnimated()) {
            return;
        }
        float partialTick = animationState.getPartialTick();
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone body = this.getAnimationProcessor().getBone("body");
        GeoBone torso = this.getAnimationProcessor().getBone("torso");
        GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
        if (!entity.isAnimating() || entity.shouldAlwaysAnimateHead()) {
            this.transformStack.pushRotation(head, Mth.lerp((float)partialTick, (float)(-entity.xRotO), (float)(-entity.getXRot())) * ((float)Math.PI / 180), Mth.lerp((float)partialTick, (float)(Mth.wrapDegrees((float)(-entity.yHeadRotO + entity.yBodyRotO)) * ((float)Math.PI / 180)), (float)(Mth.wrapDegrees((float)(-entity.yHeadRot + entity.yBodyRot)) * ((float)Math.PI / 180))), 0.0f);
        }
        Vector2f limbSwing = this.getLimbSwing(entity, entity.walkAnimation, partialTick);
        float limbSwingAmount = limbSwing.x;
        float limbSwingSpeed = limbSwing.y;
        if (entity.isPassenger() && entity.getVehicle().shouldRiderSit()) {
            this.transformStack.pushRotation(rightLeg, 1.4137167f, -0.31415927f, -0.07853982f);
            this.transformStack.pushRotation(leftLeg, 1.4137167f, 0.31415927f, 0.07853982f);
        } else if (!entity.isAnimating() || entity.shouldAlwaysAnimateLegs()) {
            float strength = 0.75f;
            Vec3 facing = entity.getForward().multiply(1.0, 0.0, 1.0).normalize();
            Vec3 momentum = entity.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize();
            Vec3 facingOrth = new Vec3(-facing.z, 0.0, facing.x);
            float directionForward = (float)facing.dot(momentum);
            float directionSide = (float)facingOrth.dot(momentum) * 0.35f;
            float rightLateral = -Mth.sin((float)(limbSwingSpeed * 0.6662f)) * 4.0f * limbSwingAmount;
            float leftLateral = -Mth.sin((float)(limbSwingSpeed * 0.6662f - (float)Math.PI)) * 4.0f * limbSwingAmount;
            this.transformStack.pushPosition(rightLeg, rightLateral * directionSide, Mth.cos((float)(limbSwingSpeed * 0.6662f)) * 4.0f * strength * limbSwingAmount, rightLateral * directionForward);
            this.transformStack.pushRotation(rightLeg, Mth.cos((float)(limbSwingSpeed * 0.6662f)) * 1.4f * limbSwingAmount * strength, 0.0f, 0.0f);
            this.transformStack.pushPosition(leftLeg, leftLateral * directionSide, Mth.cos((float)(limbSwingSpeed * 0.6662f - (float)Math.PI)) * 4.0f * strength * limbSwingAmount, leftLateral * directionForward);
            this.transformStack.pushRotation(leftLeg, Mth.cos((float)(limbSwingSpeed * 0.6662f + (float)Math.PI)) * 1.4f * limbSwingAmount * strength, 0.0f, 0.0f);
            if (entity.bobBodyWhileWalking()) {
                this.transformStack.pushPosition(body, 0.0f, Mth.abs((float)Mth.cos((float)((limbSwingSpeed * 1.2662f - 1.5707964f) * 0.5f))) * 2.0f * strength * limbSwingAmount, 0.0f);
            }
        }
        if (!entity.isAnimating()) {
            this.transformStack.pushRotation(rightArm, Mth.cos((float)(limbSwingSpeed * 0.6662f + (float)Math.PI)) * 2.0f * limbSwingAmount * 0.5f, 0.0f, 0.0f);
            this.transformStack.pushRotation(leftArm, Mth.cos((float)(limbSwingSpeed * 0.6662f)) * 2.0f * limbSwingAmount * 0.5f, 0.0f, 0.0f);
            this.bobBone(rightArm, (float)entity.tickCount + partialTick, 1.0f);
            this.bobBone(leftArm, (float)entity.tickCount + partialTick, -1.0f);
            if (entity.isDrinkingPotion()) {
                this.transformStack.pushRotation(entity.isLeftHanded() ? leftArm : rightArm, 0.61086524f, (float)(entity.isLeftHanded() ? -25 : 25) * ((float)Math.PI / 180), (float)(entity.isLeftHanded() ? 15 : -15) * ((float)Math.PI / 180));
            }
        }
    }

    protected void bobBone(GeoBone bone, float offset, float multiplier) {
        float z = multiplier * (Mth.cos((float)(offset * 0.09f)) * 0.05f + 0.05f);
        float x = multiplier * Mth.sin((float)(offset * 0.067f)) * 0.05f;
        this.transformStack.pushRotation(bone, x, 0.0f, z);
    }

    protected Vector2f getLimbSwing(AbstractSpellCastingMob entity, WalkAnimationState walkAnimationState, float partialTick) {
        float limbSwingAmount = 0.0f;
        float limbSwingSpeed = 0.0f;
        if (entity.isAlive()) {
            limbSwingAmount = walkAnimationState.speed(partialTick);
            limbSwingSpeed = walkAnimationState.position(partialTick);
            if (entity.isBaby()) {
                limbSwingSpeed *= 3.0f;
            }
            if (limbSwingAmount > 1.0f) {
                limbSwingAmount = 1.0f;
            }
        }
        return new Vector2f(limbSwingAmount, limbSwingSpeed);
    }
}

