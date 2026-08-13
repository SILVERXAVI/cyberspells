/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class VoidTentacleModel
extends GeoModel<VoidTentacle> {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/void_tentacle.geo.json");
    public static final ResourceLocation textureResource = IronsSpellbooks.id("textures/entity/void_tentacle/void_tentacle.png");
    public static final ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/void_tentacle_animations.json");

    public ResourceLocation getModelResource(VoidTentacle object) {
        return modelResource;
    }

    public ResourceLocation getTextureResource(VoidTentacle mob) {
        return textureResource;
    }

    public ResourceLocation getAnimationResource(VoidTentacle animatable) {
        return animationResource;
    }

    public void setCustomAnimations(VoidTentacle animatable, long instanceId, AnimationState<VoidTentacle> animationState) {
        super.setCustomAnimations((GeoAnimatable)animatable, instanceId, animationState);
        float seed = (float)(animatable.getX() * animatable.getZ()) % 173.0f;
        float speed = 0.55f;
        float f = (seed + (float)animatable.tickCount + animationState.getPartialTick()) * speed;
        List<GeoBone> bones = List.of(this.getAnimationProcessor().getBone("root"), this.getAnimationProcessor().getBone("segment_1"), this.getAnimationProcessor().getBone("segment_2"), this.getAnimationProcessor().getBone("segment_3"), this.getAnimationProcessor().getBone("segment_4"));
        int age = animatable.tickCount;
        float tween = Mth.clamp((float)(age < 15 ? (float)(age - 5) / 10.0f : (age > 240 ? 1.0f - (float)(age - 240) / 50.0f : 1.0f)), (float)0.0f, (float)1.0f);
        for (int i = 0; i < bones.size(); ++i) {
            GeoBone bone = bones.get(i);
            float intensity = 3.0f - (float)i * 0.2f;
            bone.updateRotation(Mth.lerp((float)tween, (float)bone.getRotX(), (float)(intensity * ((float)Math.PI / 180) * VoidTentacleModel.shittyNoise(f + 100.0f + (float)i))), 0.0f, Mth.lerp((float)tween, (float)bone.getRotZ(), (float)(intensity * ((float)Math.PI / 180) * VoidTentacleModel.shittyNoise(f + (float)i))));
        }
        this.getAnimationProcessor().getBone("root").updateRotation(0.0f, (float)Math.PI / 180 * (VoidTentacleModel.shittyNoise(f + 150.0f) * 0.25f + (float)animatable.tickCount + animationState.getPartialTick()), 0.0f);
    }

    private static float shittyNoise(float f) {
        return (Mth.sin((float)(f * 0.1f)) + Mth.sin((float)(f * 0.25f)) + 2.0f * Mth.sin((float)(f * 0.333f)) + 3.0f * Mth.sin((float)(f * 0.5f)) + 4.0f * Mth.sin((float)f)) * 1.5f;
    }
}

