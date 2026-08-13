/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.kosmx.playerAnim.api.TransformType
 *  dev.kosmx.playerAnim.api.layered.IAnimation
 *  dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
 *  dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier
 *  dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier$PartModifier
 *  dev.kosmx.playerAnim.core.util.Vec3f
 */
package io.redspace.ironsspellbooks.setup;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.Optional;
import java.util.function.BiFunction;

public class IronsAdjustmentModifier
extends AbstractModifier {
    public static IronsAdjustmentModifier INSTANCE;
    public boolean enabled = true;
    protected BiFunction<String, Float, Optional<AdjustmentModifier.PartModifier>> transformFunction;
    protected int instructedFadeout = 0;
    private int remainingFadeout = 0;

    public IronsAdjustmentModifier(BiFunction<String, Float, Optional<AdjustmentModifier.PartModifier>> transformFunction) {
        this.transformFunction = transformFunction;
    }

    protected float getFadeIn(float delta) {
        float fadeIn = 1.0f;
        IAnimation animation = this.getAnim();
        if (animation instanceof KeyframeAnimationPlayer) {
            KeyframeAnimationPlayer player = (KeyframeAnimationPlayer)this.anim;
            float currentTick = (float)player.getTick() + delta;
            fadeIn = currentTick / (float)player.getData().beginTick;
            fadeIn = Math.min(fadeIn, 1.0f);
        }
        return fadeIn;
    }

    public void tick() {
        super.tick();
        if (this.remainingFadeout > 0) {
            --this.remainingFadeout;
            if (this.remainingFadeout <= 0) {
                this.instructedFadeout = 0;
            }
        }
    }

    public void fadeOut(int fadeOut) {
        if (this.instructedFadeout == 0) {
            this.instructedFadeout = fadeOut;
            this.remainingFadeout = fadeOut + 1;
        }
    }

    public void resetFadeOut() {
        this.instructedFadeout = 0;
        this.remainingFadeout = 0;
    }

    protected float getFadeOut(float delta) {
        float fadeOut = 1.0f;
        if (this.remainingFadeout > 0 && this.instructedFadeout > 0) {
            float current = Math.max((float)this.remainingFadeout - delta, 0.0f);
            fadeOut = current / (float)this.instructedFadeout;
            fadeOut = Math.min(fadeOut, 1.0f);
            return fadeOut;
        }
        IAnimation animation = this.getAnim();
        if (animation instanceof KeyframeAnimationPlayer) {
            KeyframeAnimationPlayer player = (KeyframeAnimationPlayer)this.anim;
            float currentTick = (float)player.getTick() + delta;
            float position = -1.0f * (currentTick - (float)player.getData().stopTick);
            float length = player.getData().stopTick - player.getData().endTick;
            if (length > 0.0f) {
                fadeOut = position / length;
                fadeOut = Math.min(fadeOut, 1.0f);
            }
        }
        return fadeOut;
    }

    public Vec3f get3DTransform(String modelName, TransformType type, float partialTick, Vec3f value0) {
        if (!this.enabled) {
            return super.get3DTransform(modelName, type, partialTick, value0);
        }
        Optional<AdjustmentModifier.PartModifier> partModifier = this.transformFunction.apply(modelName, Float.valueOf(partialTick));
        Vec3f modifiedVector = value0;
        float fade = this.getFadeIn(partialTick) * this.getFadeOut(partialTick);
        if (partModifier.isPresent()) {
            modifiedVector = super.get3DTransform(modelName, type, partialTick, modifiedVector);
            return this.transformVector(modifiedVector, type, partModifier.get(), fade);
        }
        return super.get3DTransform(modelName, type, partialTick, value0);
    }

    protected Vec3f transformVector(Vec3f vector, TransformType type, AdjustmentModifier.PartModifier partModifier, float fade) {
        switch (type) {
            case POSITION: {
                return vector.add(partModifier.offset().scale(fade));
            }
            case ROTATION: {
                return vector.add(partModifier.rotation().scale(fade));
            }
            case SCALE: {
                return vector.add(partModifier.scale().scale(fade));
            }
        }
        return vector;
    }
}

