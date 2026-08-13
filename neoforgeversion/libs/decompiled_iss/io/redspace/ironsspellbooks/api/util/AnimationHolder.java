/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.animation.Animation$LoopType
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

public class AnimationHolder {
    private final RawAnimation geckoAnimation;
    private final ResourceLocation playerAnimation;
    public final boolean isPass;
    public final boolean animatesLegs;
    private static final AnimationHolder empty = new AnimationHolder(false);
    private static final AnimationHolder pass = new AnimationHolder(true);

    @Deprecated(forRemoval=true)
    public AnimationHolder(String path, boolean playOnce, boolean animatesLegs) {
        this(path.contains(":") ? ResourceLocation.parse((String)path) : IronsSpellbooks.id(path), playOnce, animatesLegs);
    }

    public AnimationHolder(ResourceLocation animation, boolean playOnce, boolean animatesLegs) {
        this.playerAnimation = animation;
        this.geckoAnimation = RawAnimation.begin().then(this.playerAnimation.getPath(), playOnce ? Animation.LoopType.PLAY_ONCE : Animation.LoopType.HOLD_ON_LAST_FRAME);
        this.isPass = false;
        this.animatesLegs = animatesLegs;
    }

    @Deprecated(forRemoval=true)
    public AnimationHolder(String path, boolean playOnce) {
        this(path, playOnce, false);
    }

    public AnimationHolder(ResourceLocation path, boolean playOnce) {
        this(path, playOnce, false);
    }

    private AnimationHolder(boolean isPass) {
        this.playerAnimation = null;
        this.geckoAnimation = null;
        this.isPass = isPass;
        this.animatesLegs = false;
    }

    public static AnimationHolder none() {
        return empty;
    }

    public static AnimationHolder pass() {
        return pass;
    }

    public Optional<RawAnimation> getForMob() {
        return this.geckoAnimation == null ? Optional.empty() : Optional.of(this.geckoAnimation);
    }

    public Optional<ResourceLocation> getForPlayer() {
        return this.playerAnimation == null ? Optional.empty() : Optional.of(this.playerAnimation);
    }
}

