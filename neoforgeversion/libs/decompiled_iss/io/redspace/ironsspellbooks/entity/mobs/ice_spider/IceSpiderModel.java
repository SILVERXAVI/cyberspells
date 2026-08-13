/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.WalkAnimationState
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Quaternionf
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoReplacedEntity
 *  software.bernie.geckolib.animation.AnimatableManager
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.constant.DataTickets
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 *  software.bernie.geckolib.model.DefaultedEntityGeoModel
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class IceSpiderModel
extends DefaultedEntityGeoModel<IceSpiderEntity> {
    static final String[] SIDES = new String[]{"right", "left"};
    static final String[] LEGS = new String[]{"Fore", "ForeMiddle", "BackMiddle", "Back"};
    static final String SHOULDER = "Shoulder";
    static final String LEG = "Leg";
    static final float OFFSET_PER_LEG = 0.61086524f;
    protected TransformStack transformStack = new TransformStack();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/ice_spider/ice_spider.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/ice_spider.geo.json");
    public static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/ice_spider.animation.json");
    public static final DataTicket<TransformStack> STACK_TICKET = new DataTicket("irons_spellbooks:transform_stack", TransformStack.class);
    private long lastRenderedInstance = -1L;

    public IceSpiderModel() {
        super(IronsSpellbooks.id("spellcastingmob"));
    }

    public ResourceLocation getModelResource(IceSpiderEntity object) {
        return MODEL;
    }

    public ResourceLocation getTextureResource(IceSpiderEntity object) {
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(IceSpiderEntity animatable) {
        return ANIMATION;
    }

    public void handleAnimations(IceSpiderEntity entity, long instanceId, AnimationState<IceSpiderEntity> animationState, float partialTick) {
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

    public void setCustomAnimations(IceSpiderEntity entity, long instanceId, AnimationState<IceSpiderEntity> animationState) {
        super.setCustomAnimations((GeoAnimatable)entity, instanceId, animationState);
        float partialTick = animationState.getPartialTick();
        this.transformStack.pushPosition(this.getAnimationProcessor().getBone("torso"), (float)IceSpiderEntity.TORSO_OFFSET.x, (float)IceSpiderEntity.TORSO_OFFSET.y * entity.getCrouchHeightMultiplier(partialTick), (float)IceSpiderEntity.TORSO_OFFSET.z);
        Vec3 normal = Utils.lerp(partialTick, entity.lastNormal, entity.normal);
        Quaternionf normalRotation = Utils.rotationBetweenVectors(normal.toVector3f(), new Vector3f(0.0f, 1.0f, 0.0f));
        Vector3f headRotation = new Vector3f(Mth.lerp((float)partialTick, (float)entity.xRotO, (float)entity.getXRot()) * ((float)Math.PI / 180), Mth.lerp((float)partialTick, (float)(Mth.wrapDegrees((float)(entity.yHeadRotO - entity.yBodyRotO)) * ((float)Math.PI / 180)), (float)(Mth.wrapDegrees((float)(entity.yHeadRot - entity.yBodyRot)) * ((float)Math.PI / 180))), 0.0f);
        normalRotation.invert().transform(headRotation);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        this.transformStack.pushRotation(head, -headRotation.x, -headRotation.y, -headRotation.z);
        Vector2f limbSwingVec = this.getLimbSwing(entity, entity.walkAnimation, partialTick);
        float limbSwing = limbSwingVec.y;
        float limbSwingAmount = limbSwingVec.x;
        float f = 0.5f;
        float yRange = 0.34906584f * f;
        float zRange = 0.20943952f * f;
        float speed = 0.1f / f;
        float primaryY = this.legY(limbSwing, speed, 0.0f) * yRange * limbSwingAmount;
        float secondaryY = this.legY(limbSwing, speed, (float)Math.PI) * yRange * limbSwingAmount;
        float primaryZ = this.legZ(limbSwing, speed, (float)Math.PI) * zRange * limbSwingAmount;
        float secondaryZ = this.legZ(limbSwing, speed, 0.0f) * zRange * limbSwingAmount;
        for (int i = 0; i < SIDES.length; ++i) {
            for (int j = 0; j < LEGS.length; ++j) {
                int sideSign = Mth.sign((double)((double)i - 0.5));
                float baseY = 0.0f;
                float baseZ = Mth.lerp((float)entity.crouchTweenPercent(partialTick), (float)10.0f, (float)0.0f) * ((float)Math.PI / 180);
                String shoulderBone = String.format("%s%s%s", SIDES[i], LEGS[j], SHOULDER);
                String legBone = String.format("%s%s%s", SIDES[i], LEGS[j], LEG);
                boolean primary = j % 2 == 0;
                try {
                    this.transformStack.pushRotation(Objects.requireNonNull(this.getAnimationProcessor().getBone(shoulderBone)), 0.0f, ((primary ? primaryY : secondaryY) + baseY) * (float)sideSign, 0.0f);
                    this.transformStack.pushRotation(Objects.requireNonNull(this.getAnimationProcessor().getBone(legBone)), 0.0f, 0.0f, ((primary ? primaryZ : secondaryZ) + baseZ) * (float)(-sideSign));
                    continue;
                }
                catch (Exception e) {
                    IronsSpellbooks.LOGGER.error("beep");
                }
            }
        }
    }

    private float legY(float limbSwing, float speedFactor, float offset) {
        float f = offset - 1.5707964f;
        return Mth.sin((float)(limbSwing * ((float)Math.PI * 2) * speedFactor + f + Mth.sin((float)(limbSwing * ((float)Math.PI * 2) * speedFactor + f)) * 0.5f));
    }

    private float legZ(float limbSwing, float speedFactor, float offset) {
        float f = Mth.sin((float)(limbSwing * ((float)Math.PI * 2) * speedFactor + offset));
        f = f * f * f;
        return Math.max(f, 0.0f);
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

